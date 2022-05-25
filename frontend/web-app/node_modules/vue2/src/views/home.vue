<template>
  <div class="home" v-if="showPage">
    <div class="homeList" v-for="(item,index) in list" @click="goPage(index)">{{item.name}}</div>
  </div>
</template>
<script>
import Utils from '../utils'
import Api from '../api'
export default {
  data() {
    return {
      list: [{
        name: '绑定设备',
        goUrl: 'bindDevice'
      }, {
        name: '健康管理',
        goUrl: 'healthManage'
      }, {
        name: '检测数据',
        goUrl: 'measureData'
      }, {
        name: '关注管理',
        goUrl: 'followerManage'
      }, {
        name: '会员中心',
        goUrl: 'membership'
      }],
      code: '',
      showPage: false
    }
  },
  mounted: function() {
    let query = this.$route.query
    let WECHAT = this.Cookie.getJSON('WECHAT')
    // let userId = ''
    let data = {
      wechatId: query.wechatId
    }
    if (WECHAT && WECHAT.userId) {
      this.goPageView(WECHAT, query)
      return
    }
    let code = this.getUrlParam('code')
    if (code) {
      data.code = code
      this.getWechatInfo(data, query)
    }
  },
  methods: {
    goPage: function(index) {
      let goUrl = this.list[index].goUrl
      if (goUrl === 'measureData' || goUrl === 'healthManage' || goUrl === 'followerManage') {
        this.$router.push({
          name: 'follow-list',
          query: { name: goUrl }
        })
      } else {
        this.$router.push({
          name: goUrl,
          query: { mainStoreId: goUrl }
        })
      }
    },
    getWechatInfo: function(data, query) {
      Utils.post(Api.GET_WECHAT_INFO, data, result => {
        let WECHAT = {}
        WECHAT = result.data || {}
        WECHAT['wechatId'] = data.wechatId
        this.Cookie.set('WECHAT', WECHAT)
        this.goPageView(result.data, query)
      })
    },
    goPageView: function(data, query) {
      console.log(query.ourselves)
      if (query.ourselves) {
        if (query.pageName === 'measureData' || query.pageName === 'healthManage' || query.pageName === 'followerManage') {
          if (data.userId) {
            this.$router.push({
              name: 'follow-list',
              query: { name: query.pageName }
            })
          } else {
            this.$router.push({
              name: 'binding',
              query: { name: query.pageName }
            })
          }
        } else {
          if (query.pageName === 'memberCenter') {
            this.$router.push({
              name: 'membership',
              query: { mainStoreId: query.mainStoreId }
            })
          } else {
            this.$router.push({
              name: query.pageName
            })
          }
        }
      } else {
        this.showPage = true
      }
    }
  }
}
//  = /#/home?ourselves=1&pageName=device&wechatId=carelinker
</script>
<style scoped lang="scss">
.home{  
  .homeList{
    width: 100%;
    height:1.25rem;
    line-height: 1.25rem;
    text-align: center;
    border-bottom: 0.015625rem solid grey;
  }
}
</style>
