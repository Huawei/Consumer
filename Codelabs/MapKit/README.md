mapkit-java-sample


## Table of Contents

 * [Introduction](#introduction)
 * [Getting Started](#Getting-Started)
 * [Installation](#installation)
 * [Configuration ](#configuration )
 * [Supported Environments](#supported-environments)
 * [Sample Code](# Sample Code)
 * [License](#license)


## Introduction
    This mapkit sample encapsulates APIs of the HUAWEI Map Kit. It provides many demos for your reference or usage.

## Getting Started

    1. Register as a developer.
    Register a [HUAWEI account](https://developer.huawei.com/consumer/en/).
    2. Create an app.
    Create an app and enable APIs.
    3. Build the demo.
    To build this demo, please first import the demo to Android Studio (3.X or later). Then download the agconnect-services.json file of the app from AppGallery Connect, and add the file to the app directory (\app) of the demo. For details, please refer to [Preparations for Integrating HUAWEI HMS Core](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/index.html)

## Installation
    Before using this sample code, check whether the android environment and Java environment have been installed. 
    Decompress the mapkit sample code package.
    
    Use Android Studio to open the MapKit folder, synchronize it, and compile it after successful synchronization. Then connect a mobile phone running the Android operating system to your PC and click the run icon (green arrow). The APK is successfully installed on the mobile phone.

## Supported Environments
	Android SDK 27 or a later version is recommended.
	Android Build Tools


​	
## Configuration 
    To use functions provided by packages in examples, you can to use the agconnect-services.json in the app package.
    If you want to create your demo, you need to change the agconnect-services.json file.


​	
## Sample Code
    The sample code integrates the HUAWEI Map Kit capability of HUAWEI HMS Core to implement basic map functions. You can find the sample code project in the MapKit\app\src\main\java\com\huawei\hms\maps\demo directory. The following describes the functions of each class file in the project in sequence:
    
    1) BasicMapDemoActivity: starts activities for loading maps in different ways, including MapViewDemoActivity, MapViewCodeDemoActivity, MapFragmentDemoActivity, MapFragmentCodeDemoActivity, SupportMapDemoActivity, and SupportMapCodeDemoActivity.
    
    2) CameraDemoActivity: animates or instantly moves the map camera by setting camera parameters, including the latitude and longitude, zoom level, tilt angle, and rotation angle, and implements some camera movement listening events.
    
    3) CircleDemo: adds circles to maps and sets circle attributes, including the center position, radius, stroke width, stroke color, fill color, and tag.
    
    4) ControlsDemo: uses gestures to change the view of a map, including the scroll, tilt, rotate, and zoom gestures.
    
    5) DistanceCalculatorDemo: calculates the distance between two points, in meters.
    
    6) EventsDemoActivity: implements the tap and long tap events of maps.
    
    7) GestureDemo: show some controls methods of map.
    
    8) GroundOverlayDemo: adds ground overlays to maps in different ways and sets and obtains ground overlay attributes.
    
    9) LiteModeDemo: enables the lite mode.
    
    10) LocationSourceDemoActivity: obtains location source information.
    
    11) MainActivity: provides the function entry.
    
    12) MapFunctionsDemo: implements basic map functions, such as setting the maximum and minimum zoom levels and map type.
    
    13) MarkerDemoActivity: implements marker-related APIs, including APIs for adding a tag, removing a tag, and setting tag attributes.
    
    14) PolygonDemo: implements polygon-related APIs, including APIs for adding a polygon, removing a polygon, and setting polygon attributes.
    
    15) PolylineDemo: implements polyline-related APIs, including APIs for adding a polyline, removing a polyline, and setting polyline attributes.


​	
##  License
    Mapkit Java sample is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

