import Vue from 'vue'
import App from './App'
import router from './router/routes'
import VueResource from 'vue-resource'
import Storage from './storage'
import Cookie from 'js-cookie'
import MintUI from 'mint-ui'
import 'mint-ui/lib/style.css'
import store from './store/index'
Vue.prototype.Storage = Storage
Cookie.removeAll = function (attributes) {
  Object.keys(Cookie.get()).forEach(function (cookie) {
    Cookie.remove(cookie, attributes)
  })
}
Vue.prototype.Cookie = Cookie
  /* 开启debug模式 */
Vue.config.debug = true
Vue.use(VueResource)
Vue.use(MintUI)
Vue.http.options.emulateJSON = true

Vue.prototype.getUrlParam = function(name) {
  var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)') // 构造一个含有目标参数的正则表达式对象
  var r = window.location.search.substr(1).match(reg) // 匹配目标参数
  if (r != null) return unescape(r[2])
  return null // 返回参数值
}

// window.bus = new Vue()
// window.bus.$emit('test','hahaha')
// window.bus.$on('test', options => {
//   console.log(11111)
// })
// return new Promise(function(resolve,reject) {
//   resolve()
// },function(){
//   reject()
// })
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router: router,
  store: store,
  render: h => h(App)
})
