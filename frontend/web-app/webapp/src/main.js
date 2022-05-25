import 'material-design-icons-iconfont/dist/material-design-icons.css'
import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './router'
// import firebase from "firebase/app";
// import "firebase/firestore";
Vue.config.productionTip = false




new Vue({
  vuetify,
  router,
  render: h => h(App)
}).$mount('#app')
