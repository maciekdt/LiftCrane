<template>
  <div class="memberBenefits">
    <div class="benefitsList" v-for="item in benefitsList">
      <div class="left">{{item.name}}</div>
      <div class="right">
        {{item.detail}}
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
      benefitsList: []
    }
  },

  methods: {},

  created: function() {},

  mounted: function() {
    var temp = this.Storage.get('useInfo').textDescr.memberRights
    for (let i in temp) {
      this.benefitsList.push({
        name: i,
        detail: temp[i]
      })
    }
  }
}
</script>
<style scoped lang="scss">
@import '../scss/common';
@import '../scss/vue-swipe.css';
.memberBenefits {
  width: 100%;
  height: 100%;
  background-color: white;
  font-size: 0.4375rem;
}

.benefitsList {
  width: 100%;
  overflow: hidden;
  padding-top: 20px;
}

.left {
  width: 23%;
  padding-left: 2%;
  float: left;
  color: grey;
}

.right {
  width: 73%;
  margin-right: 2%;
  float: left;
}
</style>
