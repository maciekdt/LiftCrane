import Vue from "vue";
import Vuex from "vuex";
import { db } from "@/fb";
import { vuexfireMutations, firestoreAction } from "vuexfire";
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex);

export default new Vuex.Store({
  plugins: [
    createPersistedState({
    })
  ],
  state: {
    lifts: [],
    review: [],
    users: [],
    isSignedIn: false,
    userName: "",
  },
  mutations: {
    ...vuexfireMutations,
    updateIsSignedIn(state, isSignedIn) {
      state.isSignedIn = isSignedIn;
    },
    updateUserName(state,  userName) {
      state.userName = userName;
    },
    userReviewsAmount(state,user,  amount){
      state.users.amount = state.users.amount + amount

    },
    // To ponizej to syf
    deviceStatus(state, amount){
      try{
      state.lifts[0][amount ]= amount
    }
      catch(e){
        console.log(e)
      }
      console.log(state.lifts[0])
    }
  },
  actions: {
    bindLiftsRef: firestoreAction(({ bindFirestoreRef }) => {
      return bindFirestoreRef("lifts", db.collection("devices"));
    }),

    bindReviewRef: firestoreAction(({ bindFirestoreRef }) => {
      return bindFirestoreRef("review", db.collection("reviews"));
    }),
    bindUsersRef: firestoreAction(({ bindFirestoreRef }) => {
      return bindFirestoreRef("users", db.collection("users"));
    }),
  },
  modules: {},
});
