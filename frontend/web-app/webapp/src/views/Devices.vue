<template>
  <v-card>
    <v-card-title>
      Urządzenia
      <v-spacer></v-spacer>
      <v-dialog secondary v-model="dialogDelete" max-width="500px">
        <v-card>
          <v-card-title class="text-h5 white--text primary"
            >Usuń wpis</v-card-title
          >
          <v-card-text class="pa-2"
            >Czy napewno chcesz usunąć ten wpis?</v-card-text
          >
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="closeDelete"
              >Anuluj</v-btn
            >
            <v-btn color="blue darken-1" @click="deleteItemConfirm">Usuń</v-btn>
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
      <!-- <v-btn class="ma-2"> Genweruj QR kody </v-btn> -->
      <AddNewDevice />
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
      :items-per-page="20"
      :server-items-length="liftsCount"
      :item-class="itemClass"
      :options.sync="options"
      @update:page="loadMore"
      loading-text="Pobieranie danych, proszę czekać"
    >
      <template v-slot:item.name="{ item }">
        <v-btn text>
          <PrintDeviceRaports
            :id="item.id"
            :nrfab="item.nrfab"
            :name="item.name"
            :loc="item.loc"
            :prod="item.prod"
            :udt="item.udt"
            :maulfunction="item.malfunction"
            :dtr="item.dtr"
            :kg="item.kg"
            :working="item.working"
          />
        </v-btn>
      </template>
      <!--      <template v-slot:item.status ="{item}">-->
      <!--         <v-chip pill>{{item.status ? color="red" : "Nie"}}<v-icon>check</v-icon></v-chip>-->
      <!--      </template>-->
      <template end v-slot:item.edit="{ item }">
        <EditDeviceDetails
          :id="item.id"
          :nrfab="item.nrfab"
          :name="item.name"
          :loc="item.loc"
          :prod="item.prod"
          :udt="item.udt"
          :udtTime="item.udtTime"
          :kg="item.kg"
          :working="item.working"
        />
        <!-- <PrintDeviceRaports :id="item.id" :nrfab="item.nrfab" :name="item.name" :loc="item.loc" :prod="item.prod" :udt="item.udt" :kg="item.kg"/> -->
        <!-- <v-icon small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon> -->
        <!-- <v-icon small class="mr-2" @click="generateQrCode(item)">
          qr_code_2
        </v-icon> -->
        <v-icon small class="mr-2" @click="deleteItem(item)"> delete </v-icon>
      </template>
      <!-- <template v-slot:expanded-item="{ headers, item }">
        <td :colspan="headers.length" class="pa-0 pt-1 ps-1 elevation-5">
          <deviceDetails :liftId="item.id" />
        </td>
      </template> -->
    </v-data-table>
    <v-btn block @click="loadMore()">Załaduj więcej</v-btn>
  </v-card>
</template>

<script>
import "material-design-icons-iconfont/dist/material-design-icons.css";
import Vue from "vue";
import excel from "vue-excel-export";
// import deviceDetails from "../components/deviceDetails";
import EditDeviceDetails from "../components/EditDeviceDetails.vue";
// import { mapState, mapMutations } from "vuex";
import { db } from "@/fb";
import PrintDeviceRaports from "../components/PrintDeviceRaports.vue";
import AddNewDevice from "../components/AddNewDevice.vue";
// import QrcodeVue from 'qrcode.vue'
Vue.use(excel);
export default {
  name: "Table",
  data() {
    return {
      lifts: [],
      liftsCount: 7,
      options: {},
      // search: "",
      headers: [
        // { text: "Stan", align: "start", value: "state" },
        {
          text: "Nazwa",
          // align: "start",
          value: "name",
        },
        { text: "id", value: "id" },
        { text: "Stan windy", value: "status" },
        { text: "Lokalizacja", value: "loc" },
        { text: "nr fabryczny", value: "nrfab" },
        { text: "Nr UDT", value: "udt" },
        { text: "co ile lat udt", value: "udtTime" },
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
      loader: false,
      singleExpand: true,
      editedIndex: -1,
      dialogDelete: false,
      dialogEdit: false,
      dialogPrint: false,

      lastVisable: 0,
    };
  },
  methods: {
    // ...mapMutations(["deviceStatus"]),

    // printItem(item){
    //   this.dialogPrint = true
    // },
    checkDeviceStatus() {
      this.deviceStatus(5);
    },
    editItem(item) {
      this.editedIndex = item.id;
      this.dialogEdit = true;
    },
    deleteItem(item) {
      this.editedIndex = item.id;
      this.dialogDelete = true;
    },
    deleteItemConfirm() {
      db.collection("devices")
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
    itemClass(item) {
      let returnedClass = "";
      if (item.working === false)
        returnedClass = returnedClass + "background-color: red lighten-2 ";
      return returnedClass;
    },
    firstQuery() {
      this.loader = true;
      db.collection("devices")
        .get()
        .then((snapshot) => (this.liftsCount = snapshot.size));
      db.collection("devices")
        .orderBy("name", "desc")
        .limit(5)
        .get()
        .then((querySnapshot) =>
          querySnapshot.forEach(
            (doc) => {
              this.lifts.push(doc.data());
              console.log(doc.id);
            },
            (this.lastVisable =
              querySnapshot.docs[querySnapshot.docs.length - 1]),
            console.log("last: ", this.lastVisable)
          )
        );
      this.loader = false;
    },
    loadMore() {
      this.loader = true;
      console.log("Load clicked");
      db.collection("devices")
        .orderBy("name", "desc")
        .limit(5)
        .startAfter(this.lastVisable)
        .get()
        .then((querySnapshot) =>
          querySnapshot.forEach((doc) => {
            this.lifts.push(doc.data());
            console.log(doc.id);
          }, (this.lastVisable = querySnapshot.docs[querySnapshot.docs.length - 1]))
        );
      this.loader = false;
    },
  },
  mounted() {},
  created() {
    this.firstQuery();
    //slow, not paginated
    // this.$store.dispatch("bindLiftsRef").then(() => {
    //   console.log("Created and dispatched"), (this.loader = false);
    // });
  },
  computed: {
    // ...mapState(["lifts"]),
  },
  watch: {
    dialogDelete(val) {
      val || this.closeDelete();
    },
  },
  components: {
    // deviceDetails,
    EditDeviceDetails,
    PrintDeviceRaports,
    AddNewDevice,
  },
  props: {
    search: String,
  },
};
</script>
