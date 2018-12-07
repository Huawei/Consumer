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
/******/ 	return __webpack_require__(__webpack_require__.s = 7);
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
/* 3 */,
/* 4 */,
/* 5 */,
/* 6 */,
/* 7 */
/***/ (function(module, exports, __webpack_require__) {

var $app_template$ = __webpack_require__(8)
var $app_style$ = __webpack_require__(9)
var $app_script$ = __webpack_require__(10)

$app_define$('@app-component/detect_face_land_mark', [], function($app_require$, $app_exports$, $app_module$){
     $app_script$($app_module$, $app_exports$, $app_require$)
     if ($app_exports$.__esModule && $app_exports$.default) {
            $app_module$.exports = $app_exports$.default
        }
     $app_module$.exports.template = $app_template$
     $app_module$.exports.style = $app_style$
})

$app_bootstrap$('@app-component/detect_face_land_mark',{ packagerName:'fa-toolkit', packagerVersion: '1.0.8-Stable.300'})

/***/ }),
/* 8 */
/***/ (function(module, exports) {

module.exports = {
  "type": "div",
  "attr": {},
  "classList": [
    "container"
  ],
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
            "src": function () {return this.imageUri}
          },
          "classList": [
            "face-image"
          ]
        },
        {
          "type": "canvas",
          "attr": {
            "id": "canvas"
          },
          "id": "canvas",
          "style": {
            "flex": 1,
            "width": function () {return (this.canvasWidth) + 'px'},
            "height": function () {return (this.canvasHeight) + 'px'}
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
/* 9 */
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
/* 10 */
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
    canvasWidth: '',
    canvasHeight: '',
    ratio: 1,
    isFirst: true,
    screenWidth: '',
    screenHeight: ''
  },

  onInit: function onInit() {
    this.$page.setTitleBar({ text: '五官特征检测' });
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
    console.log("real_width" + data.width, "real_height" + data.height);

    var caculateHeight = that.screenHeight / 2040 * 580;
    console.log("caculateHeight" + caculateHeight);
    if (data.height > data.width) {
      var ratio = data.height / caculateHeight;
      data.height = caculateHeight;
      data.width = Math.ceil(data.width / ratio) % 2 != 0 ? Math.ceil(data.width / ratio) - 1 : Math.ceil(data.width / ratio);
    } else if (real_height < real_width) {
      data.height = caculateHeight;
      var ratio = real_height / data.height;
      data.width = Math.ceil(data.width / ratio) % 2 != 0 ? Math.ceil(data.width / ratio) - 1 : Math.ceil(data.width / ratio);
    }

    that.ratio = (real_width / data.width).toFixed(3);
    console.log("压缩后的宽高：" + data.width + "," + data.height);

    that.canvasWidth = data.width;
    that.canvasHeight = data.height;
    console.log("canvasWidth:" + that.canvasWidth, "canvasHeight:" + that.canvasHeight);
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
      console.log("pickphoto image url:" + that.imageUri);
      that.detectFaceLand();
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
            console.log("imageInfo: " + imageInfo, "code:" + code);
          }
        });
        that.isFirst = false;
      } else {
        _system4.default.pickImage({
          success: function success(data) {
            _system6.default.getImageInfo({
              uri: data.uri,
              success: function success(imageInfo) {
                console.log("原始-拍照-图片路径：" + imageInfo.uri + "宽度：" + imageInfo.width + "px,高度：" + imageInfo.height + "px,尺寸：" + (imageInfo.size / 1024).toFixed(2) + 'KB');

                resolve(imageInfo);
              },
              fail: function fail(imageInfo, code) {
                console.log("imageInfo: " + imageInfo, "code:" + code);
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
        success: function success(compressedImg) {
          console.log(compressedImg.uri);

          _system6.default.getImageInfo({
            uri: compressedImg.uri,
            success: function success(compressedImgInfo) {
              console.log("图片路径：" + compressedImgInfo.uri + "宽度：" + compressedImgInfo.width + "px,高度：" + compressedImgInfo.height + "px,尺寸：" + (compressedImgInfo.size / 1024).toFixed(2) + 'KB');
              if (compressedImgInfo.height % 2 != 0) {
                _system6.default.applyOperations({
                  uri: compressedImgInfo.uri,

                  operations: [{
                    action: 'crop',
                    width: compressedImgInfo.width,
                    height: compressedImgInfo.height - 1
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
                resolve(compressedImgInfo);
              }
            },
            fail: function fail(compressedImgInfo, code) {
              console.log("compressImagebtn getImageInfo fail, code=" + code);
              reject(compressedImgInfo);
            }
          });
        },
        fail: function fail(compressedImg, code) {
          console.log("compressImagebtn fail, code=" + code);
        }
      });
    });
  },

  detectFaceLand: function detectFaceLand() {
    var that = this;
    var tempList = [];

    var clearRect = that.$element("canvas");
    var ctx = clearRect.getContext("2d");

    ctx.clearRect(0, 0, that.screenWidth, 800);

    _system2.default.detectFaceLandMark({
      uri: that.imageUri,
      success: function success(data) {
        that.pointArray = data.marks;
        console.log("data.mark:" + JSON.stringify(data.marks));
      },
      fail: function fail(data, code) {
        console.log("handling fail, code: " + code);
        var co = "错误码:  " + _data.errCodeList[code];
        tempList.push(co);
      },
      complete: function complete(msg) {
        console.log("detect_face_land_mark handling complete");

        var cans = that.$element('canvas');
        var ctx = cans.getContext("2d");
        for (var i = 0; i < that.pointArray.length; i++) {
          ctx.beginPath();
          ctx.arc(that.pointArray[i].positionF.x, that.pointArray[i].positionF.y, 3, 0, 2 * Math.PI);
          ctx.strokeStyle = "#00ffff";
          ctx.fillStyle = "#00ffff";
          ctx.fill();
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
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiYnVpbGRcXERldGVjdF9GYWNlX0xhbmRfTWFya1xcZGV0ZWN0X2ZhY2VfbGFuZF9tYXJrLmpzIiwic291cmNlcyI6WyJ3ZWJwYWNrOi8vL3dlYnBhY2svYm9vdHN0cmFwIGM0NTczNjc3ZDUwYzBjZTZiNjFjIiwid2VicGFjazovLy8uL3NyYy9Db21tb24vZGF0YS5qcyIsIndlYnBhY2s6Ly8vLi9zcmMvRGV0ZWN0X0ZhY2VfTGFuZF9NYXJrL2RldGVjdF9mYWNlX2xhbmRfbWFyay51eCIsIndlYnBhY2s6Ly8vLi9zcmMvRGV0ZWN0X0ZhY2VfTGFuZF9NYXJrL2RldGVjdF9mYWNlX2xhbmRfbWFyay51eD8yOGZiIiwid2VicGFjazovLy8uL3NyYy9EZXRlY3RfRmFjZV9MYW5kX01hcmsvZGV0ZWN0X2ZhY2VfbGFuZF9tYXJrLnV4P2JjMzAiLCJ3ZWJwYWNrOi8vLy4vc3JjL0RldGVjdF9GYWNlX0xhbmRfTWFyay9kZXRlY3RfZmFjZV9sYW5kX21hcmsudXg/MDRmZCJdLCJzb3VyY2VzQ29udGVudCI6WyIgXHQvLyBUaGUgbW9kdWxlIGNhY2hlXG4gXHR2YXIgaW5zdGFsbGVkTW9kdWxlcyA9IHt9O1xuXG4gXHQvLyBUaGUgcmVxdWlyZSBmdW5jdGlvblxuIFx0ZnVuY3Rpb24gX193ZWJwYWNrX3JlcXVpcmVfXyhtb2R1bGVJZCkge1xuXG4gXHRcdC8vIENoZWNrIGlmIG1vZHVsZSBpcyBpbiBjYWNoZVxuIFx0XHRpZihpbnN0YWxsZWRNb2R1bGVzW21vZHVsZUlkXSkge1xuIFx0XHRcdHJldHVybiBpbnN0YWxsZWRNb2R1bGVzW21vZHVsZUlkXS5leHBvcnRzO1xuIFx0XHR9XG4gXHRcdC8vIENyZWF0ZSBhIG5ldyBtb2R1bGUgKGFuZCBwdXQgaXQgaW50byB0aGUgY2FjaGUpXG4gXHRcdHZhciBtb2R1bGUgPSBpbnN0YWxsZWRNb2R1bGVzW21vZHVsZUlkXSA9IHtcbiBcdFx0XHRpOiBtb2R1bGVJZCxcbiBcdFx0XHRsOiBmYWxzZSxcbiBcdFx0XHRleHBvcnRzOiB7fVxuIFx0XHR9O1xuXG4gXHRcdC8vIEV4ZWN1dGUgdGhlIG1vZHVsZSBmdW5jdGlvblxuIFx0XHRtb2R1bGVzW21vZHVsZUlkXS5jYWxsKG1vZHVsZS5leHBvcnRzLCBtb2R1bGUsIG1vZHVsZS5leHBvcnRzLCBfX3dlYnBhY2tfcmVxdWlyZV9fKTtcblxuIFx0XHQvLyBGbGFnIHRoZSBtb2R1bGUgYXMgbG9hZGVkXG4gXHRcdG1vZHVsZS5sID0gdHJ1ZTtcblxuIFx0XHQvLyBSZXR1cm4gdGhlIGV4cG9ydHMgb2YgdGhlIG1vZHVsZVxuIFx0XHRyZXR1cm4gbW9kdWxlLmV4cG9ydHM7XG4gXHR9XG5cblxuIFx0Ly8gZXhwb3NlIHRoZSBtb2R1bGVzIG9iamVjdCAoX193ZWJwYWNrX21vZHVsZXNfXylcbiBcdF9fd2VicGFja19yZXF1aXJlX18ubSA9IG1vZHVsZXM7XG5cbiBcdC8vIGV4cG9zZSB0aGUgbW9kdWxlIGNhY2hlXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLmMgPSBpbnN0YWxsZWRNb2R1bGVzO1xuXG4gXHQvLyBkZWZpbmUgZ2V0dGVyIGZ1bmN0aW9uIGZvciBoYXJtb255IGV4cG9ydHNcbiBcdF9fd2VicGFja19yZXF1aXJlX18uZCA9IGZ1bmN0aW9uKGV4cG9ydHMsIG5hbWUsIGdldHRlcikge1xuIFx0XHRpZighX193ZWJwYWNrX3JlcXVpcmVfXy5vKGV4cG9ydHMsIG5hbWUpKSB7XG4gXHRcdFx0T2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIG5hbWUsIHtcbiBcdFx0XHRcdGNvbmZpZ3VyYWJsZTogZmFsc2UsXG4gXHRcdFx0XHRlbnVtZXJhYmxlOiB0cnVlLFxuIFx0XHRcdFx0Z2V0OiBnZXR0ZXJcbiBcdFx0XHR9KTtcbiBcdFx0fVxuIFx0fTtcblxuIFx0Ly8gZ2V0RGVmYXVsdEV4cG9ydCBmdW5jdGlvbiBmb3IgY29tcGF0aWJpbGl0eSB3aXRoIG5vbi1oYXJtb255IG1vZHVsZXNcbiBcdF9fd2VicGFja19yZXF1aXJlX18ubiA9IGZ1bmN0aW9uKG1vZHVsZSkge1xuIFx0XHR2YXIgZ2V0dGVyID0gbW9kdWxlICYmIG1vZHVsZS5fX2VzTW9kdWxlID9cbiBcdFx0XHRmdW5jdGlvbiBnZXREZWZhdWx0KCkgeyByZXR1cm4gbW9kdWxlWydkZWZhdWx0J107IH0gOlxuIFx0XHRcdGZ1bmN0aW9uIGdldE1vZHVsZUV4cG9ydHMoKSB7IHJldHVybiBtb2R1bGU7IH07XG4gXHRcdF9fd2VicGFja19yZXF1aXJlX18uZChnZXR0ZXIsICdhJywgZ2V0dGVyKTtcbiBcdFx0cmV0dXJuIGdldHRlcjtcbiBcdH07XG5cbiBcdC8vIE9iamVjdC5wcm90b3R5cGUuaGFzT3duUHJvcGVydHkuY2FsbFxuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5vID0gZnVuY3Rpb24ob2JqZWN0LCBwcm9wZXJ0eSkgeyByZXR1cm4gT2JqZWN0LnByb3RvdHlwZS5oYXNPd25Qcm9wZXJ0eS5jYWxsKG9iamVjdCwgcHJvcGVydHkpOyB9O1xuXG4gXHQvLyBfX3dlYnBhY2tfcHVibGljX3BhdGhfX1xuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5wID0gXCJcIjtcblxuIFx0Ly8gTG9hZCBlbnRyeSBtb2R1bGUgYW5kIHJldHVybiBleHBvcnRzXG4gXHRyZXR1cm4gX193ZWJwYWNrX3JlcXVpcmVfXyhfX3dlYnBhY2tfcmVxdWlyZV9fLnMgPSA3KTtcblxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyB3ZWJwYWNrL2Jvb3RzdHJhcCBjNDU3MzY3N2Q1MGMwY2U2YjYxYyIsImNvbnN0IGVyckNvZGVMaXN0ID0ge1xuICBcIjIwMlwiOiBcIuWPguaVsOmUmeivr1wiLFxuICBcIjIwM1wiOiBcIuacjeWKoeS4jeWPr+eUqFwiLFxuICBcIjgwMFwiOiBcIuS4gOiIrOaAp+ajgOa1i+Wksei0pVwiLFxuICBcIjgwMVwiOiBcIuaooeWei+aWh+S7tumUmeivr1wiLFxuICBcIjgwMlwiOiBcIuaooeWei+WKoOi9veWksei0pVwiLFxuICBcIjgwM1wiOiBcIuelnue7j+e9kee7nOWkhOeQhuWNleWFg+mUmeivr1wiLFxuICBcIjgwNFwiOiBcIuW9lemfs+ebuOWFs+mUmeivr1wiLFxuICBcIjgwNVwiOiBcIuWuouaIt+err+acquiwg+eUqOW8leaTjuWIneWni+WMllwiLFxuICBcIjgwNlwiOiBcIuayoeacieWjsOmfs1wiLFxuICBcIjgwN1wiOiBcIuacquWMuemFjeivhuWIq+e7k+aenFwiLFxuICBcIjgwOFwiOiBcIuWuouaIt+err+adg+mZkOS4jei2s1wiLFxuICBcIjgwOVwiOiBcIuacjeWKoeerr+adg+mZkOS4jei2s1wiLFxuICBcIjgxMFwiOiBcIuiuvuWkh0FJRW5naW5l55qE5Yqf6IO95byA5YWz6KKr5YWz6Zet5LqG77yM5peg5rOV5L2/55SoXCIsXG4gIFwiODExXCI6IFwi5LiN5pSv5oyB55qE5Yqf6IO95oiW5o6l5Y+jXCJcbn07XG5cbmV4cG9ydCB7IGVyckNvZGVMaXN0IH07XG5cblxuLy8vLy8vLy8vLy8vLy8vLy8vXG4vLyBXRUJQQUNLIEZPT1RFUlxuLy8gLi9zcmMvQ29tbW9uL2RhdGEuanNcbi8vIG1vZHVsZSBpZCA9IDBcbi8vIG1vZHVsZSBjaHVua3MgPSAwIDEiLCJ2YXIgJGFwcF90ZW1wbGF0ZSQgPSByZXF1aXJlKFwiISEuLi8uLi8uLi8uLi8uLi8uLi8uLi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvZmEtdG9vbGtpdC9saWIvZmEtanNvbi1sb2FkZXIuanMhLi4vLi4vLi4vLi4vLi4vLi4vLi4vdG9vbHMvSHVhd2VpIEZhc3RBcHAgSURFL3Jlc291cmNlcy9hcHAvZXh0ZW5zaW9ucy9kZXZlY28tZGVidWcvbm9kZV9tb2R1bGVzL2ZhLXRvb2xraXQvbGliL2ZhLXRlbXBsYXRlLWxvYWRlci5qcyEuLi8uLi8uLi8uLi8uLi8uLi8uLi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvZmEtdG9vbGtpdC9saWIvZmEtZnJhZ21lbnQtbG9hZGVyLmpzP2luZGV4PTAmdHlwZT10ZW1wbGF0ZXMhLi9kZXRlY3RfZmFjZV9sYW5kX21hcmsudXhcIilcbnZhciAkYXBwX3N0eWxlJCA9IHJlcXVpcmUoXCIhIS4uLy4uLy4uLy4uLy4uLy4uLy4uL3Rvb2xzL0h1YXdlaSBGYXN0QXBwIElERS9yZXNvdXJjZXMvYXBwL2V4dGVuc2lvbnMvZGV2ZWNvLWRlYnVnL25vZGVfbW9kdWxlcy9mYS10b29sa2l0L2xpYi9mYS1qc29uLWxvYWRlci5qcyEuLi8uLi8uLi8uLi8uLi8uLi8uLi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvZmEtdG9vbGtpdC9saWIvZmEtc3R5bGUtbG9hZGVyLmpzP2luZGV4PTAmdHlwZT1zdHlsZXMmcmVzb3VyY2VQYXRoPWQ6XFxcXFNlcnZpY2UgdmVyaWZpY2F0aW9uXFxcXENvZGVcXFxcZmFzdEFwcFxcXFxsYW5kbWFya0FuZGRldGVjdGVcXFxcY29tLmh1YXdlaS5sYW5kbWFya2FuZGRldGVjdFxcXFxzcmNcXFxcRGV0ZWN0X0ZhY2VfTGFuZF9NYXJrXFxcXGRldGVjdF9mYWNlX2xhbmRfbWFyay51eCEuLi8uLi8uLi8uLi8uLi8uLi8uLi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvZmEtdG9vbGtpdC9saWIvZmEtZnJhZ21lbnQtbG9hZGVyLmpzP2luZGV4PTAmdHlwZT1zdHlsZXMmcmVzb3VyY2VQYXRoPWQ6XFxcXFNlcnZpY2UgdmVyaWZpY2F0aW9uXFxcXENvZGVcXFxcZmFzdEFwcFxcXFxsYW5kbWFya0FuZGRldGVjdGVcXFxcY29tLmh1YXdlaS5sYW5kbWFya2FuZGRldGVjdFxcXFxzcmNcXFxcRGV0ZWN0X0ZhY2VfTGFuZF9NYXJrXFxcXGRldGVjdF9mYWNlX2xhbmRfbWFyay51eCEuL2RldGVjdF9mYWNlX2xhbmRfbWFyay51eFwiKVxudmFyICRhcHBfc2NyaXB0JCA9IHJlcXVpcmUoXCIhIS4uLy4uLy4uLy4uLy4uLy4uLy4uL3Rvb2xzL0h1YXdlaSBGYXN0QXBwIElERS9yZXNvdXJjZXMvYXBwL2V4dGVuc2lvbnMvZGV2ZWNvLWRlYnVnL25vZGVfbW9kdWxlcy9mYS10b29sa2l0L2xpYi9mYS1zY3JpcHQtbG9hZGVyLmpzIS4uLy4uLy4uLy4uLy4uLy4uLy4uL3Rvb2xzL0h1YXdlaSBGYXN0QXBwIElERS9yZXNvdXJjZXMvYXBwL2V4dGVuc2lvbnMvZGV2ZWNvLWRlYnVnL25vZGVfbW9kdWxlcy9mYS10b29sa2l0L2xpYi9mYS1hY2Nlc3MtbG9hZGVyLmpzIS4uLy4uLy4uLy4uLy4uLy4uLy4uL3Rvb2xzL0h1YXdlaSBGYXN0QXBwIElERS9yZXNvdXJjZXMvYXBwL2V4dGVuc2lvbnMvZGV2ZWNvLWRlYnVnL25vZGVfbW9kdWxlcy9iYWJlbC1sb2FkZXI/cHJlc2V0c1tdPWQ6XFxcXHRvb2xzXFxcXEh1YXdlaSBGYXN0QXBwIElERVxcXFxyZXNvdXJjZXNcXFxcYXBwXFxcXGV4dGVuc2lvbnNcXFxcZGV2ZWNvLWRlYnVnXFxcXG5vZGVfbW9kdWxlc1xcXFxiYWJlbC1wcmVzZXQtZW52JnBsdWdpbnNbXT1kOlxcXFx0b29sc1xcXFxIdWF3ZWkgRmFzdEFwcCBJREVcXFxccmVzb3VyY2VzXFxcXGFwcFxcXFxleHRlbnNpb25zXFxcXGRldmVjby1kZWJ1Z1xcXFxub2RlX21vZHVsZXNcXFxcZmEtdG9vbGtpdFxcXFxsaWJcXFxcanN4LWxvYWRlci5qcyZjb21tZW50cz1mYWxzZSEuLi8uLi8uLi8uLi8uLi8uLi8uLi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvZmEtdG9vbGtpdC9saWIvZmEtZnJhZ21lbnQtbG9hZGVyLmpzP2luZGV4PTAmdHlwZT1zY3JpcHRzIS4vZGV0ZWN0X2ZhY2VfbGFuZF9tYXJrLnV4XCIpXG5cbiRhcHBfZGVmaW5lJCgnQGFwcC1jb21wb25lbnQvZGV0ZWN0X2ZhY2VfbGFuZF9tYXJrJywgW10sIGZ1bmN0aW9uKCRhcHBfcmVxdWlyZSQsICRhcHBfZXhwb3J0cyQsICRhcHBfbW9kdWxlJCl7XG4gICAgICRhcHBfc2NyaXB0JCgkYXBwX21vZHVsZSQsICRhcHBfZXhwb3J0cyQsICRhcHBfcmVxdWlyZSQpXG4gICAgIGlmICgkYXBwX2V4cG9ydHMkLl9fZXNNb2R1bGUgJiYgJGFwcF9leHBvcnRzJC5kZWZhdWx0KSB7XG4gICAgICAgICAgICAkYXBwX21vZHVsZSQuZXhwb3J0cyA9ICRhcHBfZXhwb3J0cyQuZGVmYXVsdFxuICAgICAgICB9XG4gICAgICRhcHBfbW9kdWxlJC5leHBvcnRzLnRlbXBsYXRlID0gJGFwcF90ZW1wbGF0ZSRcbiAgICAgJGFwcF9tb2R1bGUkLmV4cG9ydHMuc3R5bGUgPSAkYXBwX3N0eWxlJFxufSlcblxuJGFwcF9ib290c3RyYXAkKCdAYXBwLWNvbXBvbmVudC9kZXRlY3RfZmFjZV9sYW5kX21hcmsnLHsgcGFja2FnZXJOYW1lOidmYS10b29sa2l0JywgcGFja2FnZXJWZXJzaW9uOiAnMS4wLjgtU3RhYmxlLjMwMCd9KVxuXG5cbi8vLy8vLy8vLy8vLy8vLy8vL1xuLy8gV0VCUEFDSyBGT09URVJcbi8vIC4vc3JjL0RldGVjdF9GYWNlX0xhbmRfTWFyay9kZXRlY3RfZmFjZV9sYW5kX21hcmsudXhcbi8vIG1vZHVsZSBpZCA9IDdcbi8vIG1vZHVsZSBjaHVua3MgPSAwIiwibW9kdWxlLmV4cG9ydHMgPSB7XG4gIFwidHlwZVwiOiBcImRpdlwiLFxuICBcImF0dHJcIjoge30sXG4gIFwiY2xhc3NMaXN0XCI6IFtcbiAgICBcImNvbnRhaW5lclwiXG4gIF0sXG4gIFwiY2hpbGRyZW5cIjogW1xuICAgIHtcbiAgICAgIFwidHlwZVwiOiBcInN0YWNrXCIsXG4gICAgICBcImF0dHJcIjoge30sXG4gICAgICBcImNsYXNzTGlzdFwiOiBbXG4gICAgICAgIFwidG9wLWNvbnRhaW5lclwiXG4gICAgICBdLFxuICAgICAgXCJjaGlsZHJlblwiOiBbXG4gICAgICAgIHtcbiAgICAgICAgICBcInR5cGVcIjogXCJpbWFnZVwiLFxuICAgICAgICAgIFwiYXR0clwiOiB7XG4gICAgICAgICAgICBcInNyY1wiOiBmdW5jdGlvbiAoKSB7cmV0dXJuIHRoaXMuaW1hZ2VVcml9XG4gICAgICAgICAgfSxcbiAgICAgICAgICBcImNsYXNzTGlzdFwiOiBbXG4gICAgICAgICAgICBcImZhY2UtaW1hZ2VcIlxuICAgICAgICAgIF1cbiAgICAgICAgfSxcbiAgICAgICAge1xuICAgICAgICAgIFwidHlwZVwiOiBcImNhbnZhc1wiLFxuICAgICAgICAgIFwiYXR0clwiOiB7XG4gICAgICAgICAgICBcImlkXCI6IFwiY2FudmFzXCJcbiAgICAgICAgICB9LFxuICAgICAgICAgIFwiaWRcIjogXCJjYW52YXNcIixcbiAgICAgICAgICBcInN0eWxlXCI6IHtcbiAgICAgICAgICAgIFwiZmxleFwiOiAxLFxuICAgICAgICAgICAgXCJ3aWR0aFwiOiBmdW5jdGlvbiAoKSB7cmV0dXJuICh0aGlzLmNhbnZhc1dpZHRoKSArICdweCd9LFxuICAgICAgICAgICAgXCJoZWlnaHRcIjogZnVuY3Rpb24gKCkge3JldHVybiAodGhpcy5jYW52YXNIZWlnaHQpICsgJ3B4J31cbiAgICAgICAgICB9LFxuICAgICAgICAgIFwiZXZlbnRzXCI6IHtcbiAgICAgICAgICAgIFwiY2xpY2tcIjogXCJzZWxlY3RcIlxuICAgICAgICAgIH1cbiAgICAgICAgfVxuICAgICAgXVxuICAgIH0sXG4gICAge1xuICAgICAgXCJ0eXBlXCI6IFwiZGl2XCIsXG4gICAgICBcImF0dHJcIjoge30sXG4gICAgICBcImNsYXNzTGlzdFwiOiBbXG4gICAgICAgIFwiYm90dG9tLWNvbnRhaW5lclwiXG4gICAgICBdLFxuICAgICAgXCJjaGlsZHJlblwiOiBbXG4gICAgICAgIHtcbiAgICAgICAgICBcInR5cGVcIjogXCJ0YWJzXCIsXG4gICAgICAgICAgXCJhdHRyXCI6IHt9LFxuICAgICAgICAgIFwiY2hpbGRyZW5cIjogW1xuICAgICAgICAgICAge1xuICAgICAgICAgICAgICBcInR5cGVcIjogXCJ0YWItY29udGVudFwiLFxuICAgICAgICAgICAgICBcImF0dHJcIjoge30sXG4gICAgICAgICAgICAgIFwiY2xhc3NMaXN0XCI6IFtcbiAgICAgICAgICAgICAgICBcInJlc3VsdC1ncm91cC1jb250YWluZXJcIlxuICAgICAgICAgICAgICBdLFxuICAgICAgICAgICAgICBcImNoaWxkcmVuXCI6IFtcbiAgICAgICAgICAgICAgICB7XG4gICAgICAgICAgICAgICAgICBcInR5cGVcIjogXCJkaXZcIixcbiAgICAgICAgICAgICAgICAgIFwiYXR0clwiOiB7fSxcbiAgICAgICAgICAgICAgICAgIFwiY2xhc3NMaXN0XCI6IFtcbiAgICAgICAgICAgICAgICAgICAgXCJyZXN1bHQtY29udGVudFwiXG4gICAgICAgICAgICAgICAgICBdLFxuICAgICAgICAgICAgICAgICAgXCJjaGlsZHJlblwiOiBbXG4gICAgICAgICAgICAgICAgICAgIHtcbiAgICAgICAgICAgICAgICAgICAgICBcInR5cGVcIjogXCJkaXZcIixcbiAgICAgICAgICAgICAgICAgICAgICBcImF0dHJcIjoge30sXG4gICAgICAgICAgICAgICAgICAgICAgXCJjbGFzc0xpc3RcIjogW1xuICAgICAgICAgICAgICAgICAgICAgICAgXCJyZXN1bHQtaXRlbS1jb250ZW50XCJcbiAgICAgICAgICAgICAgICAgICAgICBdLFxuICAgICAgICAgICAgICAgICAgICAgIFwiY2hpbGRyZW5cIjogW1xuICAgICAgICAgICAgICAgICAgICAgICAge1xuICAgICAgICAgICAgICAgICAgICAgICAgICBcInR5cGVcIjogXCJsaXN0XCIsXG4gICAgICAgICAgICAgICAgICAgICAgICAgIFwiYXR0clwiOiB7XG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgXCJpZFwiOiBcImxpc3RcIlxuICAgICAgICAgICAgICAgICAgICAgICAgICB9LFxuICAgICAgICAgICAgICAgICAgICAgICAgICBcImNsYXNzTGlzdFwiOiBbXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgXCJyZXN1bHQtbGlzdFwiXG4gICAgICAgICAgICAgICAgICAgICAgICAgIF0sXG4gICAgICAgICAgICAgICAgICAgICAgICAgIFwiaWRcIjogXCJsaXN0XCIsXG4gICAgICAgICAgICAgICAgICAgICAgICAgIFwiY2hpbGRyZW5cIjogW1xuICAgICAgICAgICAgICAgICAgICAgICAgICAgIHtcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFwidHlwZVwiOiBcImJsb2NrXCIsXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcImF0dHJcIjoge30sXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcInJlcGVhdFwiOiBmdW5jdGlvbiAoKSB7cmV0dXJuIHRoaXMucmVzdWx0X2xpc3R9LFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXCJjaGlsZHJlblwiOiBbXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHtcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcInR5cGVcIjogXCJsaXN0LWl0ZW1cIixcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcImF0dHJcIjoge1xuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXCJ0eXBlXCI6IFwicmVzdWx0XCJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB9LFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFwiY2xhc3NMaXN0XCI6IFtcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFwiaXRlbXNcIlxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIF0sXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXCJjaGlsZHJlblwiOiBbXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB7XG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFwidHlwZVwiOiBcImRpdlwiLFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcImF0dHJcIjoge30sXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFwiY2xhc3NMaXN0XCI6IFtcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcIml0ZW0tbGlcIlxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBdLFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcImNoaWxkcmVuXCI6IFtcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB7XG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcInR5cGVcIjogXCJ0ZXh0XCIsXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcImF0dHJcIjoge1xuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcInZhbHVlXCI6IGZ1bmN0aW9uICgpIHtyZXR1cm4gdGhpcy4kaXRlbX1cbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIH0sXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcImNsYXNzTGlzdFwiOiBbXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFwiaXRlbS1saS1kZXRhaWxcIlxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXVxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIH1cbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXVxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgfVxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIF1cbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgfVxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXVxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIH1cbiAgICAgICAgICAgICAgICAgICAgICAgICAgXVxuICAgICAgICAgICAgICAgICAgICAgICAgfVxuICAgICAgICAgICAgICAgICAgICAgIF1cbiAgICAgICAgICAgICAgICAgICAgfVxuICAgICAgICAgICAgICAgICAgXVxuICAgICAgICAgICAgICAgIH1cbiAgICAgICAgICAgICAgXVxuICAgICAgICAgICAgfVxuICAgICAgICAgIF1cbiAgICAgICAgfVxuICAgICAgXVxuICAgIH0sXG4gICAge1xuICAgICAgXCJ0eXBlXCI6IFwiZGl2XCIsXG4gICAgICBcImF0dHJcIjoge30sXG4gICAgICBcImNsYXNzTGlzdFwiOiBbXG4gICAgICAgIFwic2VsZWN0LWNvbnRhaW5lclwiXG4gICAgICBdLFxuICAgICAgXCJjaGlsZHJlblwiOiBbXG4gICAgICAgIHtcbiAgICAgICAgICBcInR5cGVcIjogXCJpbnB1dFwiLFxuICAgICAgICAgIFwiYXR0clwiOiB7XG4gICAgICAgICAgICBcInR5cGVcIjogXCJidXR0b25cIixcbiAgICAgICAgICAgIFwidmFsdWVcIjogXCLkuIrkvKDlm77niYdcIlxuICAgICAgICAgIH0sXG4gICAgICAgICAgXCJjbGFzc0xpc3RcIjogW1xuICAgICAgICAgICAgXCJzZWxlY3QtYnRuXCJcbiAgICAgICAgICBdLFxuICAgICAgICAgIFwiZXZlbnRzXCI6IHtcbiAgICAgICAgICAgIFwiY2xpY2tcIjogXCJzZWxlY3RcIlxuICAgICAgICAgIH1cbiAgICAgICAgfVxuICAgICAgXVxuICAgIH0sXG4gICAge1xuICAgICAgXCJ0eXBlXCI6IFwiZGl2XCIsXG4gICAgICBcImF0dHJcIjoge30sXG4gICAgICBcImNsYXNzTGlzdFwiOiBmdW5jdGlvbiAoKSB7cmV0dXJuIFsnbWFyaycsIHRoaXMuaXNTaG93XX0sXG4gICAgICBcImV2ZW50c1wiOiB7XG4gICAgICAgIFwiY2xpY2tcIjogXCJjYW5jZWxcIlxuICAgICAgfSxcbiAgICAgIFwiY2hpbGRyZW5cIjogW1xuICAgICAgICB7XG4gICAgICAgICAgXCJ0eXBlXCI6IFwiZGl2XCIsXG4gICAgICAgICAgXCJhdHRyXCI6IHt9LFxuICAgICAgICAgIFwiY2xhc3NMaXN0XCI6IGZ1bmN0aW9uICgpIHtyZXR1cm4gWydwb3B1cC1jb250YWluZXInLCB0aGlzLmlzU2hvd119LFxuICAgICAgICAgIFwiY2hpbGRyZW5cIjogW1xuICAgICAgICAgICAge1xuICAgICAgICAgICAgICBcInR5cGVcIjogXCJ0ZXh0XCIsXG4gICAgICAgICAgICAgIFwiYXR0clwiOiB7XG4gICAgICAgICAgICAgICAgXCJ2YWx1ZVwiOiBcIuaLjeeFp1wiXG4gICAgICAgICAgICAgIH0sXG4gICAgICAgICAgICAgIFwiY2xhc3NMaXN0XCI6IFtcbiAgICAgICAgICAgICAgICBcInBvcHVwLXRleHRcIlxuICAgICAgICAgICAgICBdLFxuICAgICAgICAgICAgICBcImV2ZW50c1wiOiB7XG4gICAgICAgICAgICAgICAgXCJjbGlja1wiOiBmdW5jdGlvbiAoZXZ0KSB7dGhpcy5zZWxlY3RNZWRpYSgn5ouN54WnJyxldnQpfVxuICAgICAgICAgICAgICB9XG4gICAgICAgICAgICB9LFxuICAgICAgICAgICAge1xuICAgICAgICAgICAgICBcInR5cGVcIjogXCJkaXZcIixcbiAgICAgICAgICAgICAgXCJhdHRyXCI6IHt9LFxuICAgICAgICAgICAgICBcImNsYXNzTGlzdFwiOiBbXG4gICAgICAgICAgICAgICAgXCJwb3B1cC1saW5lMVwiXG4gICAgICAgICAgICAgIF1cbiAgICAgICAgICAgIH0sXG4gICAgICAgICAgICB7XG4gICAgICAgICAgICAgIFwidHlwZVwiOiBcInRleHRcIixcbiAgICAgICAgICAgICAgXCJhdHRyXCI6IHtcbiAgICAgICAgICAgICAgICBcInZhbHVlXCI6IFwi5LuO55u45YaM5Lit6YCJ5oupXCJcbiAgICAgICAgICAgICAgfSxcbiAgICAgICAgICAgICAgXCJjbGFzc0xpc3RcIjogW1xuICAgICAgICAgICAgICAgIFwicG9wdXAtdGV4dFwiXG4gICAgICAgICAgICAgIF0sXG4gICAgICAgICAgICAgIFwiZXZlbnRzXCI6IHtcbiAgICAgICAgICAgICAgICBcImNsaWNrXCI6IGZ1bmN0aW9uIChldnQpIHt0aGlzLnNlbGVjdE1lZGlhKCfku47nm7jlhozkuK3pgInmi6knLGV2dCl9XG4gICAgICAgICAgICAgIH1cbiAgICAgICAgICAgIH0sXG4gICAgICAgICAgICB7XG4gICAgICAgICAgICAgIFwidHlwZVwiOiBcImRpdlwiLFxuICAgICAgICAgICAgICBcImF0dHJcIjoge30sXG4gICAgICAgICAgICAgIFwiY2xhc3NMaXN0XCI6IFtcbiAgICAgICAgICAgICAgICBcInBvcHVwLWxpbmUyXCJcbiAgICAgICAgICAgICAgXVxuICAgICAgICAgICAgfSxcbiAgICAgICAgICAgIHtcbiAgICAgICAgICAgICAgXCJ0eXBlXCI6IFwidGV4dFwiLFxuICAgICAgICAgICAgICBcImF0dHJcIjoge1xuICAgICAgICAgICAgICAgIFwidmFsdWVcIjogXCLlj5bmtohcIlxuICAgICAgICAgICAgICB9LFxuICAgICAgICAgICAgICBcImNsYXNzTGlzdFwiOiBbXG4gICAgICAgICAgICAgICAgXCJwb3B1cC10ZXh0XCJcbiAgICAgICAgICAgICAgXSxcbiAgICAgICAgICAgICAgXCJldmVudHNcIjoge1xuICAgICAgICAgICAgICAgIFwiY2xpY2tcIjogZnVuY3Rpb24gKGV2dCkge3RoaXMuc2VsZWN0TWVkaWEoJ+WPlua2iCcsZXZ0KX1cbiAgICAgICAgICAgICAgfVxuICAgICAgICAgICAgfVxuICAgICAgICAgIF1cbiAgICAgICAgfVxuICAgICAgXVxuICAgIH1cbiAgXVxufVxuXG5cbi8vLy8vLy8vLy8vLy8vLy8vL1xuLy8gV0VCUEFDSyBGT09URVJcbi8vIGQ6L3Rvb2xzL0h1YXdlaSBGYXN0QXBwIElERS9yZXNvdXJjZXMvYXBwL2V4dGVuc2lvbnMvZGV2ZWNvLWRlYnVnL25vZGVfbW9kdWxlcy9mYS10b29sa2l0L2xpYi9mYS1qc29uLWxvYWRlci5qcyFkOi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvZmEtdG9vbGtpdC9saWIvZmEtdGVtcGxhdGUtbG9hZGVyLmpzIWQ6L3Rvb2xzL0h1YXdlaSBGYXN0QXBwIElERS9yZXNvdXJjZXMvYXBwL2V4dGVuc2lvbnMvZGV2ZWNvLWRlYnVnL25vZGVfbW9kdWxlcy9mYS10b29sa2l0L2xpYi9mYS1mcmFnbWVudC1sb2FkZXIuanM/aW5kZXg9MCZ0eXBlPXRlbXBsYXRlcyEuL3NyYy9EZXRlY3RfRmFjZV9MYW5kX01hcmsvZGV0ZWN0X2ZhY2VfbGFuZF9tYXJrLnV4XG4vLyBtb2R1bGUgaWQgPSA4XG4vLyBtb2R1bGUgY2h1bmtzID0gMCIsIm1vZHVsZS5leHBvcnRzID0ge1xuICBcIi5jb250YWluZXJcIjoge1xuICAgIFwiZmxleFwiOiAxLFxuICAgIFwiZmxleERpcmVjdGlvblwiOiBcImNvbHVtblwiLFxuICAgIFwiYmFja2dyb3VuZENvbG9yXCI6IFwiI2YwZWNlY1wiXG4gIH0sXG4gIFwiLnRvcC1jb250YWluZXJcIjoge1xuICAgIFwiZmxleERpcmVjdGlvblwiOiBcImNvbHVtblwiLFxuICAgIFwid2lkdGhcIjogXCIxMDAlXCIsXG4gICAgXCJoZWlnaHRcIjogXCI0NSVcIixcbiAgICBcImFsaWduSXRlbXNcIjogXCJjZW50ZXJcIlxuICB9LFxuICBcIi5mYWNlLWltYWdlXCI6IHtcbiAgICBcImZsZXhcIjogMVxuICB9LFxuICBcIi5ib3R0b20tY29udGFpbmVyXCI6IHtcbiAgICBcIndpZHRoXCI6IFwiMTAwJVwiLFxuICAgIFwiaGVpZ2h0XCI6IFwiNTUlXCIsXG4gICAgXCJiYWNrZ3JvdW5kQ29sb3JcIjogXCIjZmZmZmZmXCIsXG4gICAgXCJmbGV4RGlyZWN0aW9uXCI6IFwiY29sdW1uXCJcbiAgfSxcbiAgXCIucmVzdWx0LWdyb3VwLWNvbnRhaW5lclwiOiB7XG4gICAgXCJmbGV4XCI6IDEsXG4gICAgXCJmbGV4RGlyZWN0aW9uXCI6IFwiY29sdW1uXCIsXG4gICAgXCJiYWNrZ3JvdW5kQ29sb3JcIjogXCIjZmZmZmZmXCJcbiAgfSxcbiAgXCIucmVzdWx0LWNvbnRlbnRcIjoge1xuICAgIFwiZmxleERpcmVjdGlvblwiOiBcImNvbHVtblwiLFxuICAgIFwiZmxleFwiOiAxXG4gIH0sXG4gIFwiLnJlc3VsdC10aXRsZS1kZXRhaWxcIjoge1xuICAgIFwicGFkZGluZ0xlZnRcIjogXCIzMHB4XCIsXG4gICAgXCJwYWRkaW5nQm90dG9tXCI6IFwiMjBweFwiLFxuICAgIFwicGFkZGluZ1RvcFwiOiBcIjIwcHhcIixcbiAgICBcImhlaWdodFwiOiBcIjkwcHhcIixcbiAgICBcImZvbnRTaXplXCI6IFwiNDBweFwiLFxuICAgIFwiZm9udEZhbWlseVwiOiBcIlRpbWVzIE5ldyBSb21hbiwgVGltZXMsIHNlcmlmXCJcbiAgfSxcbiAgXCIucmVzdWx0LWxpbmVcIjoge1xuICAgIFwibWFyZ2luVG9wXCI6IFwiNXB4XCIsXG4gICAgXCJ3aWR0aFwiOiBcIjEwMCVcIixcbiAgICBcImhlaWdodFwiOiBcIjFweFwiLFxuICAgIFwiYmFja2dyb3VuZENvbG9yXCI6IFwiI2YwZWNlY1wiXG4gIH0sXG4gIFwiLnJlc3VsdC1pdGVtLWNvbnRlbnRcIjoge1xuICAgIFwiZmxleERpcmVjdGlvblwiOiBcImNvbHVtblwiLFxuICAgIFwiZmxleFwiOiAxLFxuICAgIFwicGFkZGluZ1RvcFwiOiBcIjMwcHhcIixcbiAgICBcInBhZGRpbmdSaWdodFwiOiBcIjMwcHhcIixcbiAgICBcInBhZGRpbmdCb3R0b21cIjogXCIzMHB4XCIsXG4gICAgXCJwYWRkaW5nTGVmdFwiOiBcIjMwcHhcIixcbiAgICBcImRpc3BsYXlcIjogXCJmbGV4XCJcbiAgfSxcbiAgXCIucmVzdWx0LWxpc3RcIjoge1xuICAgIFwid2lkdGhcIjogXCIxMDAlXCIsXG4gICAgXCJoZWlnaHRcIjogXCI0MDBweFwiXG4gIH0sXG4gIFwiLml0ZW1zXCI6IHtcbiAgICBcIndpZHRoXCI6IFwiMTAwJVwiLFxuICAgIFwiaGVpZ2h0XCI6IFwiODBweFwiXG4gIH0sXG4gIFwiLml0ZW0tbGlcIjoge1xuICAgIFwiYWxpZ25JdGVtc1wiOiBcImNlbnRlclwiXG4gIH0sXG4gIFwiLml0ZW0tbGktZGV0YWlsXCI6IHtcbiAgICBcImZvbnRTaXplXCI6IFwiMzBweFwiLFxuICAgIFwiY29sb3JcIjogXCIjMDAwMDAwXCIsXG4gICAgXCJmbGV4XCI6IDFcbiAgfSxcbiAgXCIuc2VsZWN0LWNvbnRhaW5lclwiOiB7XG4gICAgXCJwb3NpdGlvblwiOiBcImZpeGVkXCIsXG4gICAgXCJ3aWR0aFwiOiBcIjEwMCVcIixcbiAgICBcImhlaWdodFwiOiBcIjEwMHB4XCIsXG4gICAgXCJmbGV4RGlyZWN0aW9uXCI6IFwiY29sdW1uXCIsXG4gICAgXCJhbGlnbkl0ZW1zXCI6IFwiY2VudGVyXCIsXG4gICAgXCJib3R0b21cIjogXCI0MHB4XCJcbiAgfSxcbiAgXCIuc2VsZWN0LWJ0blwiOiB7XG4gICAgXCJ3aWR0aFwiOiBcIjgwJVwiLFxuICAgIFwiaGVpZ2h0XCI6IFwiMTAwJVwiLFxuICAgIFwiYmFja2dyb3VuZENvbG9yXCI6IFwiIzE0NzhmYVwiLFxuICAgIFwidGV4dEFsaWduXCI6IFwiY2VudGVyXCIsXG4gICAgXCJjb2xvclwiOiBcIiNmZmZmZmZcIixcbiAgICBcImZvbnRTaXplXCI6IFwiNDBweFwiLFxuICAgIFwiYm9yZGVyUmFkaXVzXCI6IFwiMTBweFwiXG4gIH0sXG4gIFwiLm1hcmtcIjoge1xuICAgIFwicG9zaXRpb25cIjogXCJmaXhlZFwiLFxuICAgIFwiZGlzcGxheVwiOiBcIm5vbmVcIixcbiAgICBcImxlZnRcIjogXCIwcHhcIixcbiAgICBcInRvcFwiOiBcIjBweFwiLFxuICAgIFwid2lkdGhcIjogXCIxMDAlXCIsXG4gICAgXCJoZWlnaHRcIjogXCIxMDAlXCIsXG4gICAgXCJiYWNrZ3JvdW5kQ29sb3JcIjogXCIjZDNkM2QzXCIsXG4gICAgXCJvcGFjaXR5XCI6IDAuNVxuICB9LFxuICBcIi5wb3B1cC1jb250YWluZXJcIjoge1xuICAgIFwid2lkdGhcIjogXCIxMDAlXCIsXG4gICAgXCJoZWlnaHRcIjogXCIzMTVweFwiLFxuICAgIFwiZGlzcGxheVwiOiBcIm5vbmVcIixcbiAgICBcImJhY2tncm91bmRDb2xvclwiOiBcIiNmZmZmZmZcIixcbiAgICBcImZsZXhEaXJlY3Rpb25cIjogXCJjb2x1bW5cIixcbiAgICBcInBvc2l0aW9uXCI6IFwiZml4ZWRcIixcbiAgICBcImJvdHRvbVwiOiBcIjBweFwiLFxuICAgIFwiYm9yZGVyVG9wV2lkdGhcIjogXCIxcHhcIixcbiAgICBcImJvcmRlclJpZ2h0V2lkdGhcIjogXCIxcHhcIixcbiAgICBcImJvcmRlckJvdHRvbVdpZHRoXCI6IFwiMXB4XCIsXG4gICAgXCJib3JkZXJMZWZ0V2lkdGhcIjogXCIxcHhcIixcbiAgICBcImJvcmRlclN0eWxlXCI6IFwic29saWRcIixcbiAgICBcImJvcmRlclRvcENvbG9yXCI6IFwiI2YwZWNlY1wiLFxuICAgIFwiYm9yZGVyUmlnaHRDb2xvclwiOiBcIiNmMGVjZWNcIixcbiAgICBcImJvcmRlckJvdHRvbUNvbG9yXCI6IFwiI2YwZWNlY1wiLFxuICAgIFwiYm9yZGVyTGVmdENvbG9yXCI6IFwiI2YwZWNlY1wiXG4gIH0sXG4gIFwiLnNob3dcIjoge1xuICAgIFwiZGlzcGxheVwiOiBcImZsZXhcIlxuICB9LFxuICBcIi5wb3B1cC1saW5lMVwiOiB7XG4gICAgXCJ3aWR0aFwiOiBcIjEwMCVcIixcbiAgICBcImhlaWdodFwiOiBcIjFweFwiLFxuICAgIFwiYmFja2dyb3VuZENvbG9yXCI6IFwiI2YwZWNlY1wiXG4gIH0sXG4gIFwiLnBvcHVwLXRleHRcIjoge1xuICAgIFwid2lkdGhcIjogXCIxMDAlXCIsXG4gICAgXCJoZWlnaHRcIjogXCIxMDBweFwiLFxuICAgIFwidGV4dEFsaWduXCI6IFwiY2VudGVyXCIsXG4gICAgXCJmb250U2l6ZVwiOiBcIjMwcHhcIixcbiAgICBcImNvbG9yXCI6IFwiIzAwMDAwMFwiXG4gIH0sXG4gIFwiLnBvcHVwLWxpbmUyXCI6IHtcbiAgICBcIndpZHRoXCI6IFwiMTAwJVwiLFxuICAgIFwiaGVpZ2h0XCI6IFwiMTBweFwiLFxuICAgIFwiYmFja2dyb3VuZENvbG9yXCI6IFwiI2YwZWNlY1wiXG4gIH1cbn1cblxuXG4vLy8vLy8vLy8vLy8vLy8vLy9cbi8vIFdFQlBBQ0sgRk9PVEVSXG4vLyBkOi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvZmEtdG9vbGtpdC9saWIvZmEtanNvbi1sb2FkZXIuanMhZDovdG9vbHMvSHVhd2VpIEZhc3RBcHAgSURFL3Jlc291cmNlcy9hcHAvZXh0ZW5zaW9ucy9kZXZlY28tZGVidWcvbm9kZV9tb2R1bGVzL2ZhLXRvb2xraXQvbGliL2ZhLXN0eWxlLWxvYWRlci5qcz9pbmRleD0wJnR5cGU9c3R5bGVzJnJlc291cmNlUGF0aD1kOi9TZXJ2aWNlIHZlcmlmaWNhdGlvbi9Db2RlL2Zhc3RBcHAvbGFuZG1hcmtBbmRkZXRlY3RlL2NvbS5odWF3ZWkubGFuZG1hcmthbmRkZXRlY3Qvc3JjL0RldGVjdF9GYWNlX0xhbmRfTWFyay9kZXRlY3RfZmFjZV9sYW5kX21hcmsudXghZDovdG9vbHMvSHVhd2VpIEZhc3RBcHAgSURFL3Jlc291cmNlcy9hcHAvZXh0ZW5zaW9ucy9kZXZlY28tZGVidWcvbm9kZV9tb2R1bGVzL2ZhLXRvb2xraXQvbGliL2ZhLWZyYWdtZW50LWxvYWRlci5qcz9pbmRleD0wJnR5cGU9c3R5bGVzJnJlc291cmNlUGF0aD1kOi9TZXJ2aWNlIHZlcmlmaWNhdGlvbi9Db2RlL2Zhc3RBcHAvbGFuZG1hcmtBbmRkZXRlY3RlL2NvbS5odWF3ZWkubGFuZG1hcmthbmRkZXRlY3Qvc3JjL0RldGVjdF9GYWNlX0xhbmRfTWFyay9kZXRlY3RfZmFjZV9sYW5kX21hcmsudXghLi9zcmMvRGV0ZWN0X0ZhY2VfTGFuZF9NYXJrL2RldGVjdF9mYWNlX2xhbmRfbWFyay51eFxuLy8gbW9kdWxlIGlkID0gOVxuLy8gbW9kdWxlIGNodW5rcyA9IDAiLCJtb2R1bGUuZXhwb3J0cyA9IGZ1bmN0aW9uKG1vZHVsZSwgZXhwb3J0cywgJGFwcF9yZXF1aXJlJCl7J3VzZSBzdHJpY3QnO1xuXG52YXIgX3N5c3RlbSA9ICRhcHBfcmVxdWlyZSQoJ0BhcHAtbW9kdWxlL3N5c3RlbS5haScpO1xuXG52YXIgX3N5c3RlbTIgPSBfaW50ZXJvcFJlcXVpcmVEZWZhdWx0KF9zeXN0ZW0pO1xuXG52YXIgX3N5c3RlbTMgPSAkYXBwX3JlcXVpcmUkKCdAYXBwLW1vZHVsZS9zeXN0ZW0ubWVkaWEnKTtcblxudmFyIF9zeXN0ZW00ID0gX2ludGVyb3BSZXF1aXJlRGVmYXVsdChfc3lzdGVtMyk7XG5cbnZhciBfc3lzdGVtNSA9ICRhcHBfcmVxdWlyZSQoJ0BhcHAtbW9kdWxlL3N5c3RlbS5pbWFnZScpO1xuXG52YXIgX3N5c3RlbTYgPSBfaW50ZXJvcFJlcXVpcmVEZWZhdWx0KF9zeXN0ZW01KTtcblxudmFyIF9zeXN0ZW03ID0gJGFwcF9yZXF1aXJlJCgnQGFwcC1tb2R1bGUvc3lzdGVtLmRldmljZScpO1xuXG52YXIgX3N5c3RlbTggPSBfaW50ZXJvcFJlcXVpcmVEZWZhdWx0KF9zeXN0ZW03KTtcblxudmFyIF9kYXRhID0gcmVxdWlyZSgnLi4vQ29tbW9uL2RhdGEnKTtcblxuZnVuY3Rpb24gX2ludGVyb3BSZXF1aXJlRGVmYXVsdChvYmopIHsgcmV0dXJuIG9iaiAmJiBvYmouX19lc01vZHVsZSA/IG9iaiA6IHsgZGVmYXVsdDogb2JqIH07IH1cblxubW9kdWxlLmV4cG9ydHMgPSB7XG4gIGRhdGE6IHtcbiAgICBsaXN0OiBbXCLmi43nhadcIiwgXCLku47nm7jlhozkuK3pgInmi6lcIiwgXCLlj5bmtohcIl0sXG4gICAgaW1hZ2VVcmk6IFwiL0NvbW1vbi9pbWcvaW5pdGlhbF9waWN0dXJlX29uZS5wbmdcIixcbiAgICByZXN1bHRfbGlzdDogW10sXG4gICAgaXNTaG93OiAnJyxcbiAgICBwb2ludEFycmF5OiBbXSxcbiAgICBsZXZlbDogJycsXG4gICAgY2FudmFzV2lkdGg6ICcnLFxuICAgIGNhbnZhc0hlaWdodDogJycsXG4gICAgcmF0aW86IDEsXG4gICAgaXNGaXJzdDogdHJ1ZSxcbiAgICBzY3JlZW5XaWR0aDogJycsXG4gICAgc2NyZWVuSGVpZ2h0OiAnJ1xuICB9LFxuXG4gIG9uSW5pdDogZnVuY3Rpb24gb25Jbml0KCkge1xuICAgIHRoaXMuJHBhZ2Uuc2V0VGl0bGVCYXIoeyB0ZXh0OiAn5LqU5a6Y54m55b6B5qOA5rWLJyB9KTtcbiAgICB2YXIgdGhhdCA9IHRoaXM7XG5cbiAgICBfc3lzdGVtOC5kZWZhdWx0LmdldEluZm8oe1xuICAgICAgc3VjY2VzczogZnVuY3Rpb24gc3VjY2VzcyhyZXQpIHtcbiAgICAgICAgY29uc29sZS5sb2coJ2hhbmRsaW5nIHN1Y2Nlc3MnKTtcbiAgICAgICAgdGhhdC5zY3JlZW5XaWR0aCA9IHJldC5zY3JlZW5XaWR0aDtcbiAgICAgICAgdGhhdC5zY3JlZW5IZWlnaHQgPSByZXQuc2NyZWVuSGVpZ2h0O1xuICAgICAgICBjb25zb2xlLmxvZyhcIuWxj+W5leWuveW6pu+8mlwiICsgcmV0LnNjcmVlbldpZHRoLCBcIuWxj+W5lemrmOW6pjogXCIgKyByZXQuc2NyZWVuSGVpZ2h0KTtcbiAgICAgICAgdGhhdC5zZWxlY3RPbmVJbWFnZSgpO1xuICAgICAgfVxuICAgIH0pO1xuICB9LFxuXG4gIHNlbGVjdDogZnVuY3Rpb24gc2VsZWN0KCkge1xuICAgIHZhciB0aGF0ID0gdGhpcztcbiAgICB0aGF0LmlzU2hvdyA9ICdzaG93JztcbiAgfSxcblxuICBjYW5jZWw6IGZ1bmN0aW9uIGNhbmNlbCgpIHtcbiAgICB2YXIgdGhhdCA9IHRoaXM7XG4gICAgdGhhdC5pc1Nob3cgPSAnJztcbiAgfSxcblxuICBzZWxlY3RNZWRpYTogZnVuY3Rpb24gc2VsZWN0TWVkaWEoZSkge1xuICAgIHZhciB0aGF0ID0gdGhpcztcbiAgICBpZiAoZSA9PT0gXCLmi43nhadcIikge1xuICAgICAgdGhhdC50YWtlUGhvdG8oKTtcbiAgICB9IGVsc2UgaWYgKGUgPT09IFwi5LuO55u45YaM5Lit6YCJ5oupXCIpIHtcbiAgICAgIHRoYXQuc2VsZWN0T25lSW1hZ2UoKTtcbiAgICB9IGVsc2Uge1xuICAgICAgdGhhdC5jYW5jZWwoKTtcbiAgICB9XG4gIH0sXG5cbiAgdGFrZVBob3RvOiBmdW5jdGlvbiB0YWtlUGhvdG8oKSB7XG4gICAgdmFyIHRoYXQgPSB0aGlzO1xuICAgIHRoYXQuaXNTaG93ID0gJyc7XG4gICAgdGhhdC50YWtlcGhvdG9idG4oKS50aGVuKGZ1bmN0aW9uIChkYXRhKSB7XG4gICAgICB0aGF0LmNhY3VsYXRlUmF0aW8oZGF0YSk7XG4gICAgfSk7XG4gIH0sXG5cbiAgc2VsZWN0T25lSW1hZ2U6IGZ1bmN0aW9uIHNlbGVjdE9uZUltYWdlKCkge1xuICAgIHZhciB0aGF0ID0gdGhpcztcbiAgICB0aGF0LmlzU2hvdyA9ICcnO1xuICAgIHRoYXQucGlja3Bob3RvYnRuKCkudGhlbihmdW5jdGlvbiAoZGF0YSkge1xuICAgICAgdGhhdC5jYWN1bGF0ZVJhdGlvKGRhdGEpO1xuICAgIH0pO1xuICB9LFxuXG4gIGNhY3VsYXRlUmF0aW86IGZ1bmN0aW9uIGNhY3VsYXRlUmF0aW8oZGF0YSkge1xuICAgIHZhciB0aGF0ID0gdGhpcztcbiAgICB2YXIgcmVhbF93aWR0aCA9IGRhdGEud2lkdGg7XG4gICAgdmFyIHJlYWxfaGVpZ2h0ID0gZGF0YS5oZWlnaHQ7XG4gICAgY29uc29sZS5sb2coXCJyZWFsX3dpZHRoXCIgKyBkYXRhLndpZHRoLCBcInJlYWxfaGVpZ2h0XCIgKyBkYXRhLmhlaWdodCk7XG5cbiAgICB2YXIgY2FjdWxhdGVIZWlnaHQgPSB0aGF0LnNjcmVlbkhlaWdodCAvIDIwNDAgKiA1ODA7XG4gICAgY29uc29sZS5sb2coXCJjYWN1bGF0ZUhlaWdodFwiICsgY2FjdWxhdGVIZWlnaHQpO1xuICAgIGlmIChkYXRhLmhlaWdodCA+IGRhdGEud2lkdGgpIHtcbiAgICAgIHZhciByYXRpbyA9IGRhdGEuaGVpZ2h0IC8gY2FjdWxhdGVIZWlnaHQ7XG4gICAgICBkYXRhLmhlaWdodCA9IGNhY3VsYXRlSGVpZ2h0O1xuICAgICAgZGF0YS53aWR0aCA9IE1hdGguY2VpbChkYXRhLndpZHRoIC8gcmF0aW8pICUgMiAhPSAwID8gTWF0aC5jZWlsKGRhdGEud2lkdGggLyByYXRpbykgLSAxIDogTWF0aC5jZWlsKGRhdGEud2lkdGggLyByYXRpbyk7XG4gICAgfSBlbHNlIGlmIChyZWFsX2hlaWdodCA8IHJlYWxfd2lkdGgpIHtcbiAgICAgIGRhdGEuaGVpZ2h0ID0gY2FjdWxhdGVIZWlnaHQ7XG4gICAgICB2YXIgcmF0aW8gPSByZWFsX2hlaWdodCAvIGRhdGEuaGVpZ2h0O1xuICAgICAgZGF0YS53aWR0aCA9IE1hdGguY2VpbChkYXRhLndpZHRoIC8gcmF0aW8pICUgMiAhPSAwID8gTWF0aC5jZWlsKGRhdGEud2lkdGggLyByYXRpbykgLSAxIDogTWF0aC5jZWlsKGRhdGEud2lkdGggLyByYXRpbyk7XG4gICAgfVxuXG4gICAgdGhhdC5yYXRpbyA9IChyZWFsX3dpZHRoIC8gZGF0YS53aWR0aCkudG9GaXhlZCgzKTtcbiAgICBjb25zb2xlLmxvZyhcIuWOi+e8qeWQjueahOWuvemrmO+8mlwiICsgZGF0YS53aWR0aCArIFwiLFwiICsgZGF0YS5oZWlnaHQpO1xuXG4gICAgdGhhdC5jYW52YXNXaWR0aCA9IGRhdGEud2lkdGg7XG4gICAgdGhhdC5jYW52YXNIZWlnaHQgPSBkYXRhLmhlaWdodDtcbiAgICBjb25zb2xlLmxvZyhcImNhbnZhc1dpZHRoOlwiICsgdGhhdC5jYW52YXNXaWR0aCwgXCJjYW52YXNIZWlnaHQ6XCIgKyB0aGF0LmNhbnZhc0hlaWdodCk7XG4gICAgY29uc29sZS5sb2coXCLljovnvKnmr5TkvovvvJpcIiArIChkYXRhLndpZHRoIC8gcmVhbF93aWR0aCkudG9GaXhlZCgzKSk7XG4gICAgY29uc29sZS5sb2coXCLplb/lrr3vvJpcIiArIChkYXRhLndpZHRoIC8gdGhhdC5yYXRpbykudG9GaXhlZCgxKSArIFwiLFwiICsgKGRhdGEuaGVpZ2h0IC8gdGhhdC5yYXRpbykudG9GaXhlZCgxKSk7XG5cbiAgICB2YXIgYXJncyA9IFtkYXRhLnVyaSwgNzBdO1xuICAgIGlmIChwYXJzZUZsb2F0KHRoYXQucmF0aW8pICE9IDEpIHtcbiAgICAgIGFyZ3MucHVzaCh0aGF0LnJhdGlvKTtcbiAgICB9XG4gICAgY29uc29sZS5sb2coXCJjb21wcmVzcyBhcmdzLS0tLS0tLVwiLCBhcmdzKTtcbiAgICB2YXIgcHJvbWlzZSA9IHRoYXQuY29tcHJlc3NJbWFnZWJ0bi5hcHBseSh7fSwgYXJncyk7XG4gICAgcHJvbWlzZS50aGVuKGZ1bmN0aW9uIChkYXRhKSB7XG4gICAgICB0aGF0LmltYWdlVXJpID0gZGF0YS51cmk7XG4gICAgICBjb25zb2xlLmxvZyhcInBpY2twaG90byBpbWFnZSB1cmw6XCIgKyB0aGF0LmltYWdlVXJpKTtcbiAgICAgIHRoYXQuZGV0ZWN0RmFjZUxhbmQoKTtcbiAgICB9KTtcbiAgfSxcblxuICBwaWNrcGhvdG9idG46IGZ1bmN0aW9uIHBpY2twaG90b2J0bigpIHtcbiAgICB2YXIgdGhhdCA9IHRoaXM7XG4gICAgcmV0dXJuIG5ldyBQcm9taXNlKGZ1bmN0aW9uIChyZXNvbHZlLCByZWplY3QpIHtcbiAgICAgIGlmICh0aGF0LmlzRmlyc3QgPT09IHRydWUpIHtcbiAgICAgICAgX3N5c3RlbTYuZGVmYXVsdC5nZXRJbWFnZUluZm8oe1xuICAgICAgICAgIHVyaTogdGhhdC5pbWFnZVVyaSxcbiAgICAgICAgICBzdWNjZXNzOiBmdW5jdGlvbiBzdWNjZXNzKGltYWdlSW5mbykge1xuICAgICAgICAgICAgY29uc29sZS5sb2coXCLljp/lp4st5ouN54WnLeWbvueJh+i3r+W+hO+8mlwiICsgaW1hZ2VJbmZvLnVyaSArIFwi5a695bqm77yaXCIgKyBpbWFnZUluZm8ud2lkdGggKyBcInB4LOmrmOW6pu+8mlwiICsgaW1hZ2VJbmZvLmhlaWdodCArIFwicHgs5bC65a+477yaXCIgKyAoaW1hZ2VJbmZvLnNpemUgLyAxMDI0KS50b0ZpeGVkKDIpICsgJ0tCJyk7XG5cbiAgICAgICAgICAgIHJlc29sdmUoaW1hZ2VJbmZvKTtcbiAgICAgICAgICB9LFxuICAgICAgICAgIGZhaWw6IGZ1bmN0aW9uIGZhaWwoaW1hZ2VJbmZvLCBjb2RlKSB7XG4gICAgICAgICAgICBjb25zb2xlLmxvZyhcImltYWdlSW5mbzogXCIgKyBpbWFnZUluZm8sIFwiY29kZTpcIiArIGNvZGUpO1xuICAgICAgICAgIH1cbiAgICAgICAgfSk7XG4gICAgICAgIHRoYXQuaXNGaXJzdCA9IGZhbHNlO1xuICAgICAgfSBlbHNlIHtcbiAgICAgICAgX3N5c3RlbTQuZGVmYXVsdC5waWNrSW1hZ2Uoe1xuICAgICAgICAgIHN1Y2Nlc3M6IGZ1bmN0aW9uIHN1Y2Nlc3MoZGF0YSkge1xuICAgICAgICAgICAgX3N5c3RlbTYuZGVmYXVsdC5nZXRJbWFnZUluZm8oe1xuICAgICAgICAgICAgICB1cmk6IGRhdGEudXJpLFxuICAgICAgICAgICAgICBzdWNjZXNzOiBmdW5jdGlvbiBzdWNjZXNzKGltYWdlSW5mbykge1xuICAgICAgICAgICAgICAgIGNvbnNvbGUubG9nKFwi5Y6f5aeLLeaLjeeFpy3lm77niYfot6/lvoTvvJpcIiArIGltYWdlSW5mby51cmkgKyBcIuWuveW6pu+8mlwiICsgaW1hZ2VJbmZvLndpZHRoICsgXCJweCzpq5jluqbvvJpcIiArIGltYWdlSW5mby5oZWlnaHQgKyBcInB4LOWwuuWvuO+8mlwiICsgKGltYWdlSW5mby5zaXplIC8gMTAyNCkudG9GaXhlZCgyKSArICdLQicpO1xuXG4gICAgICAgICAgICAgICAgcmVzb2x2ZShpbWFnZUluZm8pO1xuICAgICAgICAgICAgICB9LFxuICAgICAgICAgICAgICBmYWlsOiBmdW5jdGlvbiBmYWlsKGltYWdlSW5mbywgY29kZSkge1xuICAgICAgICAgICAgICAgIGNvbnNvbGUubG9nKFwiaW1hZ2VJbmZvOiBcIiArIGltYWdlSW5mbywgXCJjb2RlOlwiICsgY29kZSk7XG4gICAgICAgICAgICAgIH1cbiAgICAgICAgICAgIH0pO1xuICAgICAgICAgIH1cbiAgICAgICAgfSk7XG4gICAgICB9XG4gICAgfSk7XG4gIH0sXG5cbiAgdGFrZXBob3RvYnRuOiBmdW5jdGlvbiB0YWtlcGhvdG9idG4oKSB7XG4gICAgcmV0dXJuIG5ldyBQcm9taXNlKGZ1bmN0aW9uIChyZXNvbHZlLCByZWplY3QpIHtcbiAgICAgIF9zeXN0ZW00LmRlZmF1bHQudGFrZVBob3RvKHtcbiAgICAgICAgc3VjY2VzczogZnVuY3Rpb24gc3VjY2VzcyhkYXRhKSB7XG4gICAgICAgICAgY29uc29sZS5sb2coJ3Rha2VwaG90b+WujOaIkCcpO1xuICAgICAgICAgIF9zeXN0ZW02LmRlZmF1bHQuZ2V0SW1hZ2VJbmZvKHtcbiAgICAgICAgICAgIHVyaTogZGF0YS51cmksXG4gICAgICAgICAgICBzdWNjZXNzOiBmdW5jdGlvbiBzdWNjZXNzKGltYWdlSW5mbykge1xuICAgICAgICAgICAgICBjb25zb2xlLmxvZyhcIuWOn+Wniy3mi43nhact5Zu+54mH6Lev5b6E77yaXCIgKyBpbWFnZUluZm8udXJpICsgXCLlrr3luqbvvJpcIiArIGltYWdlSW5mby53aWR0aCArIFwicHgs6auY5bqm77yaXCIgKyBpbWFnZUluZm8uaGVpZ2h0ICsgXCJweCzlsLrlr7jvvJpcIiArIChpbWFnZUluZm8uc2l6ZSAvIDEwMjQpLnRvRml4ZWQoMikgKyAnS0InKTtcblxuICAgICAgICAgICAgICByZXNvbHZlKGltYWdlSW5mbyk7XG4gICAgICAgICAgICB9LFxuICAgICAgICAgICAgZmFpbDogZnVuY3Rpb24gZmFpbChpbWFnZUluZm8sIGNvZGUpIHtcbiAgICAgICAgICAgICAgY29uc29sZS5sb2coXCJpbWFnZUluZm86IFwiICsgaW1hZ2VJbmZvLCBcImNvZGU6IFwiICsgY29kZSk7XG4gICAgICAgICAgICB9XG4gICAgICAgICAgfSk7XG4gICAgICAgIH1cbiAgICAgIH0pO1xuICAgIH0pO1xuICB9LFxuXG4gIGNvbXByZXNzSW1hZ2VidG46IGZ1bmN0aW9uIGNvbXByZXNzSW1hZ2VidG4odXJpLCBxdWFsaXR5LCByYXRpbykge1xuICAgIHJldHVybiBuZXcgUHJvbWlzZShmdW5jdGlvbiAocmVzb2x2ZSwgcmVqZWN0KSB7XG4gICAgICBfc3lzdGVtNi5kZWZhdWx0LmNvbXByZXNzSW1hZ2Uoe1xuICAgICAgICB1cmk6IHVyaSxcbiAgICAgICAgcXVhbGl0eTogcXVhbGl0eSxcbiAgICAgICAgcmF0aW86IHJhdGlvID8gcmF0aW8gOiB1bmRlZmluZWQsXG4gICAgICAgIGZvcm1hdDogXCJKUEVHXCIsXG4gICAgICAgIHN1Y2Nlc3M6IGZ1bmN0aW9uIHN1Y2Nlc3MoY29tcHJlc3NlZEltZykge1xuICAgICAgICAgIGNvbnNvbGUubG9nKGNvbXByZXNzZWRJbWcudXJpKTtcblxuICAgICAgICAgIF9zeXN0ZW02LmRlZmF1bHQuZ2V0SW1hZ2VJbmZvKHtcbiAgICAgICAgICAgIHVyaTogY29tcHJlc3NlZEltZy51cmksXG4gICAgICAgICAgICBzdWNjZXNzOiBmdW5jdGlvbiBzdWNjZXNzKGNvbXByZXNzZWRJbWdJbmZvKSB7XG4gICAgICAgICAgICAgIGNvbnNvbGUubG9nKFwi5Zu+54mH6Lev5b6E77yaXCIgKyBjb21wcmVzc2VkSW1nSW5mby51cmkgKyBcIuWuveW6pu+8mlwiICsgY29tcHJlc3NlZEltZ0luZm8ud2lkdGggKyBcInB4LOmrmOW6pu+8mlwiICsgY29tcHJlc3NlZEltZ0luZm8uaGVpZ2h0ICsgXCJweCzlsLrlr7jvvJpcIiArIChjb21wcmVzc2VkSW1nSW5mby5zaXplIC8gMTAyNCkudG9GaXhlZCgyKSArICdLQicpO1xuICAgICAgICAgICAgICBpZiAoY29tcHJlc3NlZEltZ0luZm8uaGVpZ2h0ICUgMiAhPSAwKSB7XG4gICAgICAgICAgICAgICAgX3N5c3RlbTYuZGVmYXVsdC5hcHBseU9wZXJhdGlvbnMoe1xuICAgICAgICAgICAgICAgICAgdXJpOiBjb21wcmVzc2VkSW1nSW5mby51cmksXG5cbiAgICAgICAgICAgICAgICAgIG9wZXJhdGlvbnM6IFt7XG4gICAgICAgICAgICAgICAgICAgIGFjdGlvbjogJ2Nyb3AnLFxuICAgICAgICAgICAgICAgICAgICB3aWR0aDogY29tcHJlc3NlZEltZ0luZm8ud2lkdGgsXG4gICAgICAgICAgICAgICAgICAgIGhlaWdodDogY29tcHJlc3NlZEltZ0luZm8uaGVpZ2h0IC0gMVxuICAgICAgICAgICAgICAgICAgfV0sXG4gICAgICAgICAgICAgICAgICBxdWFsaXR5OiA3MCxcbiAgICAgICAgICAgICAgICAgIGZvcm1hdDogJ0pQRUcnLFxuICAgICAgICAgICAgICAgICAgc3VjY2VzczogZnVuY3Rpb24gc3VjY2VzcyhkYXRhKSB7XG4gICAgICAgICAgICAgICAgICAgIF9zeXN0ZW02LmRlZmF1bHQuZ2V0SW1hZ2VJbmZvKHtcbiAgICAgICAgICAgICAgICAgICAgICB1cmk6IGRhdGEudXJpLFxuICAgICAgICAgICAgICAgICAgICAgIHN1Y2Nlc3M6IGZ1bmN0aW9uIHN1Y2Nlc3Mob3BlcmF0aW9uSW1nSW5mbykge1xuICAgICAgICAgICAgICAgICAgICAgICAgY29uc29sZS5sb2coXCLoo4HliarlkI4t5Zu+54mH6Lev5b6E77yaXCIgKyBvcGVyYXRpb25JbWdJbmZvLnVyaSArIFwi5a695bqm77yaXCIgKyBvcGVyYXRpb25JbWdJbmZvLndpZHRoICsgXCJweCzpq5jluqbvvJpcIiArIG9wZXJhdGlvbkltZ0luZm8uaGVpZ2h0ICsgXCJweCzlsLrlr7jvvJpcIiArIChvcGVyYXRpb25JbWdJbmZvLnNpemUgLyAxMDI0KS50b0ZpeGVkKDIpICsgJ0tCJyk7XG5cbiAgICAgICAgICAgICAgICAgICAgICAgIHJlc29sdmUob3BlcmF0aW9uSW1nSW5mbyk7XG4gICAgICAgICAgICAgICAgICAgICAgfSxcbiAgICAgICAgICAgICAgICAgICAgICBmYWlsOiBmdW5jdGlvbiBmYWlsKG9wZXJhdGlvbkltZ0luZm8sIGNvZGUpIHtcbiAgICAgICAgICAgICAgICAgICAgICAgIGNvbnNvbGUubG9nKFwiaGFuZGxpbmcgZmFpbCwgY29kZT1cIiArIGNvZGUpO1xuICAgICAgICAgICAgICAgICAgICAgIH1cbiAgICAgICAgICAgICAgICAgICAgfSk7XG4gICAgICAgICAgICAgICAgICB9LFxuICAgICAgICAgICAgICAgICAgZmFpbDogZnVuY3Rpb24gZmFpbChkYXRhLCBjb2RlKSB7XG4gICAgICAgICAgICAgICAgICAgIGNvbnNvbGUubG9nKFwiaGFuZGxpbmcgZmFpbCwgY29kZT1cIiArIGNvZGUpO1xuICAgICAgICAgICAgICAgICAgfVxuICAgICAgICAgICAgICAgIH0pO1xuICAgICAgICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgICAgICAgIGNvbnNvbGUubG9nKFwi5LiN5Ymq6KOBXCIpO1xuICAgICAgICAgICAgICAgIHJlc29sdmUoY29tcHJlc3NlZEltZ0luZm8pO1xuICAgICAgICAgICAgICB9XG4gICAgICAgICAgICB9LFxuICAgICAgICAgICAgZmFpbDogZnVuY3Rpb24gZmFpbChjb21wcmVzc2VkSW1nSW5mbywgY29kZSkge1xuICAgICAgICAgICAgICBjb25zb2xlLmxvZyhcImNvbXByZXNzSW1hZ2VidG4gZ2V0SW1hZ2VJbmZvIGZhaWwsIGNvZGU9XCIgKyBjb2RlKTtcbiAgICAgICAgICAgICAgcmVqZWN0KGNvbXByZXNzZWRJbWdJbmZvKTtcbiAgICAgICAgICAgIH1cbiAgICAgICAgICB9KTtcbiAgICAgICAgfSxcbiAgICAgICAgZmFpbDogZnVuY3Rpb24gZmFpbChjb21wcmVzc2VkSW1nLCBjb2RlKSB7XG4gICAgICAgICAgY29uc29sZS5sb2coXCJjb21wcmVzc0ltYWdlYnRuIGZhaWwsIGNvZGU9XCIgKyBjb2RlKTtcbiAgICAgICAgfVxuICAgICAgfSk7XG4gICAgfSk7XG4gIH0sXG5cbiAgZGV0ZWN0RmFjZUxhbmQ6IGZ1bmN0aW9uIGRldGVjdEZhY2VMYW5kKCkge1xuICAgIHZhciB0aGF0ID0gdGhpcztcbiAgICB2YXIgdGVtcExpc3QgPSBbXTtcblxuICAgIHZhciBjbGVhclJlY3QgPSB0aGF0LiRlbGVtZW50KFwiY2FudmFzXCIpO1xuICAgIHZhciBjdHggPSBjbGVhclJlY3QuZ2V0Q29udGV4dChcIjJkXCIpO1xuXG4gICAgY3R4LmNsZWFyUmVjdCgwLCAwLCB0aGF0LnNjcmVlbldpZHRoLCA4MDApO1xuXG4gICAgX3N5c3RlbTIuZGVmYXVsdC5kZXRlY3RGYWNlTGFuZE1hcmsoe1xuICAgICAgdXJpOiB0aGF0LmltYWdlVXJpLFxuICAgICAgc3VjY2VzczogZnVuY3Rpb24gc3VjY2VzcyhkYXRhKSB7XG4gICAgICAgIHRoYXQucG9pbnRBcnJheSA9IGRhdGEubWFya3M7XG4gICAgICAgIGNvbnNvbGUubG9nKFwiZGF0YS5tYXJrOlwiICsgSlNPTi5zdHJpbmdpZnkoZGF0YS5tYXJrcykpO1xuICAgICAgfSxcbiAgICAgIGZhaWw6IGZ1bmN0aW9uIGZhaWwoZGF0YSwgY29kZSkge1xuICAgICAgICBjb25zb2xlLmxvZyhcImhhbmRsaW5nIGZhaWwsIGNvZGU6IFwiICsgY29kZSk7XG4gICAgICAgIHZhciBjbyA9IFwi6ZSZ6K+v56CBOiAgXCIgKyBfZGF0YS5lcnJDb2RlTGlzdFtjb2RlXTtcbiAgICAgICAgdGVtcExpc3QucHVzaChjbyk7XG4gICAgICB9LFxuICAgICAgY29tcGxldGU6IGZ1bmN0aW9uIGNvbXBsZXRlKG1zZykge1xuICAgICAgICBjb25zb2xlLmxvZyhcImRldGVjdF9mYWNlX2xhbmRfbWFyayBoYW5kbGluZyBjb21wbGV0ZVwiKTtcblxuICAgICAgICB2YXIgY2FucyA9IHRoYXQuJGVsZW1lbnQoJ2NhbnZhcycpO1xuICAgICAgICB2YXIgY3R4ID0gY2Fucy5nZXRDb250ZXh0KFwiMmRcIik7XG4gICAgICAgIGZvciAodmFyIGkgPSAwOyBpIDwgdGhhdC5wb2ludEFycmF5Lmxlbmd0aDsgaSsrKSB7XG4gICAgICAgICAgY3R4LmJlZ2luUGF0aCgpO1xuICAgICAgICAgIGN0eC5hcmModGhhdC5wb2ludEFycmF5W2ldLnBvc2l0aW9uRi54LCB0aGF0LnBvaW50QXJyYXlbaV0ucG9zaXRpb25GLnksIDMsIDAsIDIgKiBNYXRoLlBJKTtcbiAgICAgICAgICBjdHguc3Ryb2tlU3R5bGUgPSBcIiMwMGZmZmZcIjtcbiAgICAgICAgICBjdHguZmlsbFN0eWxlID0gXCIjMDBmZmZmXCI7XG4gICAgICAgICAgY3R4LmZpbGwoKTtcbiAgICAgICAgfVxuICAgICAgfVxuICAgIH0pO1xuICB9XG59O1xudmFyIG1vZHVsZU93biA9IGV4cG9ydHMuZGVmYXVsdCB8fCBtb2R1bGUuZXhwb3J0cztcbnZhciBhY2Nlc3NvcnMgPSBbJ3B1YmxpYycsICdwcm90ZWN0ZWQnLCAncHJpdmF0ZSddO1xuaWYgKG1vZHVsZU93bi5kYXRhICYmIGFjY2Vzc29ycy5zb21lKGZ1bmN0aW9uIChhY2MpIHtcbiAgICByZXR1cm4gbW9kdWxlT3duW2FjY107XG4gIH0pKSB7XG4gIHRocm93IG5ldyBFcnJvcign6aG16Z2iVk3lr7nosaHkuK3lsZ7mgKdkYXRh5LiN5Y+v5LiOcHVibGljLCBwcm90ZWN0ZWQsIHByaXZhdGXlkIzml7blrZjlnKjvvIzor7fkvb/nlKhwdWJsaWPmm7/ku6NkYXRh77yBJyk7XG59IGVsc2UgaWYgKCFtb2R1bGVPd24uZGF0YSkge1xuICBtb2R1bGVPd24uZGF0YSA9IHt9O1xuICBtb2R1bGVPd24uX2Rlc2NyaXB0b3IgPSB7fTtcbiAgYWNjZXNzb3JzLmZvckVhY2goZnVuY3Rpb24oYWNjKSB7XG4gICAgdmFyIGFjY1R5cGUgPSB0eXBlb2YgbW9kdWxlT3duW2FjY107XG4gICAgaWYgKGFjY1R5cGUgPT09ICdvYmplY3QnKSB7XG4gICAgICBtb2R1bGVPd24uZGF0YSA9IE9iamVjdC5hc3NpZ24obW9kdWxlT3duLmRhdGEsIG1vZHVsZU93blthY2NdKTtcbiAgICAgIGZvciAodmFyIG5hbWUgaW4gbW9kdWxlT3duW2FjY10pIHtcbiAgICAgICAgbW9kdWxlT3duLl9kZXNjcmlwdG9yW25hbWVdID0ge2FjY2VzcyA6IGFjY307XG4gICAgICB9XG4gICAgfSBlbHNlIGlmIChhY2NUeXBlID09PSAnZnVuY3Rpb24nKSB7XG4gICAgICBjb25zb2xlLndhcm4oJ+mhtemdolZN5a+56LGh5Lit55qE5bGe5oCnJyArIGFjYyArICfnmoTlgLzkuI3og73kvb/lh73mlbDvvIzor7fkvb/nlKjlr7nosaEnKTtcbiAgICB9XG4gIH0pO1xufX1cblxuXG4vLy8vLy8vLy8vLy8vLy8vLy9cbi8vIFdFQlBBQ0sgRk9PVEVSXG4vLyBkOi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvZmEtdG9vbGtpdC9saWIvZmEtc2NyaXB0LWxvYWRlci5qcyFkOi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvZmEtdG9vbGtpdC9saWIvZmEtYWNjZXNzLWxvYWRlci5qcyFkOi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvYmFiZWwtbG9hZGVyL2xpYj9wcmVzZXRzW109ZDovdG9vbHMvSHVhd2VpIEZhc3RBcHAgSURFL3Jlc291cmNlcy9hcHAvZXh0ZW5zaW9ucy9kZXZlY28tZGVidWcvbm9kZV9tb2R1bGVzL2JhYmVsLXByZXNldC1lbnYmcGx1Z2luc1tdPWQ6L3Rvb2xzL0h1YXdlaSBGYXN0QXBwIElERS9yZXNvdXJjZXMvYXBwL2V4dGVuc2lvbnMvZGV2ZWNvLWRlYnVnL25vZGVfbW9kdWxlcy9mYS10b29sa2l0L2xpYi9qc3gtbG9hZGVyLmpzJmNvbW1lbnRzPWZhbHNlIWQ6L3Rvb2xzL0h1YXdlaSBGYXN0QXBwIElERS9yZXNvdXJjZXMvYXBwL2V4dGVuc2lvbnMvZGV2ZWNvLWRlYnVnL25vZGVfbW9kdWxlcy9mYS10b29sa2l0L2xpYi9mYS1mcmFnbWVudC1sb2FkZXIuanM/aW5kZXg9MCZ0eXBlPXNjcmlwdHMhLi9zcmMvRGV0ZWN0X0ZhY2VfTGFuZF9NYXJrL2RldGVjdF9mYWNlX2xhbmRfbWFyay51eFxuLy8gbW9kdWxlIGlkID0gMTBcbi8vIG1vZHVsZSBjaHVua3MgPSAwIl0sIm1hcHBpbmdzIjoiO0FBQUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7Ozs7Ozs7QUM3REE7QUFBQTtBQUFBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7Ozs7Ozs7Ozs7Ozs7QUNoQkE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7Ozs7O0FDYkE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7Ozs7O0FDNU5BO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7Ozs7O0FDdElBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBOzs7QSIsInNvdXJjZVJvb3QiOiIifQ==