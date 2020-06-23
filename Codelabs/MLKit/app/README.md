## face-demo


## Table of Contents

 * [Introduction](#introduction)
 * [Installation](#installation)
 * [Supported Environments](#supported-environments)
 * [License](#license)


## Introduction
    The sample code describes how to use the face detection service provided by the HMS Core ML SDK to recognize facial features and facial expressions.
    
    Ability called by the demo:
    
    1. MLAnalyzerFactory.getInstance (). GetFaceAnalyzer (MLFaceAnalyzerSetting setting): Create a face recognizer. Creates a face analyzer.
    2. MLFaceAnalyzer.selectTransactor (): Sets a face detection result processor for subsequent processing of the result.
    3. MLFaceAnalyzerSetting.Factory (). SetFeatureType (MLFaceAnalyzerSetting.TYPE_FEATURES): Enables facial expression and feature detection, including smiling, the possibility of opening the eyes, possibility of wearing a beard, and age.
    4. MLFaceAnalyzerSetting.Factory (). AllowTracing (): Indicates whether to enable the face tracking mode.
    5. LensEngine: Camera source that used for generating continuous image data for detection.

## Installation
    Download the HUAWEI-HMS-MLKit-Sample code and open it in Android Studio. Ensure that your device has been connected to the Internet and obtain the APK by building a project.

## Supported Environments
    Devices with Android 4.4 or later are recommended.

##  License
    The face detection sample of HUAWEI ML Kit has obtained the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0).

