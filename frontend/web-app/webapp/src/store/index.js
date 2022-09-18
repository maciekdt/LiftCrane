import Vue from "vue";
import Vuex from "vuex";
import { db } from "@/fb";
import { vuexfireMutations, firestoreAction } from "vuexfire";
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex);

export default new Vuex.Store({
  plugins: [
    createPersistedState({
      storage: window.sessionStorage
    })
  ],
  state: {
    lifts: [],
    review: [],
    isSignedIn: false,
  },
  mutations: {
    ...vuexfireMutations,
    updateIsSignedIn(state, isSignedIn) {
      state.isSignedIn = isSignedIn;
    },
  },
  actions: {
    bindLiftsRef: firestoreAction(({ bindFirestoreRef }) => {
      return bindFirestoreRef("lifts", db.collection("devices"));
    }),

    bindReviewRef: firestoreAction(({ bindFirestoreRef }) => {
      return bindFirestoreRef("review", db.collection("reviews"));
    }),
  },
  modules: {},
});
