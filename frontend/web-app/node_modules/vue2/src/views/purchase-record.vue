<template>
  <div class="basicInform">
    <div class="item" v-for="item in informList">
      <div class="tel">
        <div class="medicine">{{item.drugName}}</div>
        <div class="buy-date">{{item.createTime}}</div>
      </div>
      <div class="insertTel" :style="{ 'color': newColor}">{{parseInt(item.buyNum)}}{{item.spec}}</div>
    </div>
  </div>
</template>
<script>
// 引入组件以及公共文件
import Utils from '../utils'
import Api from '../api'
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
      newColor: '',
      informList: []
    }
  },

  methods: {},

  created: function() {},

  mounted: function() {
    this.newColor = this.Storage.get('useInfo').color
    Utils.post(Api.PURCHASE_RECORD, {
      // 'mStId': this.Storage.get('mainStoreId'),
      'mStId': '9990000',
      // 'uNm': this.$route.query.tel'
      'uNm': '13001601117'
    }, result => {
      this.informList = result.dataList
    })
  }
}
</script>
<style scoped lang="scss">
@import '../scss/common';
@import '../scss/vue-swipe.css';
.basicInform{
  padding-bottom: 0.9375rem;
}
.item {
  box-sizing: border-box;
  width: 100%;
  height: 1.96875rem;
  font-size: 0.5rem;
  border-bottom: 0.015625rem solid #E9E6E6;
  padding: 0.375rem 0.3125rem;
  position: relative;
  overflow: hidden;
}

.tel {
  height: 100%;
  float: left;
}

.medicine {
  font-size: 0.5rem;
}

.buy-date {
  margin-top: 0.15625rem;
  color: grey;
  font-size: 0.46875rem
}

.insertTel {
  line-height: 1;
  position: absolute;
  top: 50%;
  right: 24px;
  transform: translateY(-50%);
}
</style>
