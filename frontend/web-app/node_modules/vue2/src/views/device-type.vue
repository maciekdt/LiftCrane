<template>
  <div class="deviceType">
    <div class="header">
      <div class="link" v-for="(item,index) in items">
        <div class="title" v-bind:class="{'linkStyle':currentIndex==index}" @click="clickJump(index)">{{item.name}}</div>
      </div>
    </div>
    <div class="deviceContent" v-if="!ifBind">
      <div class="deviceNoBinds" v-for="(blood,index) in bloodList" :style="{'background-image': 'url(' + blood.productImgUrl   + ')'}" @click="goScan(blood)">
        <div class="content">
          <div class="contentNum">{{blood.model}}</div>
        </div>
      </div>
    </div>
    <div class="deviceContent" v-if="ifBind">
      <mt-cell-swipe class="deviceLeft" v-for="(blood,bloodIndex) in bloodList1" :right="[
        {
          content: bindText,
          style: { background: '#ed5564', color: '#fff',fontSize:'0.40625rem',width:'2.34375rem',textAlign:'center',height:'4.28125rem',paddingTop:'1.5rem'},
          handler: () => handler1(bloodIndex)
        }]">
        <div class="content" :style="{'background-image': 'url(' + blood.url + ')'}">
          <div class="contentNum" style="padding-left: 0.46875rem;">{{blood.model}}</div>
          <div class="contentNum1" style="padding-left: 0.46875rem;">绑定时间</div>
          <div class="contentNum1" style="padding-left: 0.46875rem;">{{blood.bindTime}}</div>
        </div>
      </mt-cell-swipe>
    </div>
  </div>
</template>
<script>
import Vue from 'vue'
import Utils from '../utils'
import Api from '../api'
import {
  CellSwipe,
  Toast,
  Indicator
} from 'mint-ui'

Vue.component(CellSwipe.name, CellSwipe)
export default {
  data() {
    return {
      resultList: '',
      bindText: '解除<br>绑定',
      ifBind: false,
      contentWidth: window.innerWidth,
      currentIndex: 0,
      items: [{
        name: '血压计'
      }, {
        name: '血糖仪'
      }, {
        name: '已绑'
      }],
      bloodList: [],
      bloodList1: [],
      bloodList2: [],
      bloodList3: [],
      typeAndImg: {},
      typeAndModel: {},
      userId: ''
    }
  },
  mounted: function() {
    Indicator.open()
    let WECHAT = this.Cookie.getJSON('WECHAT')
    this.userId = WECHAT.userId
    let api = Api.GET_DEVICES
    let data = {}
    // console.log(WECHAT)
    if (WECHAT && WECHAT.userId) {
      // window.alert('login')
      api = Api.GET_DEVICE_LIST
      data.uId = WECHAT.userId
    }
    Utils.post(api, data, result => {
      this.setShowViewList(result)
      Indicator.close()
    })
  },
  methods: {
    setShowViewList: function(result) {
      this.bloodList = []
      this.bloodList1 = []
      this.bloodList2 = []
      this.bloodList3 = []
      this.resultList = result
      for (let i = 0; i < this.resultList.personalDeviceList.length; i++) {
        let item = this.resultList.personalDeviceList[i]
        if (item.name === '无线智能血压计') {
          this.bloodList2.push(item)
        } else {
          this.bloodList3.push(item)
        }
        this.typeAndImg[item.brandType] = item.productImgUrl
        this.typeAndModel[item.brandType] = item.model
      }
      let userDeviceList = []
      if (this.resultList.userDeviceList) {
        for (let i = 0; i < this.resultList.userDeviceList.length; i++) {
          let item = this.resultList.userDeviceList[i]
          userDeviceList.push({
            imei: item.imei,
            deviceBrandType: item.brandType,
            bindTime: item.bindTime,
            model: this.typeAndModel[item.brandType],
            url: this.typeAndImg[item.brandType]
          })
        }
      }
      this.bloodList1 = userDeviceList
      this.bloodList = this.bloodList2
    },
    goScan: function(item) {
      // console.log(item)
      this.$router.push({
        name: 'scan',
        query: { brandType: item.brandType }
      })
    },
    handler1: function(index) {
      console.log(index)
      let imei = this.bloodList1[index].imei
      let deviceBrandType = this.bloodList1[index].deviceBrandType
      Utils.post(Api.DELETE_DEVICE_LIST, {
        'uId': this.userId,
        'imei': imei,
        'deviceBrandType': deviceBrandType
      }, result => {
        if (result.success) {
          Toast({
            message: '解绑成功！',
            position: 'middle',
            duration: 2000
          })
          Indicator.open()
          Utils.post(Api.GET_DEVICE_LIST, {
            'uId': this.userId
          }, result => {
            this.setShowViewList(result)
            Indicator.close()
          })
        }
      })
    },
    clickJump: function(index) {
      this.currentIndex = index
      if (index === 0) {
        this.ifBind = false
        this.bloodList = this.bloodList2
      } else if (index === 1) {
        this.ifBind = false
        this.bloodList = this.bloodList3
      } else if (index === 2) {
        this.ifBind = true
      }
    }
  }
}
</script>
<style>
  .mint-toast-text {
    font-size: 0.46875rem;
  }
  .mint-indicator-wrapper {
    padding: 0.46875rem !important;
  }
</style>
<style scoped lang="scss">
.header {
  padding: 0 1.78125rem;
  display: flex;
  height: 1.25rem;
  line-height: 1.25rem;
  flex-direction: row;
  justify-content: space-between;
  font-size: 0.40625rem;
  border-bottom: 0.015625rem solid #DDDDDD;
  .linkStyle {
    color: #2ac4fb;
    font-size: 0.4375rem;
    border-bottom: 0.0625rem solid #2ac4fb;
  }
}

.deviceContent {
  width: 100%;
  height: 4.28125rem;
  .deviceNoBinds {
    width: 100%;
    height: 4.28125rem;
    line-height: 4.28125rem;
    background-image: url(../images/BL-B918.png);
    background-size: 100% 100%;
    border-bottom: 0.015625rem solid #f2f2f2;
    .content {
      padding-left: 0.46875rem;
      font-size: 0.46875rem;
    }
  }
  .deviceLeft {
    height: -webkit-calc(4.28125rem + 0.015625rem);
    border-bottom: 0.015625rem solid #f2f2f2;
    .content {
      width: 10rem;
      color: black;
      font-size: 0.46875rem;
      // background-image: url(../images/BL-B918.png);
      background-size: 100% 100%;
      height: 4.28125rem;
      .contentNum {
        width: 100%;
        margin-top: 1.2rem;
      }
      .contentNum1 {
        width: 100%;
        font-size: 0.4375rem;
        margin-top: 0.34375rem;
      }
    }
  }
}
</style>
<style>
 .deviceContent .mint-cell-wrapper{
    width: 100%;
    padding: 0;
  }
</style>
