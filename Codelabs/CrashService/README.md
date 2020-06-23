## crash quickstart

## Introduction
AppGallery Connect Crash provides lightweight crash analysis service and enables fast integration without coding, helping you quickly learn about the version quality, detect and resolve crash problems, and evaluate the problem impact.

## Preparing the Environment
Before using the quickstart app, prepare your Android development environment.


## Environment Requirements
Android Studio 3.0 or later.

## Configuration
Before running the quickstart app, you need to:
1. If you do not have a HUAWEI Developer account, you need to register an account and pass identity verification.
2. Use your account to sign in to AppGallery Connect, create an app, and set Package type to APK (Android app).
3. Click the app you have just created, go to Develop > Quality > Crash. (The Crash service integrates HUAWEI Analysis Kit for crash event reporting. As a result, you need to enable HUAWEI Analysis Kit before integrating the Crash SDK.)
4. Download the agconnect-services.json file from AppGallery Connect and replace the JSON file of the quickstart app with it.

## Sample Code
The Crash SDK supports crash simulation and crash collection switch setting.
Sample code: src\main\java\com\huawei\agc\quickstart\crash\MainActivity.java

## License
Crash quickstart is licensed under the [Apache License, version 2.0] (http://www.apache.org/licenses/LICENSE-2.0).
