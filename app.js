const $ = (s) => document.querySelector(s);
const esc = (s) => String(s).replace(/[&<>"]/g, (c) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;" }[c]));

/* Cover: the real image when covers/<file> exists, otherwise a drawn poster
   (a tangle of lines that resolves into one straight line). */
function coverHTML(b) {
  const drawn = `<svg viewBox="0 0 200 300" preserveAspectRatio="xMidYMid slice" aria-hidden="true">
      <rect width="200" height="300" fill="${b.color}"/>
      <circle cx="150" cy="62" r="74" fill="#ffffff" opacity=".07"/>
      <path d="M-10 70 C 30 20, 60 120, 90 70 S 130 10, 150 80 S 100 150, 70 105 S 20 130, 60 168 S 130 120, 160 150 S 190 140, 215 150" fill="none" stroke="#ffd166" stroke-width="2.2" stroke-linecap="round" opacity=".95"/>
      <path d="M-10 95 C 40 130, 70 40, 110 92 S 170 120, 120 138" fill="none" stroke="#ffffff" stroke-width="1.2" opacity=".5"/>
      <line x1="12" y1="192" x2="188" y2="192" stroke="#ffd166" stroke-width="3" stroke-linecap="round"/>
    </svg>
    <div class="t"><b>${esc(b.title)}</b><span>${esc(b.author)}</span></div>`;
  const img = b.cover
    ? `<img src="${esc(b.cover)}" alt="" onload="this.parentNode.classList.add('real')" onerror="this.remove()">`
    : "";
  return `<div class="cover" style="background:${b.color}">${drawn}${img}</div>`;
}

function renderShelf() {
  $("#shelf").innerHTML = BOOKS.map((b) => `
    <div>
      <button class="book" data-id="${b.id}" aria-label="Open learnings from ${esc(b.title)} by ${esc(b.author)}">
        ${coverHTML(b)}
        <div class="meta"><strong>${esc(b.title)}</strong><small>${esc(b.author)} · ${b.year}</small></div>
      </button>
    </div>`).join("") + `
    <div class="add-wrap">
      <div class="add" aria-hidden="true">Next book goes here</div>
      <div class="meta"><strong>Add a book</strong><small>Add an entry to data/books.js</small></div>
    </div>`;
}

/* per-device state: ticked practices, voice and speed */
const store = {
  get(k, d = []) { try { return JSON.parse(localStorage.getItem(k)) ?? d; } catch (e) { return d; } },
  set(k, v) { try { localStorage.setItem(k, JSON.stringify(v)); } catch (e) {} }
};

let current = null, lastFocus = null;
const TABS = [["core", "In one page"], ["chapters", "Chapter notes"], ["practice", "Practices"], ["quotes", "Quotes"]];

function tabBody(b, tab) {
  if (tab === "core") {
    return `<p class="lead">${esc(b.oneLine)}</p>
      <ol class="core">${b.core.map(([h, t]) => `<li><strong>${esc(h)}</strong> ${esc(t)}</li>`).join("")}</ol>
      <p class="note">${esc(b.caveat)}</p>`;
  }
  if (tab === "chapters") {
    return b.parts.map((p) => `
      <h3 class="part">${esc(p.name)}</h3><p class="part-sub">${esc(p.sub)}</p>
      ${p.chapters.map(([t, n, pts]) => `<details><summary>${esc(t)}<span class="n">${esc(n)}</span></summary><ul>${pts.map((x) => `<li>${esc(x)}</li>`).join("")}</ul></details>`).join("")}`).join("");
  }
  if (tab === "practice") {
    const done = store.get("done:" + b.id);
    return `<p class="lead">The book is meant to be practiced. Tick these off as you try them.</p>
      <ul class="chk">${b.practices.map((x, i) => `<li><label><input type="checkbox" data-i="${i}" ${done.includes(i) ? "checked" : ""}><span>${esc(x)}</span></label></li>`).join("")}</ul>`;
  }
  return b.quotes.map(([q, who]) => `<blockquote>“${esc(q)}”<cite>${esc(who)}</cite></blockquote>`).join("");
}

/* ---------- Voice reader: the browser's built-in speech, no API and no model ---------- */
const Reader = (() => {
  const synth = "speechSynthesis" in window ? window.speechSynthesis : null;
  let voices = [], chunks = [], idx = 0, state = "idle", token = 0;

  const score = (v) => {
    let s = 0;
    if (/natural|neural|online/i.test(v.name)) s += 6;
    if (/premium|enhanced|siri/i.test(v.name)) s += 5;
    if (/google/i.test(v.name)) s += 3;
    if (/samantha|ava|allison|serena|daniel|karen|moira|jenny|aria|guy|libby|ryan|sonia|zira|david/i.test(v.name)) s += 2;
    if (/^en[-_]?(US|GB|AU|IN)?/i.test(v.lang)) s += 1;
    return s;
  };
  const loadVoices = () => {
    if (!synth) return;
    voices = synth.getVoices().filter((v) => /^en/i.test(v.lang)).sort((a, b) => score(b) - score(a));
    const sel = $("#voice");
    const saved = store.get("voice", null);
    sel.innerHTML = voices.map((v) => `<option value="${esc(v.voiceURI)}">${esc(v.name)} (${esc(v.lang)})</option>`).join("");
    if (saved && voices.some((v) => v.voiceURI === saved)) sel.value = saved;
    $("#voice-note").textContent = voices.length
      ? "Voices come from your device. The most natural ones are marked Natural (Edge, Windows) or Enhanced / Premium (iPhone, Mac)."
      : "No English voices found on this device.";
  };
  if (synth) { loadVoices(); if (synth.addEventListener) synth.addEventListener("voiceschanged", loadVoices); }

  const currentVoice = () => voices.find((v) => v.voiceURI === $("#voice").value) || voices[0];

  /* gather the readable pieces of the open tab, splitting long text at sentence ends */
  function collect() {
    chunks = [];
    const sel = "#body p.lead, #body h3, #body summary, #body li, #body blockquote, #body .note";
    $("#body").querySelectorAll(sel).forEach((el) => {
      const text0 = el.matches("summary, blockquote") ? el.firstChild.textContent : el.textContent;
      const text = text0.replace(/\s+/g, " ").trim();
      if (!text) return;
      const parts = text.match(/[^.!?]+[.!?]+["')”]*\s*|[^.!?]+$/g) || [text];
      let buf = "";
      parts.forEach((p) => {
        if ((buf + p).length > 220 && buf) { chunks.push({ el, text: buf.trim() }); buf = p; } else buf += p;
      });
      if (buf.trim()) chunks.push({ el, text: buf.trim() });
    });
  }

  function mark(el) {
    document.querySelectorAll(".speaking").forEach((n) => n.classList.remove("speaking"));
    if (!el) return;
    const d = el.closest("details");
    if (d) d.open = true;
    el.classList.add("speaking");
    el.scrollIntoView({ block: "center", behavior: matchMedia("(prefers-reduced-motion: reduce)").matches ? "auto" : "smooth" });
  }

  function speakAt(i) {
    synth.cancel();
    const my = ++token;
    if (i >= chunks.length) { stop(); return; }
    idx = i;
    const c = chunks[i];
    mark(c.el);
    const u = new SpeechSynthesisUtterance(c.text);
    const v = currentVoice();
    if (v) { u.voice = v; u.lang = v.lang; }
    u.rate = parseFloat($("#rate").value);
    u.onend = () => { if (my === token && state === "playing") speakAt(i + 1); };
    u.onerror = (e) => { if (my === token && e.error !== "interrupted" && e.error !== "canceled") stop(); };
    synth.speak(u);
  }

  function ui() {
    $("#play").textContent = state === "playing" ? "Pause" : state === "paused" ? "Resume" : "Listen";
    $("#play").setAttribute("aria-pressed", String(state === "playing"));
    $("#stop").disabled = state === "idle";
    $("#reader").dataset.state = state;
  }

  /* pause cancels and remembers the position; resume speaks that piece again.
     Native pause/resume is unreliable on Android Chrome. */
  function play() {
    if (!synth) return;
    if (state === "playing") { token++; synth.cancel(); state = "paused"; }
    else if (state === "paused") { state = "playing"; speakAt(idx); }
    else { collect(); state = "playing"; speakAt(0); }
    ui();
  }
  function stop() {
    token++;
    if (synth) synth.cancel();
    state = "idle"; idx = 0; mark(null); ui();
  }
  function restartHere() { if (state === "playing") speakAt(idx); }

  return { supported: !!synth, play, stop, restartHere, loadVoices };
})();

function showTab(tab) {
  Reader.stop();
  $("#tabs").querySelectorAll(".tab").forEach((t) => t.setAttribute("aria-selected", String(t.dataset.tab === tab)));
  $("#body").innerHTML = tabBody(current, tab);
  $("#body").scrollTop = 0;
}

function openBook(id) {
  current = BOOKS.find((b) => b.id === id);
  lastFocus = document.activeElement;
  $("#mini").innerHTML = coverHTML(current);
  $("#sh-title").textContent = current.title;
  $("#sh-sub").textContent = current.author + " · " + current.tagline;
  $("#tabs").innerHTML = TABS.map(([k, l]) => `<button class="tab" role="tab" data-tab="${k}">${l}</button>`).join("");
  $("#reader").hidden = !Reader.supported;
  Reader.loadVoices();
  $("#scrim").hidden = false;
  document.body.style.overflow = "hidden";
  showTab("core");
  $("#close").focus();
}
function closeBook() {
  Reader.stop();
  $("#scrim").hidden = true;
  document.body.style.overflow = "";
  if (lastFocus) lastFocus.focus();
}

$("#shelf").addEventListener("click", (e) => { const b = e.target.closest(".book"); if (b) openBook(b.dataset.id); });
$("#tabs").addEventListener("click", (e) => { const t = e.target.closest(".tab"); if (t) showTab(t.dataset.tab); });
$("#close").addEventListener("click", closeBook);
$("#scrim").addEventListener("click", (e) => { if (e.target.id === "scrim") closeBook(); });
document.addEventListener("keydown", (e) => { if (e.key === "Escape" && !$("#scrim").hidden) closeBook(); });
window.addEventListener("pagehide", () => Reader.stop());
$("#body").addEventListener("change", (e) => {
  if (!e.target.matches("input[data-i]")) return;
  const k = "done:" + current.id, set = new Set(store.get(k)), i = +e.target.dataset.i;
  if (e.target.checked) set.add(i); else set.delete(i);
  store.set(k, [...set]);
});
$("#play").addEventListener("click", () => Reader.play());
$("#stop").addEventListener("click", () => Reader.stop());
$("#voice").addEventListener("change", () => { store.set("voice", $("#voice").value); Reader.restartHere(); });
$("#rate").addEventListener("input", () => { $("#rate-out").textContent = parseFloat($("#rate").value).toFixed(2).replace(/0$/, "") + "×"; });
$("#rate").addEventListener("change", () => { store.set("rate", $("#rate").value); Reader.restartHere(); });
{ const r = store.get("rate", null); if (r) $("#rate").value = r; $("#rate").dispatchEvent(new Event("input")); }

renderShelf();
