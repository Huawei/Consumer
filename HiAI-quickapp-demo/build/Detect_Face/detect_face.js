/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, {
/******/ 				configurable: false,
/******/ 				enumerable: true,
/******/ 				get: getter
/******/ 			});
/******/ 		}
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 3);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "errCodeList", function() { return errCodeList; });
const errCodeList = {
  "202": "参数错误",
  "203": "服务不可用",
  "800": "一般性检测失败",
  "801": "模型文件错误",
  "802": "模型加载失败",
  "803": "神经网络处理单元错误",
  "804": "录音相关错误",
  "805": "客户端未调用引擎初始化",
  "806": "没有声音",
  "807": "未匹配识别结果",
  "808": "客户端权限不足",
  "809": "服务端权限不足",
  "810": "设备AIEngine的功能开关被关闭了，无法使用",
  "811": "不支持的功能或接口"
};



/***/ }),
/* 1 */,
/* 2 */,
/* 3 */
/***/ (function(module, exports, __webpack_require__) {

var $app_template$ = __webpack_require__(4)
var $app_style$ = __webpack_require__(5)
var $app_script$ = __webpack_require__(6)

$app_define$('@app-component/detect_face', [], function($app_require$, $app_exports$, $app_module$){
     $app_script$($app_module$, $app_exports$, $app_require$)
     if ($app_exports$.__esModule && $app_exports$.default) {
            $app_module$.exports = $app_exports$.default
        }
     $app_module$.exports.template = $app_template$
     $app_module$.exports.style = $app_style$
})

$app_bootstrap$('@app-component/detect_face',{ packagerName:'fa-toolkit', packagerVersion: '1.0.8-Stable.300'})

/***/ }),
/* 4 */
/***/ (function(module, exports) {

module.exports = {
  "type": "div",
  "attr": {
    "id": "container"
  },
  "classList": [
    "container"
  ],
  "id": "container",
  "children": [
    {
      "type": "stack",
      "attr": {},
      "classList": [
        "top-container"
      ],
      "children": [
        {
          "type": "image",
          "attr": {
            "src": function () {return this.imageUri},
            "id": "face-image-id"
          },
          "classList": [
            "face-image"
          ],
          "id": "face-image-id"
        },
        {
          "type": "canvas",
          "attr": {
            "id": "canvas"
          },
          "id": "canvas",
          "style": {
            "flex": 1,
            "width": function () {return this.canvasWidth},
            "height": function () {return this.canvasHeight}
          },
          "events": {
            "click": "select"
          }
        }
      ]
    },
    {
      "type": "div",
      "attr": {},
      "classList": [
        "bottom-container"
      ],
      "children": [
        {
          "type": "tabs",
          "attr": {},
          "children": [
            {
              "type": "tab-content",
              "attr": {},
              "classList": [
                "result-group-container"
              ],
              "children": [
                {
                  "type": "div",
                  "attr": {},
                  "classList": [
                    "result-content"
                  ],
                  "children": [
                    {
                      "type": "div",
                      "attr": {},
                      "classList": [
                        "result-title"
                      ],
                      "children": [
                        {
                          "type": "text",
                          "attr": {
                            "value": "分析结果"
                          },
                          "classList": [
                            "result-title-detail"
                          ]
                        }
                      ]
                    },
                    {
                      "type": "div",
                      "attr": {},
                      "classList": [
                        "result-line"
                      ]
                    },
                    {
                      "type": "div",
                      "attr": {},
                      "classList": [
                        "result-item-content"
                      ],
                      "children": [
                        {
                          "type": "list",
                          "attr": {
                            "id": "list"
                          },
                          "classList": [
                            "result-list"
                          ],
                          "id": "list",
                          "children": [
                            {
                              "type": "block",
                              "attr": {},
                              "repeat": function () {return this.result_list},
                              "children": [
                                {
                                  "type": "list-item",
                                  "attr": {
                                    "type": "result"
                                  },
                                  "classList": [
                                    "items"
                                  ],
                                  "children": [
                                    {
                                      "type": "div",
                                      "attr": {},
                                      "classList": [
                                        "item-li"
                                      ],
                                      "children": [
                                        {
                                          "type": "text",
                                          "attr": {
                                            "value": function () {return this.$item}
                                          },
                                          "classList": [
                                            "item-li-detail"
                                          ]
                                        }
                                      ]
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        }
                      ]
                    }
                  ]
                }
              ]
            }
          ]
        }
      ]
    },
    {
      "type": "div",
      "attr": {},
      "classList": [
        "select-container"
      ],
      "children": [
        {
          "type": "input",
          "attr": {
            "type": "button",
            "value": "上传图片"
          },
          "classList": [
            "select-btn"
          ],
          "events": {
            "click": "select"
          }
        }
      ]
    },
    {
      "type": "div",
      "attr": {},
      "classList": function () {return ['mark', this.isShow]},
      "events": {
        "click": "cancel"
      },
      "children": [
        {
          "type": "div",
          "attr": {},
          "classList": function () {return ['popup-container', this.isShow]},
          "children": [
            {
              "type": "text",
              "attr": {
                "value": "拍照"
              },
              "classList": [
                "popup-text"
              ],
              "events": {
                "click": function (evt) {this.selectMedia('拍照',evt)}
              }
            },
            {
              "type": "div",
              "attr": {},
              "classList": [
                "popup-line1"
              ]
            },
            {
              "type": "text",
              "attr": {
                "value": "从相册中选择"
              },
              "classList": [
                "popup-text"
              ],
              "events": {
                "click": function (evt) {this.selectMedia('从相册中选择',evt)}
              }
            },
            {
              "type": "div",
              "attr": {},
              "classList": [
                "popup-line2"
              ]
            },
            {
              "type": "text",
              "attr": {
                "value": "取消"
              },
              "classList": [
                "popup-text"
              ],
              "events": {
                "click": function (evt) {this.selectMedia('取消',evt)}
              }
            }
          ]
        }
      ]
    }
  ]
}

/***/ }),
/* 5 */
/***/ (function(module, exports) {

module.exports = {
  ".container": {
    "flex": 1,
    "flexDirection": "column",
    "backgroundColor": "#f0ecec"
  },
  ".top-container": {
    "flexDirection": "column",
    "width": "100%",
    "height": "45%",
    "alignItems": "center"
  },
  ".face-image": {
    "flex": 1
  },
  ".bottom-container": {
    "width": "100%",
    "height": "55%",
    "backgroundColor": "#ffffff",
    "flexDirection": "column"
  },
  ".result-group-container": {
    "flex": 1,
    "flexDirection": "column",
    "backgroundColor": "#ffffff"
  },
  ".result-content": {
    "flexDirection": "column",
    "flex": 1
  },
  ".result-title-detail": {
    "paddingLeft": "30px",
    "paddingBottom": "20px",
    "paddingTop": "20px",
    "height": "90px",
    "fontSize": "40px",
    "fontFamily": "Times New Roman, Times, serif"
  },
  ".result-line": {
    "marginTop": "5px",
    "width": "100%",
    "height": "1px",
    "backgroundColor": "#f0ecec"
  },
  ".result-item-content": {
    "flexDirection": "column",
    "flex": 1,
    "paddingTop": "30px",
    "paddingRight": "30px",
    "paddingBottom": "30px",
    "paddingLeft": "30px",
    "display": "flex"
  },
  ".result-list": {
    "width": "100%",
    "height": "400px"
  },
  ".items": {
    "width": "100%",
    "height": "80px"
  },
  ".item-li": {
    "alignItems": "center"
  },
  ".item-li-detail": {
    "fontSize": "30px",
    "color": "#000000",
    "flex": 1
  },
  ".select-container": {
    "position": "fixed",
    "width": "100%",
    "height": "100px",
    "flexDirection": "column",
    "alignItems": "center",
    "bottom": "40px"
  },
  ".select-btn": {
    "width": "80%",
    "height": "100%",
    "backgroundColor": "#1478fa",
    "textAlign": "center",
    "color": "#ffffff",
    "fontSize": "40px",
    "borderRadius": "10px"
  },
  ".mark": {
    "position": "fixed",
    "display": "none",
    "left": "0px",
    "top": "0px",
    "width": "100%",
    "height": "100%",
    "backgroundColor": "#d3d3d3",
    "opacity": 0.5
  },
  ".popup-container": {
    "width": "100%",
    "height": "315px",
    "display": "none",
    "backgroundColor": "#ffffff",
    "flexDirection": "column",
    "position": "fixed",
    "bottom": "0px",
    "borderTopWidth": "1px",
    "borderRightWidth": "1px",
    "borderBottomWidth": "1px",
    "borderLeftWidth": "1px",
    "borderStyle": "solid",
    "borderTopColor": "#f0ecec",
    "borderRightColor": "#f0ecec",
    "borderBottomColor": "#f0ecec",
    "borderLeftColor": "#f0ecec"
  },
  ".show": {
    "display": "flex"
  },
  ".popup-line1": {
    "width": "100%",
    "height": "1px",
    "backgroundColor": "#f0ecec"
  },
  ".popup-text": {
    "width": "100%",
    "height": "100px",
    "textAlign": "center",
    "fontSize": "30px",
    "color": "#000000"
  },
  ".popup-line2": {
    "width": "100%",
    "height": "10px",
    "backgroundColor": "#f0ecec"
  }
}

/***/ }),
/* 6 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = function(module, exports, $app_require$){'use strict';

var _system = $app_require$('@app-module/system.ai');

var _system2 = _interopRequireDefault(_system);

var _system3 = $app_require$('@app-module/system.media');

var _system4 = _interopRequireDefault(_system3);

var _system5 = $app_require$('@app-module/system.image');

var _system6 = _interopRequireDefault(_system5);

var _system7 = $app_require$('@app-module/system.device');

var _system8 = _interopRequireDefault(_system7);

var _data = __webpack_require__(0);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

module.exports = {
  data: {
    list: ["拍照", "从相册中选择", "取消"],
    imageUri: "/Common/img/initial_picture_one.png",
    result_list: [],
    isShow: '',
    pointArray: [],
    level: '',
    canvasWidth: "750px",
    canvasHeight: "600px",
    isFirst: true,
    screenWidth: '',
    screenHeight: ''
  },

  onInit: function onInit() {
    this.$page.setTitleBar({ text: '人脸检测' });
    var that = this;

    _system8.default.getInfo({
      success: function success(ret) {
        console.log('handling success');
        that.screenWidth = ret.screenWidth;
        that.screenHeight = ret.screenHeight;
        console.log("屏幕宽度：" + ret.screenWidth, "屏幕高度: " + ret.screenHeight);
        that.selectOneImage();
      }
    });
  },

  select: function select() {
    var that = this;
    that.isShow = 'show';
  },

  cancel: function cancel() {
    var that = this;
    that.isShow = '';
  },

  selectMedia: function selectMedia(e) {
    var that = this;
    if (e === "拍照") {
      that.takePhoto();
    } else if (e === "从相册中选择") {
      that.selectOneImage();
    } else {
      that.cancel();
    }
  },

  takePhoto: function takePhoto() {
    var that = this;
    that.isShow = '';
    that.takephotobtn().then(function (data) {
      that.caculateRatio(data);
    });
  },

  selectOneImage: function selectOneImage() {
    var that = this;
    that.isShow = '';
    that.pickphotobtn().then(function (data) {
      that.caculateRatio(data);
    });
  },

  caculateRatio: function caculateRatio(data) {
    var that = this;
    var real_width = data.width;
    var real_height = data.height;

    var caculateHeight = that.screenHeight / 2040 * 580;
    console.log("caculateHeight" + caculateHeight);
    if (data.height > data.width) {
      var ratio = data.height / caculateHeight;
      data.height = caculateHeight;
      data.width = Math.ceil(data.width / ratio) % 2 != 0 ? Math.ceil(data.width / ratio) - 1 : Math.ceil(data.width / ratio);
      console.log("第一个if");
    } else if (real_height < real_width) {
      data.height = caculateHeight;
      var ratio = real_height / data.height;
      data.width = Math.ceil(data.width / ratio) % 2 != 0 ? Math.ceil(data.width / ratio) - 1 : Math.ceil(data.width / ratio);
      console.log("第二个if");
    }

    that.ratio = (real_width / data.width).toFixed(3);
    console.log("压缩后的宽高：" + data.width + "," + data.height);

    that.canvasWidth = data.width + "px";
    that.canvasHeight = data.height + "px";
    console.log("压缩比例：" + (data.width / real_width).toFixed(3));
    console.log("长宽：" + (data.width / that.ratio).toFixed(1) + "," + (data.height / that.ratio).toFixed(1));

    var args = [data.uri, 70];
    if (parseFloat(that.ratio) != 1) {
      args.push(that.ratio);
    }
    console.log("compress args-------", args);
    var promise = that.compressImagebtn.apply({}, args);
    promise.then(function (data) {
      that.imageUri = data.uri;
      console.log("pickphoto图片url:" + that.imageUri);
      that.detectFace();
    });
  },

  pickphotobtn: function pickphotobtn() {
    var that = this;
    return new Promise(function (resolve, reject) {
      if (that.isFirst === true) {
        _system6.default.getImageInfo({
          uri: that.imageUri,
          success: function success(imageInfo) {
            console.log("原始-拍照-图片路径：" + imageInfo.uri + "宽度：" + imageInfo.width + "px,高度：" + imageInfo.height + "px,尺寸：" + (imageInfo.size / 1024).toFixed(2) + 'KB');

            resolve(imageInfo);
          },
          fail: function fail(imageInfo, code) {
            console.log("imageInfo: " + imageInfo, "code: " + code);
          }
        });
        that.isFirst = false;
      } else {
        _system4.default.pickImage({
          success: function success(data) {
            console.log('pickphoto完成');
            _system6.default.getImageInfo({
              uri: data.uri,
              success: function success(imageInfo) {
                console.log("原始-拍照-图片路径：" + imageInfo.uri + "宽度：" + imageInfo.width + "px,高度：" + imageInfo.height + "px,尺寸：" + (imageInfo.size / 1024).toFixed(2) + 'KB');

                resolve(imageInfo);
              },
              fail: function fail(imageInfo, code) {
                console.log("imageInfo: " + imageInfo, "code: " + code);
              }
            });
          }

        });
      }
    });
  },

  takephotobtn: function takephotobtn() {
    return new Promise(function (resolve, reject) {
      _system4.default.takePhoto({
        success: function success(data) {
          console.log('takephoto完成');
          _system6.default.getImageInfo({
            uri: data.uri,
            success: function success(imageInfo) {
              console.log("原始-拍照-图片路径：" + imageInfo.uri + "宽度：" + imageInfo.width + "px,高度：" + imageInfo.height + "px,尺寸：" + (imageInfo.size / 1024).toFixed(2) + 'KB');

              resolve(imageInfo);
            },
            fail: function fail(imageInfo, code) {
              console.log("imageInfo: " + imageInfo, "code: " + code);
            }
          });
        }
      });
    });
  },

  compressImagebtn: function compressImagebtn(uri, quality, ratio) {
    return new Promise(function (resolve, reject) {
      _system6.default.compressImage({
        uri: uri,
        quality: quality,
        ratio: ratio ? ratio : undefined,
        format: "JPEG",
        success: function success(compressedImgInfo) {
          _system6.default.getImageInfo({
            uri: compressedImgInfo.uri,
            success: function success(imageInfo) {
              console.log("图片路径：" + imageInfo.uri + "宽度：" + imageInfo.width + "px,高度：" + imageInfo.height + "px,尺寸：" + (imageInfo.size / 1024).toFixed(2) + 'KB');
              if (imageInfo.height % 2 != 0) {
                _system6.default.applyOperations({
                  uri: imageInfo.uri,

                  operations: [{
                    action: 'crop',
                    width: imageInfo.width,
                    height: imageInfo.height - 1
                  }],
                  quality: 70,
                  format: 'JPEG',
                  success: function success(data) {
                    _system6.default.getImageInfo({
                      uri: data.uri,
                      success: function success(operationImgInfo) {
                        console.log("裁剪后-图片路径：" + operationImgInfo.uri + "宽度：" + operationImgInfo.width + "px,高度：" + operationImgInfo.height + "px,尺寸：" + (operationImgInfo.size / 1024).toFixed(2) + 'KB');

                        resolve(operationImgInfo);
                      },
                      fail: function fail(operationImgInfo, code) {
                        console.log("handling fail, code=" + code);
                      }
                    });
                  },
                  fail: function fail(data, code) {
                    console.log("handling fail, code=" + code);
                  }
                });
              } else {
                console.log("不剪裁");
                resolve(imageInfo);
              }
            },
            fail: function fail(imageInfo, code) {
              console.log("compressImagebtn getImageInfo fail, code=" + code);
              reject(imageInfo);
            }
          });
        },
        fail: function fail(compressedImgInfo, code) {
          console.log("compressImagebtn fail, code=" + code);
        }
      });
    });
  },

  detectFace: function detectFace() {
    var that = this;
    var tempList = [];

    var clearRect = that.$element("canvas");
    var ctx = clearRect.getContext("2d");

    ctx.clearRect(0, 0, that.screenWidth, 800);

    _system2.default.detectFace({
      uri: that.imageUri,
      success: function success(data) {
        console.log("success data:" + JSON.stringify(data));
        that.pointArray = data.faces;
        var faceNumber = "人脸个数：  " + data.faces.length;
        tempList.push(faceNumber);
        that.result_list = tempList;
      },
      fail: function fail(data, code) {
        console.log("handling fail, code: " + code);
        var failCode = "错误码：  " + _data.errCodeList[code];
        that.result_list.push(failCode);
      },
      complete: function complete() {
        console.log("handling complete");

        var cans = that.$element('canvas');
        var ctx = cans.getContext("2d");
        for (var i = 0; i < that.pointArray.length; i++) {
          ctx.strokeStyle = "#00ffff";
          ctx.lineWidth = 5;
          console.log("left:" + that.pointArray[i].faceRect.left + "top:" + that.pointArray[i].faceRect.top + "width:" + that.pointArray[i].faceRect.width + "height:" + that.pointArray[i].faceRect.height);
          ctx.strokeRect(that.pointArray[i].faceRect.left, that.pointArray[i].faceRect.top, that.pointArray[i].faceRect.width, that.pointArray[i].faceRect.height);
        }
      }
    });
  }
};
var moduleOwn = exports.default || module.exports;
var accessors = ['public', 'protected', 'private'];
if (moduleOwn.data && accessors.some(function (acc) {
    return moduleOwn[acc];
  })) {
  throw new Error('页面VM对象中属性data不可与public, protected, private同时存在，请使用public替代data！');
} else if (!moduleOwn.data) {
  moduleOwn.data = {};
  moduleOwn._descriptor = {};
  accessors.forEach(function(acc) {
    var accType = typeof moduleOwn[acc];
    if (accType === 'object') {
      moduleOwn.data = Object.assign(moduleOwn.data, moduleOwn[acc]);
      for (var name in moduleOwn[acc]) {
        moduleOwn._descriptor[name] = {access : acc};
      }
    } else if (accType === 'function') {
      console.warn('页面VM对象中的属性' + acc + '的值不能使函数，请使用对象');
    }
  });
}}

/***/ })
/******/ ]);
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiYnVpbGRcXERldGVjdF9GYWNlXFxkZXRlY3RfZmFjZS5qcyIsInNvdXJjZXMiOlsid2VicGFjazovLy93ZWJwYWNrL2Jvb3RzdHJhcCBjNDU3MzY3N2Q1MGMwY2U2YjYxYyIsIndlYnBhY2s6Ly8vLi9zcmMvQ29tbW9uL2RhdGEuanMiLCJ3ZWJwYWNrOi8vLy4vc3JjL0RldGVjdF9GYWNlL2RldGVjdF9mYWNlLnV4Iiwid2VicGFjazovLy8uL3NyYy9EZXRlY3RfRmFjZS9kZXRlY3RfZmFjZS51eD9kMWJiIiwid2VicGFjazovLy8uL3NyYy9EZXRlY3RfRmFjZS9kZXRlY3RfZmFjZS51eD80OWU0Iiwid2VicGFjazovLy8uL3NyYy9EZXRlY3RfRmFjZS9kZXRlY3RfZmFjZS51eD9lYWI2Il0sInNvdXJjZXNDb250ZW50IjpbIiBcdC8vIFRoZSBtb2R1bGUgY2FjaGVcbiBcdHZhciBpbnN0YWxsZWRNb2R1bGVzID0ge307XG5cbiBcdC8vIFRoZSByZXF1aXJlIGZ1bmN0aW9uXG4gXHRmdW5jdGlvbiBfX3dlYnBhY2tfcmVxdWlyZV9fKG1vZHVsZUlkKSB7XG5cbiBcdFx0Ly8gQ2hlY2sgaWYgbW9kdWxlIGlzIGluIGNhY2hlXG4gXHRcdGlmKGluc3RhbGxlZE1vZHVsZXNbbW9kdWxlSWRdKSB7XG4gXHRcdFx0cmV0dXJuIGluc3RhbGxlZE1vZHVsZXNbbW9kdWxlSWRdLmV4cG9ydHM7XG4gXHRcdH1cbiBcdFx0Ly8gQ3JlYXRlIGEgbmV3IG1vZHVsZSAoYW5kIHB1dCBpdCBpbnRvIHRoZSBjYWNoZSlcbiBcdFx0dmFyIG1vZHVsZSA9IGluc3RhbGxlZE1vZHVsZXNbbW9kdWxlSWRdID0ge1xuIFx0XHRcdGk6IG1vZHVsZUlkLFxuIFx0XHRcdGw6IGZhbHNlLFxuIFx0XHRcdGV4cG9ydHM6IHt9XG4gXHRcdH07XG5cbiBcdFx0Ly8gRXhlY3V0ZSB0aGUgbW9kdWxlIGZ1bmN0aW9uXG4gXHRcdG1vZHVsZXNbbW9kdWxlSWRdLmNhbGwobW9kdWxlLmV4cG9ydHMsIG1vZHVsZSwgbW9kdWxlLmV4cG9ydHMsIF9fd2VicGFja19yZXF1aXJlX18pO1xuXG4gXHRcdC8vIEZsYWcgdGhlIG1vZHVsZSBhcyBsb2FkZWRcbiBcdFx0bW9kdWxlLmwgPSB0cnVlO1xuXG4gXHRcdC8vIFJldHVybiB0aGUgZXhwb3J0cyBvZiB0aGUgbW9kdWxlXG4gXHRcdHJldHVybiBtb2R1bGUuZXhwb3J0cztcbiBcdH1cblxuXG4gXHQvLyBleHBvc2UgdGhlIG1vZHVsZXMgb2JqZWN0IChfX3dlYnBhY2tfbW9kdWxlc19fKVxuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5tID0gbW9kdWxlcztcblxuIFx0Ly8gZXhwb3NlIHRoZSBtb2R1bGUgY2FjaGVcbiBcdF9fd2VicGFja19yZXF1aXJlX18uYyA9IGluc3RhbGxlZE1vZHVsZXM7XG5cbiBcdC8vIGRlZmluZSBnZXR0ZXIgZnVuY3Rpb24gZm9yIGhhcm1vbnkgZXhwb3J0c1xuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5kID0gZnVuY3Rpb24oZXhwb3J0cywgbmFtZSwgZ2V0dGVyKSB7XG4gXHRcdGlmKCFfX3dlYnBhY2tfcmVxdWlyZV9fLm8oZXhwb3J0cywgbmFtZSkpIHtcbiBcdFx0XHRPYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgbmFtZSwge1xuIFx0XHRcdFx0Y29uZmlndXJhYmxlOiBmYWxzZSxcbiBcdFx0XHRcdGVudW1lcmFibGU6IHRydWUsXG4gXHRcdFx0XHRnZXQ6IGdldHRlclxuIFx0XHRcdH0pO1xuIFx0XHR9XG4gXHR9O1xuXG4gXHQvLyBnZXREZWZhdWx0RXhwb3J0IGZ1bmN0aW9uIGZvciBjb21wYXRpYmlsaXR5IHdpdGggbm9uLWhhcm1vbnkgbW9kdWxlc1xuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5uID0gZnVuY3Rpb24obW9kdWxlKSB7XG4gXHRcdHZhciBnZXR0ZXIgPSBtb2R1bGUgJiYgbW9kdWxlLl9fZXNNb2R1bGUgP1xuIFx0XHRcdGZ1bmN0aW9uIGdldERlZmF1bHQoKSB7IHJldHVybiBtb2R1bGVbJ2RlZmF1bHQnXTsgfSA6XG4gXHRcdFx0ZnVuY3Rpb24gZ2V0TW9kdWxlRXhwb3J0cygpIHsgcmV0dXJuIG1vZHVsZTsgfTtcbiBcdFx0X193ZWJwYWNrX3JlcXVpcmVfXy5kKGdldHRlciwgJ2EnLCBnZXR0ZXIpO1xuIFx0XHRyZXR1cm4gZ2V0dGVyO1xuIFx0fTtcblxuIFx0Ly8gT2JqZWN0LnByb3RvdHlwZS5oYXNPd25Qcm9wZXJ0eS5jYWxsXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLm8gPSBmdW5jdGlvbihvYmplY3QsIHByb3BlcnR5KSB7IHJldHVybiBPYmplY3QucHJvdG90eXBlLmhhc093blByb3BlcnR5LmNhbGwob2JqZWN0LCBwcm9wZXJ0eSk7IH07XG5cbiBcdC8vIF9fd2VicGFja19wdWJsaWNfcGF0aF9fXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLnAgPSBcIlwiO1xuXG4gXHQvLyBMb2FkIGVudHJ5IG1vZHVsZSBhbmQgcmV0dXJuIGV4cG9ydHNcbiBcdHJldHVybiBfX3dlYnBhY2tfcmVxdWlyZV9fKF9fd2VicGFja19yZXF1aXJlX18ucyA9IDMpO1xuXG5cblxuLy8gV0VCUEFDSyBGT09URVIgLy9cbi8vIHdlYnBhY2svYm9vdHN0cmFwIGM0NTczNjc3ZDUwYzBjZTZiNjFjIiwiY29uc3QgZXJyQ29kZUxpc3QgPSB7XG4gIFwiMjAyXCI6IFwi5Y+C5pWw6ZSZ6K+vXCIsXG4gIFwiMjAzXCI6IFwi5pyN5Yqh5LiN5Y+v55SoXCIsXG4gIFwiODAwXCI6IFwi5LiA6Iis5oCn5qOA5rWL5aSx6LSlXCIsXG4gIFwiODAxXCI6IFwi5qih5Z6L5paH5Lu26ZSZ6K+vXCIsXG4gIFwiODAyXCI6IFwi5qih5Z6L5Yqg6L295aSx6LSlXCIsXG4gIFwiODAzXCI6IFwi56We57uP572R57uc5aSE55CG5Y2V5YWD6ZSZ6K+vXCIsXG4gIFwiODA0XCI6IFwi5b2V6Z+z55u45YWz6ZSZ6K+vXCIsXG4gIFwiODA1XCI6IFwi5a6i5oi356uv5pyq6LCD55So5byV5pOO5Yid5aeL5YyWXCIsXG4gIFwiODA2XCI6IFwi5rKh5pyJ5aOw6Z+zXCIsXG4gIFwiODA3XCI6IFwi5pyq5Yy56YWN6K+G5Yir57uT5p6cXCIsXG4gIFwiODA4XCI6IFwi5a6i5oi356uv5p2D6ZmQ5LiN6LazXCIsXG4gIFwiODA5XCI6IFwi5pyN5Yqh56uv5p2D6ZmQ5LiN6LazXCIsXG4gIFwiODEwXCI6IFwi6K6+5aSHQUlFbmdpbmXnmoTlip/og73lvIDlhbPooqvlhbPpl63kuobvvIzml6Dms5Xkvb/nlKhcIixcbiAgXCI4MTFcIjogXCLkuI3mlK/mjIHnmoTlip/og73miJbmjqXlj6NcIlxufTtcblxuZXhwb3J0IHsgZXJyQ29kZUxpc3QgfTtcblxuXG4vLy8vLy8vLy8vLy8vLy8vLy9cbi8vIFdFQlBBQ0sgRk9PVEVSXG4vLyAuL3NyYy9Db21tb24vZGF0YS5qc1xuLy8gbW9kdWxlIGlkID0gMFxuLy8gbW9kdWxlIGNodW5rcyA9IDAgMSIsInZhciAkYXBwX3RlbXBsYXRlJCA9IHJlcXVpcmUoXCIhIS4uLy4uLy4uLy4uLy4uLy4uLy4uL3Rvb2xzL0h1YXdlaSBGYXN0QXBwIElERS9yZXNvdXJjZXMvYXBwL2V4dGVuc2lvbnMvZGV2ZWNvLWRlYnVnL25vZGVfbW9kdWxlcy9mYS10b29sa2l0L2xpYi9mYS1qc29uLWxvYWRlci5qcyEuLi8uLi8uLi8uLi8uLi8uLi8uLi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvZmEtdG9vbGtpdC9saWIvZmEtdGVtcGxhdGUtbG9hZGVyLmpzIS4uLy4uLy4uLy4uLy4uLy4uLy4uL3Rvb2xzL0h1YXdlaSBGYXN0QXBwIElERS9yZXNvdXJjZXMvYXBwL2V4dGVuc2lvbnMvZGV2ZWNvLWRlYnVnL25vZGVfbW9kdWxlcy9mYS10b29sa2l0L2xpYi9mYS1mcmFnbWVudC1sb2FkZXIuanM/aW5kZXg9MCZ0eXBlPXRlbXBsYXRlcyEuL2RldGVjdF9mYWNlLnV4XCIpXG52YXIgJGFwcF9zdHlsZSQgPSByZXF1aXJlKFwiISEuLi8uLi8uLi8uLi8uLi8uLi8uLi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvZmEtdG9vbGtpdC9saWIvZmEtanNvbi1sb2FkZXIuanMhLi4vLi4vLi4vLi4vLi4vLi4vLi4vdG9vbHMvSHVhd2VpIEZhc3RBcHAgSURFL3Jlc291cmNlcy9hcHAvZXh0ZW5zaW9ucy9kZXZlY28tZGVidWcvbm9kZV9tb2R1bGVzL2ZhLXRvb2xraXQvbGliL2ZhLXN0eWxlLWxvYWRlci5qcz9pbmRleD0wJnR5cGU9c3R5bGVzJnJlc291cmNlUGF0aD1kOlxcXFxTZXJ2aWNlIHZlcmlmaWNhdGlvblxcXFxDb2RlXFxcXGZhc3RBcHBcXFxcbGFuZG1hcmtBbmRkZXRlY3RlXFxcXGNvbS5odWF3ZWkubGFuZG1hcmthbmRkZXRlY3RcXFxcc3JjXFxcXERldGVjdF9GYWNlXFxcXGRldGVjdF9mYWNlLnV4IS4uLy4uLy4uLy4uLy4uLy4uLy4uL3Rvb2xzL0h1YXdlaSBGYXN0QXBwIElERS9yZXNvdXJjZXMvYXBwL2V4dGVuc2lvbnMvZGV2ZWNvLWRlYnVnL25vZGVfbW9kdWxlcy9mYS10b29sa2l0L2xpYi9mYS1mcmFnbWVudC1sb2FkZXIuanM/aW5kZXg9MCZ0eXBlPXN0eWxlcyZyZXNvdXJjZVBhdGg9ZDpcXFxcU2VydmljZSB2ZXJpZmljYXRpb25cXFxcQ29kZVxcXFxmYXN0QXBwXFxcXGxhbmRtYXJrQW5kZGV0ZWN0ZVxcXFxjb20uaHVhd2VpLmxhbmRtYXJrYW5kZGV0ZWN0XFxcXHNyY1xcXFxEZXRlY3RfRmFjZVxcXFxkZXRlY3RfZmFjZS51eCEuL2RldGVjdF9mYWNlLnV4XCIpXG52YXIgJGFwcF9zY3JpcHQkID0gcmVxdWlyZShcIiEhLi4vLi4vLi4vLi4vLi4vLi4vLi4vdG9vbHMvSHVhd2VpIEZhc3RBcHAgSURFL3Jlc291cmNlcy9hcHAvZXh0ZW5zaW9ucy9kZXZlY28tZGVidWcvbm9kZV9tb2R1bGVzL2ZhLXRvb2xraXQvbGliL2ZhLXNjcmlwdC1sb2FkZXIuanMhLi4vLi4vLi4vLi4vLi4vLi4vLi4vdG9vbHMvSHVhd2VpIEZhc3RBcHAgSURFL3Jlc291cmNlcy9hcHAvZXh0ZW5zaW9ucy9kZXZlY28tZGVidWcvbm9kZV9tb2R1bGVzL2ZhLXRvb2xraXQvbGliL2ZhLWFjY2Vzcy1sb2FkZXIuanMhLi4vLi4vLi4vLi4vLi4vLi4vLi4vdG9vbHMvSHVhd2VpIEZhc3RBcHAgSURFL3Jlc291cmNlcy9hcHAvZXh0ZW5zaW9ucy9kZXZlY28tZGVidWcvbm9kZV9tb2R1bGVzL2JhYmVsLWxvYWRlcj9wcmVzZXRzW109ZDpcXFxcdG9vbHNcXFxcSHVhd2VpIEZhc3RBcHAgSURFXFxcXHJlc291cmNlc1xcXFxhcHBcXFxcZXh0ZW5zaW9uc1xcXFxkZXZlY28tZGVidWdcXFxcbm9kZV9tb2R1bGVzXFxcXGJhYmVsLXByZXNldC1lbnYmcGx1Z2luc1tdPWQ6XFxcXHRvb2xzXFxcXEh1YXdlaSBGYXN0QXBwIElERVxcXFxyZXNvdXJjZXNcXFxcYXBwXFxcXGV4dGVuc2lvbnNcXFxcZGV2ZWNvLWRlYnVnXFxcXG5vZGVfbW9kdWxlc1xcXFxmYS10b29sa2l0XFxcXGxpYlxcXFxqc3gtbG9hZGVyLmpzJmNvbW1lbnRzPWZhbHNlIS4uLy4uLy4uLy4uLy4uLy4uLy4uL3Rvb2xzL0h1YXdlaSBGYXN0QXBwIElERS9yZXNvdXJjZXMvYXBwL2V4dGVuc2lvbnMvZGV2ZWNvLWRlYnVnL25vZGVfbW9kdWxlcy9mYS10b29sa2l0L2xpYi9mYS1mcmFnbWVudC1sb2FkZXIuanM/aW5kZXg9MCZ0eXBlPXNjcmlwdHMhLi9kZXRlY3RfZmFjZS51eFwiKVxuXG4kYXBwX2RlZmluZSQoJ0BhcHAtY29tcG9uZW50L2RldGVjdF9mYWNlJywgW10sIGZ1bmN0aW9uKCRhcHBfcmVxdWlyZSQsICRhcHBfZXhwb3J0cyQsICRhcHBfbW9kdWxlJCl7XG4gICAgICRhcHBfc2NyaXB0JCgkYXBwX21vZHVsZSQsICRhcHBfZXhwb3J0cyQsICRhcHBfcmVxdWlyZSQpXG4gICAgIGlmICgkYXBwX2V4cG9ydHMkLl9fZXNNb2R1bGUgJiYgJGFwcF9leHBvcnRzJC5kZWZhdWx0KSB7XG4gICAgICAgICAgICAkYXBwX21vZHVsZSQuZXhwb3J0cyA9ICRhcHBfZXhwb3J0cyQuZGVmYXVsdFxuICAgICAgICB9XG4gICAgICRhcHBfbW9kdWxlJC5leHBvcnRzLnRlbXBsYXRlID0gJGFwcF90ZW1wbGF0ZSRcbiAgICAgJGFwcF9tb2R1bGUkLmV4cG9ydHMuc3R5bGUgPSAkYXBwX3N0eWxlJFxufSlcblxuJGFwcF9ib290c3RyYXAkKCdAYXBwLWNvbXBvbmVudC9kZXRlY3RfZmFjZScseyBwYWNrYWdlck5hbWU6J2ZhLXRvb2xraXQnLCBwYWNrYWdlclZlcnNpb246ICcxLjAuOC1TdGFibGUuMzAwJ30pXG5cblxuLy8vLy8vLy8vLy8vLy8vLy8vXG4vLyBXRUJQQUNLIEZPT1RFUlxuLy8gLi9zcmMvRGV0ZWN0X0ZhY2UvZGV0ZWN0X2ZhY2UudXhcbi8vIG1vZHVsZSBpZCA9IDNcbi8vIG1vZHVsZSBjaHVua3MgPSAxIiwibW9kdWxlLmV4cG9ydHMgPSB7XG4gIFwidHlwZVwiOiBcImRpdlwiLFxuICBcImF0dHJcIjoge1xuICAgIFwiaWRcIjogXCJjb250YWluZXJcIlxuICB9LFxuICBcImNsYXNzTGlzdFwiOiBbXG4gICAgXCJjb250YWluZXJcIlxuICBdLFxuICBcImlkXCI6IFwiY29udGFpbmVyXCIsXG4gIFwiY2hpbGRyZW5cIjogW1xuICAgIHtcbiAgICAgIFwidHlwZVwiOiBcInN0YWNrXCIsXG4gICAgICBcImF0dHJcIjoge30sXG4gICAgICBcImNsYXNzTGlzdFwiOiBbXG4gICAgICAgIFwidG9wLWNvbnRhaW5lclwiXG4gICAgICBdLFxuICAgICAgXCJjaGlsZHJlblwiOiBbXG4gICAgICAgIHtcbiAgICAgICAgICBcInR5cGVcIjogXCJpbWFnZVwiLFxuICAgICAgICAgIFwiYXR0clwiOiB7XG4gICAgICAgICAgICBcInNyY1wiOiBmdW5jdGlvbiAoKSB7cmV0dXJuIHRoaXMuaW1hZ2VVcml9LFxuICAgICAgICAgICAgXCJpZFwiOiBcImZhY2UtaW1hZ2UtaWRcIlxuICAgICAgICAgIH0sXG4gICAgICAgICAgXCJjbGFzc0xpc3RcIjogW1xuICAgICAgICAgICAgXCJmYWNlLWltYWdlXCJcbiAgICAgICAgICBdLFxuICAgICAgICAgIFwiaWRcIjogXCJmYWNlLWltYWdlLWlkXCJcbiAgICAgICAgfSxcbiAgICAgICAge1xuICAgICAgICAgIFwidHlwZVwiOiBcImNhbnZhc1wiLFxuICAgICAgICAgIFwiYXR0clwiOiB7XG4gICAgICAgICAgICBcImlkXCI6IFwiY2FudmFzXCJcbiAgICAgICAgICB9LFxuICAgICAgICAgIFwiaWRcIjogXCJjYW52YXNcIixcbiAgICAgICAgICBcInN0eWxlXCI6IHtcbiAgICAgICAgICAgIFwiZmxleFwiOiAxLFxuICAgICAgICAgICAgXCJ3aWR0aFwiOiBmdW5jdGlvbiAoKSB7cmV0dXJuIHRoaXMuY2FudmFzV2lkdGh9LFxuICAgICAgICAgICAgXCJoZWlnaHRcIjogZnVuY3Rpb24gKCkge3JldHVybiB0aGlzLmNhbnZhc0hlaWdodH1cbiAgICAgICAgICB9LFxuICAgICAgICAgIFwiZXZlbnRzXCI6IHtcbiAgICAgICAgICAgIFwiY2xpY2tcIjogXCJzZWxlY3RcIlxuICAgICAgICAgIH1cbiAgICAgICAgfVxuICAgICAgXVxuICAgIH0sXG4gICAge1xuICAgICAgXCJ0eXBlXCI6IFwiZGl2XCIsXG4gICAgICBcImF0dHJcIjoge30sXG4gICAgICBcImNsYXNzTGlzdFwiOiBbXG4gICAgICAgIFwiYm90dG9tLWNvbnRhaW5lclwiXG4gICAgICBdLFxuICAgICAgXCJjaGlsZHJlblwiOiBbXG4gICAgICAgIHtcbiAgICAgICAgICBcInR5cGVcIjogXCJ0YWJzXCIsXG4gICAgICAgICAgXCJhdHRyXCI6IHt9LFxuICAgICAgICAgIFwiY2hpbGRyZW5cIjogW1xuICAgICAgICAgICAge1xuICAgICAgICAgICAgICBcInR5cGVcIjogXCJ0YWItY29udGVudFwiLFxuICAgICAgICAgICAgICBcImF0dHJcIjoge30sXG4gICAgICAgICAgICAgIFwiY2xhc3NMaXN0XCI6IFtcbiAgICAgICAgICAgICAgICBcInJlc3VsdC1ncm91cC1jb250YWluZXJcIlxuICAgICAgICAgICAgICBdLFxuICAgICAgICAgICAgICBcImNoaWxkcmVuXCI6IFtcbiAgICAgICAgICAgICAgICB7XG4gICAgICAgICAgICAgICAgICBcInR5cGVcIjogXCJkaXZcIixcbiAgICAgICAgICAgICAgICAgIFwiYXR0clwiOiB7fSxcbiAgICAgICAgICAgICAgICAgIFwiY2xhc3NMaXN0XCI6IFtcbiAgICAgICAgICAgICAgICAgICAgXCJyZXN1bHQtY29udGVudFwiXG4gICAgICAgICAgICAgICAgICBdLFxuICAgICAgICAgICAgICAgICAgXCJjaGlsZHJlblwiOiBbXG4gICAgICAgICAgICAgICAgICAgIHtcbiAgICAgICAgICAgICAgICAgICAgICBcInR5cGVcIjogXCJkaXZcIixcbiAgICAgICAgICAgICAgICAgICAgICBcImF0dHJcIjoge30sXG4gICAgICAgICAgICAgICAgICAgICAgXCJjbGFzc0xpc3RcIjogW1xuICAgICAgICAgICAgICAgICAgICAgICAgXCJyZXN1bHQtdGl0bGVcIlxuICAgICAgICAgICAgICAgICAgICAgIF0sXG4gICAgICAgICAgICAgICAgICAgICAgXCJjaGlsZHJlblwiOiBbXG4gICAgICAgICAgICAgICAgICAgICAgICB7XG4gICAgICAgICAgICAgICAgICAgICAgICAgIFwidHlwZVwiOiBcInRleHRcIixcbiAgICAgICAgICAgICAgICAgICAgICAgICAgXCJhdHRyXCI6IHtcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICBcInZhbHVlXCI6IFwi5YiG5p6Q57uT5p6cXCJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgfSxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgXCJjbGFzc0xpc3RcIjogW1xuICAgICAgICAgICAgICAgICAgICAgICAgICAgIFwicmVzdWx0LXRpdGxlLWRldGFpbFwiXG4gICAgICAgICAgICAgICAgICAgICAgICAgIF1cbiAgICAgICAgICAgICAgICAgICAgICAgIH1cbiAgICAgICAgICAgICAgICAgICAgICBdXG4gICAgICAgICAgICAgICAgICAgIH0sXG4gICAgICAgICAgICAgICAgICAgIHtcbiAgICAgICAgICAgICAgICAgICAgICBcInR5cGVcIjogXCJkaXZcIixcbiAgICAgICAgICAgICAgICAgICAgICBcImF0dHJcIjoge30sXG4gICAgICAgICAgICAgICAgICAgICAgXCJjbGFzc0xpc3RcIjogW1xuICAgICAgICAgICAgICAgICAgICAgICAgXCJyZXN1bHQtbGluZVwiXG4gICAgICAgICAgICAgICAgICAgICAgXVxuICAgICAgICAgICAgICAgICAgICB9LFxuICAgICAgICAgICAgICAgICAgICB7XG4gICAgICAgICAgICAgICAgICAgICAgXCJ0eXBlXCI6IFwiZGl2XCIsXG4gICAgICAgICAgICAgICAgICAgICAgXCJhdHRyXCI6IHt9LFxuICAgICAgICAgICAgICAgICAgICAgIFwiY2xhc3NMaXN0XCI6IFtcbiAgICAgICAgICAgICAgICAgICAgICAgIFwicmVzdWx0LWl0ZW0tY29udGVudFwiXG4gICAgICAgICAgICAgICAgICAgICAgXSxcbiAgICAgICAgICAgICAgICAgICAgICBcImNoaWxkcmVuXCI6IFtcbiAgICAgICAgICAgICAgICAgICAgICAgIHtcbiAgICAgICAgICAgICAgICAgICAgICAgICAgXCJ0eXBlXCI6IFwibGlzdFwiLFxuICAgICAgICAgICAgICAgICAgICAgICAgICBcImF0dHJcIjoge1xuICAgICAgICAgICAgICAgICAgICAgICAgICAgIFwiaWRcIjogXCJsaXN0XCJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgfSxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgXCJjbGFzc0xpc3RcIjogW1xuICAgICAgICAgICAgICAgICAgICAgICAgICAgIFwicmVzdWx0LWxpc3RcIlxuICAgICAgICAgICAgICAgICAgICAgICAgICBdLFxuICAgICAgICAgICAgICAgICAgICAgICAgICBcImlkXCI6IFwibGlzdFwiLFxuICAgICAgICAgICAgICAgICAgICAgICAgICBcImNoaWxkcmVuXCI6IFtcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICB7XG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcInR5cGVcIjogXCJibG9ja1wiLFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXCJhdHRyXCI6IHt9LFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXCJyZXBlYXRcIjogZnVuY3Rpb24gKCkge3JldHVybiB0aGlzLnJlc3VsdF9saXN0fSxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFwiY2hpbGRyZW5cIjogW1xuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB7XG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXCJ0eXBlXCI6IFwibGlzdC1pdGVtXCIsXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXCJhdHRyXCI6IHtcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFwidHlwZVwiOiBcInJlc3VsdFwiXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgfSxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcImNsYXNzTGlzdFwiOiBbXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcIml0ZW1zXCJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBdLFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFwiY2hpbGRyZW5cIjogW1xuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAge1xuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcInR5cGVcIjogXCJkaXZcIixcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXCJhdHRyXCI6IHt9LFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcImNsYXNzTGlzdFwiOiBbXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXCJpdGVtLWxpXCJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXSxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXCJjaGlsZHJlblwiOiBbXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAge1xuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXCJ0eXBlXCI6IFwidGV4dFwiLFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXCJhdHRyXCI6IHtcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXCJ2YWx1ZVwiOiBmdW5jdGlvbiAoKSB7cmV0dXJuIHRoaXMuJGl0ZW19XG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB9LFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXCJjbGFzc0xpc3RcIjogW1xuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcIml0ZW0tbGktZGV0YWlsXCJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIF1cbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB9XG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIF1cbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIH1cbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBdXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIH1cbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIF1cbiAgICAgICAgICAgICAgICAgICAgICAgICAgICB9XG4gICAgICAgICAgICAgICAgICAgICAgICAgIF1cbiAgICAgICAgICAgICAgICAgICAgICAgIH1cbiAgICAgICAgICAgICAgICAgICAgICBdXG4gICAgICAgICAgICAgICAgICAgIH1cbiAgICAgICAgICAgICAgICAgIF1cbiAgICAgICAgICAgICAgICB9XG4gICAgICAgICAgICAgIF1cbiAgICAgICAgICAgIH1cbiAgICAgICAgICBdXG4gICAgICAgIH1cbiAgICAgIF1cbiAgICB9LFxuICAgIHtcbiAgICAgIFwidHlwZVwiOiBcImRpdlwiLFxuICAgICAgXCJhdHRyXCI6IHt9LFxuICAgICAgXCJjbGFzc0xpc3RcIjogW1xuICAgICAgICBcInNlbGVjdC1jb250YWluZXJcIlxuICAgICAgXSxcbiAgICAgIFwiY2hpbGRyZW5cIjogW1xuICAgICAgICB7XG4gICAgICAgICAgXCJ0eXBlXCI6IFwiaW5wdXRcIixcbiAgICAgICAgICBcImF0dHJcIjoge1xuICAgICAgICAgICAgXCJ0eXBlXCI6IFwiYnV0dG9uXCIsXG4gICAgICAgICAgICBcInZhbHVlXCI6IFwi5LiK5Lyg5Zu+54mHXCJcbiAgICAgICAgICB9LFxuICAgICAgICAgIFwiY2xhc3NMaXN0XCI6IFtcbiAgICAgICAgICAgIFwic2VsZWN0LWJ0blwiXG4gICAgICAgICAgXSxcbiAgICAgICAgICBcImV2ZW50c1wiOiB7XG4gICAgICAgICAgICBcImNsaWNrXCI6IFwic2VsZWN0XCJcbiAgICAgICAgICB9XG4gICAgICAgIH1cbiAgICAgIF1cbiAgICB9LFxuICAgIHtcbiAgICAgIFwidHlwZVwiOiBcImRpdlwiLFxuICAgICAgXCJhdHRyXCI6IHt9LFxuICAgICAgXCJjbGFzc0xpc3RcIjogZnVuY3Rpb24gKCkge3JldHVybiBbJ21hcmsnLCB0aGlzLmlzU2hvd119LFxuICAgICAgXCJldmVudHNcIjoge1xuICAgICAgICBcImNsaWNrXCI6IFwiY2FuY2VsXCJcbiAgICAgIH0sXG4gICAgICBcImNoaWxkcmVuXCI6IFtcbiAgICAgICAge1xuICAgICAgICAgIFwidHlwZVwiOiBcImRpdlwiLFxuICAgICAgICAgIFwiYXR0clwiOiB7fSxcbiAgICAgICAgICBcImNsYXNzTGlzdFwiOiBmdW5jdGlvbiAoKSB7cmV0dXJuIFsncG9wdXAtY29udGFpbmVyJywgdGhpcy5pc1Nob3ddfSxcbiAgICAgICAgICBcImNoaWxkcmVuXCI6IFtcbiAgICAgICAgICAgIHtcbiAgICAgICAgICAgICAgXCJ0eXBlXCI6IFwidGV4dFwiLFxuICAgICAgICAgICAgICBcImF0dHJcIjoge1xuICAgICAgICAgICAgICAgIFwidmFsdWVcIjogXCLmi43nhadcIlxuICAgICAgICAgICAgICB9LFxuICAgICAgICAgICAgICBcImNsYXNzTGlzdFwiOiBbXG4gICAgICAgICAgICAgICAgXCJwb3B1cC10ZXh0XCJcbiAgICAgICAgICAgICAgXSxcbiAgICAgICAgICAgICAgXCJldmVudHNcIjoge1xuICAgICAgICAgICAgICAgIFwiY2xpY2tcIjogZnVuY3Rpb24gKGV2dCkge3RoaXMuc2VsZWN0TWVkaWEoJ+aLjeeFpycsZXZ0KX1cbiAgICAgICAgICAgICAgfVxuICAgICAgICAgICAgfSxcbiAgICAgICAgICAgIHtcbiAgICAgICAgICAgICAgXCJ0eXBlXCI6IFwiZGl2XCIsXG4gICAgICAgICAgICAgIFwiYXR0clwiOiB7fSxcbiAgICAgICAgICAgICAgXCJjbGFzc0xpc3RcIjogW1xuICAgICAgICAgICAgICAgIFwicG9wdXAtbGluZTFcIlxuICAgICAgICAgICAgICBdXG4gICAgICAgICAgICB9LFxuICAgICAgICAgICAge1xuICAgICAgICAgICAgICBcInR5cGVcIjogXCJ0ZXh0XCIsXG4gICAgICAgICAgICAgIFwiYXR0clwiOiB7XG4gICAgICAgICAgICAgICAgXCJ2YWx1ZVwiOiBcIuS7juebuOWGjOS4remAieaLqVwiXG4gICAgICAgICAgICAgIH0sXG4gICAgICAgICAgICAgIFwiY2xhc3NMaXN0XCI6IFtcbiAgICAgICAgICAgICAgICBcInBvcHVwLXRleHRcIlxuICAgICAgICAgICAgICBdLFxuICAgICAgICAgICAgICBcImV2ZW50c1wiOiB7XG4gICAgICAgICAgICAgICAgXCJjbGlja1wiOiBmdW5jdGlvbiAoZXZ0KSB7dGhpcy5zZWxlY3RNZWRpYSgn5LuO55u45YaM5Lit6YCJ5oupJyxldnQpfVxuICAgICAgICAgICAgICB9XG4gICAgICAgICAgICB9LFxuICAgICAgICAgICAge1xuICAgICAgICAgICAgICBcInR5cGVcIjogXCJkaXZcIixcbiAgICAgICAgICAgICAgXCJhdHRyXCI6IHt9LFxuICAgICAgICAgICAgICBcImNsYXNzTGlzdFwiOiBbXG4gICAgICAgICAgICAgICAgXCJwb3B1cC1saW5lMlwiXG4gICAgICAgICAgICAgIF1cbiAgICAgICAgICAgIH0sXG4gICAgICAgICAgICB7XG4gICAgICAgICAgICAgIFwidHlwZVwiOiBcInRleHRcIixcbiAgICAgICAgICAgICAgXCJhdHRyXCI6IHtcbiAgICAgICAgICAgICAgICBcInZhbHVlXCI6IFwi5Y+W5raIXCJcbiAgICAgICAgICAgICAgfSxcbiAgICAgICAgICAgICAgXCJjbGFzc0xpc3RcIjogW1xuICAgICAgICAgICAgICAgIFwicG9wdXAtdGV4dFwiXG4gICAgICAgICAgICAgIF0sXG4gICAgICAgICAgICAgIFwiZXZlbnRzXCI6IHtcbiAgICAgICAgICAgICAgICBcImNsaWNrXCI6IGZ1bmN0aW9uIChldnQpIHt0aGlzLnNlbGVjdE1lZGlhKCflj5bmtognLGV2dCl9XG4gICAgICAgICAgICAgIH1cbiAgICAgICAgICAgIH1cbiAgICAgICAgICBdXG4gICAgICAgIH1cbiAgICAgIF1cbiAgICB9XG4gIF1cbn1cblxuXG4vLy8vLy8vLy8vLy8vLy8vLy9cbi8vIFdFQlBBQ0sgRk9PVEVSXG4vLyBkOi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvZmEtdG9vbGtpdC9saWIvZmEtanNvbi1sb2FkZXIuanMhZDovdG9vbHMvSHVhd2VpIEZhc3RBcHAgSURFL3Jlc291cmNlcy9hcHAvZXh0ZW5zaW9ucy9kZXZlY28tZGVidWcvbm9kZV9tb2R1bGVzL2ZhLXRvb2xraXQvbGliL2ZhLXRlbXBsYXRlLWxvYWRlci5qcyFkOi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvZmEtdG9vbGtpdC9saWIvZmEtZnJhZ21lbnQtbG9hZGVyLmpzP2luZGV4PTAmdHlwZT10ZW1wbGF0ZXMhLi9zcmMvRGV0ZWN0X0ZhY2UvZGV0ZWN0X2ZhY2UudXhcbi8vIG1vZHVsZSBpZCA9IDRcbi8vIG1vZHVsZSBjaHVua3MgPSAxIiwibW9kdWxlLmV4cG9ydHMgPSB7XG4gIFwiLmNvbnRhaW5lclwiOiB7XG4gICAgXCJmbGV4XCI6IDEsXG4gICAgXCJmbGV4RGlyZWN0aW9uXCI6IFwiY29sdW1uXCIsXG4gICAgXCJiYWNrZ3JvdW5kQ29sb3JcIjogXCIjZjBlY2VjXCJcbiAgfSxcbiAgXCIudG9wLWNvbnRhaW5lclwiOiB7XG4gICAgXCJmbGV4RGlyZWN0aW9uXCI6IFwiY29sdW1uXCIsXG4gICAgXCJ3aWR0aFwiOiBcIjEwMCVcIixcbiAgICBcImhlaWdodFwiOiBcIjQ1JVwiLFxuICAgIFwiYWxpZ25JdGVtc1wiOiBcImNlbnRlclwiXG4gIH0sXG4gIFwiLmZhY2UtaW1hZ2VcIjoge1xuICAgIFwiZmxleFwiOiAxXG4gIH0sXG4gIFwiLmJvdHRvbS1jb250YWluZXJcIjoge1xuICAgIFwid2lkdGhcIjogXCIxMDAlXCIsXG4gICAgXCJoZWlnaHRcIjogXCI1NSVcIixcbiAgICBcImJhY2tncm91bmRDb2xvclwiOiBcIiNmZmZmZmZcIixcbiAgICBcImZsZXhEaXJlY3Rpb25cIjogXCJjb2x1bW5cIlxuICB9LFxuICBcIi5yZXN1bHQtZ3JvdXAtY29udGFpbmVyXCI6IHtcbiAgICBcImZsZXhcIjogMSxcbiAgICBcImZsZXhEaXJlY3Rpb25cIjogXCJjb2x1bW5cIixcbiAgICBcImJhY2tncm91bmRDb2xvclwiOiBcIiNmZmZmZmZcIlxuICB9LFxuICBcIi5yZXN1bHQtY29udGVudFwiOiB7XG4gICAgXCJmbGV4RGlyZWN0aW9uXCI6IFwiY29sdW1uXCIsXG4gICAgXCJmbGV4XCI6IDFcbiAgfSxcbiAgXCIucmVzdWx0LXRpdGxlLWRldGFpbFwiOiB7XG4gICAgXCJwYWRkaW5nTGVmdFwiOiBcIjMwcHhcIixcbiAgICBcInBhZGRpbmdCb3R0b21cIjogXCIyMHB4XCIsXG4gICAgXCJwYWRkaW5nVG9wXCI6IFwiMjBweFwiLFxuICAgIFwiaGVpZ2h0XCI6IFwiOTBweFwiLFxuICAgIFwiZm9udFNpemVcIjogXCI0MHB4XCIsXG4gICAgXCJmb250RmFtaWx5XCI6IFwiVGltZXMgTmV3IFJvbWFuLCBUaW1lcywgc2VyaWZcIlxuICB9LFxuICBcIi5yZXN1bHQtbGluZVwiOiB7XG4gICAgXCJtYXJnaW5Ub3BcIjogXCI1cHhcIixcbiAgICBcIndpZHRoXCI6IFwiMTAwJVwiLFxuICAgIFwiaGVpZ2h0XCI6IFwiMXB4XCIsXG4gICAgXCJiYWNrZ3JvdW5kQ29sb3JcIjogXCIjZjBlY2VjXCJcbiAgfSxcbiAgXCIucmVzdWx0LWl0ZW0tY29udGVudFwiOiB7XG4gICAgXCJmbGV4RGlyZWN0aW9uXCI6IFwiY29sdW1uXCIsXG4gICAgXCJmbGV4XCI6IDEsXG4gICAgXCJwYWRkaW5nVG9wXCI6IFwiMzBweFwiLFxuICAgIFwicGFkZGluZ1JpZ2h0XCI6IFwiMzBweFwiLFxuICAgIFwicGFkZGluZ0JvdHRvbVwiOiBcIjMwcHhcIixcbiAgICBcInBhZGRpbmdMZWZ0XCI6IFwiMzBweFwiLFxuICAgIFwiZGlzcGxheVwiOiBcImZsZXhcIlxuICB9LFxuICBcIi5yZXN1bHQtbGlzdFwiOiB7XG4gICAgXCJ3aWR0aFwiOiBcIjEwMCVcIixcbiAgICBcImhlaWdodFwiOiBcIjQwMHB4XCJcbiAgfSxcbiAgXCIuaXRlbXNcIjoge1xuICAgIFwid2lkdGhcIjogXCIxMDAlXCIsXG4gICAgXCJoZWlnaHRcIjogXCI4MHB4XCJcbiAgfSxcbiAgXCIuaXRlbS1saVwiOiB7XG4gICAgXCJhbGlnbkl0ZW1zXCI6IFwiY2VudGVyXCJcbiAgfSxcbiAgXCIuaXRlbS1saS1kZXRhaWxcIjoge1xuICAgIFwiZm9udFNpemVcIjogXCIzMHB4XCIsXG4gICAgXCJjb2xvclwiOiBcIiMwMDAwMDBcIixcbiAgICBcImZsZXhcIjogMVxuICB9LFxuICBcIi5zZWxlY3QtY29udGFpbmVyXCI6IHtcbiAgICBcInBvc2l0aW9uXCI6IFwiZml4ZWRcIixcbiAgICBcIndpZHRoXCI6IFwiMTAwJVwiLFxuICAgIFwiaGVpZ2h0XCI6IFwiMTAwcHhcIixcbiAgICBcImZsZXhEaXJlY3Rpb25cIjogXCJjb2x1bW5cIixcbiAgICBcImFsaWduSXRlbXNcIjogXCJjZW50ZXJcIixcbiAgICBcImJvdHRvbVwiOiBcIjQwcHhcIlxuICB9LFxuICBcIi5zZWxlY3QtYnRuXCI6IHtcbiAgICBcIndpZHRoXCI6IFwiODAlXCIsXG4gICAgXCJoZWlnaHRcIjogXCIxMDAlXCIsXG4gICAgXCJiYWNrZ3JvdW5kQ29sb3JcIjogXCIjMTQ3OGZhXCIsXG4gICAgXCJ0ZXh0QWxpZ25cIjogXCJjZW50ZXJcIixcbiAgICBcImNvbG9yXCI6IFwiI2ZmZmZmZlwiLFxuICAgIFwiZm9udFNpemVcIjogXCI0MHB4XCIsXG4gICAgXCJib3JkZXJSYWRpdXNcIjogXCIxMHB4XCJcbiAgfSxcbiAgXCIubWFya1wiOiB7XG4gICAgXCJwb3NpdGlvblwiOiBcImZpeGVkXCIsXG4gICAgXCJkaXNwbGF5XCI6IFwibm9uZVwiLFxuICAgIFwibGVmdFwiOiBcIjBweFwiLFxuICAgIFwidG9wXCI6IFwiMHB4XCIsXG4gICAgXCJ3aWR0aFwiOiBcIjEwMCVcIixcbiAgICBcImhlaWdodFwiOiBcIjEwMCVcIixcbiAgICBcImJhY2tncm91bmRDb2xvclwiOiBcIiNkM2QzZDNcIixcbiAgICBcIm9wYWNpdHlcIjogMC41XG4gIH0sXG4gIFwiLnBvcHVwLWNvbnRhaW5lclwiOiB7XG4gICAgXCJ3aWR0aFwiOiBcIjEwMCVcIixcbiAgICBcImhlaWdodFwiOiBcIjMxNXB4XCIsXG4gICAgXCJkaXNwbGF5XCI6IFwibm9uZVwiLFxuICAgIFwiYmFja2dyb3VuZENvbG9yXCI6IFwiI2ZmZmZmZlwiLFxuICAgIFwiZmxleERpcmVjdGlvblwiOiBcImNvbHVtblwiLFxuICAgIFwicG9zaXRpb25cIjogXCJmaXhlZFwiLFxuICAgIFwiYm90dG9tXCI6IFwiMHB4XCIsXG4gICAgXCJib3JkZXJUb3BXaWR0aFwiOiBcIjFweFwiLFxuICAgIFwiYm9yZGVyUmlnaHRXaWR0aFwiOiBcIjFweFwiLFxuICAgIFwiYm9yZGVyQm90dG9tV2lkdGhcIjogXCIxcHhcIixcbiAgICBcImJvcmRlckxlZnRXaWR0aFwiOiBcIjFweFwiLFxuICAgIFwiYm9yZGVyU3R5bGVcIjogXCJzb2xpZFwiLFxuICAgIFwiYm9yZGVyVG9wQ29sb3JcIjogXCIjZjBlY2VjXCIsXG4gICAgXCJib3JkZXJSaWdodENvbG9yXCI6IFwiI2YwZWNlY1wiLFxuICAgIFwiYm9yZGVyQm90dG9tQ29sb3JcIjogXCIjZjBlY2VjXCIsXG4gICAgXCJib3JkZXJMZWZ0Q29sb3JcIjogXCIjZjBlY2VjXCJcbiAgfSxcbiAgXCIuc2hvd1wiOiB7XG4gICAgXCJkaXNwbGF5XCI6IFwiZmxleFwiXG4gIH0sXG4gIFwiLnBvcHVwLWxpbmUxXCI6IHtcbiAgICBcIndpZHRoXCI6IFwiMTAwJVwiLFxuICAgIFwiaGVpZ2h0XCI6IFwiMXB4XCIsXG4gICAgXCJiYWNrZ3JvdW5kQ29sb3JcIjogXCIjZjBlY2VjXCJcbiAgfSxcbiAgXCIucG9wdXAtdGV4dFwiOiB7XG4gICAgXCJ3aWR0aFwiOiBcIjEwMCVcIixcbiAgICBcImhlaWdodFwiOiBcIjEwMHB4XCIsXG4gICAgXCJ0ZXh0QWxpZ25cIjogXCJjZW50ZXJcIixcbiAgICBcImZvbnRTaXplXCI6IFwiMzBweFwiLFxuICAgIFwiY29sb3JcIjogXCIjMDAwMDAwXCJcbiAgfSxcbiAgXCIucG9wdXAtbGluZTJcIjoge1xuICAgIFwid2lkdGhcIjogXCIxMDAlXCIsXG4gICAgXCJoZWlnaHRcIjogXCIxMHB4XCIsXG4gICAgXCJiYWNrZ3JvdW5kQ29sb3JcIjogXCIjZjBlY2VjXCJcbiAgfVxufVxuXG5cbi8vLy8vLy8vLy8vLy8vLy8vL1xuLy8gV0VCUEFDSyBGT09URVJcbi8vIGQ6L3Rvb2xzL0h1YXdlaSBGYXN0QXBwIElERS9yZXNvdXJjZXMvYXBwL2V4dGVuc2lvbnMvZGV2ZWNvLWRlYnVnL25vZGVfbW9kdWxlcy9mYS10b29sa2l0L2xpYi9mYS1qc29uLWxvYWRlci5qcyFkOi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvZmEtdG9vbGtpdC9saWIvZmEtc3R5bGUtbG9hZGVyLmpzP2luZGV4PTAmdHlwZT1zdHlsZXMmcmVzb3VyY2VQYXRoPWQ6L1NlcnZpY2UgdmVyaWZpY2F0aW9uL0NvZGUvZmFzdEFwcC9sYW5kbWFya0FuZGRldGVjdGUvY29tLmh1YXdlaS5sYW5kbWFya2FuZGRldGVjdC9zcmMvRGV0ZWN0X0ZhY2UvZGV0ZWN0X2ZhY2UudXghZDovdG9vbHMvSHVhd2VpIEZhc3RBcHAgSURFL3Jlc291cmNlcy9hcHAvZXh0ZW5zaW9ucy9kZXZlY28tZGVidWcvbm9kZV9tb2R1bGVzL2ZhLXRvb2xraXQvbGliL2ZhLWZyYWdtZW50LWxvYWRlci5qcz9pbmRleD0wJnR5cGU9c3R5bGVzJnJlc291cmNlUGF0aD1kOi9TZXJ2aWNlIHZlcmlmaWNhdGlvbi9Db2RlL2Zhc3RBcHAvbGFuZG1hcmtBbmRkZXRlY3RlL2NvbS5odWF3ZWkubGFuZG1hcmthbmRkZXRlY3Qvc3JjL0RldGVjdF9GYWNlL2RldGVjdF9mYWNlLnV4IS4vc3JjL0RldGVjdF9GYWNlL2RldGVjdF9mYWNlLnV4XG4vLyBtb2R1bGUgaWQgPSA1XG4vLyBtb2R1bGUgY2h1bmtzID0gMSIsIm1vZHVsZS5leHBvcnRzID0gZnVuY3Rpb24obW9kdWxlLCBleHBvcnRzLCAkYXBwX3JlcXVpcmUkKXsndXNlIHN0cmljdCc7XG5cbnZhciBfc3lzdGVtID0gJGFwcF9yZXF1aXJlJCgnQGFwcC1tb2R1bGUvc3lzdGVtLmFpJyk7XG5cbnZhciBfc3lzdGVtMiA9IF9pbnRlcm9wUmVxdWlyZURlZmF1bHQoX3N5c3RlbSk7XG5cbnZhciBfc3lzdGVtMyA9ICRhcHBfcmVxdWlyZSQoJ0BhcHAtbW9kdWxlL3N5c3RlbS5tZWRpYScpO1xuXG52YXIgX3N5c3RlbTQgPSBfaW50ZXJvcFJlcXVpcmVEZWZhdWx0KF9zeXN0ZW0zKTtcblxudmFyIF9zeXN0ZW01ID0gJGFwcF9yZXF1aXJlJCgnQGFwcC1tb2R1bGUvc3lzdGVtLmltYWdlJyk7XG5cbnZhciBfc3lzdGVtNiA9IF9pbnRlcm9wUmVxdWlyZURlZmF1bHQoX3N5c3RlbTUpO1xuXG52YXIgX3N5c3RlbTcgPSAkYXBwX3JlcXVpcmUkKCdAYXBwLW1vZHVsZS9zeXN0ZW0uZGV2aWNlJyk7XG5cbnZhciBfc3lzdGVtOCA9IF9pbnRlcm9wUmVxdWlyZURlZmF1bHQoX3N5c3RlbTcpO1xuXG52YXIgX2RhdGEgPSByZXF1aXJlKCcuLi9Db21tb24vZGF0YScpO1xuXG5mdW5jdGlvbiBfaW50ZXJvcFJlcXVpcmVEZWZhdWx0KG9iaikgeyByZXR1cm4gb2JqICYmIG9iai5fX2VzTW9kdWxlID8gb2JqIDogeyBkZWZhdWx0OiBvYmogfTsgfVxuXG5tb2R1bGUuZXhwb3J0cyA9IHtcbiAgZGF0YToge1xuICAgIGxpc3Q6IFtcIuaLjeeFp1wiLCBcIuS7juebuOWGjOS4remAieaLqVwiLCBcIuWPlua2iFwiXSxcbiAgICBpbWFnZVVyaTogXCIvQ29tbW9uL2ltZy9pbml0aWFsX3BpY3R1cmVfb25lLnBuZ1wiLFxuICAgIHJlc3VsdF9saXN0OiBbXSxcbiAgICBpc1Nob3c6ICcnLFxuICAgIHBvaW50QXJyYXk6IFtdLFxuICAgIGxldmVsOiAnJyxcbiAgICBjYW52YXNXaWR0aDogXCI3NTBweFwiLFxuICAgIGNhbnZhc0hlaWdodDogXCI2MDBweFwiLFxuICAgIGlzRmlyc3Q6IHRydWUsXG4gICAgc2NyZWVuV2lkdGg6ICcnLFxuICAgIHNjcmVlbkhlaWdodDogJydcbiAgfSxcblxuICBvbkluaXQ6IGZ1bmN0aW9uIG9uSW5pdCgpIHtcbiAgICB0aGlzLiRwYWdlLnNldFRpdGxlQmFyKHsgdGV4dDogJ+S6uuiEuOajgOa1iycgfSk7XG4gICAgdmFyIHRoYXQgPSB0aGlzO1xuXG4gICAgX3N5c3RlbTguZGVmYXVsdC5nZXRJbmZvKHtcbiAgICAgIHN1Y2Nlc3M6IGZ1bmN0aW9uIHN1Y2Nlc3MocmV0KSB7XG4gICAgICAgIGNvbnNvbGUubG9nKCdoYW5kbGluZyBzdWNjZXNzJyk7XG4gICAgICAgIHRoYXQuc2NyZWVuV2lkdGggPSByZXQuc2NyZWVuV2lkdGg7XG4gICAgICAgIHRoYXQuc2NyZWVuSGVpZ2h0ID0gcmV0LnNjcmVlbkhlaWdodDtcbiAgICAgICAgY29uc29sZS5sb2coXCLlsY/luZXlrr3luqbvvJpcIiArIHJldC5zY3JlZW5XaWR0aCwgXCLlsY/luZXpq5jluqY6IFwiICsgcmV0LnNjcmVlbkhlaWdodCk7XG4gICAgICAgIHRoYXQuc2VsZWN0T25lSW1hZ2UoKTtcbiAgICAgIH1cbiAgICB9KTtcbiAgfSxcblxuICBzZWxlY3Q6IGZ1bmN0aW9uIHNlbGVjdCgpIHtcbiAgICB2YXIgdGhhdCA9IHRoaXM7XG4gICAgdGhhdC5pc1Nob3cgPSAnc2hvdyc7XG4gIH0sXG5cbiAgY2FuY2VsOiBmdW5jdGlvbiBjYW5jZWwoKSB7XG4gICAgdmFyIHRoYXQgPSB0aGlzO1xuICAgIHRoYXQuaXNTaG93ID0gJyc7XG4gIH0sXG5cbiAgc2VsZWN0TWVkaWE6IGZ1bmN0aW9uIHNlbGVjdE1lZGlhKGUpIHtcbiAgICB2YXIgdGhhdCA9IHRoaXM7XG4gICAgaWYgKGUgPT09IFwi5ouN54WnXCIpIHtcbiAgICAgIHRoYXQudGFrZVBob3RvKCk7XG4gICAgfSBlbHNlIGlmIChlID09PSBcIuS7juebuOWGjOS4remAieaLqVwiKSB7XG4gICAgICB0aGF0LnNlbGVjdE9uZUltYWdlKCk7XG4gICAgfSBlbHNlIHtcbiAgICAgIHRoYXQuY2FuY2VsKCk7XG4gICAgfVxuICB9LFxuXG4gIHRha2VQaG90bzogZnVuY3Rpb24gdGFrZVBob3RvKCkge1xuICAgIHZhciB0aGF0ID0gdGhpcztcbiAgICB0aGF0LmlzU2hvdyA9ICcnO1xuICAgIHRoYXQudGFrZXBob3RvYnRuKCkudGhlbihmdW5jdGlvbiAoZGF0YSkge1xuICAgICAgdGhhdC5jYWN1bGF0ZVJhdGlvKGRhdGEpO1xuICAgIH0pO1xuICB9LFxuXG4gIHNlbGVjdE9uZUltYWdlOiBmdW5jdGlvbiBzZWxlY3RPbmVJbWFnZSgpIHtcbiAgICB2YXIgdGhhdCA9IHRoaXM7XG4gICAgdGhhdC5pc1Nob3cgPSAnJztcbiAgICB0aGF0LnBpY2twaG90b2J0bigpLnRoZW4oZnVuY3Rpb24gKGRhdGEpIHtcbiAgICAgIHRoYXQuY2FjdWxhdGVSYXRpbyhkYXRhKTtcbiAgICB9KTtcbiAgfSxcblxuICBjYWN1bGF0ZVJhdGlvOiBmdW5jdGlvbiBjYWN1bGF0ZVJhdGlvKGRhdGEpIHtcbiAgICB2YXIgdGhhdCA9IHRoaXM7XG4gICAgdmFyIHJlYWxfd2lkdGggPSBkYXRhLndpZHRoO1xuICAgIHZhciByZWFsX2hlaWdodCA9IGRhdGEuaGVpZ2h0O1xuXG4gICAgdmFyIGNhY3VsYXRlSGVpZ2h0ID0gdGhhdC5zY3JlZW5IZWlnaHQgLyAyMDQwICogNTgwO1xuICAgIGNvbnNvbGUubG9nKFwiY2FjdWxhdGVIZWlnaHRcIiArIGNhY3VsYXRlSGVpZ2h0KTtcbiAgICBpZiAoZGF0YS5oZWlnaHQgPiBkYXRhLndpZHRoKSB7XG4gICAgICB2YXIgcmF0aW8gPSBkYXRhLmhlaWdodCAvIGNhY3VsYXRlSGVpZ2h0O1xuICAgICAgZGF0YS5oZWlnaHQgPSBjYWN1bGF0ZUhlaWdodDtcbiAgICAgIGRhdGEud2lkdGggPSBNYXRoLmNlaWwoZGF0YS53aWR0aCAvIHJhdGlvKSAlIDIgIT0gMCA/IE1hdGguY2VpbChkYXRhLndpZHRoIC8gcmF0aW8pIC0gMSA6IE1hdGguY2VpbChkYXRhLndpZHRoIC8gcmF0aW8pO1xuICAgICAgY29uc29sZS5sb2coXCLnrKzkuIDkuKppZlwiKTtcbiAgICB9IGVsc2UgaWYgKHJlYWxfaGVpZ2h0IDwgcmVhbF93aWR0aCkge1xuICAgICAgZGF0YS5oZWlnaHQgPSBjYWN1bGF0ZUhlaWdodDtcbiAgICAgIHZhciByYXRpbyA9IHJlYWxfaGVpZ2h0IC8gZGF0YS5oZWlnaHQ7XG4gICAgICBkYXRhLndpZHRoID0gTWF0aC5jZWlsKGRhdGEud2lkdGggLyByYXRpbykgJSAyICE9IDAgPyBNYXRoLmNlaWwoZGF0YS53aWR0aCAvIHJhdGlvKSAtIDEgOiBNYXRoLmNlaWwoZGF0YS53aWR0aCAvIHJhdGlvKTtcbiAgICAgIGNvbnNvbGUubG9nKFwi56ys5LqM5LiqaWZcIik7XG4gICAgfVxuXG4gICAgdGhhdC5yYXRpbyA9IChyZWFsX3dpZHRoIC8gZGF0YS53aWR0aCkudG9GaXhlZCgzKTtcbiAgICBjb25zb2xlLmxvZyhcIuWOi+e8qeWQjueahOWuvemrmO+8mlwiICsgZGF0YS53aWR0aCArIFwiLFwiICsgZGF0YS5oZWlnaHQpO1xuXG4gICAgdGhhdC5jYW52YXNXaWR0aCA9IGRhdGEud2lkdGggKyBcInB4XCI7XG4gICAgdGhhdC5jYW52YXNIZWlnaHQgPSBkYXRhLmhlaWdodCArIFwicHhcIjtcbiAgICBjb25zb2xlLmxvZyhcIuWOi+e8qeavlOS+i++8mlwiICsgKGRhdGEud2lkdGggLyByZWFsX3dpZHRoKS50b0ZpeGVkKDMpKTtcbiAgICBjb25zb2xlLmxvZyhcIumVv+Wuve+8mlwiICsgKGRhdGEud2lkdGggLyB0aGF0LnJhdGlvKS50b0ZpeGVkKDEpICsgXCIsXCIgKyAoZGF0YS5oZWlnaHQgLyB0aGF0LnJhdGlvKS50b0ZpeGVkKDEpKTtcblxuICAgIHZhciBhcmdzID0gW2RhdGEudXJpLCA3MF07XG4gICAgaWYgKHBhcnNlRmxvYXQodGhhdC5yYXRpbykgIT0gMSkge1xuICAgICAgYXJncy5wdXNoKHRoYXQucmF0aW8pO1xuICAgIH1cbiAgICBjb25zb2xlLmxvZyhcImNvbXByZXNzIGFyZ3MtLS0tLS0tXCIsIGFyZ3MpO1xuICAgIHZhciBwcm9taXNlID0gdGhhdC5jb21wcmVzc0ltYWdlYnRuLmFwcGx5KHt9LCBhcmdzKTtcbiAgICBwcm9taXNlLnRoZW4oZnVuY3Rpb24gKGRhdGEpIHtcbiAgICAgIHRoYXQuaW1hZ2VVcmkgPSBkYXRhLnVyaTtcbiAgICAgIGNvbnNvbGUubG9nKFwicGlja3Bob3Rv5Zu+54mHdXJsOlwiICsgdGhhdC5pbWFnZVVyaSk7XG4gICAgICB0aGF0LmRldGVjdEZhY2UoKTtcbiAgICB9KTtcbiAgfSxcblxuICBwaWNrcGhvdG9idG46IGZ1bmN0aW9uIHBpY2twaG90b2J0bigpIHtcbiAgICB2YXIgdGhhdCA9IHRoaXM7XG4gICAgcmV0dXJuIG5ldyBQcm9taXNlKGZ1bmN0aW9uIChyZXNvbHZlLCByZWplY3QpIHtcbiAgICAgIGlmICh0aGF0LmlzRmlyc3QgPT09IHRydWUpIHtcbiAgICAgICAgX3N5c3RlbTYuZGVmYXVsdC5nZXRJbWFnZUluZm8oe1xuICAgICAgICAgIHVyaTogdGhhdC5pbWFnZVVyaSxcbiAgICAgICAgICBzdWNjZXNzOiBmdW5jdGlvbiBzdWNjZXNzKGltYWdlSW5mbykge1xuICAgICAgICAgICAgY29uc29sZS5sb2coXCLljp/lp4st5ouN54WnLeWbvueJh+i3r+W+hO+8mlwiICsgaW1hZ2VJbmZvLnVyaSArIFwi5a695bqm77yaXCIgKyBpbWFnZUluZm8ud2lkdGggKyBcInB4LOmrmOW6pu+8mlwiICsgaW1hZ2VJbmZvLmhlaWdodCArIFwicHgs5bC65a+477yaXCIgKyAoaW1hZ2VJbmZvLnNpemUgLyAxMDI0KS50b0ZpeGVkKDIpICsgJ0tCJyk7XG5cbiAgICAgICAgICAgIHJlc29sdmUoaW1hZ2VJbmZvKTtcbiAgICAgICAgICB9LFxuICAgICAgICAgIGZhaWw6IGZ1bmN0aW9uIGZhaWwoaW1hZ2VJbmZvLCBjb2RlKSB7XG4gICAgICAgICAgICBjb25zb2xlLmxvZyhcImltYWdlSW5mbzogXCIgKyBpbWFnZUluZm8sIFwiY29kZTogXCIgKyBjb2RlKTtcbiAgICAgICAgICB9XG4gICAgICAgIH0pO1xuICAgICAgICB0aGF0LmlzRmlyc3QgPSBmYWxzZTtcbiAgICAgIH0gZWxzZSB7XG4gICAgICAgIF9zeXN0ZW00LmRlZmF1bHQucGlja0ltYWdlKHtcbiAgICAgICAgICBzdWNjZXNzOiBmdW5jdGlvbiBzdWNjZXNzKGRhdGEpIHtcbiAgICAgICAgICAgIGNvbnNvbGUubG9nKCdwaWNrcGhvdG/lrozmiJAnKTtcbiAgICAgICAgICAgIF9zeXN0ZW02LmRlZmF1bHQuZ2V0SW1hZ2VJbmZvKHtcbiAgICAgICAgICAgICAgdXJpOiBkYXRhLnVyaSxcbiAgICAgICAgICAgICAgc3VjY2VzczogZnVuY3Rpb24gc3VjY2VzcyhpbWFnZUluZm8pIHtcbiAgICAgICAgICAgICAgICBjb25zb2xlLmxvZyhcIuWOn+Wniy3mi43nhact5Zu+54mH6Lev5b6E77yaXCIgKyBpbWFnZUluZm8udXJpICsgXCLlrr3luqbvvJpcIiArIGltYWdlSW5mby53aWR0aCArIFwicHgs6auY5bqm77yaXCIgKyBpbWFnZUluZm8uaGVpZ2h0ICsgXCJweCzlsLrlr7jvvJpcIiArIChpbWFnZUluZm8uc2l6ZSAvIDEwMjQpLnRvRml4ZWQoMikgKyAnS0InKTtcblxuICAgICAgICAgICAgICAgIHJlc29sdmUoaW1hZ2VJbmZvKTtcbiAgICAgICAgICAgICAgfSxcbiAgICAgICAgICAgICAgZmFpbDogZnVuY3Rpb24gZmFpbChpbWFnZUluZm8sIGNvZGUpIHtcbiAgICAgICAgICAgICAgICBjb25zb2xlLmxvZyhcImltYWdlSW5mbzogXCIgKyBpbWFnZUluZm8sIFwiY29kZTogXCIgKyBjb2RlKTtcbiAgICAgICAgICAgICAgfVxuICAgICAgICAgICAgfSk7XG4gICAgICAgICAgfVxuXG4gICAgICAgIH0pO1xuICAgICAgfVxuICAgIH0pO1xuICB9LFxuXG4gIHRha2VwaG90b2J0bjogZnVuY3Rpb24gdGFrZXBob3RvYnRuKCkge1xuICAgIHJldHVybiBuZXcgUHJvbWlzZShmdW5jdGlvbiAocmVzb2x2ZSwgcmVqZWN0KSB7XG4gICAgICBfc3lzdGVtNC5kZWZhdWx0LnRha2VQaG90byh7XG4gICAgICAgIHN1Y2Nlc3M6IGZ1bmN0aW9uIHN1Y2Nlc3MoZGF0YSkge1xuICAgICAgICAgIGNvbnNvbGUubG9nKCd0YWtlcGhvdG/lrozmiJAnKTtcbiAgICAgICAgICBfc3lzdGVtNi5kZWZhdWx0LmdldEltYWdlSW5mbyh7XG4gICAgICAgICAgICB1cmk6IGRhdGEudXJpLFxuICAgICAgICAgICAgc3VjY2VzczogZnVuY3Rpb24gc3VjY2VzcyhpbWFnZUluZm8pIHtcbiAgICAgICAgICAgICAgY29uc29sZS5sb2coXCLljp/lp4st5ouN54WnLeWbvueJh+i3r+W+hO+8mlwiICsgaW1hZ2VJbmZvLnVyaSArIFwi5a695bqm77yaXCIgKyBpbWFnZUluZm8ud2lkdGggKyBcInB4LOmrmOW6pu+8mlwiICsgaW1hZ2VJbmZvLmhlaWdodCArIFwicHgs5bC65a+477yaXCIgKyAoaW1hZ2VJbmZvLnNpemUgLyAxMDI0KS50b0ZpeGVkKDIpICsgJ0tCJyk7XG5cbiAgICAgICAgICAgICAgcmVzb2x2ZShpbWFnZUluZm8pO1xuICAgICAgICAgICAgfSxcbiAgICAgICAgICAgIGZhaWw6IGZ1bmN0aW9uIGZhaWwoaW1hZ2VJbmZvLCBjb2RlKSB7XG4gICAgICAgICAgICAgIGNvbnNvbGUubG9nKFwiaW1hZ2VJbmZvOiBcIiArIGltYWdlSW5mbywgXCJjb2RlOiBcIiArIGNvZGUpO1xuICAgICAgICAgICAgfVxuICAgICAgICAgIH0pO1xuICAgICAgICB9XG4gICAgICB9KTtcbiAgICB9KTtcbiAgfSxcblxuICBjb21wcmVzc0ltYWdlYnRuOiBmdW5jdGlvbiBjb21wcmVzc0ltYWdlYnRuKHVyaSwgcXVhbGl0eSwgcmF0aW8pIHtcbiAgICByZXR1cm4gbmV3IFByb21pc2UoZnVuY3Rpb24gKHJlc29sdmUsIHJlamVjdCkge1xuICAgICAgX3N5c3RlbTYuZGVmYXVsdC5jb21wcmVzc0ltYWdlKHtcbiAgICAgICAgdXJpOiB1cmksXG4gICAgICAgIHF1YWxpdHk6IHF1YWxpdHksXG4gICAgICAgIHJhdGlvOiByYXRpbyA/IHJhdGlvIDogdW5kZWZpbmVkLFxuICAgICAgICBmb3JtYXQ6IFwiSlBFR1wiLFxuICAgICAgICBzdWNjZXNzOiBmdW5jdGlvbiBzdWNjZXNzKGNvbXByZXNzZWRJbWdJbmZvKSB7XG4gICAgICAgICAgX3N5c3RlbTYuZGVmYXVsdC5nZXRJbWFnZUluZm8oe1xuICAgICAgICAgICAgdXJpOiBjb21wcmVzc2VkSW1nSW5mby51cmksXG4gICAgICAgICAgICBzdWNjZXNzOiBmdW5jdGlvbiBzdWNjZXNzKGltYWdlSW5mbykge1xuICAgICAgICAgICAgICBjb25zb2xlLmxvZyhcIuWbvueJh+i3r+W+hO+8mlwiICsgaW1hZ2VJbmZvLnVyaSArIFwi5a695bqm77yaXCIgKyBpbWFnZUluZm8ud2lkdGggKyBcInB4LOmrmOW6pu+8mlwiICsgaW1hZ2VJbmZvLmhlaWdodCArIFwicHgs5bC65a+477yaXCIgKyAoaW1hZ2VJbmZvLnNpemUgLyAxMDI0KS50b0ZpeGVkKDIpICsgJ0tCJyk7XG4gICAgICAgICAgICAgIGlmIChpbWFnZUluZm8uaGVpZ2h0ICUgMiAhPSAwKSB7XG4gICAgICAgICAgICAgICAgX3N5c3RlbTYuZGVmYXVsdC5hcHBseU9wZXJhdGlvbnMoe1xuICAgICAgICAgICAgICAgICAgdXJpOiBpbWFnZUluZm8udXJpLFxuXG4gICAgICAgICAgICAgICAgICBvcGVyYXRpb25zOiBbe1xuICAgICAgICAgICAgICAgICAgICBhY3Rpb246ICdjcm9wJyxcbiAgICAgICAgICAgICAgICAgICAgd2lkdGg6IGltYWdlSW5mby53aWR0aCxcbiAgICAgICAgICAgICAgICAgICAgaGVpZ2h0OiBpbWFnZUluZm8uaGVpZ2h0IC0gMVxuICAgICAgICAgICAgICAgICAgfV0sXG4gICAgICAgICAgICAgICAgICBxdWFsaXR5OiA3MCxcbiAgICAgICAgICAgICAgICAgIGZvcm1hdDogJ0pQRUcnLFxuICAgICAgICAgICAgICAgICAgc3VjY2VzczogZnVuY3Rpb24gc3VjY2VzcyhkYXRhKSB7XG4gICAgICAgICAgICAgICAgICAgIF9zeXN0ZW02LmRlZmF1bHQuZ2V0SW1hZ2VJbmZvKHtcbiAgICAgICAgICAgICAgICAgICAgICB1cmk6IGRhdGEudXJpLFxuICAgICAgICAgICAgICAgICAgICAgIHN1Y2Nlc3M6IGZ1bmN0aW9uIHN1Y2Nlc3Mob3BlcmF0aW9uSW1nSW5mbykge1xuICAgICAgICAgICAgICAgICAgICAgICAgY29uc29sZS5sb2coXCLoo4HliarlkI4t5Zu+54mH6Lev5b6E77yaXCIgKyBvcGVyYXRpb25JbWdJbmZvLnVyaSArIFwi5a695bqm77yaXCIgKyBvcGVyYXRpb25JbWdJbmZvLndpZHRoICsgXCJweCzpq5jluqbvvJpcIiArIG9wZXJhdGlvbkltZ0luZm8uaGVpZ2h0ICsgXCJweCzlsLrlr7jvvJpcIiArIChvcGVyYXRpb25JbWdJbmZvLnNpemUgLyAxMDI0KS50b0ZpeGVkKDIpICsgJ0tCJyk7XG5cbiAgICAgICAgICAgICAgICAgICAgICAgIHJlc29sdmUob3BlcmF0aW9uSW1nSW5mbyk7XG4gICAgICAgICAgICAgICAgICAgICAgfSxcbiAgICAgICAgICAgICAgICAgICAgICBmYWlsOiBmdW5jdGlvbiBmYWlsKG9wZXJhdGlvbkltZ0luZm8sIGNvZGUpIHtcbiAgICAgICAgICAgICAgICAgICAgICAgIGNvbnNvbGUubG9nKFwiaGFuZGxpbmcgZmFpbCwgY29kZT1cIiArIGNvZGUpO1xuICAgICAgICAgICAgICAgICAgICAgIH1cbiAgICAgICAgICAgICAgICAgICAgfSk7XG4gICAgICAgICAgICAgICAgICB9LFxuICAgICAgICAgICAgICAgICAgZmFpbDogZnVuY3Rpb24gZmFpbChkYXRhLCBjb2RlKSB7XG4gICAgICAgICAgICAgICAgICAgIGNvbnNvbGUubG9nKFwiaGFuZGxpbmcgZmFpbCwgY29kZT1cIiArIGNvZGUpO1xuICAgICAgICAgICAgICAgICAgfVxuICAgICAgICAgICAgICAgIH0pO1xuICAgICAgICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgICAgICAgIGNvbnNvbGUubG9nKFwi5LiN5Ymq6KOBXCIpO1xuICAgICAgICAgICAgICAgIHJlc29sdmUoaW1hZ2VJbmZvKTtcbiAgICAgICAgICAgICAgfVxuICAgICAgICAgICAgfSxcbiAgICAgICAgICAgIGZhaWw6IGZ1bmN0aW9uIGZhaWwoaW1hZ2VJbmZvLCBjb2RlKSB7XG4gICAgICAgICAgICAgIGNvbnNvbGUubG9nKFwiY29tcHJlc3NJbWFnZWJ0biBnZXRJbWFnZUluZm8gZmFpbCwgY29kZT1cIiArIGNvZGUpO1xuICAgICAgICAgICAgICByZWplY3QoaW1hZ2VJbmZvKTtcbiAgICAgICAgICAgIH1cbiAgICAgICAgICB9KTtcbiAgICAgICAgfSxcbiAgICAgICAgZmFpbDogZnVuY3Rpb24gZmFpbChjb21wcmVzc2VkSW1nSW5mbywgY29kZSkge1xuICAgICAgICAgIGNvbnNvbGUubG9nKFwiY29tcHJlc3NJbWFnZWJ0biBmYWlsLCBjb2RlPVwiICsgY29kZSk7XG4gICAgICAgIH1cbiAgICAgIH0pO1xuICAgIH0pO1xuICB9LFxuXG4gIGRldGVjdEZhY2U6IGZ1bmN0aW9uIGRldGVjdEZhY2UoKSB7XG4gICAgdmFyIHRoYXQgPSB0aGlzO1xuICAgIHZhciB0ZW1wTGlzdCA9IFtdO1xuXG4gICAgdmFyIGNsZWFyUmVjdCA9IHRoYXQuJGVsZW1lbnQoXCJjYW52YXNcIik7XG4gICAgdmFyIGN0eCA9IGNsZWFyUmVjdC5nZXRDb250ZXh0KFwiMmRcIik7XG5cbiAgICBjdHguY2xlYXJSZWN0KDAsIDAsIHRoYXQuc2NyZWVuV2lkdGgsIDgwMCk7XG5cbiAgICBfc3lzdGVtMi5kZWZhdWx0LmRldGVjdEZhY2Uoe1xuICAgICAgdXJpOiB0aGF0LmltYWdlVXJpLFxuICAgICAgc3VjY2VzczogZnVuY3Rpb24gc3VjY2VzcyhkYXRhKSB7XG4gICAgICAgIGNvbnNvbGUubG9nKFwic3VjY2VzcyBkYXRhOlwiICsgSlNPTi5zdHJpbmdpZnkoZGF0YSkpO1xuICAgICAgICB0aGF0LnBvaW50QXJyYXkgPSBkYXRhLmZhY2VzO1xuICAgICAgICB2YXIgZmFjZU51bWJlciA9IFwi5Lq66IS45Liq5pWw77yaICBcIiArIGRhdGEuZmFjZXMubGVuZ3RoO1xuICAgICAgICB0ZW1wTGlzdC5wdXNoKGZhY2VOdW1iZXIpO1xuICAgICAgICB0aGF0LnJlc3VsdF9saXN0ID0gdGVtcExpc3Q7XG4gICAgICB9LFxuICAgICAgZmFpbDogZnVuY3Rpb24gZmFpbChkYXRhLCBjb2RlKSB7XG4gICAgICAgIGNvbnNvbGUubG9nKFwiaGFuZGxpbmcgZmFpbCwgY29kZTogXCIgKyBjb2RlKTtcbiAgICAgICAgdmFyIGZhaWxDb2RlID0gXCLplJnor6/noIHvvJogIFwiICsgX2RhdGEuZXJyQ29kZUxpc3RbY29kZV07XG4gICAgICAgIHRoYXQucmVzdWx0X2xpc3QucHVzaChmYWlsQ29kZSk7XG4gICAgICB9LFxuICAgICAgY29tcGxldGU6IGZ1bmN0aW9uIGNvbXBsZXRlKCkge1xuICAgICAgICBjb25zb2xlLmxvZyhcImhhbmRsaW5nIGNvbXBsZXRlXCIpO1xuXG4gICAgICAgIHZhciBjYW5zID0gdGhhdC4kZWxlbWVudCgnY2FudmFzJyk7XG4gICAgICAgIHZhciBjdHggPSBjYW5zLmdldENvbnRleHQoXCIyZFwiKTtcbiAgICAgICAgZm9yICh2YXIgaSA9IDA7IGkgPCB0aGF0LnBvaW50QXJyYXkubGVuZ3RoOyBpKyspIHtcbiAgICAgICAgICBjdHguc3Ryb2tlU3R5bGUgPSBcIiMwMGZmZmZcIjtcbiAgICAgICAgICBjdHgubGluZVdpZHRoID0gNTtcbiAgICAgICAgICBjb25zb2xlLmxvZyhcImxlZnQ6XCIgKyB0aGF0LnBvaW50QXJyYXlbaV0uZmFjZVJlY3QubGVmdCArIFwidG9wOlwiICsgdGhhdC5wb2ludEFycmF5W2ldLmZhY2VSZWN0LnRvcCArIFwid2lkdGg6XCIgKyB0aGF0LnBvaW50QXJyYXlbaV0uZmFjZVJlY3Qud2lkdGggKyBcImhlaWdodDpcIiArIHRoYXQucG9pbnRBcnJheVtpXS5mYWNlUmVjdC5oZWlnaHQpO1xuICAgICAgICAgIGN0eC5zdHJva2VSZWN0KHRoYXQucG9pbnRBcnJheVtpXS5mYWNlUmVjdC5sZWZ0LCB0aGF0LnBvaW50QXJyYXlbaV0uZmFjZVJlY3QudG9wLCB0aGF0LnBvaW50QXJyYXlbaV0uZmFjZVJlY3Qud2lkdGgsIHRoYXQucG9pbnRBcnJheVtpXS5mYWNlUmVjdC5oZWlnaHQpO1xuICAgICAgICB9XG4gICAgICB9XG4gICAgfSk7XG4gIH1cbn07XG52YXIgbW9kdWxlT3duID0gZXhwb3J0cy5kZWZhdWx0IHx8IG1vZHVsZS5leHBvcnRzO1xudmFyIGFjY2Vzc29ycyA9IFsncHVibGljJywgJ3Byb3RlY3RlZCcsICdwcml2YXRlJ107XG5pZiAobW9kdWxlT3duLmRhdGEgJiYgYWNjZXNzb3JzLnNvbWUoZnVuY3Rpb24gKGFjYykge1xuICAgIHJldHVybiBtb2R1bGVPd25bYWNjXTtcbiAgfSkpIHtcbiAgdGhyb3cgbmV3IEVycm9yKCfpobXpnaJWTeWvueixoeS4reWxnuaAp2RhdGHkuI3lj6/kuI5wdWJsaWMsIHByb3RlY3RlZCwgcHJpdmF0ZeWQjOaXtuWtmOWcqO+8jOivt+S9v+eUqHB1YmxpY+abv+S7o2RhdGHvvIEnKTtcbn0gZWxzZSBpZiAoIW1vZHVsZU93bi5kYXRhKSB7XG4gIG1vZHVsZU93bi5kYXRhID0ge307XG4gIG1vZHVsZU93bi5fZGVzY3JpcHRvciA9IHt9O1xuICBhY2Nlc3NvcnMuZm9yRWFjaChmdW5jdGlvbihhY2MpIHtcbiAgICB2YXIgYWNjVHlwZSA9IHR5cGVvZiBtb2R1bGVPd25bYWNjXTtcbiAgICBpZiAoYWNjVHlwZSA9PT0gJ29iamVjdCcpIHtcbiAgICAgIG1vZHVsZU93bi5kYXRhID0gT2JqZWN0LmFzc2lnbihtb2R1bGVPd24uZGF0YSwgbW9kdWxlT3duW2FjY10pO1xuICAgICAgZm9yICh2YXIgbmFtZSBpbiBtb2R1bGVPd25bYWNjXSkge1xuICAgICAgICBtb2R1bGVPd24uX2Rlc2NyaXB0b3JbbmFtZV0gPSB7YWNjZXNzIDogYWNjfTtcbiAgICAgIH1cbiAgICB9IGVsc2UgaWYgKGFjY1R5cGUgPT09ICdmdW5jdGlvbicpIHtcbiAgICAgIGNvbnNvbGUud2Fybign6aG16Z2iVk3lr7nosaHkuK3nmoTlsZ7mgKcnICsgYWNjICsgJ+eahOWAvOS4jeiDveS9v+WHveaVsO+8jOivt+S9v+eUqOWvueixoScpO1xuICAgIH1cbiAgfSk7XG59fVxuXG5cbi8vLy8vLy8vLy8vLy8vLy8vL1xuLy8gV0VCUEFDSyBGT09URVJcbi8vIGQ6L3Rvb2xzL0h1YXdlaSBGYXN0QXBwIElERS9yZXNvdXJjZXMvYXBwL2V4dGVuc2lvbnMvZGV2ZWNvLWRlYnVnL25vZGVfbW9kdWxlcy9mYS10b29sa2l0L2xpYi9mYS1zY3JpcHQtbG9hZGVyLmpzIWQ6L3Rvb2xzL0h1YXdlaSBGYXN0QXBwIElERS9yZXNvdXJjZXMvYXBwL2V4dGVuc2lvbnMvZGV2ZWNvLWRlYnVnL25vZGVfbW9kdWxlcy9mYS10b29sa2l0L2xpYi9mYS1hY2Nlc3MtbG9hZGVyLmpzIWQ6L3Rvb2xzL0h1YXdlaSBGYXN0QXBwIElERS9yZXNvdXJjZXMvYXBwL2V4dGVuc2lvbnMvZGV2ZWNvLWRlYnVnL25vZGVfbW9kdWxlcy9iYWJlbC1sb2FkZXIvbGliP3ByZXNldHNbXT1kOi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvYmFiZWwtcHJlc2V0LWVudiZwbHVnaW5zW109ZDovdG9vbHMvSHVhd2VpIEZhc3RBcHAgSURFL3Jlc291cmNlcy9hcHAvZXh0ZW5zaW9ucy9kZXZlY28tZGVidWcvbm9kZV9tb2R1bGVzL2ZhLXRvb2xraXQvbGliL2pzeC1sb2FkZXIuanMmY29tbWVudHM9ZmFsc2UhZDovdG9vbHMvSHVhd2VpIEZhc3RBcHAgSURFL3Jlc291cmNlcy9hcHAvZXh0ZW5zaW9ucy9kZXZlY28tZGVidWcvbm9kZV9tb2R1bGVzL2ZhLXRvb2xraXQvbGliL2ZhLWZyYWdtZW50LWxvYWRlci5qcz9pbmRleD0wJnR5cGU9c2NyaXB0cyEuL3NyYy9EZXRlY3RfRmFjZS9kZXRlY3RfZmFjZS51eFxuLy8gbW9kdWxlIGlkID0gNlxuLy8gbW9kdWxlIGNodW5rcyA9IDEiXSwibWFwcGluZ3MiOiI7QUFBQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBOzs7Ozs7OztBQzdEQTtBQUFBO0FBQUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7Ozs7Ozs7O0FDaEJBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7Ozs7OztBQ2JBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7Ozs7OztBQzFQQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7Ozs7OztBQ3RJQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBOzs7QSIsInNvdXJjZVJvb3QiOiIifQ==