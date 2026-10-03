# Safe Downloader — Android V1

A small Android downloader starter project.

## What V1 does
- Accepts a direct HTTP/HTTPS media/file URL.
- Supports Android Share -> Safe Downloader for text URLs.
- Uses Android DownloadManager.
- Saves downloads to the public Downloads folder.
- Shows completion notifications.

## What V1 intentionally does not do
- No DRM bypass.
- No private-content access.
- No paywall/access-control bypass.
- No credential collection.
- No platform restriction circumvention.

## Build
Open this folder in Android Studio and let Gradle sync.
Then use Build > Build APK(s).

## Next versions
A more advanced version can add:
- download queue
- pause/resume UI
- history
- filename editing
- media preview
- format/quality selection where the source explicitly provides those formats
- privacy policy and Play Store assets
- tests and crash reporting
