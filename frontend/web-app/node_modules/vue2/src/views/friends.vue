<template>
  <div class="device-number">
    <div class="inputDiv">
      <input placeholder="请输入亲友的蓝信康账户" v-model="account">
    </div>
    <div class="inputDiv1">
      <input placeholder="请备注亲友名称" v-model="markName">
    </div>
    <!-- <div class="tips">注：系统将发送一条提醒短信至亲友手机</div> -->
    <div class="scanBtn" @click="addNewFollowFun">关注</div>
    <device-toast ref="deviceToast"></device-toast>
  </div>
</template>
<script>
import DeviceToast from '../components/device/device-toast'
import Api from '../api'
import Utils from '../utils'
import { Toast } from 'mint-ui'
export default {
  name: 'add-new-follow',
  components: {DeviceToast},
  data() {
    return {
      account: '',
      markName: '',
      userId: ''
    }
  },
  methods: {
    addNewFollowFun: function() {
      if (this.account === '') {
        Toast({
          message: '请输入亲友账户',
          className: 'aaa'
        })
        return
      }
      if (this.markName === '') {
        Toast({
          message: '请备注亲友名称',
          className: 'aaa'
        })
        return
      }
      this.post()
    },
    post: function() {
      let that = this
      let data = {
        followerUserId: this.userId,
        followedUserName: this.account,
        remark: this.markName
      }
      Utils.post(Api.POST_USER_FOLLOW_ADD, data, result => {
        // let dataList = []
        if (result.success) {
          this.$refs['deviceToast'].show({
            status: 'success',
            text: '关注成功'
          })
          setTimeout(function() {
            that.$router.push({
              name: 'follow-list',
              query: { name: 'followerManage' }
            })
          }, 3000)
        } else {
          this.$refs['deviceToast'].show({
            status: 'warning',
            text: '关注失败'
          })
        }
      })
    }
  },
  mounted: function() {
    let WECHAT = this.Cookie.getJSON('WECHAT')
    this.userId = WECHAT.userId
    let query = this.$route.query
    if (query.userName) {
      this.account = query.userName
    }
    this.goViewName = query.name
    Utils.setTitle('关注亲友')
  }
}
</script>
<style scoped lang="scss">
.device-number {
  width: 100%;
  height: 100%;
  position: absolute;
  background-color: #212852;
  .inputDiv {
    width: 9.0625rem;
    height: 1.25rem;
    // line-height: 1.25rem;
    margin: 0 auto;
    margin-top: 1.40625rem;
    border-radius: 0.03125rem;
    input {
      width: -webkit-calc(100% - 0.3125rem);
      height: 1.25rem;
      line-height: 1.25rem;
      padding-left: 0.3125rem;
      font-size: 0.46875rem;
    }
  }
  .inputDiv1 {
    width: 9.0625rem;
    height: 1.25rem;
    margin: 0 auto;
    margin-top: 0.6rem;
    border-radius: 0.03125rem;
    input {
      width: -webkit-calc(100% - 0.3125rem);
      height: 1.25rem;
      line-height: 1.25rem;
      padding-left: 0.3125rem;
      font-size: 0.46875rem;
    }
  }
  // ::-webkit-input-placeholder {
  //   color: #e5e9f3;
  // }
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
    margin-top: 1rem;
    border-radius: 0.03125rem;
  }
}
</style>
