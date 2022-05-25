<template>
  <div class="follow-manage">
    <div class="btn" @click="disableRel">{{btnText}}</div>
    <device-toast ref="deviceToast"></device-toast>
  </div>
</template>
<script>
import DeviceToast from '../components/device/device-toast'
import Utils from '../utils'
import Api from '../api'
export default {
  name: 'follow-manage',
  components: {
    DeviceToast
  },
  data: function() {
    return {
      WECHAT: {},
      btnText: '',
      goViewName: '',
      wechatId: '',
      openId: '',
      userId: '',
      followedUserId: ''
    }
  },
  methods: {
    disableRel() {
      let that = this
      if (this.isSelf) {
        this.$refs['deviceToast'].show({
          status: 'confirm',
          confirmText: '确定要退出当前账号？',
          submitBtnText: '确定',
          submit: function(res) {
            that.postDelete()
          }
        })
      } else {
        this.$refs['deviceToast'].show({
          status: 'confirm',
          confirmText: '确定取消关注此账号？',
          submitBtnText: '确定',
          submit: function(res) {
            that.deleteFollow()
          }
        })
      }
    },
    postDelete: function() {
      let data = {
        wechatId: this.wechatId,
        openId: this.openId,
        userId: this.userId
      }
      Utils.post(Api.DELETE_USER_WECHAT_REL, data, result => {
        if (result.success) {
          delete this.WECHAT.userId
          this.Cookie.set('WECHAT', this.WECHAT)
          this.$router.push({
            path: '/memberLogin',
            query: {
              name: 'followerManage'
            }
          })
        } else {
          this.$refs['deviceToast'].show({
            status: 'warning',
            confirmText: '退出账号失败'
          })
        }
      })
    },
    deleteFollow: function() {
      let data = {
        followedUserId: this.followedUserId,
        followerUserId: this.userId
      }
      Utils.post(Api.DELETE_USER_FOLLOW, data, result => {
        if (result.success) {
          this.$router.push({
            path: '/follow',
            query: {
              name: 'followerManage'
            }
          })
        } else {
          this.$refs['deviceToast'].show({
            status: 'warning',
            confirmText: '取消关注失败'
          })
        }
      })
    }
  },
  created() {
    if (this.Cookie.getJSON('WECHAT').userId) {
      this.$router.push({
        name: 'binding',
        query: {
          name: 'follower-manage'
        }
      })
    }
  },
  mounted: function() {
    let query = this.$route.query
    this.goViewName = query.name
    this.isSelf = query.isSelf
    this.followedUserId = query.followedUserId
    this.WECHAT = this.Cookie.getJSON('WECHAT')
    this.wechatId = this.WECHAT.wechatId
    this.openId = this.WECHAT.openId
    this.userId = this.WECHAT.userId
    if (this.isSelf || this.isSelf === 'true') {
      this.btnText = '退出当前账号'
      Utils.setTitle('退出登录')
    } else {
      this.btnText = '取消关注'
      Utils.setTitle('取消关注')
    }
  }
}
</script>
<style lang="scss" scoped>
.follow-manage {
  width: 100%;
  height: 100%;
  position: absolute;
  background-color: #212851;
  .btn {
    width: 90%;
    margin: 0.625rem auto;
    font-size: 0.4375rem;
    text-align: center;
    background: #2ac4fb;
    height: 1.25rem;
    line-height: 1.25rem;
    color: #fff;
  }
}
</style>
