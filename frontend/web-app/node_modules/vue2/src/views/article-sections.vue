<template>
  <div id="article-sections">
    <div id="banner" :style="{ height: bannerHeight + 'px', width: bannerWidth + 'px'}">
      <img :src="imgSrc" height="172" controls="controls" width="100%" style="z-index:-1" :style="{ height: bannerHeight + 'px', width: bannerWidth + 'px'}">
      <p class="banner-title">{{ lessonName }}</p>
      <div class="banner-shadow"></div>
    </div>
      <ul>
      <li class="section" v-for="(item, index) in knowledgeSectionList" @click="sectionClick(item, index)">
        <div class="section-title" style="float:left; width:75%;"><span>{{item.title}}</span></div>
        <div class="section-time"><img src="../images/ic_eye.png"><span>{{item.readerNum}}</span></div>
        <div class="section-border"></div>
      </li>
    </ul>
    </div>
  </div>
</template>
<script>
import Utils from '../utils'
import Api from '../api'
import NavTab from '../components/nav-tab'
export default {
  name: 'article-sections',
  components: {
    NavTab
  },
  data () {
    return {
      bannerHeight: window.innerWidth * 11 / 24,
      bannerWidth: window.innerWidth,
      imgSrc: '',
      lessonId: this.$route.params.lessonId,
      uId: 'system',
      firstClass: this.$route.params.firstClass,
      knowledgeSectionList: [],
      activeSectionIndex: 0
    }
  },
  computed: {
    sectionListHeight: function () {
      let maxHeight = window.innerHeight - 252
      if (this.knowledgeSectionList.length * 50 < maxHeight) {
        return this.knowledgeSectionList.length * 50 + 'px'
      }
      return window.innerHeight - 252 + 'px'
    },

    introHeight: function () {
      return window.innerHeight - 252 + 'px'
    }
  },

  methods: {
    // ##############actions begin##############
    sectionClick: function (item, index) {
      window.location.href = this.knowledgeSectionList[index].linkUrl
    }
    // ##############actions end##############
  },
  created: function () {
    document.title = '分类知识'
    Utils.post(Api.ARTICLE_SECTIONS, {
      'lessonId': this.lessonId,
      'firstClass': this.firstClass
    }, result => {
      this.knowledgeSectionList = result.knowledgeSectionList
      this.lessonImg = result.lessonImg
      this.lessonName = result.lessonName
      this.imgSrc = Utils.getLargeImg(result.lessonImg)
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
  position: relative;
  background-color: #000000;
  .banner-title{
    position:absolute;
    left:15px;
    bottom:10px;
    z-index:999;
    color:$color-5;
    font-size:$font-size-7/2;
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
}

#nav-tab {
  margin-top: 15px;
}

#section-list::-webkit-scrollbar {
  display: none
}
  .section {
    background-color: $color-5;
  float: left;
  width: 100%;
  overflow: hidden;
    height: 50px;
    .section-title {
      float: left;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      span {
        padding-left: 15px;
        font-size: $font-size-8/2;
        color: $color-17;
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
        width: 11px;
        height: 8px;
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
</style>
