## Accountkit-Android-sample


## Table of Contents

 * [Introduction](#introduction)
 * [Getting Started](#Getting-Started)
 * [Installation](#installation)
 * [Configuration ](#configuration )
 * [Supported Environments](#supported-environments)
 * [Sample Code](#Sample-Code)
 * [License](#license)
 
 
## Introduction
    Android sample code encapsulates APIs of the HUAWEI Account Kit server. It provides sample program for your reference or usage.
    The following describes of Android sample code.

    hmssample: Sample code packages. This package  contains code that implements Sign,  SignInCode and SignOut.
    logger: This packages contains code that implements logger View.

## Getting Started

    1. Register as a developer.
    Register a [HUAWEI account](https://developer.huawei.com/consumer/en/).
    2. Create an app.
    Create an app and enable APIs.
    3. Build the demo.
    To build this demo, please first import the demo to Android Studio (3.X or later). Then download the agconnect-services.json file of the app from AppGallery Connect, and add the file to the app directory (\app) of the demo. For details, please refer to [Preparations for Integrating HUAWEI HMS Core](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/index.html)
## Installation
    To use functions provided by examples, please make sure Huawei Mobile Service 4.0 has been installed on your cellphone.
## Supported Environments
    Android SDK Version >= 23 and JDK version >= 1.8 is recommended.
	
## Configurations  
    The following describes parameters in Contant.java
    CLIENT_ID:  App ID, which can be obtained from AppGallery Connect website, please replace it with your app's client ID.
    CERT_URL:  Request CERT_URL to get public key's URL from jwks_uri, then get public key.
    ID_TOKEN_ISSUE:  The value that ISS string of ID Token should be equal to.
	
## Sample Code
    This demo provides demonstration for following scenarios:
    1. ID-Token Mode Sign In.  Sign in and Id-Token verification are both included.
    2. Authorization Code Mode Sign In. 
    3. Sign out.

   The business logic of ID-Token Mode Sign In and Authorization Code Mode Sign In are implemented in HuaweiIdActivity.java while Id-Token verification are implemented in  IDTokenParse.java.
    

##  License
    Account-kit Android sample is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
