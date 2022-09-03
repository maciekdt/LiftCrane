import Vue from "vue";
import Vuex from "vuex";
import { db } from "@/fb";
import { vuexfireMutations, firestoreAction } from "vuexfire";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    lifts: [],
    review: [],
  },
  mutations: { ...vuexfireMutations },
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
