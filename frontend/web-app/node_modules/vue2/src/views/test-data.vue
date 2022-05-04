<template>
  <div id="choose-archive" v-show="showView">
    <div style="overflow: auto;height: 100%;-webkit-overflow-scrolling: touch;">
      <h3>请选择查看对象</h3>
      <ul class="list">
        <li v-for="item in dataList" @click="goView(item)">
          <div class="head-img">
            <img v-if="item.headImgUrl" :src="item.headImgUrl">
            <img v-else src="../images/pic_tx.png">
          </div>
          <div class="user-info">
            <div class="user-info-name">{{item.remark}}</div>
            <div class="testTime">最近一次测量时间</div>
            <div class="testTime">{{item.lastMeasureTime ? item.lastMeasureTime : '-'}}</div>
          </div>
          <div class="channel" v-if="item.channel !==0">用户{{item.channel}}</div>
        </li>
      </ul>
      <div class="add-new-user" @click="addNew">+ 关注新用户</div>
    </div>
  </div>
</template>
<script>
import Api from '../api'
import Utils from '../utils'
export default {
  name: 'choose-archive',
  data() {
    return {
      patientId: '',
      userInfo: '',
      toPath: '',
      openId: '',
      dataList: [],
      source: 'button',
      doctorId: null,
      code: null,
      isManageArchives: true,
      showView: true,
      isManage: false,
      goViewName: '',
      userId: ''
    }
  },
  methods: {
    goView: function(item) {
      console.log(item)
      let pId = item.patientId
      if (this.goViewName === 'followerManage') {
        let isSelf = item.relationship === 'SELF'
        this.$router.push({
          path: '/follower/manage',
          query: { isSelf: isSelf, name: 'followerManage', followedUserId: item.userId }
        })
      } else {
        if (this.goViewName === 'measureData') {
          window.location.href = 'https://platform.carelinker.com/sc/web/wechat_charts/#/bp?pId=' + pId
        } else {
          this.$router.push({
            name: this.goViewName,
            query: { patientId: pId, isSelf: item.relationship, headImgUrl: item.headImgUrl }
          })
        }
      }
    },
    addNew: function() {
      this.$router.push({
        path: 'friends',
        query: { name: this.goViewName }
      })
    },
    getUserList: function() {
      let data = {
        followerUserId: this.userId
      }
      Utils.post(Api.GET_USER_FOLLOW, data, result => {
        // let dataList = []
        this.dataList = result.dataList
      })
    },
    clear: function() {
      this.Cookie.removeAll()
      this.Storage.clear()
    }
  },
  created: function() {
    let query = this.$route.query
    this.goViewName = query.name
    let WECHAT = this.Cookie.getJSON('WECHAT')
    this.userId = WECHAT.userId
    this.getUserList()
  },
  mounted: function() {
    document.title = '选择档案'
  }
}
</script>
<style scoped lang="scss">
@import '../scss/common';
#choose-archive {
  width: 100%;
  height: 100%;
  /* // overflow: hidden; */
  position: absolute;
  background: url('../images/bg.png') no-repeat center center;
  background-size: 100% 100%;
}

.bg {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  z-index: -1;
}

#choose-archive h3 {
  font-size: 0.46875rem;
  color: $color-5;
  text-align: center;
  margin-top:0.78125rem;
  margin-bottom: 1.25rem;
}

.list {
  margin: 0 0.46875rem;
  -webkit-overflow-scrolling: touch;
}

.list li {
  background: #fff;
  height: 2.34375rem;
  border-radius: 1.40625rem;
  position: relative;
  margin-bottom: 0.46875rem;
}

.list li .head-img {
  width: 2.03125rem;
  height: 2.03125rem;
  padding-top: 0.15625rem;
  margin-left: 0.15625rem;
  overflow: hidden;
  border-radius: 50%;
}

.list li .head-img img {
  width: 100%;
  height: 100%;
  border-radius: 1.40625rem;
}

.list li .user-info {
  position: absolute;
  top: 0;
  left: 2.5rem;
}

.list li .user-info .user-info-name {
  font-size: 0.46875rem;
  color: $color-17;
  margin-top: 0.3125rem;
  margin-bottom: 0.234375rem;
}
.testTime{
  font-size: 0.3125rem;
  margin-bottom: 0.078125rem;
  color: #9b9b9b;
}

.list li .user-info p {
  font-size: 20px;
  color: #9b9b9b;
  margin-top: 10px;
}

.add-new-user {
  margin: 0.78125rem auto;
  height: 0.84375rem;
  line-height: 0.84375rem;
  text-align: center;
  width: 3.4375rem; 
  font-size: 0.4375rem;
  color: #1989fa;
  border: 0.03125rem dashed #1989fa;
  border-radius: 0.78125rem;
}
.channel{
  position: absolute;
  top: 47%;
  transform: translateY(-50%);
  right: 0.625rem;
  font-size: 0.46875rem;
  color: #101010;
  opacity: 0.5;
}
</style>
