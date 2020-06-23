# APMS Demo
## Table of Contents
 * [Introduction](#introduction)
 * [Preparation](#preparation)
 * [Environment Requirements](#environment-requirements)
 * [Sample Code](#sample-code)
 * [License](#license)

## Introduction
Helps you improve user experience of your apps by helping you discover and rectify app performance issues.

## Preparation
### 1. Register as a developer.
Register a [HUAWEI account](https://developer.huawei.com/consumer/en/).
### 2. Create an app.
Create an app and set Package type to APK (Android app). Then enable APMS by referring the chapter [Viewing Performance Monitoring Data](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-apms-viewdata).
### 3. Build
To build this demo, please first import the demo in the Android Studio (3.x+). Then download the file "agconnect-services.json" of the app on AGC, and add the file to the app root directory(\app) of the demo. Please refer to the Chapter [Integrating the APMS SDK](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-apms-apmssdk) of the Development Guide.

## Environment Requirements
Android Studio 3.0 or later.

## Sample Code
The APMS SDK supports collecting performance data automatic and collection switch setting.
Sample code: src\main\java\com\example\quickstart\apmsandroiddemo\MainActivity.java

## License
APMS quickstart is licensed under the [Apache License, version 2.0] (http://www.apache.org/licenses/LICENSE-2.0).
