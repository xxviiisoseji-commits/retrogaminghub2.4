RETRO GAMING HUB 2.3 — ANDROID PWA

Upload these files and folders to the root of your existing GitHub Pages repository, replacing the old PWA files:
- index.html
- manifest.webmanifest
- sw.js
- icons/

Keep the same GitHub Pages origin to retain the existing PWA IndexedDB library and save data. After committing, open the site in Chrome and let the updated service worker activate, then close and relaunch the installed PWA.

Version 2.3 changes:
- Fixes the speedMultiplier initialization crash during mGBA startup.
- Adds notch/status-bar safe spacing for the homepage and Settings dialog.
- Prevents accidental mobile pinch/double-tap page zoom.
- Consolidates Add a game and Import into one Import games action.
- Adds persistent Tile and List library views.
- Includes the new app icon.

Features retained: mGBA-wasm 0.3.1, local ROM library, save persistence, joystick/D-pad selection, 2× fast-forward, and portrait-first touch controls.

No ROMs are included. Import only game files you are authorized to use.
