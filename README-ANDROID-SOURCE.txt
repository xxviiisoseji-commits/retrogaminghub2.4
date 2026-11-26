RETRO GAMING HUB 2.3 — ANDROID SOURCE

This is the Android Studio project and GitHub Actions workflow for the native APK.

To update your existing repository:
1. Replace the android/ folder with this package's android/ folder.
2. Replace .github/workflows/android-apk.yml with the included workflow.
3. Commit and push.
4. Open GitHub Actions → Build Android APK.
5. Download the Retro-Gaming-Hub-Android-debug-apk artifact from the successful run.

Version 2.3 includes the mGBA startup fix, Android notch/status-bar safe layout (including Android 15 edge-to-edge handling), responsive no-zoom homepage, unified import action, tile/list library modes, and the new Android launcher icon.

DATA WARNING: Do not uninstall the old app until saves are backed up. An APK signed with a different key cannot update the existing installation; GitHub-hosted debug builds may use a different ephemeral debug key. If Android reports a signature conflict, stop before uninstalling.

Application ID: com.retrogaminghub.app
Version: 2.3.0 (versionCode 3)
