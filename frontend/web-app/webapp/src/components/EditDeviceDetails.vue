<template>
  <div class="">
    <v-dialog v-model="dialog" width="500">
      <template v-slot:activator="{ on, attrs }">
        <!-- <v-btn color="red lighten-2" dark v-bind="attrs" v-on="on">
          Click Me
        </v-btn> -->
        <v-icon small @click="editItem(item)" v-bind="attrs" v-on="on">
          edit
        </v-icon>
      </template>

      <v-card>
        <v-card-title class="text-h5 primary white--text">
          Edytuj: {{ name }}
        </v-card-title>

        <v-container fluid>
          <v-text-field v-model="name" label="Nazwa"></v-text-field>

          <v-textarea
            v-model="loc"
            label="Lokalizacja"
            auto-grow
            rows="3"
          ></v-textarea>

          <v-text-field
            v-model="nrfab"
            label="Numer ewidencyjny"
          ></v-text-field>
           <v-text-field
            v-model="udt"
            label="Numer UDT"
          ></v-text-field>
           <v-text-field
            v-model="prod"
            label="Producent"
          ></v-text-field>
           <v-text-field
            v-model="kg"
            label="Udźwig"
          ></v-text-field>
        </v-container>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red lighten-1" text @click="dialog = false">
            Anuluj
          </v-btn>
          <v-btn color="primary" text @click="(dialog = false), updateInfo()">
            Zatwierdź zmiany
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { db } from "@/fb.js";

export default {
  data() {
    return {
      dialog: false,
    };
  },
  methods: {
    updateInfo() {
      db.collection("devices").doc(this.id).update({
        name: this.name,
        loc: this.loc,
        nrfab: this.nrfab,
        udt: this.udt,
        prod: this.prod,
        kg: this.kg
      });
    },
  },
  props: {
    id: String,
    nrfab: String,
    name: String,
    loc: String,
    udt: String,
    prod: String,
    kg: String
  },
  // watch: {
  //     liftId: function () {
  //         this.raports = [];
  //         this.getInfo();
  //     },
  // },
};
</script>
