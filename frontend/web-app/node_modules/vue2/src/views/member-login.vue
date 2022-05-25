<template>
  <div class="memberLogin">
    <div v-if="ifShow" @touchstart="preventMouse($event)">
      <div class="bac"></div>
      <div class="toast"><img src="../images/timg.gif"></div>
    </div>
    <div class="banner">
    </div>
    <div class="item">
      <div class="tel">手机号码</div>
      <div class="insertTel">
        <input placeholder="请输入您的手机号码" v-model="telephone" type="tel">
      </div>
      <div class="del" @click="clickDelete()" v-if="showDel">
        <img src="../images/del.png">
      </div>
    </div>
    <div class="item">
      <div class="tel">验证码</div>
      <div class="insertCode">
        <input v-model="identifyCode" placeholder="请输入六位验证码" type="tel">
      </div>
      <div class="sendCode" @click="getVerify()">
        <div v-if="!countDown">发送验证码</div>
        <div v-if="countDown">{{countDown}}s后重发</div>
      </div>
    </div>
    <div class="logBtn" v-bind:class="{'toLogin': identifyCode && telephone}" @click="clickLogin()">登录</div>
    <device-toast ref="deviceToast"></device-toast>
  </div>
</template>
<script>
// 引入组件以及公共文件
import Utils from '../utils'
import Api from '../api'
import DeviceToast from '../components/device/device-toast'
import {
  Toast,
  Indicator
} from 'mint-ui'
import {
  Swipe,
  SwipeItem
} from 'vue-swipe'
// 实例化vue

// var test = 1111111111
export default {
  components: {
    Swipe,
    SwipeItem,
    DeviceToast
  },

  data() {
    return {
      userName: '',
      showDel: false,
      countDown: 0,
      phoneNumPatten: /^1\d{10}$/,
      newColor: '',
      bgUrl: '',
      telephone: '',
      identifyCode: '',
      ifShow: false,
      brandType: '',
      imei: '',
      pageName: '',
      WECHAT: {}
    }
  },
  watch: {
    telephone: function(val, oldVal) {
      // console.log('new: %s, old: %s', val, oldVal)
      if (val) {
        this.showDel = true
      }
    }
  },
  methods: {
    clickDelete: function() {
      this.telephone = ''
      this.showDel = false
    },
    getVerify: function() {
      var self = this
      if (this.countDown !== 0) {
        console.log('88888')
        return
      } else if (!this.telephone || !this.telephone.match(this.phoneNumPatten)) {
        Toast({
          message: '请输入正确的手机号码！',
          position: 'middle',
          duration: 2000
        })
        return
      }
      var tempInterval
      var countNum = 60
      self.countDown = countNum
      tempInterval = setInterval(function() {
        if (countNum === 0) {
          window.clearInterval(tempInterval)
          countNum = 60
          return
        }
        countNum -= 1
        self.countDown = countNum
          // self.$set(self, 'countDown', countNum)
      }, 1000)

      Utils.post(Api.SEND_MESSAGE, {
        'userName': this.telephone,
        'captchaType': 'WECHAT_LOGIN'
      }, result => {
        Toast({
          message: '验证码已发送，请注意查收！',
          position: 'middle',
          duration: 2000
        })
      })
    },
    clickLogin: function() {
      if (!this.telephone.match(this.phoneNumPatten) || !this.telephone) {
        Toast({
          message: '请输入正确的手机号码！',
          position: 'middle',
          duration: 2000
        })
      } else if (!this.identifyCode) {
        Toast({
          message: '请输入验证码！',
          position: 'middle',
          duration: 2000
        })
      } else {
        Indicator.open()
        Utils.post(Api.MEMBER_LOGIN, {
          'captcha': this.identifyCode,
          'userName': this.telephone,
          openId: this.WECHAT.openId,
          wechatId: this.WECHAT.wechatId
        }, result => {
          if (result.success) {
            this.bindUserWechat(result.data.userId, result.data.userName)
          } else {
            Toast({
              message: '验证码不正确，请重新输入！',
              position: 'middle',
              duration: 2000
            })
          }
        })
      }
    },
    bindDevice: function(userId) {
      let that = this
      let data = {
        imei: this.imei,
        uId: userId,
        deviceBrandType: this.brandType
      }
      Utils.post(Api.POST_BIND_DEVICE, data, result => {
        if (result.success) {
          this.$refs['deviceToast'].show({
            status: 'success',
            text: '绑定成功'
          })
          setTimeout(function() {
            that.$router.push({
              name: 'bindDevice'
            })
          }, 3000)
        } else {
          this.$refs['deviceToast'].show({
            status: 'fail',
            text: '绑定失败',
            submit: function() {
              window.location.href = 'tel:4006860069'
            }
          })
        }
      })
    },
    bindUserWechat: function(userId, userName) {
      let that = this
      let data = {
        openId: this.WECHAT.openId,
        userId: userId,
        wechatId: this.WECHAT.wechatId
      }
      Utils.post(Api.POST_BIND_USER_WECHAT, data, result => {
        this.WECHAT.userId = userId
        this.WECHAT.userName = userName
        this.Cookie.set('WECHAT', this.WECHAT)
        if (this.brandType && this.imei) {
          this.bindDevice(userId)
        } else {
          Indicator.close()
          this.$refs['deviceToast'].show({
            status: 'success',
            text: '登录成功'
          })
          setTimeout(function() {
            if (this.userName) {
              that.$router.push({
                name: 'friends',
                query: { name: 'followerManage', userName: this.userName }
              })
            } else {
              that.$router.push({
                name: 'follow-list',
                query: { name: that.pageName }
              })
            }
          }, 3000)
        }
      })
    }
  },

  created: function() {
    let query = this.$route.query
    this.WECHAT = this.Cookie.getJSON('WECHAT')
    this.pageName = query.name
    this.brandType = query.brandType
    this.imei = query.imei
    this.userName = query.userName
  },
  mounted: function() {
    // console.log(test)
    // this.newColor = this.Storage.get('useInfo').color
    // this.bgUrl = this.Storage.get('useInfo').imgUrlMap.logoBg
  }
}
</script>
<style>
.mint-toast-text {
  font-size: 0.46875rem;
}
.mint-msgbox,
.mint-msgbox-cancel,
.mint-msgbox-confirm,
.mint-msgbox-title,
.mint-msgbox-message {
  font-size: 1.1rem !important;
  color: black !important;
}

.mint-msgbox-btns {
  height: 50px !important;
}
</style>
<style scoped lang="scss">
@import '../scss/common';
@import '../scss/vue-swipe.css';
.memberLogin {
  width: 100%;
  height: 100%;
}

.bac {
  width: 100%;
  height: 100%;
  background-color: black;
  opacity: 0.3;
  position: fixed;
}

.toast {
  width: 18%;
  margin-left: 41%;
  margin-top: 70%;
  height: 4rem;
  position: absolute;
  text-align: center;
}

.toast img {
  width: 100%;
  height: 100%;
}

.banner {
  width: 100%;
  height: 6.40625rem;
  margin-bottom: 10px;
  background-image: url(../images/pic_dl.png);
  background-size: 100% 100%;
}

.logo {
  width: 70px;
  height: 70px;
  margin-left: 40%;
  border-radius: 70px;
  background-color: white;
  margin-bottom: 20px;
}

.name {
  width: 100%;
  height: 40px;
  line-height: 40px;
  color: white;
  font-size: 1.3rem;
  text-align: center;
}

a {
  color: #31c27c;
  text-decoration: underline;
}

.banner img {
  width: 100%;
  height: 100%;
}

.item {
  width: 100%;
  font-size: 0.46875rem;
  padding-top: 0.78125rem;
  padding-bottom: 0.625rem;
  overflow: hidden;
  border-bottom: 1px solid #E9E6E6;
}

.tel {
  height: 100%;
  float: left;
  margin-left: 0.46875rem;
}

.insertTel {
  float: left;
  padding-left: 0.46875rem;
  // padding-top: 0.0625rem;
}

.insertTel input {
  width: 100%;
  height: 100%;
  font-size: 0.46875rem;
}

.insertCode {
  width: 44%;
  float: left;
  padding-left: 0.9375rem;
  padding-top: 0.0625rem;
}
.insertCode input{
  width: 100%;
}


.sendCode {
  width: 2rem;
  height: 0.625rem;
  text-align: center;
  float: left;
  font-size: 0.3125rem;
  line-height: 0.625rem;
  border: 1px solid #2ac4fb;
  color: #2ac4fb;
  // border-radius: 0.03125rem;
}

.tip {
  width: 100%;
  height: 3rem;
  line-height: 3rem;
  font-size: 1.1rem;
  text-align: center;
}

.logBtn {
  width: 9.0625rem;
  margin:0 auto;
  opacity: 0.5;
  height: 1.25rem;
  line-height: 1.25rem;
  text-align: center;
  font-size: 0.5rem;
  color: white;
  margin-top: 1.5625rem;
  border-radius: 0.03125rem;
  background-color: #2ac4fb;
}

.toLogin {
  opacity: 1;
}

.del {
  width: 0.46875rem;
  height: 0.46875rem;
  margin-top: 0.15625rem;
  margin-right: 0.46875rem;
  float: right;
  font-size: 0.46875rem;
  img {
    width: 100%;
    height: 100%;
  }
}
</style>
