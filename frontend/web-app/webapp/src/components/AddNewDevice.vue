<template>
  <div class="">
    <v-dialog v-model="dialog" width="50%">
      <template v-slot:activator="{ on, attrs }">
        <v-btn class="primary" dark v-bind="attrs" v-on="on" @click="clear">
          <v-icon>add</v-icon>
          Dodaj nowe urządzenie
        </v-btn>
      </template>

      <v-card>
        <v-card-title class="text-h5 primary white--text">
          Dodaj nowe urządzenie
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
          <v-text-field v-model="udt" label="Numer UDT"></v-text-field>
          <v-text-field v-model="prod" label="Producent"></v-text-field>
          <v-text-field v-model="kg" label="Udźwig"></v-text-field>
        </v-container>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red lighten-1" text @click="dialog = false">
            Anuluj
          </v-btn>
          <v-btn color="primary" text @click="(dialog = false), addDevice()">
            Zatwierdź
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
      newDevice: [],
      name: "",
      loc: "",
      nrfab: "",
      udt: "",
      prod: "",
      kg: "",
    };
  },
  methods: {
    addDevice: function () {
      console.log(this.liftId);
      db.collection("devices")
        .add({
          name: this.name,
          loc: this.loc,
          nrfab: this.nrfab,
          udt: this.udt,
          prod: this.prod,
          kg: this.kg,
        })
        .then(() => {
          console.log("Dodane nowe urzadzenie");
        });
    },
    clear: function () {
      this.name = "";
      this.loc =  "";
      this.nrfab =  "";
      this.udt = "";
      this.prod = "";
      this.kg = "";
    }
  },
  props: {},
  created() {
    console.log("addnewdevice mounted");
  },
  watch: {},
  computed: {},
};
</script>
