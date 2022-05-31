<template>
  <v-card>
    <v-card-title>
      Urządzenia
      <v-spacer></v-spacer>
      <v-btn class="ma-2">
        <export-excel :data="device" :fields="fields" name="Urzadzenia.xls">
          Eksportuj do xls
          <v-icon>file_download</v-icon>
        </export-excel>
      </v-btn>
      <v-btn class="ma-2"> Genweruj QR kody </v-btn>
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
      :items="device"
      :search="search"
      :loading="loader"
      loading-text="Pobieranie danych, proszę czekać"
      :single-expand="singleExpand"
      :expanded.sync="expanded"
      show-expand
    >
      <template v-slot:item.status="{ item }">
        <v-simple-checkbox v-model="item.status" disabled></v-simple-checkbox>
      </template>
      <template v-slot:expanded-item="{ headers, item }">
        <td :colspan="headers.length"  class="pa-0 pt-1 ps-1 elevation-5"><deviceDetails :liftId="item.id"/></td>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
import { db } from "../fb.js";
import "material-design-icons-iconfont/dist/material-design-icons.css";
import Vue from "vue";
import excel from "vue-excel-export";
import firebase from "firebase";
import deviceDetails from "../components/deviceDetails";
// import QrcodeVue from 'qrcode.vue'

Vue.use(excel);

export default {
  name: "Table",
  data() {
    return {
      search: "",
      headers: [
        {
          text: "Nazwa",
          align: "start",
          sortable: false,
          value: "name",
        },
        { text: "ID", value: "id" },
        { text: "Lokalizacja", value: "loc" },
        { text: "Status", value: "status" },
      ],
      fields: {
        Nazwa: "name",
        ID: "id",
        Lokalizacja: "loc",
        Status: "status",
      },
      device: [],
      loader: true,
      singleExpand: true,
    };
  },
  methods: {
    getAll: function () {
      db.collection("lifts")
        .get({ source: "cache" })
        .then((querySnapshot) => {
          querySnapshot.forEach((doc) => {
            // console.log(`${doc.id} => ${doc.data().email}`);
            // array.indexOf(newItem) === -1 ? array.push(newItem) : console.log("This item already exists");
            // this.device.push({

            console.log(doc.data());
            this.device.indexOf(doc.id) === -1
              ? this.device.push({
                  name: doc.data().Nazwa,
                  id: doc.data().ID,
                  loc: doc.data().Adres,
                })
              : console.log("Brak nowych!");
            var source = querySnapshot.metadata.fromCache
              ? "local cache"
              : "server";
            console.log("Data came from " + source);
          });
        })
        .finally(() => {
          this.loader = false;
          if (this.device.isEmpty) {
            db.collection("lifts")
              .get({ source: "server" })
              .then((querySnapshot) => {
                querySnapshot.forEach((doc) => {
                  console.log(doc.data());
                  this.device.indexOf(doc.data()) === -1
                    ? this.device.push({
                        name: doc.data().Nazwa,
                        id: doc.data().ID,
                        loc: doc.data().Adres,
                      })
                    : console.log("Brak nowych!");
                  var source = querySnapshot.metadata.fromCache
                    ? "local cache"
                    : "server";
                  console.log("Data came from " + source);
                });
              });
          }
        });
    },
  },
  created() {
    firebase
      .firestore()
      .enablePersistence()
      .catch((err) => {
        if (err.code == "failed-precondition") {
          // Multiple tabs open, persistence can only be enabled
          // in one tab at a a time.
          // ...
        } else if (err.code == "unimplemented") {
          // The current browser does not support all of the
          // features required to enable persistence
          // ...
        }
      });
    firebase.firestore().settings({
      cacheSizeBytes: firebase.firestore.CACHE_SIZE_UNLIMITED,
    });
  },
  mounted() {
        this.getAll();

  },
  components: {
    deviceDetails,
  },
};
</script>
