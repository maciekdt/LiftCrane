<template>
  <div class="">
    <v-dialog v-model="dialog" width="50%">
      <template v-slot:activator="{ on, attrs }">
        <v-btn class="primary" dark v-bind="attrs" v-on="on">
          <v-icon>note_add</v-icon>
          Dodaj nowe zgłoszenie {{ description }}
        </v-btn>
      </template>

      <v-card>
        <v-card-title class="text-h5 primary white--text">
          Dodaj nowe zgłoszenie
        </v-card-title>

        <v-container fluid>
          <v-text-field
              disabled
              v-model="liftName"
              label="Nazwa"
          ></v-text-field>
          <v-text-field
              disabled
              v-model="userName"
              label="Osoba"
          ></v-text-field>
          <v-text-field v-model="description" label="Opis"></v-text-field>
          <v-checkbox
              v-model="dtr"
              :label="'Przegląd zgodny z DTR'"
          ></v-checkbox>
          <v-checkbox
              v-model="udt"
              :label="'Przegląd UDT'"
          ></v-checkbox>
          <v-checkbox
              v-model="malfunction"
              :label="'Naprawa'"
          ></v-checkbox>
          <v-checkbox
              v-model="working"
              :label="'Sprawny'"
          ></v-checkbox>
          Data:<br/>
          <v-date-picker v-model="date"></v-date-picker>
        </v-container>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red lighten-1" text @click="dialog = false">
            Anuluj
          </v-btn>
          <v-btn color="primary" text @click="(dialog = false), addReview()">
            Zatwierdź
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
// import { db } from "@/fb.js";
import {mapState} from "vuex";
import {db, timestamp} from "@/fb";

export default {
  data() {
    return {
      dialog: false,
      dtr: false,
      udt: false,
      malfunction: false,
      description: "",
      date: "",
    };
  },
  methods: {
    addReview: function () {
      console.log(this.liftId);
      db.collection("reviews")
          .add({
            liftId: this.liftId,
            liftName: this.liftName,
            dtr: this.dtr,
            udt: this.udt,
            malfunction: this.malfunction,
            reviewerName: this.userName,
            working: this.working,
            date: timestamp.Timestamp.fromDate(new Date(this.date)),
            description: this.description,
            seen: false,
            images: [],

          })
          .then(() => {
            console.log("Dodane nowe zgłoszenie");
          });
    },
  },
  props: {liftName: String, liftId: String, working: Boolean},
  created() {
    console.log("addNewReview mounted");
  },
  computed: {
    ...mapState(["userName"]),
  },
};
</script>
