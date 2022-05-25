<template>
  <div class="dietScheme">
    <div class="top">
      <div class="date" v-for="(date,dateIndex) in dateList" @click="clickDate(dateIndex)">
        <div v-bind:class="{'selectStyle':dateIndex==currentIndex}">{{date.name}}</div>
      </div>
    </div>
    <div class="dietList" v-for="(diet,index) in dietList">
      <div class="listTop">
        <div class="dietName">{{diet.dietName}}</div>
        <div class="rightTip" v-if="dietList[index].foodList.length>3">向右滑动查看更多</div>
      </div>
      <div class="list">
        <swiper :options="swiperOption">
          <swiper-slide v-for="(item,itemIndex) in diet.foodList">
            <div class="foodDetal" @click="goDetail(index,itemIndex)">
              <div class="foodPic" :style="{'background-image': 'url(http://app.carelinker.com/hcms/NES/images/nutritions/' + item.id.substring(0, 2) + '/' + item.id.substring(2, item.id.length) + '.png)'}"></div>
              <div class="foodName">{{item.dishName}}</div>
              <div class="foodEnergy">{{item.energy}}千卡</div>
            </div>
          </swiper-slide>
        </swiper>
      </div>
    </div>
  </div>
</template>
<script>
// 引入组件以及公共文件
import Utils from '../utils'
import Api from '../api'
import {
  swiper,
  swiperSlide
} from 'vue-awesome-swiper'
import {
  Indicator
} from 'mint-ui'
// 实例化vue
export default {
  components: {
    swiper,
    swiperSlide
  },

  data() {
    return {
      today: new Date(),
      currentIndex: 0,
      swiperOption: {
        pagination: '.swiper-pagination',
        slidesPerView: 'auto',
        paginationClickable: true,
        spaceBetween: 0
      },
      dietList: [],
      dietInfo: '',
      dateList: [{
        'name': '今天'
      }, {
        'name': '明天'
      }, {
        'name': '后天'
      }],
      patientId: ''
    }
  },

  watch: {},

  methods: {
    getNeedDate: function(date) {
      var y = date.getFullYear()
      var m = date.getMonth() + 1
      m = m < 10 ? '0' + m : m
      var d = date.getDate()
      d = d < 10 ? ('0' + d) : d
      return y.toString() + m.toString() + d.toString()
    },
    setFoodList: function() {
      this.dietList.push({
        'dietName': '早餐（热量' + this.dietInfo.breakfast.energy + '千卡，升糖指数' + this.dietInfo.breakfast.stapleGI + '）',
        'foodList': this.dietInfo.breakfast.dishes
      })
      if (this.dietInfo.afterBreakfast) {
        this.dietList.push({
          'dietName': '加餐（热量' + this.dietInfo.afterBreakfast.energy + '千卡，升糖指数' + this.dietInfo.afterBreakfast.stapleGI + '）',
          'foodList': this.dietInfo.afterBreakfast.dishes
        })
      }
      this.dietList.push({
        'dietName': '中餐（热量' + this.dietInfo.lunch.energy + '千卡，升糖指数' + this.dietInfo.lunch.stapleGI + '）',
        'foodList': this.dietInfo.lunch.dishes
      })
      if (this.dietInfo.afterLunch) {
        this.dietList.push({
          'dietName': '加餐（热量' + this.dietInfo.afterLunch.energy + '千卡，升糖指数' + this.dietInfo.afterLunch.stapleGI + '）',
          'foodList': this.dietInfo.afterLunch.dishes
        })
      }
      this.dietList.push({
        'dietName': '晚餐（热量' + this.dietInfo.supper.energy + '千卡，升糖指数' + this.dietInfo.supper.stapleGI + '）',
        'foodList': this.dietInfo.supper.dishes
      })
      if (this.dietInfo.afterSupper) {
        this.dietList.push({
          'dietName': '加餐（热量' + this.dietInfo.afterSupper.energy + '千卡，升糖指数' + this.dietInfo.afterSupper.stapleGI + '）',
          'foodList': this.dietInfo.afterSupper.dishes
        })
      }
    },
    goDetail: function(index, itemIndex) {
      this.Storage.set('detailList', this.dietList[index].foodList[itemIndex])
      console.log(this.Storage.get('detailList'))
      this.$router.push({
        name: 'dietDetail'
      })
    },
    clickDate: function(index) {
      Indicator.open()
      this.currentIndex = index
      this.dietList = []
      Utils.post(Api.DIET_SCHEME, {
        patientId: this.patientId,
        date: parseInt(this.getNeedDate(this.today)) + index
      }, result => {
        this.dietInfo = result.data
        this.setFoodList()
        Indicator.close()
      })
    }
  },

  created: function() {
    let query = this.$route.query
    this.patientId = query.patientId
    var spaceWidth = document.getElementsByTagName('html')[0].style.fontSize
    var spaceWidth1 = 20 / 64 * parseInt(spaceWidth)
    this.swiperOption.spaceBetween = spaceWidth1
  },

  mounted: function() {
    Indicator.open()
    Utils.post(Api.DIET_SCHEME, {
      patientId: this.patientId,
      date: this.getNeedDate(this.today)
    }, result => {
      this.dietInfo = result.data
      this.setFoodList()
      Indicator.close()
    })
  }
}
</script>
<style>
  .mint-indicator-wrapper {
    padding: 0.46875rem !important;
    z-index: 99;
  }
  .mint-spinner-snake {
    height: 0.78125rem !important;
    width: 0.78125rem !important;
    border: 0.125rem solid transparent;
  }
</style>
<style scoped lang="scss">
@import '../scss/common';
@import '../scss/vue-swipe.css';
.swiper-slide {
  width: 4.21875rem;
}

.swiper-slide:nth-child(2n) {
  width: 4.21875rem;
}

.swiper-slide:nth-child(3n) {
  width: 4.21875rem;
}

.dietScheme {
  width: 100%;
  height: 100%;
  .top {
    width: 100%;
    height: 1.1875rem;
    line-height: 1.1875rem;
    display: flex;
    flex-direction: row;
    text-align: center;
    border-bottom: 1px solid #f2f2f2;
    .date {
      width: -webkit-calc(100%/3);
      font-size: 0.40625rem;
    }
    .selectStyle {
      color: #2ac4fb;
      margin: 0 auto;
      width: 0.9375rem;
      height: 1.1875rem;
      font-weight: bold;
      border-bottom: 0.03125rem solid #2ac4fb;
    }
  }
  .dietList {
    width: 100%;
    padding-bottom: 0.3125rem;
    border-bottom: 0.3125rem solid #eaeff1;
    .listTop {
      width: 100%;
      height: 0.9375rem;
      line-height: 0.9375rem;
      border-bottom: 1px solid #f2f2f2;
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      .dietName {
        margin-left: 0.46875rem;
        font-size: 0.3125rem;
        font-weight: bold;
      }
      .rightTip {
        margin-right: 0.46875rem;
        font-size: 0.3125rem;
        color: #9b9b9b;
        font-weight: bold;
      }
    }
    .list {
      margin-left: 0.46875rem;
      // margin-right: 0.46875rem;
      .foodDetal {
        width: 100%;
        margin-top: 0.3125rem;
        border: 1px solid #f2f2f2;
        float: left;
        .foodPic {
          width: 100%;
          height: 1.8125rem;
          background-color: #f2f2f2;
          // background-image: url(../images/ic_din.png);
          background-size: 100% 100%;
        }
        .foodName {
          width: 100%;
          margin-top: 0.15625rem;
          margin-left: 0.15625rem;
          font-size: 0.40625rem;
          font-weight: bold;
        }
        .foodEnergy {
          width: 100%;
          margin-left: 0.15625rem;
          margin-bottom: 0.15625rem;
          font-weight: bold;
          font-size: 0.3125rem;
        }
      }
    }
  }
}
</style>
