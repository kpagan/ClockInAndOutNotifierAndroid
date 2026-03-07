# Build APK (Android Studio)
## Method 1 — Debug APK

Build → Build APK(s)

File created at:

`app/build/outputs/apk/debug/app-debug.apk`

## Method 2 — Release APK

Build → Generate Signed Bundle/APK

1. Choose APK
2. Create keystore
3. Select release
4. Finish

# ⚡ One-Click Install Script
## macOS / Linux

Create file:

`install.sh`

```shell
#!/bin/bash

APK_PATH="app/build/outputs/apk/debug/app-debug.apk"

adb start-server
adb install -r "$APK_PATH"

echo "Installed successfully"
```
Run:

```
chmod +x install.sh
./install.sh
```

## Windows

Create:

`install.bat`

```shell
adb start-server
adb install -r app\build\outputs\apk\debug\app-debug.apk
echo Installed successfully
pause
```
