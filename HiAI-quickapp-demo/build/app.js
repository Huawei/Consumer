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
/******/ 	return __webpack_require__(__webpack_require__.s = 1);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */,
/* 1 */
/***/ (function(module, exports, __webpack_require__) {

var $app_script$ = __webpack_require__(2)

$app_define$('@app-application/app', [], function($app_require$, $app_exports$, $app_module$){
     $app_script$($app_module$, $app_exports$, $app_require$)
     if ($app_exports$.__esModule && $app_exports$.default) {
            $app_module$.exports = $app_exports$.default
        }
})

$app_bootstrap$('@app-application/app',{ packagerName:'fa-toolkit', packagerVersion: '1.0.8-Stable.300'})

/***/ }),
/* 2 */
/***/ (function(module, exports) {

module.exports = function(module, exports, $app_require$){'use strict';

module.exports = {
  onCreate: function onCreate() {
    console.info('Application onCreate');
  },
  onDestroy: function onDestroy() {
    console.info('Application onDestroy');
  }
};
(exports.default || module.exports).manifest = {"package":"com.hiai.quickapp","name":"HiAI体验","versionName":"1.0.0","versionCode":1,"icon":"/Common/img/HiAI_logo.png","minPlatformVersion":1000,"features":[{"name":"system.ai"},{"name":"system.media"},{"name":"system.image"},{"name":"system.device"}],"permissions":[{"origin":"*"}],"config":{"logLevel":"log"},"router":{"entry":"Main","pages":{"Main":{"component":"main","path":"/main"},"Detect_Face":{"component":"detect_face","path":"/detect_face"},"Detect_Face_Land_Mark":{"component":"detect_face_land_mark","path":"/detect_face_land_mark"}}},"display":{"backgroundColor":"#fbf9fe","titleBar":true,"titleBarBackgroundColor":"#0faeff","titleBarTextColor":"#ffffff","menu":true,"pages":{"Hello":{"titleBarText":"TitleBar"}}}};
}

/***/ })
/******/ ]);
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiYnVpbGRcXGFwcC5qcyIsInNvdXJjZXMiOlsid2VicGFjazovLy93ZWJwYWNrL2Jvb3RzdHJhcCBjNDU3MzY3N2Q1MGMwY2U2YjYxYyIsIndlYnBhY2s6Ly8vLi9zcmMvYXBwLnV4Iiwid2VicGFjazovLy8uL3NyYy9hcHAudXg/NDJhNiJdLCJzb3VyY2VzQ29udGVudCI6WyIgXHQvLyBUaGUgbW9kdWxlIGNhY2hlXG4gXHR2YXIgaW5zdGFsbGVkTW9kdWxlcyA9IHt9O1xuXG4gXHQvLyBUaGUgcmVxdWlyZSBmdW5jdGlvblxuIFx0ZnVuY3Rpb24gX193ZWJwYWNrX3JlcXVpcmVfXyhtb2R1bGVJZCkge1xuXG4gXHRcdC8vIENoZWNrIGlmIG1vZHVsZSBpcyBpbiBjYWNoZVxuIFx0XHRpZihpbnN0YWxsZWRNb2R1bGVzW21vZHVsZUlkXSkge1xuIFx0XHRcdHJldHVybiBpbnN0YWxsZWRNb2R1bGVzW21vZHVsZUlkXS5leHBvcnRzO1xuIFx0XHR9XG4gXHRcdC8vIENyZWF0ZSBhIG5ldyBtb2R1bGUgKGFuZCBwdXQgaXQgaW50byB0aGUgY2FjaGUpXG4gXHRcdHZhciBtb2R1bGUgPSBpbnN0YWxsZWRNb2R1bGVzW21vZHVsZUlkXSA9IHtcbiBcdFx0XHRpOiBtb2R1bGVJZCxcbiBcdFx0XHRsOiBmYWxzZSxcbiBcdFx0XHRleHBvcnRzOiB7fVxuIFx0XHR9O1xuXG4gXHRcdC8vIEV4ZWN1dGUgdGhlIG1vZHVsZSBmdW5jdGlvblxuIFx0XHRtb2R1bGVzW21vZHVsZUlkXS5jYWxsKG1vZHVsZS5leHBvcnRzLCBtb2R1bGUsIG1vZHVsZS5leHBvcnRzLCBfX3dlYnBhY2tfcmVxdWlyZV9fKTtcblxuIFx0XHQvLyBGbGFnIHRoZSBtb2R1bGUgYXMgbG9hZGVkXG4gXHRcdG1vZHVsZS5sID0gdHJ1ZTtcblxuIFx0XHQvLyBSZXR1cm4gdGhlIGV4cG9ydHMgb2YgdGhlIG1vZHVsZVxuIFx0XHRyZXR1cm4gbW9kdWxlLmV4cG9ydHM7XG4gXHR9XG5cblxuIFx0Ly8gZXhwb3NlIHRoZSBtb2R1bGVzIG9iamVjdCAoX193ZWJwYWNrX21vZHVsZXNfXylcbiBcdF9fd2VicGFja19yZXF1aXJlX18ubSA9IG1vZHVsZXM7XG5cbiBcdC8vIGV4cG9zZSB0aGUgbW9kdWxlIGNhY2hlXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLmMgPSBpbnN0YWxsZWRNb2R1bGVzO1xuXG4gXHQvLyBkZWZpbmUgZ2V0dGVyIGZ1bmN0aW9uIGZvciBoYXJtb255IGV4cG9ydHNcbiBcdF9fd2VicGFja19yZXF1aXJlX18uZCA9IGZ1bmN0aW9uKGV4cG9ydHMsIG5hbWUsIGdldHRlcikge1xuIFx0XHRpZighX193ZWJwYWNrX3JlcXVpcmVfXy5vKGV4cG9ydHMsIG5hbWUpKSB7XG4gXHRcdFx0T2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIG5hbWUsIHtcbiBcdFx0XHRcdGNvbmZpZ3VyYWJsZTogZmFsc2UsXG4gXHRcdFx0XHRlbnVtZXJhYmxlOiB0cnVlLFxuIFx0XHRcdFx0Z2V0OiBnZXR0ZXJcbiBcdFx0XHR9KTtcbiBcdFx0fVxuIFx0fTtcblxuIFx0Ly8gZ2V0RGVmYXVsdEV4cG9ydCBmdW5jdGlvbiBmb3IgY29tcGF0aWJpbGl0eSB3aXRoIG5vbi1oYXJtb255IG1vZHVsZXNcbiBcdF9fd2VicGFja19yZXF1aXJlX18ubiA9IGZ1bmN0aW9uKG1vZHVsZSkge1xuIFx0XHR2YXIgZ2V0dGVyID0gbW9kdWxlICYmIG1vZHVsZS5fX2VzTW9kdWxlID9cbiBcdFx0XHRmdW5jdGlvbiBnZXREZWZhdWx0KCkgeyByZXR1cm4gbW9kdWxlWydkZWZhdWx0J107IH0gOlxuIFx0XHRcdGZ1bmN0aW9uIGdldE1vZHVsZUV4cG9ydHMoKSB7IHJldHVybiBtb2R1bGU7IH07XG4gXHRcdF9fd2VicGFja19yZXF1aXJlX18uZChnZXR0ZXIsICdhJywgZ2V0dGVyKTtcbiBcdFx0cmV0dXJuIGdldHRlcjtcbiBcdH07XG5cbiBcdC8vIE9iamVjdC5wcm90b3R5cGUuaGFzT3duUHJvcGVydHkuY2FsbFxuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5vID0gZnVuY3Rpb24ob2JqZWN0LCBwcm9wZXJ0eSkgeyByZXR1cm4gT2JqZWN0LnByb3RvdHlwZS5oYXNPd25Qcm9wZXJ0eS5jYWxsKG9iamVjdCwgcHJvcGVydHkpOyB9O1xuXG4gXHQvLyBfX3dlYnBhY2tfcHVibGljX3BhdGhfX1xuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5wID0gXCJcIjtcblxuIFx0Ly8gTG9hZCBlbnRyeSBtb2R1bGUgYW5kIHJldHVybiBleHBvcnRzXG4gXHRyZXR1cm4gX193ZWJwYWNrX3JlcXVpcmVfXyhfX3dlYnBhY2tfcmVxdWlyZV9fLnMgPSAxKTtcblxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyB3ZWJwYWNrL2Jvb3RzdHJhcCBjNDU3MzY3N2Q1MGMwY2U2YjYxYyIsInZhciAkYXBwX3NjcmlwdCQgPSByZXF1aXJlKFwiISFkOi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvZmEtdG9vbGtpdC9saWIvZmEtc2NyaXB0LWxvYWRlci5qcyFkOi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvZmEtdG9vbGtpdC9saWIvZmEtbWFuaWZlc3QtbG9hZGVyLmpzP3BhdGg9ZDpcXFxcU2VydmljZSB2ZXJpZmljYXRpb25cXFxcQ29kZVxcXFxmYXN0QXBwXFxcXGxhbmRtYXJrQW5kZGV0ZWN0ZVxcXFxjb20uaHVhd2VpLmxhbmRtYXJrYW5kZGV0ZWN0XFxcXHNyY1xcXFxhcHAudXghZDovdG9vbHMvSHVhd2VpIEZhc3RBcHAgSURFL3Jlc291cmNlcy9hcHAvZXh0ZW5zaW9ucy9kZXZlY28tZGVidWcvbm9kZV9tb2R1bGVzL2JhYmVsLWxvYWRlcj9wcmVzZXRzW109ZDpcXFxcdG9vbHNcXFxcSHVhd2VpIEZhc3RBcHAgSURFXFxcXHJlc291cmNlc1xcXFxhcHBcXFxcZXh0ZW5zaW9uc1xcXFxkZXZlY28tZGVidWdcXFxcbm9kZV9tb2R1bGVzXFxcXGJhYmVsLXByZXNldC1lbnYmcGx1Z2luc1tdPWQ6XFxcXHRvb2xzXFxcXEh1YXdlaSBGYXN0QXBwIElERVxcXFxyZXNvdXJjZXNcXFxcYXBwXFxcXGV4dGVuc2lvbnNcXFxcZGV2ZWNvLWRlYnVnXFxcXG5vZGVfbW9kdWxlc1xcXFxiYWJlbC1wbHVnaW4tdHJhbnNmb3JtLXJ1bnRpbWUmY29tbWVudHM9ZmFsc2UhZDovdG9vbHMvSHVhd2VpIEZhc3RBcHAgSURFL3Jlc291cmNlcy9hcHAvZXh0ZW5zaW9ucy9kZXZlY28tZGVidWcvbm9kZV9tb2R1bGVzL2ZhLXRvb2xraXQvbGliL2ZhLWZyYWdtZW50LWxvYWRlci5qcz9pbmRleD0wJnR5cGU9c2NyaXB0cyFkOi9TZXJ2aWNlIHZlcmlmaWNhdGlvbi9Db2RlL2Zhc3RBcHAvbGFuZG1hcmtBbmRkZXRlY3RlL2NvbS5odWF3ZWkubGFuZG1hcmthbmRkZXRlY3Qvc3JjL2FwcC51eFwiKVxuXG4kYXBwX2RlZmluZSQoJ0BhcHAtYXBwbGljYXRpb24vYXBwJywgW10sIGZ1bmN0aW9uKCRhcHBfcmVxdWlyZSQsICRhcHBfZXhwb3J0cyQsICRhcHBfbW9kdWxlJCl7XG4gICAgICRhcHBfc2NyaXB0JCgkYXBwX21vZHVsZSQsICRhcHBfZXhwb3J0cyQsICRhcHBfcmVxdWlyZSQpXG4gICAgIGlmICgkYXBwX2V4cG9ydHMkLl9fZXNNb2R1bGUgJiYgJGFwcF9leHBvcnRzJC5kZWZhdWx0KSB7XG4gICAgICAgICAgICAkYXBwX21vZHVsZSQuZXhwb3J0cyA9ICRhcHBfZXhwb3J0cyQuZGVmYXVsdFxuICAgICAgICB9XG59KVxuXG4kYXBwX2Jvb3RzdHJhcCQoJ0BhcHAtYXBwbGljYXRpb24vYXBwJyx7IHBhY2thZ2VyTmFtZTonZmEtdG9vbGtpdCcsIHBhY2thZ2VyVmVyc2lvbjogJzEuMC44LVN0YWJsZS4zMDAnfSlcblxuXG4vLy8vLy8vLy8vLy8vLy8vLy9cbi8vIFdFQlBBQ0sgRk9PVEVSXG4vLyAuL3NyYy9hcHAudXhcbi8vIG1vZHVsZSBpZCA9IDFcbi8vIG1vZHVsZSBjaHVua3MgPSAzIiwibW9kdWxlLmV4cG9ydHMgPSBmdW5jdGlvbihtb2R1bGUsIGV4cG9ydHMsICRhcHBfcmVxdWlyZSQpeyd1c2Ugc3RyaWN0JztcblxubW9kdWxlLmV4cG9ydHMgPSB7XG4gIG9uQ3JlYXRlOiBmdW5jdGlvbiBvbkNyZWF0ZSgpIHtcbiAgICBjb25zb2xlLmluZm8oJ0FwcGxpY2F0aW9uIG9uQ3JlYXRlJyk7XG4gIH0sXG4gIG9uRGVzdHJveTogZnVuY3Rpb24gb25EZXN0cm95KCkge1xuICAgIGNvbnNvbGUuaW5mbygnQXBwbGljYXRpb24gb25EZXN0cm95Jyk7XG4gIH1cbn07XG4oZXhwb3J0cy5kZWZhdWx0IHx8IG1vZHVsZS5leHBvcnRzKS5tYW5pZmVzdCA9IHtcInBhY2thZ2VcIjpcImNvbS5oaWFpLnF1aWNrYXBwXCIsXCJuYW1lXCI6XCJIaUFJ5L2T6aqMXCIsXCJ2ZXJzaW9uTmFtZVwiOlwiMS4wLjBcIixcInZlcnNpb25Db2RlXCI6MSxcImljb25cIjpcIi9Db21tb24vaW1nL0hpQUlfbG9nby5wbmdcIixcIm1pblBsYXRmb3JtVmVyc2lvblwiOjEwMDAsXCJmZWF0dXJlc1wiOlt7XCJuYW1lXCI6XCJzeXN0ZW0uYWlcIn0se1wibmFtZVwiOlwic3lzdGVtLm1lZGlhXCJ9LHtcIm5hbWVcIjpcInN5c3RlbS5pbWFnZVwifSx7XCJuYW1lXCI6XCJzeXN0ZW0uZGV2aWNlXCJ9XSxcInBlcm1pc3Npb25zXCI6W3tcIm9yaWdpblwiOlwiKlwifV0sXCJjb25maWdcIjp7XCJsb2dMZXZlbFwiOlwibG9nXCJ9LFwicm91dGVyXCI6e1wiZW50cnlcIjpcIk1haW5cIixcInBhZ2VzXCI6e1wiTWFpblwiOntcImNvbXBvbmVudFwiOlwibWFpblwiLFwicGF0aFwiOlwiL21haW5cIn0sXCJEZXRlY3RfRmFjZVwiOntcImNvbXBvbmVudFwiOlwiZGV0ZWN0X2ZhY2VcIixcInBhdGhcIjpcIi9kZXRlY3RfZmFjZVwifSxcIkRldGVjdF9GYWNlX0xhbmRfTWFya1wiOntcImNvbXBvbmVudFwiOlwiZGV0ZWN0X2ZhY2VfbGFuZF9tYXJrXCIsXCJwYXRoXCI6XCIvZGV0ZWN0X2ZhY2VfbGFuZF9tYXJrXCJ9fX0sXCJkaXNwbGF5XCI6e1wiYmFja2dyb3VuZENvbG9yXCI6XCIjZmJmOWZlXCIsXCJ0aXRsZUJhclwiOnRydWUsXCJ0aXRsZUJhckJhY2tncm91bmRDb2xvclwiOlwiIzBmYWVmZlwiLFwidGl0bGVCYXJUZXh0Q29sb3JcIjpcIiNmZmZmZmZcIixcIm1lbnVcIjp0cnVlLFwicGFnZXNcIjp7XCJIZWxsb1wiOntcInRpdGxlQmFyVGV4dFwiOlwiVGl0bGVCYXJcIn19fX07XG59XG5cblxuLy8vLy8vLy8vLy8vLy8vLy8vXG4vLyBXRUJQQUNLIEZPT1RFUlxuLy8gZDovdG9vbHMvSHVhd2VpIEZhc3RBcHAgSURFL3Jlc291cmNlcy9hcHAvZXh0ZW5zaW9ucy9kZXZlY28tZGVidWcvbm9kZV9tb2R1bGVzL2ZhLXRvb2xraXQvbGliL2ZhLXNjcmlwdC1sb2FkZXIuanMhZDovdG9vbHMvSHVhd2VpIEZhc3RBcHAgSURFL3Jlc291cmNlcy9hcHAvZXh0ZW5zaW9ucy9kZXZlY28tZGVidWcvbm9kZV9tb2R1bGVzL2ZhLXRvb2xraXQvbGliL2ZhLW1hbmlmZXN0LWxvYWRlci5qcz9wYXRoPWQ6L1NlcnZpY2UgdmVyaWZpY2F0aW9uL0NvZGUvZmFzdEFwcC9sYW5kbWFya0FuZGRldGVjdGUvY29tLmh1YXdlaS5sYW5kbWFya2FuZGRldGVjdC9zcmMvYXBwLnV4IWQ6L3Rvb2xzL0h1YXdlaSBGYXN0QXBwIElERS9yZXNvdXJjZXMvYXBwL2V4dGVuc2lvbnMvZGV2ZWNvLWRlYnVnL25vZGVfbW9kdWxlcy9iYWJlbC1sb2FkZXIvbGliP3ByZXNldHNbXT1kOi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvYmFiZWwtcHJlc2V0LWVudiZwbHVnaW5zW109ZDovdG9vbHMvSHVhd2VpIEZhc3RBcHAgSURFL3Jlc291cmNlcy9hcHAvZXh0ZW5zaW9ucy9kZXZlY28tZGVidWcvbm9kZV9tb2R1bGVzL2JhYmVsLXBsdWdpbi10cmFuc2Zvcm0tcnVudGltZSZjb21tZW50cz1mYWxzZSFkOi90b29scy9IdWF3ZWkgRmFzdEFwcCBJREUvcmVzb3VyY2VzL2FwcC9leHRlbnNpb25zL2RldmVjby1kZWJ1Zy9ub2RlX21vZHVsZXMvZmEtdG9vbGtpdC9saWIvZmEtZnJhZ21lbnQtbG9hZGVyLmpzP2luZGV4PTAmdHlwZT1zY3JpcHRzIS4vc3JjL2FwcC51eFxuLy8gbW9kdWxlIGlkID0gMlxuLy8gbW9kdWxlIGNodW5rcyA9IDMiXSwibWFwcGluZ3MiOiI7QUFBQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBOzs7Ozs7OztBQzdEQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7Ozs7O0FDVEE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBOzs7QSIsInNvdXJjZVJvb3QiOiIifQ==