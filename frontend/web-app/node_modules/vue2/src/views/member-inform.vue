<template>
  <div class="member-apply">
    <div class="header">
      <swiper :options="swiperOption" ref="mySwiper">
        <!-- slides -->
        <swiper-slide v-for="memberCard in memberCardList">
          <div class="card" :style="{ 'background-image': 'url(' + bgUrl +')'}">
            <div class="top clearfix">
              <div class="left" :style="{ 'background-image': 'url(' + logoUrl +')'}"></div>
              <div class="middle">
                <div class="title">{{storeName}}</div>
                <div class="title">微信会员卡</div>
              </div>
              <div class="right"></div>
            </div>
            <div class="bottom">{{memberCard.msCardId}}</div>
          </div>
        </swiper-slide>
      </swiper>
    </div>
    <div class="type clearfix">
      <div class="typeName" v-for="(type,typeIndex) in typeList" v-bind:class="{'borderStyle':typeIndex < 2}" @click="goScore(typeIndex)">
        <div class="typeTitle">{{type.name}}</div>
        <div class="typeValue" :style="{ 'color': newColor }">{{type.value}}</div>
      </div>
    </div>
    <div class="tip" :style="{ 'color': newColor }" v-if="!ifCheck">随时随地 享受会员价</div>
    <div class="tip" :style="{ 'color': newColor }" v-if="ifCheck">会员信息审核中</div>
    <div class="cardListWarp">
      <div class="cardList clearfix" @click="goPage('healthManage')">
        <div class="listLeft"><i class="iconfont":style="{ 'color': newColor }">&#xe65e;</i></div>
        <div class="listMiddle">健康管理</div>
        <div class="rightIcon"><i class="iconfont">&#xe692;</i></div>
      </div>
      <div class="cardList clearfix" @click="goPage('record')" v-bind:class="{'greyStyle':ifGrey}">
        <div class="listLeft"><i class="iconfont":style="{ 'color': newColor }">&#xe65a;</i></div>
        <div class="listMiddle">购药记录</div>
        <div class="rightIcon"><i class="iconfont">&#xe692;</i></div>
      </div>
      <div class="cardList clearfix" @click="goPage('measureRecord')">
        <div class="listLeft"><i class="iconfont":style="{ 'color': newColor }">&#xe659;</i></div>
        <div class="listMiddle">检测记录</div>
        <div class="rightIcon"><i class="iconfont">&#xe692;</i></div>
      </div>
      <div class="cardList clearfix" @click="goPage('bindDevice')">
        <div class="listLeft"><i class="iconfont":style="{ 'color': newColor }">&#xe65b;</i></div>
        <div class="listMiddle">设备绑定</div>
        <div class="rightIcon"><i class="iconfont">&#xe692;</i></div>
      </div>
      <div class="cardList clearfix" @click="goPage('follow-list')">
        <div class="listLeft"><i class="iconfont":style="{ 'color': newColor }">&#xe65c;</i></div>
        <div class="listMiddle">账号管理</div>
        <div class="rightIcon"><i class="iconfont">&#xe692;</i></div>
      </div>
    </div>
  </div>
</template>
<script>
// 引入组件以及公共文件
// import Utils from '../utils'
// import Api from '../api'
// import Vue from 'vue'
import {
  swiper,
  swiperSlide
} from 'vue-awesome-swiper'

// 实例化vue
export default {
  components: {
    swiper,
    swiperSlide
  },

  data() {
    return {
      ifGrey: false,
      memberCardList: [{
        'msCardId': ''
      }],
      ifCheck: false,
      storeName: '',
      newColor: '',
      logoUrl: '',
      bgUrl: '',
      swiperOption: {
        pagination: '.swiper-pagination',
        slidesPerView: 'auto',
        centeredSlides: true,
        paginationClickable: true,
        spaceBetween: 30,
        autoHeight: true
      },
      typeList: [{
        name: '会员积分',
        goUrl: 'score',
        value: '-'
      }, {
        name: '储值金额',
        goUrl: 'score',
        value: '-'
      }, {
        name: '优惠券',
        goUrl: 'coupon',
        value: '-'
      }],
      patientId: ''
    }
  },

  methods: {
    goPage: function(pageName) {
      switch (pageName) {
        case 'healthManage':
          this.$router.push({
            name: 'follow-list',
            query: { 'name': pageName }
          })
          break
        case 'record':
          if (!this.ifCheck) {
            this.$router.push({
              name: 'follow-list',
              query: { 'name': pageName }
            })
          }
          break
        case 'measureRecord':
          // window.location.href = 'https://platform.carelinker.com/sc/web/wechat_charts/#/bp?pId=' + this.patientId
          this.$router.push({
            name: 'follow-list',
            query: { 'name': 'measureData' }
          })
          break
        case 'bindDevice':
          this.$router.push({
            name: pageName
          })
          break
        case 'follow-list':
          this.$router.push({
            name: pageName,
            query: { 'name': 'followerManage' }
          })
          break
      }
    },
    goScore: function(index) {
      this.patientId = this.Storage.get('patientId')
      if (window.location.hash.indexOf('hasChecked') > -1) {
        var tempType
        if (index === 0) {
          tempType = 'memberScore'
        } else if (index === 1) {
          tempType = 'money'
        }
        if (index !== 2) {
          this.$router.push({
            name: this.typeList[index].goUrl,
            params: {
              goType: tempType
            },
            query: {
              'scoreValue': this.typeList[0].value
            }
          })
        }
      }
    }
  },

  created: function() {},

  mounted: function() {
    console.log(this.$route.params)
    if (window.location.hash.indexOf('check') > -1) {
      this.ifCheck = true
      this.ifGrey = true
    }
    if (this.Storage.get('cardInfo')) {
      this.memberCardList = this.Storage.get('cardInfo')
      this.typeList[0].value = this.memberCardList[0].points
      this.typeList[1].value = 0
      this.typeList[2].value = 0
    }
    var temp = this.Storage.get('useInfo')
    this.bgUrl = temp.imgUrlMap.cardBg
    this.logoUrl = temp.imgUrlMap.logo
    this.newColor = temp.color
    this.storeName = temp.mainStoreName
  }
}
</script>
<style scoped lang="scss">
@import '../scss/common';
@import '../scss/vue-swipe.css';
.swiper-slide {
  width: 80%;
}

.swiper-slide:nth-child(2n) {
  width: 80%;
}

.swiper-slide:nth-child(3n) {
  width: 80%;
}

.member-apply {
  width: 100%;
  height: 100%;
  background-color: white;
}

.header {
  width: 100%;
  height: 4.96875rem;
  padding-top: 0.78125rem;
}

.card {
  width: 100%;
  height: 4.96875rem;
  border-radius: 0.09375rem;
  background-size: 100% 100%;
  position: relative;
}

.top {
  /* width: 100%;
  height: 3rem; */
  padding-top: 0.46875rem;
  padding-left: 0.46875rem;
}

.left {
  width: 1.09375rem;
  height: 1.09375rem;
  background-color: white;
  border-radius: 50%;
  float: left;
  background-size: 100%;
}

.middle {
  width: 50%;
  /* height: 100%; */
  color: white;
  float: left;
  margin-left: 0.234375rem;
  font-size: 0.4375rem;
}

.bottom {
  width: 90%;
  margin-left: 0.46875rem;
  color: white;
  font-size: 0.4375rem;
  position: absolute;
  bottom: 0.625rem;
}

.type {
  width: 100%;
  /* height: 3rem; */
  margin-top: 0.9375rem;
  font-size: 0.375rem;
}
.typeTitle{
  font-size: 0.5rem;
}
.typeName {
  width: -webkit-calc(100%/3 - 0.015625rem);
  float: left;
  text-align: center;
}

.typeValue {
  margin-top: 0.15625rem;
  font-size: 0.625rem;
}

.borderStyle {
  border-right: 0.015625rem solid #DBDADA;
}

.tip {
  width: 100%;
  margin-top: 0.625rem;
  font-size: 0.53125rem;
  text-align: center;
  opacity: 0.5;
  margin-bottom: 0.15625rem
}
.cardListWarp{
  padding:0 0.46875rem 0.78125rem;
}
.cardList {
  width: 100%;
  padding:0.46875rem 0;
  border-bottom: 0.015625rem solid #E4E2E2;
}

.listLeft {
  float: left;
  margin-top: 0.0625rem;
  margin-right: 0.3125rem
}

.iconfont {
  font-size: 0.625rem;
}

.listMiddle {
  font-size: 0.5rem;
  float: left;
}

.rightIcon {
  float: right;
  margin-top: 0.09375rem
}
.rightIcon .iconfont{
  font-size: 0.5rem
}

.greyStyle {
  color: grey !important;
}
</style>
