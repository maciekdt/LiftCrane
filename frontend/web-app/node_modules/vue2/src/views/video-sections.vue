<template>
  <div id="video-sections">
    <div id="banner">
      <!--  <video-player :options="videoOptions"></video-player> -->
      <video :src="videoSrc" height="147" controls="controls" width="100%"></video>
    </div>
    <div id="tool-bar">
      <div style="float:left; width:80%;">
        <div id='lesson-name'>
          <span>{{lessonInfo.lessonName}}</span>
        </div>
        <div id='total-time'>
          <img src="../images/ic_clock.png">
          <span>{{lessonInfo.totalTime}}分钟</span>
        </div>
      </div>
      <div id='like-num' style="float:left; width:20%;" @click='likeClick'>
        <template>
          <img v-if='likeStatus===1' src="../images/ic_zan_pre.png">
          <img v-else src="../images/ic_zan.png">
        </template>
        <span>{{lessonInfo.likeNum}}</span>
      </div>
    </div>
    <nav-tab id="nav-tab" :source.once="navTabs" @tabClick="tabClick"></nav-tab>
    <div id='section-list' v-if="navTabs[0].isSelected" :style="{ height: sectionListHeight }">
      <li class="section" v-for="(item, index) in videoSectionList" @click="sectionClick(item, index)" :class="{ active: index === activeSectionIndex }">
        <div class="section-title" style="float:left; width:75%;"><span>{{item.title}}</span></div>
        <div class="section-time"><img v-if="index === activeSectionIndex" src="../images/ic_clock_pre.png"><img v-else src="../images/ic_clock.png"><span>{{item.time}}分钟</span></div>
        <div class="section-border"></div>
      </li>
    </div>
    <div id="intro" v-else :style="{ height: introHeight }">
      <div id="lesson-intro">
        <h1>课程介绍</h1>
        <p>{{lessonInfo.summary}}</p>
      </div>
      <div id="lecturer-intro">
        <h1>讲师介绍</h1>
        <div id="lecturer-img">
          <img :src="lecturerInfo.headImgUrl">
        </div>
        <div id="intro-content">
          <div id="lecturer-name"><span><b>{{lecturerInfo.name}}</b></span></div>
          <div id="lecturer-title"><span>{{lecturerInfo.title}}</span></div>
          <div id="lecturer-description"><span>{{ lecturerInfo.description }}</span></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import Utils from '../utils'
import Api from '../api'
import NavTab from '../components/nav-tab'
const navTabs = [{
  name: '目录',
  isSelected: true
}, {
  name: '介绍',
  isSelected: false
}]
export default {
  name: 'video-sections',
  components: {
    NavTab
  },
  data() {
    return {
      videoSrc: '',
      navTabs: navTabs,
      likeStatus: 0,
      lessonId: this.$route.params.lessonId,
      uId: 'system',
      videoSectionList: [],
      lessonInfo: {},
      lecturerInfo: {},
      activeSectionIndex: 0
    }
  },
  computed: {
    sectionListHeight: function() {
      let maxHeight = window.innerHeight - 252
      if (this.videoSectionList.length * 50 < maxHeight) {
        return this.videoSectionList.length * 50 + 'px'
      }
      return window.innerHeight - 252 + 'px'
    },

    introHeight: function() {
      return window.innerHeight - 252 + 'px'
    }
  },

  methods: {
    // ##############actions begin##############
    tabClick: function(index) {
      this.navTabs[index].isSelected = true
    },
    sectionClick: function(item, index) {
      this.activeSectionIndex = index
      this.videoSrc = this.videoSectionList[index].linkUrl
    },
    likeClick: function() {
      Utils.postVue(Api.VIDEO_LIKE, {
        'lessonId': this.lessonId,
        'uId': this.uId,
        'isLike': this.likeStatus === 0 || this.likeStatus === null ? 1 : 0
      }, result => {
        if (this.likeStatus === 0 || this.likeStatus === null) {
          this.lessonInfo.likeNum = this.lessonInfo.likeNum + 1
        } else {
          this.lessonInfo.likeNum = this.lessonInfo.likeNum - 1
        }
        this.likeStatus = this.likeStatus === 0 || this.likeStatus === null ? 1 : 0
      })
      // ##############actions end##############
    }
  },
  created: function() {
    document.title = '视频课堂'
    Utils.post(Api.VIDEO_SECTIONS, {
      'lessonId': this.lessonId,
      'uId': this.uId
    }, result => {
      this.videoSectionList = result.videoSectionList
      this.lessonInfo = result.lessonInfo
      this.likeStatus = result.lessonInfo.likeStatus
      this.videoSrc = result.videoSectionList[0].linkUrl
      Utils.post(Api.LECTURER_INFO, {
        'lecturerId': this.lessonInfo.lecturerId
      }, result => {
        this.lecturerInfo = result.data
      })
    })
  }
}
</script>
<style scoped lang="scss">
@import '../scss/common';
#video-share {
  height: 100%;
  -webkit-tap-highlight-color: transparent;
}

#banner {
  height: 147px;
  position: relative;
  background-color: #000000;
}

#tool-bar {
  background-color: $color-5;
  height: 50px;
  #lesson-name {
    margin: 0;
    height: 25px;
    position: relative;
    span {
      position: absolute;
      left: 0;
      bottom: 0;
      padding-left: 15px;
      padding-bottom: 2.5px;
      font-size: $font-size-7/2;
      color: $color-17;
      line-height: $font-size-7/2;
    }
  }
  #total-time {
    margin: 0;
    height: 25px;
    position: relative;
    span {
      position: absolute;
      left: 0;
      top: 0;
      padding-left: 33px;
      padding-top: 2.5px;
      font-size: $font-size-8/2;
      color: $color-18;
      line-height: $font-size-8/2;
    }
    img {
      width: 13px;
      height: 13px;
      position: absolute;
      left: 0;
      top: 0;
      padding-left: 15px;
      padding-top: 2.5px;
    }
  }
  #like-num {
    position: relative;
    height: 50px;
    img {
      width: 16px;
      height: 15px;
      position: absolute;
      right: 15px;
      bottom: 0;
      padding-bottom: 25px;
    }
    span {
      width: 46px;
      position: absolute;
      right: 0px;
      top: 0;
      padding-top: 30px;
      text-align: center;
      font-size: $font-size-3/2;
      color: $color-18;
      line-height: $font-size-3/2;
    }
  }
}

#nav-tab {
  margin-top: 15px;
}

#section-list::-webkit-scrollbar {
  display: none
}

#section-list {
  background-color: $color-5;
  float: left;
  width: 100%;
  overflow: scroll;
  .section {
    height: 50px;
    .section-title {
      float: left;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      span {
        padding-left: 15px;
        font-size: $font-size-8/2;
        color: $color-18;
        line-height: 50px;
      }
    }
    .section-time {
      float: right;
      span {
        padding-right: 15px;
        font-size: $font-size-8/2;
        color: $color-18;
        line-height: 50px;
      }
      img {
        vertical-align: middle;
        width: 13px;
        height: 13px;
        padding-right: 5px;
      }
    }
    .section-border {
      height:1px;
      background-color:$color-15;
      margin-right: 8px;
      margin-left: 8px;
    }
  }
  li.active {
    .section-title span {
      font-size: $font-size-7/2;
      color: $color-16;
    }
    .section-time span {
      font-size: $font-size-7/2;
      color: $color-16;
    }
  }
}

#intro {
  float: left;
  overflow: scroll;
  #lecturer-intro,
  #lesson-intro {
    background-color: $color-5;
    h1 {
      padding-top: 10px;
      padding-left: 15px;
      padding-bottom: 10px;
      font-size: $font-size-1/2;
      color: $color-18;
    }
  }
  #lesson-intro {
    p {
      padding-left: 15px;
      padding-right: 15px;
      padding-bottom: 10px;
      font-size: $font-size-8/2;
      color: $color-17;
      line-height: $font-size-8/2*1.2;
      text-align: justify;
    }
  }
  #lecturer-intro {
    position: relative;
    margin-top: 10px;
    float: left;
    width: 100%;
    #lecturer-img {
      float: left;
      widh: 51px;
      img {
        position: absolute;
        width: 51px;
        height: 65px;
        padding-left: 15px;
        z-index: 1;
      }
    }
    #intro-content {
      background-color: $color-5;
      position: absolute;
      top: 10;
      float: left;
      padding-left: 76px;
      padding-right: 15px;
      #lecturer-name {
        float: left;
        font-size: $font-size-7/2;
        color: $color-17;
        padding-top: 5px;
      }
      #lecturer-title {
        float: left;
        font-size: $font-size-8/2;
        color: $color-17;
        padding-top: 6px;
        padding-left: 10px;
      }
      #lecturer-description {
        float: left;
        font-size: $font-size-8/2;
        color: $color-17;
        line-height: $font-size-8/2*1.2;
        padding-top: 5px;
        padding-bottom: 10px;
        width: 100%;
        text-align: justify;
      }
    }
  }
}
</style>
