## FIDO2 Sample


## Table of Contents

 * [Introduction](#introduction)
 * [Getting Started](#Getting-Started)
 * [Installation](#installation)
 * [Configuration ](#configuration )
 * [Supported Environments](#supported-environments)
 * [Sample Code](# Sample Code)
 * [License](#license)
 
 
## Introduction
    FIDO2 Sample provides many sample programs for your reference or usage.

## Getting Started

    1. Register as a developer.
    Register a [HUAWEI account](https://developer.huawei.com/consumer/en/).
    2. Create an app.
    Create an app and enable APIs.
    3. Build the demo.
    To build this demo, please first import the demo to Android Studio (3.X or later). Then download the agconnect-services.json file of the app from AppGallery Connect, and add the file to the app directory (\app) of the demo. For details, please refer to [Preparations for Integrating HUAWEI HMS Core](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/index.html)

## Installation
    Before using FIDO2 Sample code, check whether the java environment and Android Studio have been installed. 
    Decompress the FIDO2 Sample code package.
        
## Supported Environments
	Java 1.7 or a later version is recommended.
	
## Configuration 
    NA
    
	
## Sample Code

FIDO2 includes two operations: registration and authentication. The processes are similar for the two operations.
    1). Create an activity.

    2). Obtain the challenge value and related policy from the FIDO server, and initiate a request.

    3). Call Fido2Client.getRegistrationIntent() to initiate registration, or call Fido2Client.getAuthenticationIntent() to initiate authentication.

    4). Call Fido2Intent.launchFido2Activity() in the callback to start registration (requestCode is Fido2Client.REGISTRATION_REQUEST) or authentication (requestCode is Fido2Client.AUTHENTICATION_REQUEST). The callback will be executed in the main thread.

    5). Call Fido2Client.getFido2RegistrationResponse() or Fido2Client.getFido2AuthenticationResponse() in the callback Activity.onActivityResult() to obtain the registration or authentication result.

    6). Send the registration or authentication result to the FIDO server for verification. 



##  License
    FIDO2 Sample is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

