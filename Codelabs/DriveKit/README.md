## drivekit-java-sample


## Table of Contents

 * [Introduction](#introduction)
 * [Installation](#installation)
 * [Configuration ](#configuration )
 * [Supported Environments](#supported-environments)
 * [Sample Code](# Sample Code)
 * [License](#license)


## Introduction
    This sample code encapsulates APIs of the HUAWEI Drive Kit SDK. It provides many sample programs for your reference or usage.
    The following describes packages of Java sample code.
    app:       Sample code packages. Each package can run independently.

## Installation
    Before using HUAWEI Drive Kit SDK codelab code, check whether the JAVA environment has been installed.

    Decompress the sample code package "HuaweiDrive_demo.zip".
    Take the Android Studio 3.2 version as an example. The steps to run the Drive service sample code are as follows:
    1. In Android Studio, select "File"->"Open". In the pop-up dialog box, enter the path where the sample code is stored locally, for example: "D:\HuaweiDriveSDK\samples\HuaweiDrive_demo";
    2. Select the HuaweiDrive_demo project to be opened, and then click "OK". In the pop-up dialog box, select "New Window" to open the project in a new window.
    3. In Android Studio, click "Run", then select your device as the target and click "OK" to launch the sample application on your device.

## Supported Environments
    Java 1.8 or a later version is recommended.
    Android Studio 3.2 version or a later version is recommended.

## Configuration
    To use functions provided by packages in examples, you need to set related parameters in pushcommon.go in the common package.
    The following describes parameters in build.gradle and agconnect-services.json

    appId:         App ID, which is obtained from app information.
    applicationId: ID of the application registered on the HUAWEI Developer.

## Sample Code
    Currently, the core capabilities of Drive Kit include uploading, downloading, deleting, trashing, and searching for files in Drive as well as querying and monitoring file changes.
    1. Obtain parameters in the Main function of each Java file in advance. For example, obtain the AT through the HMS SDK.

##  License
    DriveKit SDK Codelab sample is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
