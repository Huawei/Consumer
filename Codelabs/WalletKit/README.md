## pushkit-go-sample


## Table of Contents

 * [Introduction](#introduction)
 * [Installation](#installation)
 * [Configuration ](#configuration )
 * [Supported Environments](#supported-environments)
 * [Sample Code](# Sample Code)
 * [License](#license)
 
 
## Introduction
    Sample code encapsulates APIs of the HUAWEI Wallet Kit client. It provides many sample programs for your reference or usage.

## Installation
    Before using sample code, check whether the android environment has been installed. 
    Decompress the sample code package.
    
	Import the decompressed project to your Android Studio.
    Refresh and sync the project.
	Run the sample on Android device or emulator.
    
## Supported Environments
	Android Studio 3.X
	Java JDK(1.8 or later)
	SDK Platform(26 or later)
	Gradle(4.6 or later)
	
## Configuration 
    NA
	
## Sample Code
    Sample Code for Adding a Loyalty Card Through the SDK.(https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/wallet-guide-integratingsdk)

    1). Create a loyalty card instance.
    For details about the settings, please refer to the API reference.
    
    2). Convert the instance to a JWT.
    You need to implement the function of converting a generated instance to a JSON string and then converting the JSON string to a JWT on your server.
    
    3). Create CreateWalletPassRequest.
    
    4). Create a Client instance.
    
    5). Create a task.
    
    6). Execute the task.
    
    7). Receive the loyalty card adding result.

##  License
    Wallet Kit SDK sample is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

