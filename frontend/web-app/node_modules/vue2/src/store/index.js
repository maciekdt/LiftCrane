import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user_name: ''
  },
  mutations: {
    showUserName(state) {
      console.log(state.user_name)
    }
  }
})
