
<template>
  <v-card>
    <v-card-title>
      Urządzenia
      <v-spacer></v-spacer>
      <v-dialog secondary v-model="dialogDelete" max-width="500px">
        <v-card>
          <v-card-title class="text-h5 grey lighten-2">Usuń wpis</v-card-title>
          <v-card-text class="pa-2"
            >Czy napewno chcesz usunąć ten wpis?</v-card-text
          >
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" @click="closeDelete">Nie</v-btn>
            <v-btn color="blue darken-1" text @click="deleteItemConfirm"
              >Tak</v-btn
            >
            
            <v-spacer></v-spacer>
          </v-card-actions>
        </v-card>
      </v-dialog>
      <v-btn class="ma-2">
        <export-excel :data="lifts" :fields="fields" name="Urzadzenia.xls">
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
      :items="lifts"
      :search="search"
      :loading="loader"
      loading-text="Pobieranie danych, proszę czekać"
      :single-expand="singleExpand"
      show-expand
    >
      <template end v-slot:item.edit="{ item }">
          <EditDeviceDetails :id="item.id" :nrfab="item.nrfab" :name="item.name" :loc="item.loc" :prod="item.prod" :udt="item.udt" :kg="item.kg"/>
        <!-- <v-icon small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon> -->
        <v-icon small class="mr-2" @click="generateQrCode(item)"> qr_code_2 </v-icon>
        <v-icon small class="mr-2" @click="deleteItem(item)"> delete </v-icon>
      </template>
      <template v-slot:expanded-item="{ headers, item }">
        <td :colspan="headers.length" class="pa-0 pt-1 ps-1 elevation-5">
          <deviceDetails :liftId="item.id" />
        </td>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
import "material-design-icons-iconfont/dist/material-design-icons.css";
import Vue from "vue";
import excel from "vue-excel-export";
import deviceDetails from "../components/deviceDetails";
import EditDeviceDetails from "../components/EditDeviceDetails.vue";
import { mapState } from "vuex";
import { db } from "../fb.js";
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
          value: "name",
        },
        { text: "Lokalizacja", value: "loc" },
        { text: "nr fabryczny", value: "nrfab" },
        { text: "Nr UDT", value: "udt" },
        { text: "producent", value: "prod" },
        { text: "udzwig", value: "kg" },
        { text: "Edytuj", value: "edit", sortable: false },
      ],
      fields: {
        Nazwa: "name",
        ID: "id",
        Lokalizacja: "loc",
        Status: "status",
      },
      loader: true,
      singleExpand: true,
      editedIndex: -1,
      dialogDelete: false,
      dialogEdit: false,
    };
  },
  methods: {
    editItem(item) {
            this.editedIndex = item.id;
            this.dialogEdit = true;
    },
    deleteItem(item) {
      this.editedIndex = item.id;
      this.dialogDelete = true;
    },
    deleteItemConfirm() {
      db.collection("lifts")
        .doc(this.editedIndex)
        .delete()
        .then(() => {
          console.log("Document successfully deleted!");
          this.dialog = false;
        })
        .catch((error) => {
          console.error("Error removing document: ", error);
          this.dialog = false;
        });
      this.closeDelete();
    },
    closeDelete() {
      this.dialogDelete = false;
    },
  },
  mounted() {},
  created() {
    this.$store.dispatch("bindLiftsRef").then(() => {
      console.log("Created and dispatched"), (this.loader = false);
    });
  },
  computed: {
    ...mapState(["lifts"]),
  },
  watch: {
    dialogDelete(val) {
      val || this.closeDelete();
    },
  },
  components: {
    deviceDetails,
    EditDeviceDetails
},
};
</script>
