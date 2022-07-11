<template>
  <v-app>
    <NavBar />
    <v-content class="mx-4 mb-4 mt-4">
      <router-view></router-view>
          <Footer/>

    </v-content>
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
};
</script>
