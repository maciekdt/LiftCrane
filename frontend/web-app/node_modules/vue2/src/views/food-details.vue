<template>
  <div class="foodDetails">
    <div class="banner" :style="{'background-image': imgUrl}">
      <div class="bannerBac">
        <div class="foodName">{{foodList.dishName}}</div>
        <div class="energy">{{foodList.energy}}千卡</div>
      </div>
    </div>
    <div class="foodList" v-for="(item,index) in foodList.foods" v-bind:class="{'firstStyle': index == (foodList.length-1)}">
      <div class="title">{{item.foodName}} {{item.energy}}克</div>
      <div class="nutrition">碳水化合物 {{item.carboG}}克</div>
      <div class="nutrition">脂肪 {{item.fatG}}克</div>
      <div class="nutrition">蛋白质 {{item.proteinG}}克</div>
      <div class="nutrition">纤维素 {{item.fibreG}}克</div>
    </div>
  </div>
</template>
<script>
// 引入组件以及公共文件
// import Utils from '../utils'
// import Api from '../api'
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
      imgUrl: '',
      foodList: [{
        'name': '马铃薯 142克',
        'nutritionList': [{
          'detail': '碳水化合物 22克'
        }, {
          'detail': '脂肪 0克'
        }, {
          'detail': '蛋白质 2克'
        }, {
          'detail': '纤维素 0.99克'
        }]
      }, {
        'name': '豆角 142克',
        'nutritionList': [{
          'detail': '碳水化合物 22克'
        }, {
          'detail': '脂肪 0克'
        }, {
          'detail': '蛋白质 2克'
        }, {
          'detail': '纤维素 0.99克'
        }]
      }]
    }
  },

  watch: {},

  methods: {},

  created: function() {},

  mounted: function() {
    this.foodList = this.Storage.get('detailList')
    console.log(this.foodList)
    this.imgUrl = 'url(http://app.carelinker.com/hcms/NES/images/nutritions/' + this.foodList.id.substring(0, 2) + '/' + this.foodList.id.substring(2, this.foodList.id.length) + '.png)'
  }
}
</script>
<style scoped lang="scss">
@import '../scss/common';
@import '../scss/vue-swipe.css';
.foodDetails {
  width: 100%;
  height: 100%;
  position: absolute;
  background-color: #eaeff1;
  .banner {
    width: 100%;
    height: 4.375rem;
    background-image: url(../images/banner.png);
    background-size: 100% 100%;
    .bannerBac {
      width: 100%;
      height: 100%;
      background-image: url(../images/banner_shadow.png);
      background-size: 100% 100%;
      position: relative;
      .foodName {
        position: absolute;
        bottom: 0.46875rem;
        left: 0.46875rem;
        font-size: 0.40625rem;
        font-weight: bold;
        color: white;
      }
      .energy {
        font-size: 0.3125rem;
        color: white;
        position: absolute;
        bottom: 0.46875rem;
        right: 0.46875rem;
      }
    }
  }
  .foodList {
    width: 100%;
    padding-bottom: 0.46875rem;
    background-color: white;
    margin-top: -2px;
    .title {
      margin: 0 auto;
      width: 6.25rem;
      text-align: center;
      padding-top: 0.5rem;
      padding-bottom: 0.3125rem;
      border-bottom: 1px solid #2ac4fb;
      font-size: 0.40625rem;
      color: #2ac4fb;
      font-weight: bold;
    }
    .nutrition {
      margin: 0 auto;
      width: 6.25rem;
      font-size: 0.3125rem;
      text-align: center;
      color: #9b9b9b;
      padding-top: 0.3125rem;
    }
  }
  .firstStyle {
    padding-bottom: 0.9375rem;
  }
}
</style>
