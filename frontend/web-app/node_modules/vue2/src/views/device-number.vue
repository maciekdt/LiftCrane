<template>
  <div class="device-number">
    <div class="inputDiv">
      <input placeholder="请输入设备号" v-model="deviceNum">
    </div>
    <div class="scanBtn" @click="makeSure()">确定</div>
    <device-function ref="deviceFunction"></device-function>
  </div>
</template>
<script>
// import Utils from '../utils'
// import Api from '../api'
import DeviceFunction from '../components/device/device-function'
import {
  Toast
  // Indicator
} from 'mint-ui'
export default {
  components: {DeviceFunction},
  data() {
    return {
      deviceNum: ''
    }
  },
  mounted: function() {
  },
  methods: {
    makeSure: function() {
      if (this.deviceNum === '') {
        Toast({
          message: '请输入设备号',
          className: 'aaa'
        })
        return
      }
      this.$refs['deviceFunction'].checkScanResult(this.deviceNum)
    }
  }
}
</script>
<style scoped lang="scss">
.device-number{
  width: 100%;
  height: 100%;
  position: absolute;
  background-color: #212852;
  .inputDiv{
    width: 9.0625rem;
    height: 1.25rem;
    margin: 0 auto;
    margin-top: 1.40625rem;
    border-radius: 0.03125rem;
    input{
      width: -webkit-calc(100% - 0.3125rem);
      height: 1.25rem;
      line-height: 1.25rem;
      padding-left: 0.3125rem;
      font-size: 0.46875rem;
    }
  }
  .scanBtn{
    width: 9.0625rem;
    height: 1.25rem;
    line-height: 1.25rem;
    text-align: center;
    font-size: 0.5rem;
    margin: 0 auto;
    background-color: #2ac4fb;
    color: white;
    margin-top:0.625rem; 
    border-radius: 0.03125rem;
  }
}

</style>
