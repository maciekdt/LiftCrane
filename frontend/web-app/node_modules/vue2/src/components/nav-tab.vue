<template>
  <div id="nav-tab">
    <div class="tab">
      <div v-for="(item, index) in source" @click="tabClick(item, index)" :class="{ active: item.isSelected }">
        <div>{{item.name}}</div>
      </div>
    </div>
    <div class="nav-border"></div>
  </div>
</template>
<script>
export default {
  name: 'nav-tab',
  props: {
    source: {
      type: Array,
      default () {
        return []
      }
    }
  },
  computed: {
    width: function() {
      return window.innerWidth / this.source.length + 'px'
    }
  },
  methods: {
    tabClick: function(item, index) {
      for (let i = 0; i < this.source.length; i++) {
        this.source[i].isSelected = false
      }
      item.isSelected = true
      this.$emit('tabClick', index)
    }
  }
}
</script>
<style scoped lang="scss">
@import '../scss/common';

#nav-tab {
  position: relative;
  background-color: $color-5;
  height: 1.25rem;
  width: 100%;
  text-align: center;
  z-index: 0;
  .tab{
    padding: 0 5%;
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    height: 1.25rem;
    line-height: 1.25rem;
    font-size: 0.40625rem;
    .active{
      color: $color-16;
      font-weight: bolder;
      border-bottom: 0.0625rem solid $color-16;
    }
  }
  // li {
  //   float: left;
  //   background-color: $color-5;
  //   div {
  //     margin: 0 auto;
  //     width: 30px;
  //     height: 38px;
  //     span {
  //       font-size: $font-size-8/2;
  //       color: $color-17;
  //       line-height: 40px;
  //     }
  //   }
  // }
  // li.active {
  //   background-color: $color-5;
  //   div {
  //     border-bottom-color: $color-16;
  //     border-bottom-style: solid;
  //     border-bottom-width: 2px;
  //     height: 38px;
  //     span {
  //       font-size: $font-size-7;
  //       color: $color-16;
  //       line-height: 40px;
  //     }
  //   }
  // }
}
</style>
