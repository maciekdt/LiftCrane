<template>
  <div id="lecture-list">
    <div class="banner" :style="{ height: bannerHeight + 'px', width: bannerWidth + 'px'}"></div>
      <ul class="list-ul" :style="{ padding: listRigth + 'px'}">
        <li v-for="(listItem, index) in dataList" @click.prevent="lectureClick(listItem.articleReviewUrl)" :style="{ height: listHeight + 'px', width: listWidth + 'px'}" :class="{ listLeft : index % 2 === 0 }">
        <img class="bgImg" :src="listItem.imgUrl" alt="Lecture background image">
        <a class="list-con" href="#">{{ listItem.lessonName }}</a></li>
      </ul>
  </div>
</template>

<script>
import Utils from '../utils'
import Api from '../api'
export default {
  data: function() {
    return {
      bannerHeight: window.innerWidth * 11 / 24,
      bannerWidth: window.innerWidth,
      listWidth: window.innerWidth * 29 / 64,
      listHeight: window.innerWidth * 11 / 40,
      listRigth: window.innerWidth * 1 / 32,
      bStId: '0000001',
      uId: 'system',
      firstClass: '首页',
      activeLectureIndex: 0,
      dataList: [],
      lectureDetail: {},
      isVisible: 'false'
    }
  },
  methods: {
    lectureClick: function (url) {
      if (url == null) {
        Utils.alert(' 讲座正在整理中，敬请期待！ ')
      } else {
        window.location.href = url
      }
    }
  },
  created: function() {
    document.title = '讲座回顾'
    Utils.post(Api.LECTURE_LIST, {
      'bStId': this.bStId,
      'uId': this.uId,
      'firstClass': this.firstClass
    }, result => {
      this.dataList = result.dataList
    })
// 周期函数，执行ajax请求
  }
}
</script>
<style scoped lang="scss">
@import '../scss/common';
#lecture-list {
  background-color: $color-5;
  overflow: hidden;
  width: 100%;
  -webkit-tap-highlight-color: transparent;
  padding-bottom: 92px;
    .banner{
  background-image:url('../images/banner.png');
  background-size: 100% 100%;
}
  .list-ul{
  margin: 0 auto;
  padding-top: 9px;
  padding-bottom: 184px;
 li{
  float: left;
  border-radius: 2px;
  padding-bottom: 8px;
  text-align: center;
  position: relative;
  }
  .list-con{
  width:80%;
  padding-left:10%;
  display: inline-block;
  font-size: $font-size-7/2;
  color: $color-5;
  text-shadow: 1px 1px 1px rgba(0, 0, 0, .7);
  text-align: center;
  z-index: 999;
  position: absolute;
  top: 35%;
  left: 0;
  }
}
 .listLeft{
  padding-right: 9px;
  }
.bgImg{
  border-radius: 2px;
  width: 100%;
  height: 100%;
  z-index: -1;

}
}
</style>