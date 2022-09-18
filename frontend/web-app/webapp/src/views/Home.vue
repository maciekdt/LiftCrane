<template>
  <div class="home">
    <v-row justify="center" class="ma-2">
    <v-img
    src="https://firebasestorage.googleapis.com/v0/b/liftcrane-bfea5.appspot.com/o/logo4.svg?alt=media&token=1b47f4d1-dd67-40c9-8618-8a6597448705"
    contain
    max-height="540"
    min-height="320"
    placeholder="logo"
    ></v-img>
    </v-row>
    <h1>Rozwiń listę po lewej by wybrać aktywność</h1>
    <div></div>
  </div>
</template>

<script>
  import { db } from "../fb.js";
export default {
  data() {
    return {
      userName: null,
      newRaports: 0
    }
  },
  methods: {
    showNewRaports () {
      console.log("clicked")
      db.collection("reviews").where("seen", '==', false).get().then((querySnapshot) => {
        querySnapshot.forEach(() => {
          this.newRaports = this.newRaports + 1;
          console.log("hej")
        })
    })
    }
},
  watch: {
  },
  mounted() {
    db.collection("raports").where("seen", '==', false).get().then((querySnapshot) => {
        querySnapshot.forEach(() => {
          this.newRaports = this.newRaports + 1
        })
    })

  },
  
  }
</script>
