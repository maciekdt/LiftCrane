<template>
  <div class="text-center">
    <v-dialog v-model="dialog" width="500">
      <template v-slot:activator="{ on, attrs }">
        <v-btn color="red lighten-2" dark v-bind="attrs" v-on="on">
          Click Me
        </v-btn>
      </template>

      <v-card>
        <v-card-title class="text-h5 grey lighten-2">Usuń wpis</v-card-title>

        <v-card-text class="my-2">Czy napewno chcesz usunąć wpis?</v-card-text>
        <v-card-text class="my-2">{{liftId}} data: {{date}}</v-card-text>


        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" v-on:click="dialog = false"> Nie </v-btn>
          <v-btn color="primary" text v-on:click="deleteRev()"> Tak </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { db } from "@/fb";

export default {
  data() {
    return {
      dialog: false,
    };
  },
  methods: {
    deleteRev: function () {
      db.collection("reviews")
        .doc(this.id)
        .delete()
        .then(() => {
          console.log("Document successfully deleted!");
          this.dialog = false;
        })
        .catch((error) => {
          console.error("Error removing document: ", error);
          this.dialog = false;
        });
    },
  },
  props: {
      liftId : String,
      date : String,
      id : String
}
};
</script>
