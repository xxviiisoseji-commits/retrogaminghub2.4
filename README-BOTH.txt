RETRO GAMING HUB 2.4 — PWA + ANDROID SOURCE

This package contains:
- PWA files at the package root for the existing GitHub Pages site.
- android/ — Android Studio project for the native APK.
- .github/workflows/android-apk.yml — GitHub Actions workflow to compile a debug APK.

WHAT CHANGED IN 2.4
- Refreshed the in-game controls: removed the library/ROM header and fullscreen button, added in-game Settings for save import/export and save-state save/load, an x1/x2 speed toggle, a joystick/D-pad toggle, and mute/unmute.
- Game launch requests fullscreen automatically where supported and remains full-viewport when browser fullscreen is unavailable.
- Corrected the D-pad into a centered plus layout. The mGBA emulation engine and its core configuration remain unchanged.

WHAT CHANGED IN 2.3
- Fixed the startup crash "Cannot access 'speedMultiplier' before initialization" by initializing the mGBA playback speed before the first audio-rate synchronization.
- Added Android safe-area handling so the home screen and Settings dialog stay clear of the status bar/notch. The native WebView no longer draws the home page underneath the status bar.
- Disabled mobile page pinch/double-tap zoom and kept responsive sizing for phone screens.
- Consolidated the duplicate Add a game / Import controls into one Import games action (the empty-library prompt remains when the library is empty).
- Added persistent Tile and List library views; the selected view is remembered on the device.
- Created a new Retro Gaming Hub app icon and applied it to the PWA and Android launcher.
- Updated Android versionName to 2.3.0 and versionCode to 3.
- Preserved the existing IndexedDB database name/version and app ID so an in-place update can retain local data if Android accepts the APK signature.

PWA UPDATE
1. Extract this ZIP.
2. In the root of the existing GitHub Pages repository, replace index.html, manifest.webmanifest, sw.js, and icons/ with the package-root versions.
3. Commit the changes.
4. Open the GitHub Pages site in Chrome, allow the new service worker to activate, then close and relaunch the installed PWA.
5. If the old UI remains, close all PWA windows and open the site once in Chrome before relaunching.

ANDROID APK BUILD
1. Replace the repository's android/ folder with this package's android/ folder.
2. Replace .github/workflows/android-apk.yml with the included workflow.
3. Commit the changes.
4. Open GitHub Actions → Build Android APK and wait for a successful run.
5. Download the Retro-Gaming-Hub-Android-debug-apk artifact and extract app-debug.apk.
6. Install it on Android.

IMPORTANT INSTALLATION / DATA NOTE
- Keep the same Android application ID: com.retrogaminghub.app.
- Android only installs this APK over an existing installation if its signing certificate matches. GitHub-hosted debug builds may use a newly generated debug key, so an update may be rejected with a signature conflict.
- Do NOT uninstall the old app until important .sav files have been exported and backed up. Uninstalling the Android app removes its private WebView storage. If a signature conflict appears, stop and ask for help before uninstalling.
- The PWA and native APK have separate local storage. PWA data does not automatically transfer into the APK.

The APK is a debug build, not a Play Store-signed release. No ROMs are included. Import only game files you are authorized to use.
