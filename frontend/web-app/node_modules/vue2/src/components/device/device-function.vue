<template>
  <device-toast ref="deviceToast"></device-toast>
</template>
<script>
import Utils from '../../utils'
import Api from '../../api'
import DeviceToast from './device-toast'
// import {
//   Toast
// } from 'mint-ui'
export default {
  components: {DeviceToast},
  data() {
    return {
      brandType: '',
      WECHAT: {}
    }
  },
  mounted: function() {
    let query = this.$route.query
    this.WECHAT = this.Cookie.getJSON('WECHAT')
    this.brandType = query.brandType
  },
  methods: {
    checkScanResult: function(imei) {
      console.log(imei)
      // Toast({
      //   message: 'post',
      //   className: 'aaa'
      // })
      let that = this
      let data = {
        'imei': imei,
        'deviceBrandType': this.brandType
      }
      Utils.post(Api.GET_DEVICE_STATUS, data, result => {
        // let that = this
        if (result.success) {
          let rData = result.data
          // 设备号错误
          if (rData.deviceStatusEnum === 'WRONG_DEVICE') {
            this.$refs['deviceToast'].show({
              status: 'WRONG_DEVICE'
            })
            return
          } else if (rData.deviceStatusEnum === 'BOUND') {
            // 已绑定
            let isSelf = rData.userId === this.WECHAT.userId
            this.$refs['deviceToast'].show({
              status: 'BOUND',
              submitBtnText: isSelf ? '已绑定' : '去关注',
              isSelf: isSelf,
              userName: rData.userName,
              // boundDateTime: rData.boundDateTime,
              submit: function(res) {
                if (that.WECHAT.userId) {
                  if (!isSelf) {
                    // that.bindDevice(imei)
                    that.$router.push({
                      name: 'friends',
                      query: { name: 'bindDevice', userName: rData.userName }
                    })
                  }
                } else {
                  that.$router.push({
                    name: 'binding',
                    query: {
                      userName: rData.userName
                    }
                  })
                }
              }
            })
            return
          } else {
            // 未绑定
            this.$refs['deviceToast'].show({
              status: 'NOT_BOUND',
              submitBtnText: this.WECHAT.userId ? '绑定' : '去绑定',
              submit: function(res) {
                if (that.WECHAT.userId) {
                  that.bindDevice(imei)
                } else {
                  that.$router.push({
                    name: 'binding',
                    query: { brandType: that.brandType, imei: imei }
                  })
                }
              }
            })
            return
          }
        } else {
          this.$refs['deviceToast'].show({
            status: 'warning',
            text: 'error'
          })
        }
      })
    },
    bindDevice: function(imei) {
      let that = this
      let data = {
        imei: imei,
        uId: this.WECHAT.userId,
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
    }
  }
}
</script>
<style scoped lang="scss">
</style>
