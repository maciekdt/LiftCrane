<template>
  <v-card>
    <v-card-title>
      Pracownicy
      <v-spacer></v-spacer>
      <v-text-field
          v-model="search"
          append-icon="mdi-magnify"
          label="Szukaj"
          single-line
          hide-details
      ></v-text-field>
    </v-card-title>
    <v-data-table
        :headers="headers"
        :items="users"
        :search="search"
    ></v-data-table>
  </v-card>
</template>

<script>
import {mapMutations, mapState} from "vuex";

export default {
  data() {
    return {
      search: "",
      headers: [
        {
          text: "Nazwisko",
          align: "start",
          value: "lastName",
        },
        {text: "Imię", value: "firstName"},
        {text: "Email", value: "email"},
        {text: "Pełny dostęp", value: "access"},
        {text: "Ilość serwisów", value: "amount"},
      ],
    };
  },
  methods: {
    ...mapMutations(["userReviewsAmount"]),
  },
  mounted() {
  },
  created() {
    this.$store.dispatch("bindUsersRef").then(() => {
      console.log("Users created and db dispatched");
      this.loader = false;
    });
    this.userReviewsAmount(5)
  },
  computed: {
    ...mapState(["users"]),


  },
};
</script>
