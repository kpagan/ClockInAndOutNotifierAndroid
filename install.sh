#!/bin/bash

APK_PATH="app/build/outputs/apk/debug/app-debug.apk"

adb start-server
adb install -r "$APK_PATH"

echo "Installed successfully"
