<template>
  <div id="app">
    <!--   <video-lessons></video-lessons> -->
    <router-view></router-view>
    <!--  -->
    <!-- <video-sections></video-sections> -->
  </div>
</template>
<script>
import VideoLessons from './views/video-lessons'
import VideoSections from './views/video-sections'
import ArticleLessons from './views/article-lessons'
import ArticleSections from './views/article-sections'
import lectureList from './views/lecture-list'
export default {
  data() {
    return {}
  },
  components: {
    VideoLessons,
    VideoSections,
    ArticleLessons,
    ArticleSections,
    lectureList
  }
}
</script>
<style lang="scss">
@import '/scss/common';
body,
html {
  font-family: Helvetica, sans-serif;
  background-color: white;
}

@font-face {
  font-family: 'iconfont';
  src: url('./font-style/iconfont.eot');
  src: url('./font-style/iconfont.eot?#iefix') format('embedded-opentype'), url('./font-style/iconfont.woff') format('woff'), url('./font-style/iconfont.ttf') format('truetype'), url('./font-style/iconfont.svg#iconfont') format('svg');
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -webkit-text-stroke-width: 0.2px;
  -moz-osx-font-smoothing: grayscale;
}
</style>
<style type="text/css">
  .aaa{}
  .aaa.mint-toast {
      position: fixed;
      max-width: 80%;
      border-radius: 0.078125rem;
      background: rgba(0, 0, 0, 0.7);
      color: #fff;
      box-sizing: border-box;
      text-align: center;
      z-index: 1000;
      -webkit-transition: opacity .3s linear;
      transition: opacity .3s linear;
      padding: 0.3125rem!important;
  }
  .aaa.mint-toast.is-placemiddle {
      left: 50%;
      top: 50%;
      -webkit-transform: translate(-50%, -50%);
              transform: translate(-50%, -50%)
  }
  .aaa .mint-toast-icon {
      display: block;
      text-align: center;
      font-size: 0.875rem
  }
  .aaa .mint-toast-text {
      font-size: 0.4375rem;
      display: block;
      text-align: center
  }
  .aaa .mint-toast-pop-enter, .aaa .mint-toast-pop-leave-active {
      opacity: 0
  }
</style>