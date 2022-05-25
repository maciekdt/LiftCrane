<template>
  <div id='lesson-list'>
    <ul>
      <li class="lesson" v-for="(item, index) in source" @click="lessonClick(item)">
        <div>
          <img :src="item.imgUrl">
        </div>
        <div class="lesson-title" style="paddingLeft:30%;"><span>{{item.lessonName}}</span></div>
        <div class="lesson-count" style="paddingLeft:30%;"><span>共{{item.lessonCount}}讲</span></div>
        <div class="border"></div>
      </li>
    </ul>
  </div> 
</template>
<script>
export default {
  name: 'lesson-list',
  props: {
    source: {
      type: Array,
      default () {
        return []
      }
    },
    imageWidth: {
      type: Number,
      default () {
        return 0
      }
    }
  },
  data() {
    return {}
  },
  computed: {},
  methods: {
    lessonClick: function(item) {
      this.$emit('lessonClick', item)
    }
  }
}
</script>
<style scoped lang="scss">
@import '../scss/common';
#lesson-list::-webkit-scrollbar {
  display: none
}

#lesson-list {
  background-color: $color-5;
  overflow: hidden;
  width: 100%;
  .lesson {
    z-index: 1;
    position: relative;
    height: 2.1875rem;
    width: 100%;
    div{
      position: absolute;
      top: 0.3125rem;
      padding-left: 0.3125rem;
      img {
        width:2.34375rem;
        height: 1.5625rem;
        border-radius:2px;
      }
    }
    .lesson-title {
      white-space: nowrap;
      text-overflow: ellipsis;
      bottom: 37.5px;
      top: 0.6rem;
      span {
        font-size: $font-size-7/2;
        color: $color-17;
        line-height: $font-size-7/2;
      }
    }
    .lesson-count {
      white-space: nowrap;
      text-overflow: ellipsis;
      padding-left: 10px;
      top: 1.2rem;
      span {
        font-size: $font-size-1/2;
        color: $color-18;
        line-height: $font-size-1/2;
      }
    }
    .border {
      position: absolute;
      top: 0px;
      right: 8px;
      left: 8px;
      height: 0.5px;
      width: 100%;
      background-color: $color-15;
    }
  }
}
</style>
