<template>
  <v-app>
    <NavBar/>
    <v-content class="mx-4 mb-4 mt-4">
      <router-view></router-view>
      
    </v-content>
    <Footer />
  </v-app>
</template>

<script>
import NavBar from "@/components/NavBar";
import Footer from "@/components/Footer";
export default {
  name: "App",
  components: { NavBar, Footer },
  data() {
    return {};
  },
  methods: {},
  created(){
    document.title = 'LiftCrane';
    // this.$store.dispatch("bindReviewRef").then(() => {
    //   console.log("Created and dispatched");
    //   this.loader = false;
    // });
    //     this.$store.dispatch("bindLiftsRef").then(() => {
    //   console.log("Created and dispatched"), (this.loader = false);
    // });
  }
};
</script>
