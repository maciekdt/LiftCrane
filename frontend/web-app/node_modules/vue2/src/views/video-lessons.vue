<template>
  <div id="video-lessons" @scroll="scroll" :style="{ height: lessonListHeight + 'px'}">
    <div id="banner" :style="{ height: bannerHeight + 'px', width: bannerWidth + 'px'}">
      <swipe class="banner-swipe">
        <swipe-item v-for="(item, index) in recommendLessonList" :style="{ height: bannerHeight + 'px', width: bannerWidth + 'px'}" @click.native="recommendLessonListClick(item)">
          <img :src="item.largeImgUrl" :style="{ height: bannerHeight + 'px', width: bannerWidth + 'px'}">
        </swipe-item>
      </swipe>
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
import Utils from '../utils'
import Api from '../api'
import NavTab from '../components/nav-tab'
import LessonList from '../components/lesson-list'
import {
  Swipe,
  SwipeItem
} from 'vue-swipe'

export default {
  name: 'video-lessons',
  components: {
    NavTab,
    LessonList,
    Swipe,
    SwipeItem
  },

  data() {
    return {
      bannerHeight: window.innerWidth * 11 / 24,
      bannerWidth: window.innerWidth,
      imageWidth: 78,
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
    navTabClick: function(index) {
      this.activeLessonIndex = index
      if (document.getElementById('video-lessons').scrollTop - this.navTabTop > 0) {
        this.pin = true
      }
    },
    lessonListClick: function(item) {
      this.$router.push({
        name: 'video-sections',
        params: {
          lessonId: item.id
        }
      })
    },
    recommendLessonListClick: function(item) {
      this.$router.push({
        name: 'video-sections',
        params: {
          lessonId: item.id
        }
      })
    },
    scroll: function() {
      if (document.getElementById('video-lessons').scrollTop - this.navTabTop > 0) {
        this.pin = true
      } else {
        this.pin = false
      }
    }
  },

  created: function() {
    document.title = '视频课堂'
    Utils.post(Api.VIDEO_FIRST_CLASS, {}, result => {
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
        Utils.post(Api.VIDEO_LESSONS, {
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
          for (var index1 of result.dataList) {
            var lesson = index1
            lesson.imgUrl = Utils.getSmallImg(index1.imgUrl)
            lessonLists.push(lesson)
          }
          this.lessonLists = lessonLists
          this.recommendLessonList = recommendList
        })
      }
    })
  },

  mounted: function() {
    this.navTabTop = document.getElementById('nav-tab-wrap').getBoundingClientRect().top
  }
}
</script>
<style scoped lang="scss">
@import '../scss/common';
@import '../scss/vue-swipe.css';
#video-lessons {
  width: 100%;
  -webkit-tap-highlight-color: transparent;
  overflow: scroll;
}
#nav-tab-wrap::-webkit-scrollbar {
  display: none
}

.pin {
  position: absolute;
  top: 0px;
  z-index: 99;
  width: 100%;
}

.navPin {
  margin-top: 40px;
  margin-bottom: -40px;
}
</style>
