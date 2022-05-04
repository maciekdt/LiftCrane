import Vue from 'vue'
import VueCompositionAPI, { createApp, h } from '@vue/composition-api'

import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'

Vue.use(VueCompositionAPI)

const app = createApp({
  router,
  render: () => h(App)
})

app.mount('#app')

new Vue({
  vuetify,
  render: h => h(App)
}).$mount('#app');
