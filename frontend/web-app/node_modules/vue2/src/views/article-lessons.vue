<template>
  <div id="article-lessons" @scroll="scroll" :style="{ height: lessonListHeight + 'px'}">
    <div id="banner" :style="{ height: bannerHeight + 'px', width: bannerWidth + 'px'}">
      <swipe class="banner-swipe">
        <swipe-item v-for="(item, index) in recommendLessonList" :style="{ height: bannerHeight + 'px', width: bannerWidth + 'px'}" @click.native="recommendLessonListClick(item)">
          <img :src="item.largeImgUrl" :style="{ height: bannerHeight + 'px', width: bannerWidth + 'px'}">
          <p class="bannerTitle">{{ item.lessonName }}</p>
        </swipe-item>
      </swipe>
      <div class="banner-shadow"></div>
    </div>
    <div id="nav-tab-wrap" :class="{pin: pin}">
      <nav-tab :source.once="navTabs" @tabClick="navTabClick"></nav-tab>
    </div>
    <div id="lesson-list-wrap" v-for="(item, index) in lessonLists" :class="{navPin: pin}">
      <lesson-list :source.once="item" :imageWidth="imageWidth" @lessonClick="lessonListClick" v-if="activeLessonIndex===index"></lesson-list>
    </div>
  </div>
</template>
<script>
// 引入组件以及公共文件
import Utils from '../utils'
import Api from '../api'
import NavTab from '../components/nav-tab'
import LessonList from '../components/lesson-list'
import {
  Swipe,
  SwipeItem
} from 'vue-swipe'
// 实例化vue
export default {
  name: 'article-lessons',
  components: {
    NavTab,
    LessonList,
    Swipe,
    SwipeItem
  },

  data () {
    return {
      bannerHeight: window.innerWidth * 11 / 24,
      bannerWidth: window.innerWidth,
      imageWidth: 50,
      pin: false,
      navTabTop: 0,
      navTabs: [],
      lessonLists: [],
      recommendLessonList: [],
      activeLessonIndex: 0,
      lessonListHeight: window.innerHeight
    }
  },

  methods: {
    navTabClick: function (index) {
      this.activeLessonIndex = index
      if (document.getElementById('article-lessons').scrollTop - this.navTabTop > 0) {
        this.pin = true
      }
    },
    lessonListClick: function (item) {
      this.$router.push({
        name: 'article-sections',
        params: {
          lessonId: item.id,
          firstClass: item.firstClass
        }
      })
    },
    recommendLessonListClick: function (item) {
      this.$router.push({
        name: 'article-sections',
        params: {
          lessonId: item.id,
          firstClass: item.firstClass
        }
      })
    },
    scroll: function () {
      if (document.getElementById('article-lessons').scrollTop - this.navTabTop > 0) {
        this.pin = true
      } else {
        this.pin = false
      }
    }
  },

  created: function () {
    document.title = '分类知识'
    Utils.post(Api.ARTICLE_FIRST_CLASS, {}, result => {
      // 请求tab列表
      var tabs = []
      for (let i = 1; i < result.dataList.length; i++) {
        tabs.push({
          name: result.dataList[i],
          isSelected: false
        })
      }
      tabs[0].isSelected = true
      this.navTabs = tabs
      var recommendList = []
      var lessonLists = []
      for (let i = 0; i < tabs.length; i++) {
        Utils.post(Api.ARTICLE_LESSONS, {
          'isDesc': true,
          'bStId': '0000001',
          'firstClass': tabs[i].name
        }, result => {
          lessonLists[i] = result.dataList
          for (var index of result.dataList) {
            if (index.isBanner === 1) {
              index.largeImgUrl = Utils.getLargeImg(index.imgUrl)
              recommendList.push(index)
            }
          }
          this.lessonLists = lessonLists
          this.recommendLessonList = recommendList
        })
      }
    })
  },

  mounted: function () {
    this.navTabTop = document.getElementById('nav-tab-wrap').getBoundingClientRect().top
  }
}
</script>
<style scoped lang="scss">
@import '../scss/common';
@import '../scss/vue-swipe.css';
#article-lessons {
  width: 100%;
  -webkit-tap-highlight-color: transparent;
  overflow: scroll;
}
#banner{
  position:relative;
  border-radius:2px;
  .bannerTitle{
    position:absolute;
    left:15px;
    bottom:10px;
    z-index:999;
    color:$color-5;
    font-size:$font-size-7/2;
  }
}
#lesson-list .lesson img{
  width:50px;
  height:50px;
}
.banner-shadow{
  position:absolute;
  width:100%;
  bottom:0;
  height:70px;
  z-index: 99;
  background-image:url('../images/banner_shadow.png');
  background-size: 100% 100%;
  }
#nav-tab-wrap::-webkit-scrollbar {
  display: none
}

.pin {
  position: absolute;
  top: 0px;
  z-index: 99;
}

.navPin {
  margin-top: 40px;
  margin-bottom: -40px;
}
</style>
