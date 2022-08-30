import 'material-design-icons-iconfont/dist/material-design-icons.css'
import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './router'
import store from './store'
import { firestorePlugin} from 'vuefire'
import 'viewerjs/dist/viewer.css'
import Viewer from 'v-viewer'

// import firebase from "firebase/app";
// import "firebase/firestore";
Vue.config.productionTip = false

Vue.use(firestorePlugin)
Vue.use(Viewer)


new Vue({
  vuetify,
  router,
  store,
  render: h => h(App)
}).$mount('#app')
