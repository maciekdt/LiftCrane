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
import { db } from "../fb.js";
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
        { text: "Imię", value: "firstName" },
        { text: "Email", value: "email" },
        { text: "Pełny dostęp", value: "fullAccess" },
      ],
      users: [],
    };
  },
  methods: {
    getAll: function () {
      db.collection("users")
        .get()
        .then((querySnapshot) => {
          querySnapshot.forEach((doc) => {
            // console.log(`${doc.id} => ${doc.data().id}`);
            this.users.push({
              userId: doc.id,
              lastName: doc.data().lastName,
              firstName: doc.data().firstName,
              email: doc.data().email,
              fullAccess: doc.data().fullAccess,
            });
          });
        })
        .finally(() => {
          this.loader = false;
        });
    },
  },
  mounted() {
    this.getAll();
  },
};
</script>
