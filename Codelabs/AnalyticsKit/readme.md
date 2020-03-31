## HmsAnalyticsKitDemo


## Table of Contents

* [Introduction](#introduction)
* [Installation](#installation)
* [Configuration ](#configuration )
* [Supported Environments](#supported-environments)
* [Sample Code](#Sample-Code)
* [License](#license)


## Introduction
    HmsAnalyticsKitDemo is a app that applying HUAWEI Hianalytics SDK used for showing how to collect user engagement and user preference.
[Read more about Hianalytics](https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/3021001).

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
    1. HUAWEI Analytics Kit 4.0.2
    2. Android SDK applicable to devices using Android 4.2 Jelly Bean (API level 17) and later versions
    3. Android Studio 
    4. Java JDK 1.7 (JDK 1.8 is recommended.)

## Sample Code
After running the app you should see a screen like this:

<img src="app/src/screen_0.PNG" height="534" width="300" style="max-width:100%;">

Click the button TRUE or FALSE to answer the question; Click the NEXT, show the next question; Click POST SCORE, log the score user got. All the infomations will be upload to Hianalytics Console, and you can see these infomations in real time using Debug View.

Click button SETTINGS:

<img src="app/src/screen_1.PNG" height="534" width="300" style="max-width:100%;">

You will be asked what your favorite sport is. This choice will be logged to Hianalytics as a User Property.


##  License
    HmsAnalyticsKitDemo is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

