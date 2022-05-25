<template>
  <div class="membership" v-if="ifEntey">
    <div class="header">
      <img src="../images/bg2.png">
    </div>
    <div class="tips">
      <div class="title" :style="{ 'color': newColor}">温馨提示</div>
      <div class="content">
        欢迎使用{{storeName}}微信会员中心，老会员登录和新会员申请后，即可随时随地查询会员积分、储值金额。优惠券和健康记录等信息。
      </div>
    </div>
    <div class="bottom">
      <div class="logBtn" @click="clickLogin()" :style="{ 'background-color': newColor}">会员登录</div>
    </div>
  </div>
</template>
<script>
// 引入组件以及公共文件
import Utils from '../utils'
import Api from '../api'
import {
  Toast
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
      ifEntey: false,
      mainStoreId: '',
      storeName: '',
      newColor: ''
    }
  },

  methods: {
    clickLogin: function() {
      this.$router.push({
        name: 'memberCenterLogin'
      })
    }
  },

  created: function() {},

  mounted: function() {
    console.log(this.Storage.get('memberStatus'))
    console.log('=================')
    if (this.Storage.get('memberStatus')) {
      console.log(new Date().getTime())
      if (this.Storage.get('saveTime') + 604800000 < (new Date().getTime())) {
        this.$router.push({
          name: 'binding'
        })
        Toast({
          message: '您的登陆信息已过期，请重新登陆！',
          position: 'middle',
          duration: 2000
        })
      } else {
        if (this.Storage.get('memberStatus').status === 1) {
          this.$router.push({
            name: 'information',
            params: {
              checkOrMember: 'hasChecked'
            },
            query: {
              'tel': this.Storage.get('memberStatus').tel
            }
          })
        }
        if (this.Storage.get('memberStatus').status === 2) {
          Utils.post(Api.MEMBER_CARD, {
            'mStId': this.Storage.get('mainStoreId'),
            'uNm': this.telephone
              // 'uNm': '13863541469'
          }, result => {
            if (result.dataList.length > 0) {
              this.Storage.set('cardInfo', result.dataList)
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
            } else {
              this.$router.push({
                name: 'information',
                params: {
                  checkOrMember: 'check'
                }
              })
            }
          })
        }
      }
    } else {
      this.ifEntey = true
      this.Storage.clear()
      this.mainStoreId = this.$route.query.mainStoreId
        // this.mainStoreId = '8096000'
      this.Storage.set('mainStoreId', this.mainStoreId)
      console.log(this.Storage.get('mainStoreId'))
      Utils.post(Api.MEMBER_STYLES, {
        'mainStoreId': this.mainStoreId
      }, result => {
        this.Storage.set('useInfo', result.data)
        console.log(this.Storage.get('useInfo'))
        this.newColor = this.Storage.get('useInfo').color
        this.storeName = this.Storage.get('useInfo').mainStoreName
      })
    }
  }
}
</script>
<style scoped lang="scss">
@import '../scss/common';
@import '../scss/vue-swipe.css';
.membership {
  width: 100%;
  height: 100%;
  font-family: microsoft yahei;
  .header { 
    width: 100%; 
    height: 5.59375rem; 
  }
}


.header img {
  width: 100%;
  height: 100%;
}

.tips {
  width: 100%;
}

.title {
  width: 100%;
  height: 1.875rem;
  font-size: 0.5rem;
  line-height: 1.875rem;
  text-align: center;
  color: #2ac4fb;
}

.content {
  width: 90%;
  line-height: 0.7rem;
  margin-left: 5%;
  margin-right: 5%;
  font-size: 0.5rem;
}

.bottom {
  width: 100%;
  height: 1.25rem;
  line-height: 1.25rem;
  font-size: 0.53125rem;
  color: white;
}

.logBtn {
  margin-top: 2rem;
  width: 90%;
  margin-left: 5%;
  height: 1.25rem;
  line-height: 1.25rem;
  text-align: center;
  color: white;
  background: #2ac4fb;
}
</style>
