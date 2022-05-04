import Vue from 'vue'
import VueRouter from 'vue-router'
// import Cookie from 'js-cookie'

Vue.use(VueRouter)

const router = new VueRouter({
  mode: 'hash',
  base: __dirname,
  routes: [{
    path: '/video-lessons',
    component: resolve => require(['../views/video-lessons'], resolve)
  }, {
    path: '/video-sections/:lessonId',
    name: 'video-sections',
    component: resolve => require(['../views/video-sections'], resolve)
  }, {
    path: '/article-lessons',
    component: resolve => require(['../views/article-lessons'], resolve)
  }, {
    path: '/article-sections/:lessonId/:firstClass',
    name: 'article-sections',
    component: resolve => require(['../views/article-sections'], resolve)
  }, {
    path: '/lecture-list',
    component: resolve => require(['../views/lecture-list'], resolve)
  }, {
    path: '/membership',
    name: 'membership',
    component: resolve => require(['../views/membership'], resolve)
  }, {
    path: '/memberLogin',
    name: 'binding',
    component: resolve => require(['../views/member-login'], resolve)
  }, {
    path: '/submitApply',
    name: 'apply',
    component: resolve => require(['../views/submit-apply'], resolve)
  }, {
    path: '/memberBenefits',
    component: resolve => require(['../views/member-benefits'], resolve)
  }, {
    path: '/member-information/:checkOrMember',
    name: 'information',
    component: resolve => require(['../views/member-inform'], resolve)
  }, {
    path: '/score-stored/:goType',
    name: 'score',
    component: resolve => require(['../views/score-stored'], resolve)
  }, {
    path: '/memberCenterLogin',
    name: 'memberCenterLogin',
    component: resolve => require(['../views/member-center-login'], resolve)
  }, {
    path: '/coupon',
    name: 'coupon',
    component: resolve => require(['../views/coupon'], resolve)
  }, {
    path: '/basic-inform',
    name: 'basic',
    component: resolve => require(['../views/basic-inform'], resolve)
  }, {
    path: '/purchase-record',
    name: 'record',
    component: resolve => require(['../views/purchase-record'], resolve)
  }, {
    path: '/health-guide',
    name: 'healthManage',
    component: resolve => require(['../views/healthGuide'], resolve)
  }, {
    path: '/diet-scheme',
    name: 'diet',
    component: resolve => require(['../views/diet-scheme'], resolve)
  }, {
    path: '/food-details',
    name: 'dietDetail',
    component: resolve => require(['../views/food-details'], resolve)
  }, {
    path: '/user-name',
    component: resolve => require(['../views/user-name'], resolve)
  }, {
    path: '/health-document',
    name: 'document',
    component: resolve => require(['../views/health-document'], resolve)
  }, {
    path: '/healthSub',
    name: 'detail',
    component: resolve => require(['../views/healthSub'], resolve)
  }, {
    path: '/device-type',
    name: 'bindDevice',
    component: resolve => require(['../views/device-type'], resolve)
  }, {
    path: '/scan-bind',
    name: 'scan',
    component: resolve => require(['../views/scan-bind'], resolve)
  }, {
    path: '/device-number',
    name: 'deviceNumber',
    component: resolve => require(['../views/device-number'], resolve)
  }, {
    path: '/friends',
    name: 'friends',
    component: resolve => require(['../views/friends'], resolve)
  }, {
    path: '/follow',
    name: 'follow-list',
    component: resolve => require(['../views/test-data'], resolve)
    // meta: { requiresAuth: true }
  }, {
    path: '/follower/manage',
    name: 'follower-manage',
    component: resolve => require(['../views/follow-manage'], resolve)
    // meta: { requiresAuth: true }
  }, {
    path: '/home',
    name: 'home',
    component: resolve => require(['../views/home'], resolve)
  }, {
    path: '/test-vuex',
    name: 'vuex',
    component: resolve => require(['../views/health-management/test-vuex'], resolve)
  }]
})

// router.beforeEach((to, from, next) => {
//   if (to.matched.some(record => record.meta.requiresAuth)) {
//     if (Cookie.getJSON('WECHAT').userId) {
//       next()
//     } else {
//       next(vm => {
//         vm.$router.push({
//           name: 'binding'
//         })
//       })
//     }
//   } else {
//     next() // 确保一定要调用 next()
//   }
// })

export default router
