<template>
  <div>
    <test-vuex></test-vuex>
    <user-submit></user-submit>
  </div>
</template>
<script>
// 引入组件以及公共文件
// import Api from '../api'
import TestVuex from './health-management/test-vuex'
import UserSubmit from './user-submit'
// 实例化vue
export default {
  components: {
    TestVuex,
    UserSubmit
  },

  data() {
    return {}
  },

  methods: {},

  created: function() {},

  mounted: function() {}
}
</script>
<style scoped lang="scss">
@import '../scss/common';
@import '../scss/vue-swipe.css';
</style>
