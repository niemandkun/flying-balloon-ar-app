## Flying balloon AR app

<a href="https://github.com/niemandkun/flying-balloon-ar-app/blob/master/screenshot.png">
  <img src="https://raw.githubusercontent.com/niemandkun/flying-balloon-ar-app/master/screenshot.png" height="400" align="right">
</a>

Simple AR game about blowing off a balloon to a portal.

How to play:

1. Print image of the balloon from `images-db/balloon.png` and place it horizontally on floor
2. Install the app on Android smartphone and launch it
3. Scan image of the balloon with the camera of your smartphone
4. A flying balloon should appear in AR above the physical image
5. Blow air in the mic of your smartphone to accelerate the balloon
6. Deliver the balloon to the portal with the strength of your lungs!

### Build

Debug build:

```
./gradlew assembleDebug
```

Release build (unsigned):

```
./gradlew assembleRelease
```

### Install

```
adb install app/build/outputs/apk/standard/debug/app-standard-debug.apk
```

Or simply copy/move `app-standard-debug.apk` on your smartphone and install it through File Manager.

### Requirements

To run this app your Android device must be compatible with ARCore.
List of devices supported by ARCore may be found here: https://developers.google.com/ar/devices

