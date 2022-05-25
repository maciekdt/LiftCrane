<template>
  <div class="scan-bind">
    <div>
      <div class="pic">
        <img src="../images/bp.png">
      </div>
      <div class="tips">请对设备后背条形码进行扫码绑定</div>
      <div class="scanBtn" @click="clickBind">扫码绑定</div>
      <div class="hand">
        <div class="handText" @click="goInhand()">也可手动输入条形码</div>
      </div>
    </div>
    <device-function ref="deviceFunction"></device-function>
  </div>
</template>
<script>
import Utils from '../utils'
import Api from '../api'
// import DeviceToast from '../components/device/device-toast'
import DeviceFunction from '../components/device/device-function'
const wx = require('weixin-js-sdk')
export default {
  components: {DeviceFunction},
  data() {
    return {
      brandType: '',
      WECHAT: {}
    }
  },
  mounted: function() {
    console.log(wx)
    let query = this.$route.query
    this.WECHAT = this.Cookie.getJSON('WECHAT')
    this.brandType = query.brandType
    setTimeout(() => {
      this.getWeChatSign()
    }, 500)
    // this.$refs['deviceFunction'].checkScanResult(111)
  },
  methods: {
    clickBind: function() {
      let that = this
      wx.scanQRCode({
        needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
        scanType: ['qrCode', 'barCode'], // 可以指定扫二维码还是一维码，默认二者都有
        success: function (res) {
          var result = res.resultStr // 当needResult 为 1 时，扫码返回的结果
          console.log(result)
          let arr = result.split(',')
          that.$refs['deviceFunction'].checkScanResult(arr[1])
        }
      })
    },
    goInhand: function() {
      this.$router.push({
        name: 'deviceNumber',
        query: { brandType: this.brandType }
      })
    },
    initScan: function(appId, timestamp, nonceStr, signature) {
      // let that = this
      wx.config({
        debug: false,
        appId: appId,
        timestamp: timestamp,
        nonceStr: nonceStr,
        signature: signature,
        jsApiList: ['scanQRCode']
      })
      // wx.ready(function() {
      //   that.clickBind()
      // })
    },
    getWeChatSign: function() {
      let url = window.location.href.split('#')[0]
      // window.alert(url)
      let data = {
        url: url,
        wechatId: this.WECHAT.wechatId
      }
      console.log(data)
      Utils.post(Api.POST_WECHAT_SIGN, data, result => {
        let wxData = result.data
        this.initScan(wxData.appId, wxData.timestamp, wxData.nonceStr, wxData.signature)
      })
    }
  }
}
</script>
<style scoped lang="scss">
.scan-bind {
  width: 100%;
  height: 100%;
  position: absolute;
  background-color: #212852;
  .toastBac {
    width: 100%;
    .bac {
      width: 100%;
      height: 100%;
      background-color: black;
      opacity: 0.3;
      position: fixed;
      z-index: 99;
    }
    .toast {
      width: 8.125rem;
      margin-left: 0.96875rem;
      margin-top: 47%;
      border-radius: 0.125rem;
      position: absolute;
      background-color: white;
      text-align: center;
      z-index: 100;
      font-size: 0.46875rem;
      .toastImg img {
        width: 15%;
        height: 15%;
        margin-top: 0.625rem;
        margin-bottom: 0.3125rem;
      }
      .toastImg1 {
        width: 2.5rem;
        height: 2.5rem;
        border-radius: 1.015625rem;
        margin: 0 auto;
        margin-top: 0.625rem;
        margin-bottom: 0.3125rem;
        img {
          width: 100%;
          height: 100%;
          border-radius: 1.25rem;
        }
      }
      .success {
        color: green;
        margin-bottom: 0.625rem;
      }
      .fail {
        color: #ed5564;
        padding-bottom: 0.3125rem;
        border-bottom: 0.015625rem solid #d2d3d5;
      }
      .bindTime {
        font-size: 0.375rem;
        color: grey;
        margin-bottom: 0.3125rem;
      }
      .bind {
        padding-left: 0.46875rem;
        text-align: left;
        padding-bottom: 0.3125rem;
        border-bottom: 0.015625rem solid #d2d3d5;
      }
      .notBind {
        padding-left: 0.46875rem;
        text-align: left;
        padding-top: 1rem;
        padding-bottom: 1rem;
        border-bottom: 0.015625rem solid #d2d3d5;
      }
      .btn {
        width: -webkit-calc(50% - 0.015625rem);
        font-size: 0.5rem;
        float: left;
        padding: 0.35rem 0;
      }
      .borderRight {
        border-right: 0.015625rem solid #d2d3d5;
      }
    }
  }
  .pic {
    width: 7.8125rem;
    height: 7.8125rem;
    padding-top: 1.953125rem;
    margin: 0 auto;
    img {
      width: 100%;
      height: 100%;
    }
  }
  .tips {
    width: 100%;
    text-align: center;
    margin-top: 0.78125rem;
    font-size: 0.46875rem;
    color: #2ac4fb;
    margin-bottom: 0.59375rem;
  }
  .scanBtn {
    width: 9.0625rem;
    height: 1.25rem;
    line-height: 1.25rem;
    text-align: center;
    font-size: 0.5rem;
    margin: 0 auto;
    background-color: #2ac4fb;
    color: white;
    border-radius: 0.03125rem;
  }
  .hand {
    width: 100%;
    text-align: center;
    .handText {
      margin-top: 1.171875rem;
      color: white;
      opacity: 0.3;
      font-size: 0.40625rem;
      text-decoration: underline;
    }
  }
}
</style>
