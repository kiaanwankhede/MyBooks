# MyBooks

A shelf of book covers. Tap a cover to read what the book taught (one-page summary, chapter notes,
practices, quotes), or press **Listen** to hear it read aloud.

An installable app (PWA): add it to your home screen and it opens full-screen and works offline.
No build step, no API, no model.

## Install
Host the folder over HTTPS (for example GitHub Pages), open it on your phone, then
**Add to Home Screen** (iPhone: Share menu; Android Chrome: menu, Install app).
On a computer, Chrome and Edge show an install icon in the address bar.

## Voice reader
Uses the browser's built-in speech (`speechSynthesis`), so it runs offline on your device. The sound
depends on the voices installed there. Pick a voice marked *Natural* (Edge on Windows) or
*Enhanced / Premium* (iPhone, Mac) for the most human result.

## Add a book
1. Put the real cover at `covers/<book-id>.jpg` (about 2:3 portrait). Until it exists, a drawn cover is shown.
2. Add an entry to `data/books.js` (copy the Think Straight one).

## Files
- `index.html`, `styles.css`, `app.js`: the app
- `data/books.js`: book notes
- `covers/`: cover images
