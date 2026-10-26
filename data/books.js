const BOOKS = [{
  id: "think-straight",
  title: "Think Straight",
  author: "Darius Foroux",
  year: 2017,
  color: "#1d3a8a",
  cover: "covers/think-straight.jpg", /* drop the real cover image here; a drawn cover shows until it exists */
  tagline: "Change your thoughts, change your life",
  oneLine: "A thought is only worth having if it is useful, meaning it helps you act.",
  core: [
    ["You become what you think.", "Your thoughts shape your feelings, actions and identity, and you can choose which ones to follow."],
    ["Your mind is a tool.", "Judge a thought by whether it works in practice, not by whether it sounds clever."],
    ["Most thoughts are noise.", "Only a small share lead to meaningful action. Filter out the rest the way an inbox filters spam."],
    ["Thinking is a skill you train.", "Like a muscle, it improves with regular practice and keeps growing if you keep learning after school."],
    ["Split control from no control.", "Spend mental energy only on what you control (your actions, words, intentions and desires) and let the rest go."],
    ["Don't trust your mind blindly.", "Cognitive biases distort how you see things. Check your thinking against facts and argue against yourself."],
    ["Slow down.", "Quick decisions are badly informed ones. Great thinkers weren't fast thinkers, and \"I don't know yet\" is a fine answer."],
    ["Clarity comes from action.", "Life isn't linear and the dots only connect looking back. Act, then reflect, rather than trying to think your way out of everything."],
    ["Ask \"what now?\" instead of \"why?\"", "When something goes wrong, \"why me?\" drains you. Ask what you can do to fix it or prevent it."],
    ["Live without regret.", "People regret what they didn't do more than what they did. Act on what you want, spend your time well, and make useful things for others."]
  ],
  parts: [
    { name: "Why thinking matters", sub: "Chapters 1–7", chapters: [
      ["You Become What You Think", "Ch. 1", [
        "Your thoughts shape who you are and what you experience.",
        "William James climbed out of a deep depression after reading a philosopher's idea about free will. The point: you can choose your thoughts.",
        "The book opens with James: \"A great many people think they are thinking when they are merely rearranging their prejudices.\""]],
      ["Why Do We Need A Book On Practical Thinking?", "Ch. 2", [
        "Schools teach what to think, not how to think in everyday life.",
        "Practical thinking means questioning your assumptions and looking for real facts.",
        "The aim isn't deep philosophy. It's thinking that helps you live, work and decide better."]],
      ["Use What Works", "Ch. 3", [
        "Foroux says he isn't an expert. For years he believed he couldn't control his thoughts, which brought mood swings and trouble with everyday problems.",
        "He got better through experience, introspection and journaling.",
        "Keep the ideas that work for you, drop the ones that don't, and test everything yourself."]],
      ["Clear Thinking Requires Training", "Ch. 4", [
        "The mind is like a muscle. Clear thinking takes regular practice, the way fitness does.",
        "Many people stop learning after school, so their thinking stops growing.",
        "Keep learning new things to keep your mind sharp."]],
      ["From Chaos To Clarity", "Ch. 5", [
        "Most of us carry a jumble of fearful, worried thoughts. The goal is to replace them with one straight, useful line of thought.",
        "A detour isn't always bad. The other road can show you something new or lead somewhere better."]],
      ["A (Very) Brief History Of Thinking", "Ch. 6", [
        "A quick tour of thinkers from Confucius and Socrates to Descartes and William James.",
        "What they share: the mind is a problem-solving tool, and the quality of your thoughts shapes the quality of your life.",
        "Pragmatism, started by Charles Sanders Peirce and William James, is the school Foroux follows: an idea is worth keeping if it works in practice."]],
      ["Life Is Not Linear", "Ch. 7", [
        "Life doesn't run in a straight line, and plans rarely go as planned.",
        "Steve Jobs: \"You can't connect the dots looking forward; you can only connect them looking backward.\"",
        "Don't wait for a perfect plan. Act, and trust the dots to connect later."]]
    ]},
    { name: "Taking control of your thoughts", sub: "The heart of the book", chapters: [
      ["Filter Your Thoughts", "Ch. 8", [
        "Thoughts arrive uninvited. You can't stop them, but you choose which ones you act on.",
        "Filter them the way you filter spam: keep what's useful, delete the rest.",
        "One summary suggests the Eisenhower Matrix: sort by urgent versus important, and give attention only to what's important.",
        "Test any thought: Is this useful? Does it help me act? If not, let it go."]],
      ["Stop \"Thinking\"", "likely Ch. 9", [
        "Much of what we call thinking is really worry: \"What if my business fails?\", \"What if I screw up?\", \"What if I get sick?\"",
        "These questions have no answer, so they don't help. They're unproductive thinking.",
        "The cure is awareness. Sit quietly for a few minutes and watch what passes through your mind. The thoughts are just there, and you pick which ones to focus on."]],
      ["Inside Your Control vs. Outside Your Control", "Ch. 10", [
        "You can't control what happens, but you can control which thoughts get your attention.",
        "Inside your control: your actions, words, intentions and desires. Almost everything else is outside it.",
        "When a thought is about something outside your control, drop it. This removes much of your worry and stress."]],
      ["Don't Trust Your Mind", "Ch. 11", [
        "People often make choices that look irrational in hindsight, because cognitive biases bend how we see reality. Foroux concludes that your mind can't be trusted.",
        "Examples: confirmation bias (favoring information that fits what you already believe) and attentional bias (fixating on some information and ignoring the rest).",
        "The fix: question your beliefs, look for other viewpoints, base decisions on facts, and play devil's advocate against your own position."]],
      ["Release Your Mind", "Ch. 14", [
        "When everything feels like too much, don't push your mind harder. Give it a break.",
        "Become aware of your thoughts, watch them, and let them go.",
        "Rest protects your mental health and your productivity."]]
    ]},
    { name: "Thinking and deciding better", sub: "Facts, patience, reflection, action", chapters: [
      ["Look At Facts", "topic", [
        "Set assumptions aside and rely on evidence. Assumptions lead to wrong actions and unnecessary stress.",
        "Ask: What do I actually know here? What am I just assuming?",
        "When there are no facts to go on, your gut feeling is a reasonable fallback."]],
      ["Take Your Time To Think", "topic", [
        "The greatest thinkers weren't fast thinkers.",
        "Drawing on Derek Sivers: your first reaction is often an old reflex, not a considered view.",
        "It's fine to say \"I don't know\" or \"let me think about it\" instead of rushing an answer."]],
      ["No More Quick Decisions", "Ch. 13", [
        "A quick decision isn't a well-informed one.",
        "Don't agree on impulse to trips, speaking invitations, new hires or other commitments. Weigh them first."]],
      ["Take Time To Reflect", "topic", [
        "Without reflection, your mistakes and weak spots go unnoticed until they turn into a crisis.",
        "Foroux journals and often rereads old entries to see how he was thinking back then."]],
      ["Draw Your Thoughts", "topic", [
        "People thought in pictures long before they had words.",
        "Sketching an idea as a drawing or diagram often makes it clearer than words alone."]],
      ["Connect The Dots", "topic", [
        "Linked to the Steve Jobs line from Ch. 7: events that seem random only make sense in hindsight.",
        "Look back over your experiences for patterns and lessons, then use them going forward."]],
      ["Don't Ask Why", "Ch. 20", [
        "When you hit an obstacle or make a mistake, asking \"why me?\" is pointless and wrecks your peace of mind.",
        "Ask instead: What can I do now to fix this, or to stop it happening again?",
        "Foroux tells of a costly mistake in a business deal (one summary says he sent a contract to the wrong person). Fixing it mattered more than asking why."]],
      ["Think About The Details Even More", "Ch. 21", [
        "Foroux overlooked a small detail that cost him a lot. He says the lesson was worth more than the business he lost.",
        "Small things left unchecked become big problems later. Recheck contracts, emails and agreements."]],
      ["Take Thinking Out Of The Equation", "Ch. 22", [
        "At some point thinking has to stop and action has to start.",
        "Don't try to think your way out of everything. Clarity usually comes after you start acting, not before."]]
    ]},
    { name: "Living without regret", sub: "Chapters 23–26", chapters: [
      ["Don't Live With Regrets", "Ch. 23", [
        "About Foroux's grandmother. She was kind and gave up a lot for her family, including leaving her native Iran to live in the Netherlands. In her last years she often spoke of her regrets.",
        "People regret what they didn't do far more than what they did.",
        "So act on your hopes now, and make choices that match what you really want."]],
      ["Never Look Back", "Ch. 24", [
        "Once you've learned from a mistake, let it go. Replaying the past changes nothing.",
        "Put that energy into a good present and a better future."]],
      ["Spend Your Time Well", "Ch. 25", [
        "Thinking is only useful if it leads to action. Thinking without acting is wasted.",
        "Don't overthink. Give your time to the activities and experiences that matter."]],
      ["Think Beyond Yourself", "Ch. 26", [
        "The book returns to pragmatism and its founders, Peirce and James.",
        "The best use of a clear mind is to make useful things for other people.",
        "Foroux recommends William James's own writing as the place to start."]]
    ]}
  ],
  practices: [
    "Watch your thoughts for a few minutes a day. Sit quietly and notice what passes through, without acting on it.",
    "Run the usefulness test on any recurring thought: Is this useful? Does it lead to action? If not, drop it.",
    "Sort worries by control. Write down which parts you control and which you don't. Act on the first list, let go of the second.",
    "Journal what you think. Try one day of writing down your thoughts every two hours or so, then reread old entries now and then.",
    "Pause before saying yes. Sleep on any new commitment, like a trip, an invitation or a hire.",
    "Check facts before you react. Ask what you know and what you're assuming, and argue against your own view.",
    "Swap \"why?\" for \"what now?\" after any setback.",
    "Double-check the details on contracts, emails and agreements before you send them.",
    "Draw it out. Sketch a hard problem as a diagram.",
    "Take a real break when your mind feels overloaded."
  ],
  quotes: [
    ["A great many people think they are thinking when they are merely rearranging their prejudices.", "William James, quoted at the book's opening"],
    ["You can't connect the dots looking forward; you can only connect them looking backward.", "Steve Jobs, used in the book"],
    ["My conclusion is that your mind can't be trusted.", "Darius Foroux, Don't Trust Your Mind (as quoted in a summary)"],
    ["If you don't take action, your thinking is completely wasted.", "A summary's wording of Spend Your Time Well"],
    ["Filter your thoughts like you filter spam.", "A summary's wording of Filter Your Thoughts"]
  ],
  caveat: "These notes come from published summaries and reviews, not the book's own text. The main lessons agree across sources. Chapter numbers follow one summary's list and a few chapters are placed by topic. Only the James and Jobs quotes are word for word; check the rest against your copy."
}];
