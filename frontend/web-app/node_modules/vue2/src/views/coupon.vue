<template>
  <div class="coupons">
    <div class="couponList" v-for="(couponItem,couponIndex) in couponList" v-bind:class="{'invalidStyle': couponItem.color == 1}" :style="{ 'background-image': 'url(' + couponItem.bgUrl +')'}">
      <div class="couponLeft">
        <div class="top clearfix">
          <div class="value">{{couponItem.value}}</div>
          <div class="yuan">元</div>
          <div class="new" v-bind:class="{'invalidStyle1': couponItem.color == 1}">新人礼包</div>
        </div>
        <div class="inspire">激励会员卡奖励</div>
      </div>
      <div class="couponRight">
        <div class="immediately" v-if="couponItem.color == 0">立即使用</div>
        <div class="immediately" v-if="couponItem.color == 1">已过期</div>
        <div class="valid" v-bind:class="{'invalidStyle1': couponItem.color == 1}">有效2017年3月1日至2018年3月1日</div>
      </div>
    </div>
  </div>
</template>
<script>
// 引入组件以及公共文件
// import Utils from '../utils'
// import Api from '../api'
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
      bgUrl: '',
      couponList: [{
        value: '5',
        color: 0,
        bgUrl: 'http://cs.carelinker.com/image/store-wechat-setup/8096000/coupon-enabled-bg.png'
      }, {
        value: '10',
        color: 0,
        bgUrl: 'http://cs.carelinker.com/image/store-wechat-setup/8096000/coupon-enabled-bg.png'
      }, {
        value: '15',
        color: 1,
        bgUrl: 'http://cs.carelinker.com/image/store-wechat-setup/8096000/coupon-disabled-bg.png'
      }, {
        value: '5',
        color: 1,
        bgUrl: 'http://cs.carelinker.com/image/store-wechat-setup/8096000/coupon-disabled-bg.png'
      }, {
        value: '5',
        color: 1,
        bgUrl: 'http://cs.carelinker.com/image/store-wechat-setup/8096000/coupon-disabled-bg.png'
      }]
    }
  },

  methods: {},

  created: function() {},

  mounted: function() {
    this.bgUrl = this.Storage.get('useInfo').imgUrlMap.couponEnabledBg
    // Utils.post(Api.MEMBER_CARD, {
    //   'mainStoreId': '8096000'
    // }, result => {
    //   this.bgUrl = result.data.imgUrlMap.couponEnabledBg
    // })
  }
}
</script>
<style scoped lang="scss">
@import '../scss/common';
@import '../scss/vue-swipe.css';
.coupons {
  width: 100%;
  height: 100%;
  background-color: white;
  padding-bottom: 1.5625rem;
}

.top {
  margin-top: 0.46875rem;
}

.couponList {
  width: 90%;
  margin:0.46875rem 0.46875rem 0;
  height: 3.125rem;
  background-size: 100% 100%;
}

.couponLeft {
  width: 60%;
  height: 100%;
  float: left;
  color: white;
}

.couponRight {
  width: 40%;
  height: 100%;
  float: left;
}

.value {
  font-size: 1.40625rem;
  margin-left: 0.75rem;
  display: inline-block;
}

.yuan {
  font-size: 0.6875rem;
  display: inline-block;
}

.new {
  display: inline-block;
  width: 1.5625rem;
  line-height: 0.5rem;
  text-align: center;
  color: green;
  font-size: 0.3125rem;
  background-color: white;
}

.inspire {
  margin-left: 0.78125rem;
  font-size: 0.5rem;
}

.immediately {
  width: 2.59375rem;
  margin: 0 auto;
  border-radius: 0.40625rem;
  height: 0.8125rem;
  line-height: 0.8125rem;
  margin-top: 0.6875rem;
  text-align: center;
  color: white;
  font-size: 0.40625rem;
  border: 0.03125rem solid white
}

.valid {
  width: 3.125rem;
  margin: 0 auto;
  text-align: left;
  margin-top: 0.15625rem;
  font-size: 0.34375rem;
  color: green;
}

.invalidStyle {
  background-image: url(../images/pic_yhqgq.png);
}

.invalidStyle1 {
  color: grey;
  opacity: 0.8;
}
</style>
