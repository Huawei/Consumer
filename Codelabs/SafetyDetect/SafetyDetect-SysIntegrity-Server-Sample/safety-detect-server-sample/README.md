## SafetyDetect-SysIntegrity-Server-sample


## Table of Contents

 * [Introduction](#introduction)
 * [Installation](#installation)
 * [Configuration ](#configuration )
 * [Supported Environments](#supported-environments)
 * [Sample Code](#Sample Code)
 * [License](#license)


## Introduction
    SafetyDetect SysIntegrity Server Sample provides sample program to verify the check result on your server.

## Installation
    Before using SafetyDetect SysIntegrity Server Sample code, check whether java environment and Maven has been installed.
    Decompress the SafetyDetect SysIntegrity Server sample code package.

## Supported Environments
	Java 1.7 or a later version is recommended.

## Configuration
    No additional configuration is required.

## Sample Code
```json
1. Parse the JWS-format result to obtain header, payload, and signature.
2. Obtain the certificate chain from header and use the HUAWEI CBG Root CA certificate to verify it.
3. Verify the domain name of the leaf certificate in the certificate chain. The correct domain name is sysintegrity.platform.hicloud.com.
4. Obtain the signature from signature and verify it.
5. Obtain the integrity verification result from payload. The format and example are as follows:
{
    "advice":"RESTORE_TO_FACTORY_ROM",
    "apkCertificateDigestSha256":[
        "yT5JtXRgeIgXssx1gQTsMA9GzM9ER4xAgCsCC69Fz3I="
    ],
    "apkDigestSha256":"6Ihk8Wcv1MLm0O5KUCEVYCI/0KWzAHn9DyN38R3WYu8=",
    "apkPackageName":"com.huawei.hms.safetydetectsample",
    "basicIntegrity":false,
    "nonce":"R2Rra24fVm5xa2Mg",
    "timestampMs":1571708929141
}
More API information please visit 
https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/27319309
```

##  License
    SafetyDetect SysIntegrity Server Sample is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

