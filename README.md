# MyBooks

An Android app that shows your books as covers. Tap a cover to read what the book taught (one-page
summary, chapter notes, practices to try, quotes), or press **Listen** to hear it read aloud in a
natural voice. No account, no server, no API, no AI model: the voice is your phone's own
text-to-speech and everything stays on the device.

## Install on your phone

1. On the phone, open the repo's **Releases → latest** (`/releases/latest`) and download `mybooks.apk`.
2. Open it and allow installing from your browser or Files app.
3. Open **Book Learnings**.

Every build is signed with the same key (`app/mybooks.keystore`), so a new APK installs over the old one and
keeps your ticked practices.

## What it does

| | |
|---|---|
| **Shelf** | Your books as covers. |
| **In one page** | The book's main lessons, short enough to read in two minutes. |
| **Chapter notes** | Chapter by chapter, grouped into parts. |
| **Practices** | The book's exercises as a checklist. Ticks are saved on the phone. |
| **Quotes** | The lines worth remembering. |
| **Listen** | Reads the open tab aloud, highlights the line being read and scrolls with it. Pause, resume, stop, speed, and a choice of voice. |

### Getting a more natural voice
The app picks the highest quality voice installed on the phone and lets you change it (**Voice**). For the
best sound, open Android **Settings → Text-to-speech output**, make sure *Speech Recognition & Synthesis by
Google* is the engine, and download its higher-quality English voices.

## Add a book

1. Put the cover image in `app/src/main/assets/covers/` (a portrait JPG, about 2:3).
2. Add an entry to `app/src/main/assets/books.json` (copy the Think Straight one) and set its `cover`
   to the file name. If the image is missing, the app draws a cover instead.

## Building

```bash
./gradlew :app:assembleDebug    # needs the Android SDK; CI builds a release APK on every push
```
