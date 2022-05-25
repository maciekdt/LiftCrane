<template>
  <div class="memberLogin">
    <div v-if="ifShow" @touchstart="preventMouse($event)">
      <div class="bac"></div>
      <div class="toast"><img src="../images/timg.gif"></div>
    </div>
    <div class="banner" :style="{ 'background-image': 'url(' + bgUrl +')'}"></div>
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
      <div class="sendCode" :style="{ 'color': newColor ,'border-color':newColor}" @click="getVerify()">
        <div v-if="!countDown">发送验证码</div>
        <div v-if="countDown">{{countDown}}s后重发</div>
      </div>
    </div>
    <div class="tip" :style="{ 'color': newColor}">登录即可享受多重<a href="#/memberBenefits":style="{ 'color': newColor}">《会员权益》</a></div>
    <div class="logBtn" v-bind:class="{'toLogin': identifyCode && telephone}" @click="clickLogin()" :style="{ 'background-color': newColor}">立即登录</div>
  </div>
</template>
<script>
// 引入组件以及公共文件
import Utils from '../utils'
import Api from '../api'
import {
  Toast,
  MessageBox,
  Indicator
} from 'mint-ui'
import {
  Swipe,
  SwipeItem
} from 'vue-swipe'
// 实例化vue

export default {
  components: {
    Swipe,
    SwipeItem
  },

  data() {
    return {
      showDel: false,
      countDown: 0,
      phoneNumPatten: /^1\d{10}$/,
      newColor: '',
      bgUrl: '',
      telephone: '',
      identifyCode: '',
      ifShow: false
    }
  },
  watch: {
    telephone: function(val, oldVal) {
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

      // Toast({
      //   message: '发送成功！',
      //   position: 'middle',
      //   duration: 2000
      // })

      Utils.post(Api.SEND_MESSAGE, {
        'userName': this.telephone,
        'captchaType': 'WECHAT_REGISTER'
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
        Indicator.open('正在登陆...')
        Utils.post(Api.MEMBER_LOGIN, {
          'captcha': this.identifyCode,
          'userName': this.telephone
        }, result => {
          if (result.success) {
            this.Storage.set('patientId', result.data.patientId1)
              // this.Storage.set('patientId', 7937)
            Utils.post(Api.MEMBER_CARD, {
              'mStId': this.Storage.get('mainStoreId'),
              'uNm': this.telephone
                // 'uNm': '13863541469'
            }, result => {
              if (result.dataList.length > 0) {
                this.Storage.set('cardInfo', result.dataList)
                Indicator.close()
                this.$router.push({
                  name: 'information',
                  params: {
                    checkOrMember: 'hasChecked'
                  },
                  query: {
                    'tel': this.telephone
                  }
                })
                this.Storage.set('memberStatus', {
                  status: 1,
                  tel: this.telephone
                })
                this.Storage.set('saveTime', (new Date().getTime()))
                console.log(this.Storage.get('memberStatus'))
              } else {
                Utils.post(Api.MEMBER_GET_APPLY, {
                  'mainStoreId': this.Storage.get('mainStoreId'),
                  'userName': this.telephone
                }, result => {
                  if (result.data) {
                    this.Storage.set('memberStatus', {
                      status: 2
                    })
                    this.Storage.set('saveTime', (new Date().getTime()))
                    console.log(this.Storage.get('memberStatus'))
                    Indicator.close()
                    this.$router.push({
                      name: 'information',
                      params: {
                        checkOrMember: 'check'
                      }
                    })
                  } else {
                    Indicator.close()
                    MessageBox.confirm('您还不是会员哦，前往申请？').then(action => {
                      this.$router.push({
                        name: 'apply',
                        query: {
                          'tel': this.telephone
                        }
                      })
                    }, function() {})
                  }
                })
              }
            })
          } else {
            Toast({
              message: '验证码不正确，请重新输入！',
              position: 'middle',
              duration: 2000
            })
          }
        })
      }
    }
  },

  created: function() {},

  mounted: function() {
    this.newColor = this.Storage.get('useInfo').color
    this.bgUrl = this.Storage.get('useInfo').imgUrlMap.logoBg
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
  font-size: 0.5rem !important;
  color: black !important;
}
.mint-msgbox-message{
  line-height: 0.5625rem;
}
.mint-msgbox-btns {
  height: 0.78125rem !important;
  padding: 0.15625rem 0;
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
  /* // align-items: center; */
  background-size: 100% 100%;
  background-image: url('../images/member-center/pic_dlbg.png')
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
  color: #2ac4fb;
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
  padding-top: 0.0625rem;
}

.insertTel input {
  width: 100%;
  height: 100%;
  font-size: 0.46875rem;
}

.insertCode {
  width: 45%;
  float: left;
  padding-left: 0.9375rem;
  padding-top: 0.0625rem;
}

.sendCode {
  width: 2.15625rem;
  height: 0.625rem;
  text-align: center;
  float: left;
  font-size: 0.3125rem;
  line-height: 0.625rem;
  border: 0.015625rem solid #2ac4fb;
  color: #2ac4fb;
}

.tip {
  width: 100%;
  height: 1.5rem;
  line-height: 1.5rem;
  font-size: 0.5rem;
  text-align: center;
  color: #2ac4fb;
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
  margin-top: 0.3125rem;
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
