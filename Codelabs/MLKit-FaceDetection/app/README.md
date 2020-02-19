## face-demo


## Table of Contents

 * [Introduction](#introduction)
 * [Installation](#installation)
 * [Supported Environments](#supported-environments)
 * [License](#license)


## Introduction
    The sample code mainly shows how to use the face detection SDK. The camera captures the video for real-time detection.
    
    Ability called by the demo:
    
    1. MLAnalyzerFactory.getInstance (). GetFaceAnalyzer (MLFaceAnalyzerSetting): Create a face recognizer. This is the most core class of face recognition.
    2. MLFaceAnalyzer.selectTransactor (): Set the face recognition result processor for subsequent processing of the recognition result.
    3. MLFaceAnalyzerSetting.Factory (). SetFeatureType (MLFaceAnalyzerSetting.TYPE_FEATURES): Turn on facial expression and feature detection, including smile, eyes open, beard and age.
    4. MLFaceAnalyzerSetting.Factory (). AllowTracing (): Whether to start face tracking mode
    5. LensEngine: camera source that generates continuous image data for detection.

## Installation
    Download demo and open in android Studio

## Supported Environments
	android 4.4 or a later version is recommended.

##  License
    ML Kit face demo is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

