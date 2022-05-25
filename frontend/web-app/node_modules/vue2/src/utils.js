/**
 * Created by aresn on 16/7/18.
 */

import MD5 from 'md5'
// import jQuery from 'jquery'
import Vue from 'vue'
let utils = {

}

var vType = 'web'
var vCode = 100
var platform = 'web'
var apiEntry = 'http://api.carelinker.com/clApi/entry'
// var apiEntry = 'https://platform.carelinker.com/clApi/entry'
var token = '000'
console.log('v0.2.5')
utils.alert = function(content) {
  window.alert(content)
}

utils.post = function(api, data, success) {
  data.platform = platform
  data.method = api[0]
  data.path = api[1]
  data.sign = MD5(api[0] + api[1] + MD5(vType))
  data.vCode = vCode
  data.vType = vType
  data.token = token
  Vue.http.post(apiEntry, data).then((response) => { success(response.body) })
}

utils.getLargeImg = function(imgUrl) {
  return imgUrl.replace('/s/', '/l/')
}

utils.getUrlParam = function(name) {
  var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)') // 构造一个含有目标参数的正则表达式对象
  var r = window.location.search.substr(1).match(reg) // 匹配目标参数
  if (r != null) return unescape(r[2])
  return null // 返回参数值
}
utils.setTitle = function(t) {
  document.title = t
  var i = document.createElement('iframe')
  i.src = 'https://platform.carelinker.com/sc/web/pharmacy_mis/images/favicon.ico'
  i.style.display = 'none'
  i.onload = function() {
    setTimeout(function() {
      i.remove()
    }, 9)
  }
  document.body.appendChild(i)
}

utils.formatedDate = function(AddDayCount, times) {
  let dd = !times ? new Date() : times
  dd.setDate(dd.getDate() + AddDayCount)
  let year = dd.getFullYear().toString()
  let month = (dd.getMonth() + 1) > 9 ? (dd.getMonth() + 1) : ('0' + (dd.getMonth() + 1))
  let day = dd.getDate() > 9 ? dd.getDate() : ('0' + dd.getDate())
  let hour = dd.getHours() > 9 ? dd.getHours() : ('0' + dd.getHours())
  let minute = dd.getMinutes() > 9 ? dd.getMinutes() : ('0' + dd.getMinutes())
  let Secends = dd.getSeconds() > 9 ? dd.getSeconds() : '0' + dd.getSeconds()
  // let fullDate = year + (month < 10 ? '0' + month : month) + (day < 10 ? '0' + day : day) + hms
  return [year, month, day, hour, minute, Secends]
}
export default utils
