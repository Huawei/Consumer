# HmsAnalyticsKitDemo


## Table of Contents

* [Introduction](#introduction)
* [Getting Started](#Getting-Started)
* [Installation](#installation)
* [Configuration ](#configuration )
* [Supported Environments](#supported-environments)
* [Result](#result)
* [License](#license)


## Introduction
	Hmsanalyticskitdemo provides an example of integrating HUAWEI Analytics Kit with an app. It illustrates how to collect predefined events and custom events on two pages, namely, question page or user attribute setting page. If you click True or False on the question page, automatically collected events including page exit and page entry will be logged when the page is switched.
	[Read more about Hianalytics](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides-V5/introduction-0000001050745149-V5).
	

## Getting Started

For more development guidance, please refer to the links below:

Development Guide: https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides-V5/ios-dev-process-0000001050168431-V5.

API References: https://developer.huawei.com/consumer/en/doc/development/HMSCore-References-V5/ios-api-huawei-analytics-overview-0000001050164673-V5.

We also provide an example to demonstrate the use of analytics SDK for iOS.

This sample uses the CocoaPods Tool,a dependency manager for Swift and Objective-C Cocoa projects.

First download the demo by cloning this repository or downloading an archived snapshot.

Open the CLI and navigate to the location of the Xcode project.Edit the Podfile file,add pod 'HiAnalytics', that is, the dependency for pod. 

Install the pod and open the .xcworkspace file to view the project.

You should create an iOS app in AppGallery Connect, and obtain the file of agconnect-services.plist and add to the project. You should also generate a signing certificate fingerprint and add the certificate file to the project,  More to https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides-V5/ios-dev-process-0000001050168431-V5.

## Installation
    Use Xcode to open the decompressed project.

## Configuration
    1. Create an iOS app in AppGallery Connect and obtain the project configuration file agconnect-services.plist. In Xcode, switch to the Project view and move the agconnect-services.plist file to the root directory of the app-level module. 

## Supported Environments
    Hardware requirements:
    1. A MAC computer (desktop or laptop)
    2. A iPhone phone, which is used to debug the developed app
    
    Software requirements:
    1. HUAWEI Analytics Kit 5.0.1.300
    2. Xcode


## Result
After running the app you should see a screen like this:

Click the button TRUE or FALSE to answer the question; Click the Setting to 
set user profiles.

Click button SETTINGS:
When you click the Setting button, you will be asked for your gender, age, and favorite sports. These information will be logged as users attributes by HUAWEI Analytics Kit.


##  License
    HmsAnalyticsKitDemo is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

