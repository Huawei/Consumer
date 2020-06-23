## sitekit-java-sample


## Table of Contents

 * [Introduction](#introduction)
 * [Getting Started](#Getting-Started)
 * [Installation](#installation)
 * [Configuration ](#configuration )
 * [Supported Environments](#supported-environments)
 * [Sample Code](# Sample Code)
 * [License](#license)
 
 
## Introduction
    This sitekit sample encapsulates APIs of the HUAWEI site Kit. It provides many demos for your search reference or usage.

## Getting Started

    1. Register as a developer.
    Register a [HUAWEI account](https://developer.huawei.com/consumer/en/).
    2. Create an app.
    Create an app and enable APIs.
    3. Build the demo.
    To build this demo, please first import the demo to Android Studio (3.X or later). Then download the agconnect-services.json file of the app from AppGallery Connect, and add the file to the app directory (\app) of the demo. For details, please refer to [Preparations for Integrating HUAWEI HMS Core](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/index.html)

## Installation
    Before using this sample code, check whether the android environment and Java environment have been installed. 
    Decompress the sitekit sample code package.
    
	Use Android Studio to open the HwSite folder, synchronize it, and compile it after successful synchronization. Then connect a mobile phone running the Android operating system to your PC and click the run icon (green arrow). The APK is successfully installed on the mobile phone.
    
## Supported Environments
	Android SDK 27 or a later version is recommended.
	Android Build Tools
	
	
## Configuration 
    To use functions provided by packages in examples, you can to use the agconnect-services.json in the app package.
	If you want to create your demo, you need to change the agconnect-services.json file.
    
	
## Sample Code
    The sample code integrates the HUAWEI site Kit capability of HUAWEI HMS Core to implement basic site functions. You can find the sample code project in the sitekit\app\src\main\java\com\huawei\hms\site\demo directory. The following describes the functions of each class file in the project in sequence:
	
	1) DetailSearchActivity: search a key word and return a list of place details, including country code, location and other information.
	
	2) NearbySearchActivity: search POI nearby or a specified address, return basic information. 
	
	3) QuerySuggestionActivity: provide a CheckboxSpinner, when we type some words, it returns some query suggestions.
	
	4) TextSearchActivity: search a key word, return a list of related POI information. 
	
	
	
##  License
    sitekit Java sample is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

