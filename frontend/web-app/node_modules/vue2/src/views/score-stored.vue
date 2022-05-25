<template>
  <div class="score-stored">
    <div class="banner" :style="{ 'background-image': 'url(' + bgUrl +')'}">
      <div class="content" v-if="scoreOrMoney">{{scoreValue}}</div>
      <div class="content" v-if="!scoreOrMoney">0</div>
    </div>
    <div class="privilege">特权说明</div>
    <div class="illustration" v-if="scoreOrMoney">{{illustration}}</div>
    <div class="illustration" v-if="!scoreOrMoney" v-html="illustration1"></div>
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
      scoreValue: '',
      scoreOrMoney: true,
      bgUrl: '',
      illustration: '',
      illustration1: ''
    }
  },

  methods: {},

  created: function() {},

  mounted: function() {
    var str = window.location.hash.indexOf('memberScore')
    if (str > -1) {
      this.scoreOrMoney = true
    } else {
      this.scoreOrMoney = false
    }
    this.scoreValue = this.$route.query.scoreValue
    this.bgUrl = this.Storage.get('useInfo').imgUrlMap.headerBg
    this.illustration = this.Storage.get('useInfo').textDescr.pointsRights.pointsRights
    this.illustration1 = this.Storage.get('useInfo').textDescr.storedValueRights.storedValueRights.replace(/\n/g, '<br>')
  }
}
</script>
<style scoped lang="scss">
@import '../scss/common';
@import '../scss/vue-swipe.css';
.score-stored {
  width: 100%;
  height: 100%;
}

.banner {
  width: 100%;
  height: 191px;
  line-height: 191px;
  margin-bottom: 10px;
  background-size: 100% 100%;
}

.content {
  width: 100%;
  height: 100%;
  text-align: center;
  color: white;
  font-size: 3rem;
}

.privilege {
  width: 90%;
  height: 3.5rem;
  line-height: 3.5rem;
  color: grey;
  padding: 0 5%;
  font-size: 1.2rem;
}

.illustration {
  padding: 0 5%;
  font-size: 1.1rem;
}
</style>
