<template>
  <v-card>
    <v-card-title>
      Urządzenia
      <v-spacer></v-spacer>
      <v-btn class="ma-2">
      <export-excel
    :data="device"
    :fields="fields"
    name="Urzadzenia.xls"
    >
    Eksportuj do xls
    <v-icon>file_download</v-icon>
</export-excel>
</v-btn>
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
      :loading='loader'
      loading-text="Pobieranie danych, proszę czekać"
      :single-expand="singleExpand"
      :expanded.sync="expanded"
      show-expand
    >
    <template v-slot:item.status="{ item }">
    <v-simple-checkbox v-model="item.status" disabled></v-simple-checkbox>
    </template>
    </v-data-table>
  </v-card>
</template>

<script>
import { db } from '../fb.js'
import 'material-design-icons-iconfont/dist/material-design-icons.css'
import Vue from 'vue'
import excel from 'vue-excel-export'
 
Vue.use(excel)


  export default { 
    name: 'Table',
    data () {
      return {
        search: '',
        headers: [
          {
            text: 'Nazwa',
            align: 'start',
            sortable: false,
            value: 'name',
          },
          { text: 'ID', value: 'id' },
          { text: 'Lokalizacja', value: 'loc' },
          { text: 'Status', value: 'status' },
        ],
        fields: {
          'Nazwa' : 'name',
          'ID' : 'id',
          'Lokalizacja' : 'loc',
          'Status' : 'status'
        },
        device: [],
        loader: true,
        singleExpand: true,
        
      }
    },
    methods: {
      
        getAll: function(){
          db.collection("lifts").get().then((querySnapshot) => {
          querySnapshot.forEach((doc) => {
          // console.log(`${doc.id} => ${doc.data().email}`);          
          this.device.push({
            name: doc.data().Nazwa, 
            id: doc.data().ID,
            loc: doc.data().Adres});
          });
          }).finally(()=>{this.loader = false});
        }

        // getRaport: function(){
        //   var docRef = db.collection("reviews").doc("DmGvlWASjZnBK1hmCoD5");

        // docRef.get().then((doc) => {
        // if (doc.exists) {
        // console.log("Document data:", doc.data());
        // } else {
        // // doc.data() will be undefined in this case
        // console.log("No such document!");
        // }
        // }).catch((error) => {
        //  console.log("Error getting document:", error);
        //   });
        // }
    },
    mounted() {
        this.getAll()
        console.log('Mounted, getAll data from firebase')
      }
}
</script>