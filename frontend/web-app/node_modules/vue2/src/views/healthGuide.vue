<template>
  <div style="width:100%;height:100%;">
    <div class="foodRecommend">
      <div class="top">
        <div class="title" v-if="isSelf">该管理依据您的健康档案来进行定制</div>
        <div class="title" v-else>该管理依据其健康档案来进行定制</div>
        <div class="prove" @click="goDocument()" v-if="isSelf">完善档案</div>
        <div class="prove" @click="goDocument()" v-else>查看档案</div>
      </div>
      <div class="header">
        <div class="recommendTitle">· 饮食推荐 ·</div>
        <div class="energy">
          <div class="total">总热量</div>
          <div class="num">{{nutritionList.energy}}</div>
          <div class="total">千卡</div>
        </div>
        <div class="percentage">脂肪 {{nutritionList.fatRate*100}}% 蛋白质 {{nutritionList.proteinRate*100}}% 碳水化合物 {{nutritionList.carboRate*100}}%</div>
        <swiper :options="swiperOption">
          <swiper-slide v-for="food in foodList">
            <div class="foodList" @click="goScheme()">
              <div class="pic" :style="{'background-image':'url(' + food.foodUrl +')'}">
                <div class="circle">{{food.time}}</div>
              </div>
              <div class="energyNum"><span class="fontNum">{{food.num}}</span><span class="unit">千卡</span></div>
              <div class="surgarIndex">升糖指数{{food.sugar}}</div>
            </div>
          </swiper-slide>
        </swiper>
      </div>
      <div class="preference">· 为你优选 ·</div>
      <div class="infinite-scroll" v-infinite-scroll="getArticleList" infinite-scroll-disabled="loading" infinite-scroll-distance="10">
        <a v-for="item in preferenceList" :href="item.linkUrl">
          <div class="preferenceList"  :style="{'background-image': 'url(' + item.imgUrl + ')'}">
            <div class="bac"></div>
            <div class="preferenceContent">
              <div class="listTitle">{{item.title}}</div>
              <div class="lead">{{item.lead}}</div>
            </div>
          </div>
        </a>
      </div>
    </div>
    <div class="healthManage" v-if="ifShowGuide">
      <div @touchmove="preventMouse($event)">
        <div class="bac"></div>
        <div class="close" @click="closeGuide"></div>
        <div class="content">
          <swiper :options="swiperOption1">
            <swiper-slide>
              <div class="guide">
                <div class="selectTitle">性别</div>
                <div class="left" @click="clickChoose">
                  <div class="selectBtn" v-if="!ifChoose"><img src="../images/bt_selected.png"></div>
                  <div class="manPic" v-if="ifChoose"><img src="../images/pic_man_defaul.png"></div>
                  <div class="manPic" v-if="!ifChoose"><img src="../images/pic_man_selected.png"></div>
                  <div class="sexName">男</div>
                </div>
                <div class="right" @click="clickChoose">
                  <div class="selectBtn" v-if="ifChoose"><img src="../images/bt_selected.png"></div>
                  <div class="womanPic" v-if="!ifChoose"><img src="../images/pic_woman_default.png"></div>
                  <div class="womanPic" v-if="ifChoose"><img src="../images/pic_woman_selected.png"></div>
                  <div class="sexName">女</div>
                </div>
              </div>
              <div class="nextBtn" @click="nextSlot(-1)">下一步</div>
            </swiper-slide>
            <swiper-slide v-for="(item,index) in selectList">
              <div class="guide">
                <div class="pic" :style="{'background-image':'url(' + item.selectImageUrl +')'}"></div>
                <div class="selectTitle">请选择{{item.title}}</div>
                <div class="pickerNum">
                  <mt-picker :itemHeight="itemHeight" :slots="item.slots" @change="onValuesChange"></mt-picker>
                </div>
                <div class="unit">{{item.unit}}</div>
              </div>
              <div class="nextBtn" @click="nextSlot(index)">下一步</div>
            </swiper-slide>
          </swiper>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
// 引入组件以及公共文件
import Utils from '../utils'
import Api from '../api'
import Vue from 'vue'
import ageUrl from '../images/ic_woman.png'
import heightUrl from '../images/ic_height.png'
import weightUrl from '../images/ic_weight.png'
import breUrl from '../images/ic_bra.png'
import breAddUrl from '../images/ic_bra_add.png'
import lunchUrl from '../images/ic_lun.png'
import lunchAddUrl from '../images/ic_lun_add.png'
import dinnerUrl from '../images/ic_din.png'
import dinnerAddUrl from '../images/ic_din_add.png'
import {
  Picker
} from 'mint-ui'

import {
  swiper,
  swiperSlide
} from 'vue-awesome-swiper'

Vue.component(Picker.name, Picker)
// var self
  // 实例化vue
export default {
  components: {
    swiper,
    swiperSlide
  },

  data() {
    return {
      newHeight: '',
      newWeight: '',
      newSex: 1,
      newAge: '',
      itemHeight: 0,
      nutritionList: '',
      ifShowGuide: false,
      swiperOption: {
        pagination: '.swiper-pagination',
        slidesPerView: 3,
        spaceBetween: 0
      },
      foodList: [],
      preferenceList: [],
      ifChoose: false,
      selectList: [{
        selectImageUrl: ageUrl,
        title: '年龄',
        unit: '岁',
        slots: [{
          flex: 1,
          values: [],
          className: 'slot1',
          textAlign: 'center',
          defaultIndex: 0
        }]
      }, {
        selectImageUrl: heightUrl,
        title: '身高',
        unit: '厘米',
        slots: [{
          flex: 2,
          values: ['149', '50', '51', '52', '53', '54'],
          className: 'slot1',
          textAlign: 'center',
          defaultIndex: 2
        }]
      }, {
        selectImageUrl: weightUrl,
        title: '体重',
        unit: '公斤',
        slots: [{
          flex: 3,
          values: ['149', '150', '151', '152', '153', '154'],
          className: 'slot1',
          textAlign: 'center',
          defaultIndex: 2
        }]
      }],
      swiperOption1: {
        pagination: '.swiper-pagination',
        nextButton: '.nextBtn',
        spaceBetween: 30,
        onSlideChangeStart: function(swiper) {
          // console.log(swiper.activeIndex)
          // self.onValuesChange()
        }
      },
      patientId: '',
      date: '',
      cPage: 1,
      isSelf: '',
      headImgUrl: ''
    }
  },

  methods: {
    getRecommend: function() {
      Utils.post(Api.DIET_SCHEME, {
        patientId: this.patientId,
        date: this.date
      }, result => {
        this.nutritionList = result.data
        this.foodList.push({
          'num': this.nutritionList.breakfast.energy,
          'sugar': this.nutritionList.breakfast.stapleGI,
          'time': '早',
          'foodUrl': breUrl
        })
        if (this.nutritionList.afterBreakfast) {
          this.foodList.push({
            'num': this.nutritionList.afterBreakfast.energy,
            'sugar': this.nutritionList.afterBreakfast.stapleGI,
            'time': '加',
            'foodUrl': breAddUrl
          })
        }
        this.foodList.push({
          'num': this.nutritionList.lunch.energy,
          'sugar': this.nutritionList.lunch.stapleGI,
          'time': '中',
          'foodUrl': lunchUrl
        })
        if (this.nutritionList.afterLunch) {
          this.foodList.push({
            'num': this.nutritionList.afterLunch.energy,
            'sugar': this.nutritionList.afterLunch.stapleGI,
            'time': '加',
            'foodUrl': lunchAddUrl
          })
        }
        this.foodList.push({
          'num': this.nutritionList.supper.energy,
          'sugar': this.nutritionList.supper.stapleGI,
          'time': '晚',
          'foodUrl': dinnerUrl
        })
        if (this.nutritionList.afterSupper) {
          this.foodList.push({
            'num': this.nutritionList.afterSupper.energy,
            'sugar': this.nutritionList.afterSupper.stapleGI,
            'time': '加',
            'foodUrl': dinnerAddUrl
          })
        }
      })
    },
    goScheme: function() {
      this.$router.push({
        name: 'diet',
        query: { patientId: this.patientId }
      })
    },
    preventMouse: function(event) {
      event.preventDefault()
    },
    goDocument: function() {
      this.$router.push({
        name: 'document',
        query: { patientId: this.patientId, isSelf: this.isSelf, headImgUrl: this.headImgUrl }
      })
    },
    nextSlot: function(index) {
      if (index === 2) {
        if (this.ifChoose) {
          this.newSex = 2
        }
        Utils.post(Api.PUT_PROFILE, {
          'pId': this.patientId,
          'height': this.newHeight,
          'weight': this.newWeight,
          'birthday': this.newAge,
          'sex': this.newSex
        }, result => {
          this.getRecommend()
        })
        this.ifShowGuide = false
      }
    },
    closeGuide: function() {
      this.ifShowGuide = false
      document.getElementsByClassName('getBody')[0].style.overflow = 'auto'
    },
    clickChoose: function() {
      this.ifChoose = !this.ifChoose
    },
    onValuesChange(picker, values) {
      console.log(picker.slots[0].flex)
      // console.log(values[0])
      if (picker.slots[0].flex === 1) {
        this.newAge = (2017 - values[0]) + '0101'
        console.log(this.newAge)
      }
      if (picker.slots[0].flex === 2) {
        this.newHeight = values[0]
      }
      if (picker.slots[0].flex === 3) {
        this.newWeight = values[0]
      }
    },
    getArticleList: function() {
      let data = {
        pId: this.patientId,
        cPage: this.cPage,
        size: 3
      }
      Utils.post(Api.GET_ARTICLES, data, result => {
        let dataList = result.dataList
        for (let i = 0; i < dataList.length; i++) {
          let item = dataList[i]
          this.preferenceList.push({
            title: item.title,
            lead: item.summary,
            imgUrl: item.imgUrl,
            linkUrl: item.linkUrl
          })
        }
        this.cPage ++
      })
    },
    getArr: function(arrLength) {
      var arr = new Array(arrLength)
      var i = arr.length
      while (i--) {
        arr[i] = i
      }
      return arr
    }
  },

  created: function() {
    let query = this.$route.query
    this.patientId = query.patientId
    this.isSelf = query.isSelf
    this.headImgUrl = query.headImgUrl
  },

  mounted: function() {
    this.selectList[0].slots[0].values = this.getArr(130)
    this.selectList[0].slots[0].defaultIndex = 45
    this.selectList[1].slots[0].values = this.getArr(201)
    this.selectList[1].slots[0].defaultIndex = 160
    this.selectList[2].slots[0].values = this.getArr(100)
    this.selectList[2].slots[0].defaultIndex = 60
    Utils.setTitle('健康管理')
    Utils.post(Api.GET_PROFILE, {
      'pId': this.patientId
      // 'pId': this.patientId,
      // 'bStId': '0000001'
    }, result => {
      var profile = result.healthProfile
      if (!profile.sex || !profile.weight || !profile.height || !profile.birthday) {
        this.ifShowGuide = true
      }
    })
    this.date = Utils.formatedDate(0)[0] + Utils.formatedDate(0)[1] + Utils.formatedDate(0)[2]
    var spaceWidth = document.getElementsByTagName('html')[0].style.fontSize
    var spaceWidth1 = 100 / 64 * parseInt(spaceWidth)
    this.itemHeight = spaceWidth1
    this.getRecommend()
  }
}
</script>
<style>
.healthManage .picker-item{
  font-size: 1.375rem;
  color: #2ac4fb;
  opacity: 0;
}
.healthManage .picker-item.picker-selected{
  color: #2ac4fb;
  opacity: 1;
}
.healthManage  .picker-selected + .picker-item ,.healthManage .picker-selected-pre{
      font-size: 1.03125rem;
      height: 1.59375rem !important;
      line-height: 1.59375rem !important;
      opacity: 0.3 !important;
    }

.healthManage  .picker-selected + .picker-item + .picker-item,.healthManage .picker-selected-pre-pre{
  font-size: 0.515625rem;
  height: 0.7rem !important;
  line-height: 0.7rem !important;
  opacity: 0.1 !important;
}
.healthManage .picker-slot-wrapper{
  height: 6rem !important;
}
</style>
<style scoped lang="scss">
@import '../scss/common';
@import '../scss/vue-swipe.css';
.foodRecommend {
  width: 100%;
  height: 100%;
  .top {
    width: 100%;
    height: 1.25rem;
    background-color: #2ac4fb;
    line-height: 1.25rem;
    display: flex;
    flex-direction: row;
    .title {
      color: white;
      margin-left: 6%;
      font-size: 0.40625rem;
    }
    .prove {
      width: 1.875rem;
      margin-left: 4%;
      margin-top: 0.3125rem;
      height: 0.625rem;
      line-height: 0.625rem;
      text-align: center;
      background-color: white;
      color: #2ac4fb;
      font-size: 0.3125rem;
    }
  }
  .header {
    width: 100%;
    overflow: hidden;
    background-image: url(../images/pic_ystj.png);
    background-size: 100% 100%;
    .recommendTitle {
      width: 100%;
      margin-top: 0.46875rem;
      text-align: center;
      font-weight: bold;
      font-size: 0.40625rem;
    }
    .energy {
      width: 100%;
      margin-top: 0.46875rem;
      display: flex;
      flex-direction: row;
      justify-content: center;
      .total {
        font-size: 0.40625rem;
        padding-top: 0.9375rem;
      }
      .num {
        font-size: 1.5625rem;
      }
    }
    .percentage {
      width: 100%;
      margin-bottom: 0.5625rem;
      text-align: center;
      font-size: 0.3125rem;
    }
    .foodList {
      // width: -webkit-calc(100%/3);
      height: 3.125rem;
      margin-left: 25%;
      float: left;
      .pic {
        width: 1.5625rem;
        height: 1.5625rem;
        margin: 0 auto;
        // background-image: url(../images/ic_bra.png);
        background-size: 100% 100%;
        display: flex;
        align-items: center;
        .circle {
          width: 0.78125rem;
          text-align: center;
          line-height: 0.78125rem;
          font-size: 0.46875rem;
          font-weight: bold;
          height: 0.78125rem;
          border-radius: 0.390625rem;
          opacity: 0.8;
          margin: 0 auto;
          background-color: white;
        }
      }
      .energyNum {
        width: 1.5625rem;
        height: 0.71875rem;
        line-height: 0.71875rem;
        margin: 0 auto;
        .fontNum {
          font-size: 0.46875rem;
        }
        .unit {
          font-size: 0.3125rem;
        }
      }
      .surgarIndex {
        padding: 0 0.0625rem;
        height: 0.5rem;
        line-height: 0.5rem;
        font-size: 0.3125rem;
        background-color: #2ac4fb;
        color: white;
        margin: 0 auto;
        border-radius: 0.03125rem;
      }
    }
  }
  .preference {
    width: 100%;
    height: 1.3125rem;
    line-height: 1.3125rem;
    text-align: center;
    font-size: 0.40625rem;
    font-weight: bold;
  }
  .preferenceList {
    width: 100%;
    height: 3.75rem;
    margin-bottom: 0.3125rem;
    position: relative;
    background-image: url(../images/banner.png);
    background-size: 100%;
    display: flex;
    align-items: center;
    color: #000;
    .bac {
      width: 100%;
      height: 100%;
      background-color: grey;
      opacity: 0.2;
      z-index: 2;
      position: absolute;
    }
    .preferenceContent {
      width: 80%;
      height: 2.7rem;
      margin-left: 10%;
      display: inline-block;
      vertical-align: middle;
      z-index: 2;
      background-color: white;
      opacity: 0.8;
      position: relative;
      .listTitle {
        width: 75%;
        margin: 0 auto;
        text-align: center;
        margin-top: 0.46875rem;
        padding-bottom: 0.15625rem;
        font-size: 0.5rem;
        border-bottom: 1px solid grey;
        font-weight: bold;
        overflow:hidden;
        text-overflow:ellipsis;
        white-space: nowrap;
      }
      .lead {
        width: 85%;
        text-align: center;
        padding-top: 0.15625rem;
        margin: 0 auto;
        font-size: 0.3125rem;
        font-weight: bold;
      }
    }
  }
}

.healthManage {
   
  width: 100%;
  height: 100%;
  position: fixed;
  z-index: 10;
  top: 0;
  .bac {
    width: 100%;
    height: 100%;
    background-color: black;
    opacity: 0.5;
    z-index: 1;
    position: absolute;
  }
  .close {
    width: 0.46875rem;
    height: 0.46875rem;
    right: 5%;
    top: 0.8rem;
    position: absolute;
    z-index: 20;
    background-image: url(../images/bt_close.png);
    background-size: 100% 100%;
  }
  .content {
    width: 78%;
    margin-left: 11%;
    position: absolute;
    margin-top: 1.5625rem;
    z-index: 3;
    .guide {
      width: 100%;
      height: 10.9375rem;
      background-color: white;
      border-radius: 3px;
      background-image: url(../images/bj_jkgl.png);
      background-size: 100% 100%;
      .left {
        width: -webkit-calc(50% - 1px);
        float: left;
        margin-top: 3.75rem;
        color: #2ac4fb;
        .selectBtn {
          width: 0.40625rem;
          left: 41%;
          height: 0.40625rem;
          position: absolute;
          img {
            width: 100%;
            height: 100%;
          }
        }
        .manPic {
          width: 100%;
          height: 100%;
          border-right: 1px solid #95E2FF;
          img {
            width: 60%;
            margin-left: 0.9375rem;
            height: 100%;
          }
        }
        .sexName {
          width: 100%;
          margin-top: 13%;
          text-align: center;
          font-size: 0.40625rem;
        }
      }
      .right {
        width: 50%;
        margin-top: 3.75rem;
        color: #2ac4fb;
        float: left;
        .selectBtn {
          width: 0.40625rem;
          right: 6%;
          height: 0.40625rem;
          position: absolute;
          img {
            width: 100%;
            height: 100%;
          }
        }
        .womanPic {
          width: 100%;
          height: 100%;
          float: left;
          img {
            width: 60%;
            margin-left: 17%;
            height: 100%;
          }
        }
        .sexName {
          width: 100%;
          float: left;
          margin-top: 13%;
          text-align: center;
          font-size: 0.40625rem;
        }
      }
      .firstPic {
        width: 40%;
        float: left !important;
        margin-left: 10%;
        margin-right: 8%;
        height: 100px;
        position: relative;
        border-right: 1px solid #2ac4fb;
        top: 130px;
        background-image: url(../images/pic_man_defaul.png);
        background-size: 80% 100%;
        background-repeat: no-repeat;
      }
      .selectTitle {
        width: 50%;
        margin-left: 25%;
        top: 1.875rem;
        font-size: 0.46875rem;
        text-align: center;
        position: absolute;
        z-index: 5;
        color: #2ac4fb;
      }
      .pic {
        width: 50%;
        height: 9.140625rem;
        position: relative;
        top: 1.40625rem;
        float: left;
        background-size: 100% 100%;
      }
      .pickerNum {
        width: 33%;
        position: relative;
        top: 3rem;
        float: left;
      }
      .unit {
        width: 17%;
        height: 100%;
        line-height: 12.5rem;
        font-size: 0.40625rem;
        float: left;
        color: #2ac4fb;
      }
    }
    .nextBtn {
      width: 100%;
      height: 1.25rem;
      line-height: 1.25rem;
      color: white;
      background-color: #2ac4fb;
      margin-top: 0.9375rem;
      border-radius: 2px;
      text-align: center;
      font-size: 0.5rem;
    }
  }
}
</style>
