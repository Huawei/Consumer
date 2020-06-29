# HmsAnalyticsKitDemo


## Table of Contents

* [Introduction](#introduction)
* [Getting Started](#Getting-Started)
* [Installation](#installation)
* [Configuration ](#configuration )
* [Supported Environments](#supported-environments)
* [Result](#Result)
* [License](#license)


## Introduction
    HmsAnalyticsKitDemo is a app that applying HUAWEI Hianalytics SDK used for showing how to collect user engagement and user preference.
[Read more about Hianalytics](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides-V5/introduction-0000001050745149-V5).

## Getting Started

For more development guidance, please refer to the links below:

[Development Guide](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides-V5/android-dev-process-0000001050163813-V5).

[API References](https://developer.huawei.com/consumer/en/doc/development/HMSCore-References-V5/android-api-analytics-overview-0000001051067140-V5).

We also provide an example to demonstrate the use of analytics SDK for Android.

This sample uses the Gradle build system.

First download the demo by cloning this repository or downloading an archived snapshot.

In Android Studio, use the "Open an existing Android Studio project", and select the directory of "analytics-sample".

You can use the "gradlew build" command to build the project directly.

You should create an app in AppGallery Connect, and obtain the file of agconnect-services.json and add to the project. You should also generate a signing certificate fingerprint and add the certificate file to the project, and add configuration to build.gradle. More to [Development Process](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides-V5/android-dev-process-0000001050163813-V5).


## Installation
    Use Android Studio to open the decompressed project.

## Configuration
    1. Create an app in AppGallery Connect and obtain the project configuration file agconnect-services.json. In Android Studio, switch to the Project view and move the agconnect-services.json file to the root directory of the app-level module. 
    2. Change the value of applicationId in the build.gradle file of the app-level module to the name of the app package applied for in the preceding step.

## Supported Environments
    Hardware requirements:
    1. A computer (desktop or laptop)
    2. A Huawei phone, which is used to debug the developed app
    
    Software requirements:
    1. HUAWEI Analytics Kit 5.0.1
    2. Android SDK applicable to devices using Android 4.4 KitKat (API level 19) and later versions
    3. Android Studio 
    4. Java JDK 1.7 (JDK 1.8 is recommended.)

## Result
After running the app you should see a screen like this:

<img src="images/screen_0.png" height="534" width="300" style="max-width:100%;">

Click the button TRUE or FALSE to answer the question; Click the NEXT, show the next question; Click POST SCORE, log the score user got. All the infomations will be upload to Hianalytics Console, and you can see these infomations in real time using Debug View.

Click button SETTINGS:
<img src="images/screen_1.png" height="534" width="300" style="max-width:100%;">

You will be asked what your favorite sport is. This choice will be logged to Hianalytics as a User Property.


##  License
    HmsAnalyticsKitDemo is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

