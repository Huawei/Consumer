# NearbyConnectionDemo
## Table of Contents

 * [Introduction](#introduction)
 * [Installation](#installation)
 * [Supported Environments](#supported-environments)
 * [Procedure](#procedure)
 * [Result](#result)
 * [License](#license)
 
## Introduction
The NearbyConnectionDemo program demonstrates how to register an Android app providing functions including scanning, broadcasting, connection, and data sending data.

## Installation
1. Register as a developer.
Register a [HUAWEI account](https://developer.huawei.com/consumer/en/).
2. Create an app.
Create an app and enable Nearby Service by referring the [Nearby Service Preparations](https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/nearby-service-preparation).
3. Build the demo.
To build this demo, please first import the demo to Android Studio (3.X or later). Then download the agconnect-services.json file of the app from AppGallery Connect, and add the file to the app directory (\app) of the demo. For details, please refer to [Nearby Service Preparations](https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/nearby-service-preparation).
Prepare two Huawei phones, and install the compiled APK by running the adb command to these phones.

## Supported Environments
   Android Studio 3.X.

## Procedure
1. Open the app on both phones, enter your name.
2. Enter your friend's name.
3. Tap BEGIN_TO_CHAT.
4. Tap SEND on either phone and enter the information that you want to send to the peer phone.

## Result
The sample app enables bi-directional data transmission.
<img src="result_1.jpg">
<img src="result_2.jpg">

## License
Nearby Service Connection sample is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).