## HiHealth kit demo

## Table of Contents

 * [Introduction](#introduction)
 * [Installation](#installation)
 * [Configuration ](#configuration )
 * [Supported Environments](#supported-environments)
 * [demo Code](#demo-Code)
 * [License](#license)

## Introduction
    HiHealthkit demo code encapsulates APIs of the HUAWEI HiHealth Kit. It provides demo programs for your reference or usage.
	open https://developer.huawei.com/consumer/cn/codelab/HiHealthKit/index.html#0 for more detail 

## Installation
    Before using HiHealthkit demo code, check whether the IDE environment has been installed. 
	1) Decompress the demo code package.    
    2) Copy the HiHealthkit package into the IDE directory and import it into the IDE Tool.
	3) Press Sync Project with Gradle Files to ensure the development environment builds completed.

## Supported Environments
	* Android Studio 3.0 or later
	* Java SDK 1.8 or later

## Configuration 
    To use functions provided by packages in examples, you need to set parameters as following:
	
	1) HuaweiAccount: HiHealthKit need verify the user's permission to operate user's health data
	2) download the healthkit jar: 
		open the url 	https://developer.huawei.com/consumer/cn/doc/development/health-Library/31206 
        downlaod the file named "HUAWEI HiHealth_kit" at the bottom, then you will get a zip package containing a jar named hihealthkit-2.0.1.jar 	
    3) create the app\libs directory in the project, copy the hihealthkit-2.0.1.jar to the app\libs directory.
	4) add the jar file dependence:  add "implementation fileTree(dir: 'libs', include: ['*.jar'])" in the dependencies tag of the build.gradle in the app directory.
	
## Demo Code
    HUAWEI HiHealthkit is an open service for sports & health capabilities provided by Huawei. 
	Developers can access the Huawei HiHealth Platform and obtain sports & health data by integrating HUAWEI HiHealthkit.
	1) register callback function 
	register callback function to get status or data
	Code location: app\src\main\java\com\huawei\codelabs\hihealth\happysport\viewmodels\HiHealthKitAdapter\HiHealthKitAdapter.java#registerSportListener
    2) unregister callback function
    The function is used to stop reading sport data.
    Code location: \app\src\main\java\com\health\demo\activity\hihealth\HihealthKitDataManagerActivity.java#unregisterSportListener

##  License
    HiHealthkit demo is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
