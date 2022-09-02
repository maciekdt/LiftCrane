<template>
  <div class="">
    <Snackbar :snackbar="true"></Snackbar>
    <v-dialog v-model="dialog" width="90%">
      <template v-slot:activator="{ on, attrs }">
        <!-- <v-btn color="red lighten-2" dark v-bind="attrs" v-on="on">
          Click Me
        </v-btn> -->
        <!-- <v-icon small @click="getInfo()" v-bind="attrs" v-on="on">
          {{name}}
        </v-icon> -->
        <chip @click="getInfo()" v-bind="attrs" v-on="on">{{name}}</chip>
      </template>

      <v-card>
        <v-card-title class="text-h5 primary white--text">
          Drukuj dziennik dla {{ name }}
        </v-card-title>
        <v-container class="ma-2">
          <v-row>
            <v-col :key="1" cols="4">
              <v-card>
                <v-text-field
                  dense
                  disabled
                  v-model="name"
                  label="Nazwa"
                ></v-text-field>

                <v-textarea
                  dense
                  disabled
                  v-model="loc"
                  label="Lokalizacja"
                  auto-grow
                  rows="3"
                ></v-textarea>

                <v-text-field
                  disabled
                  v-model="nrfab"
                  label="Numer ewidencyjny"
                ></v-text-field>
                <v-text-field
                  disabled
                  dense
                  v-model="udt"
                  label="Numer UDT"
                ></v-text-field>
                <v-text-field
                  dense
                  disabled
                  v-model="prod"
                  label="Producent"
                ></v-text-field>
                <v-text-field
                  dense
                  disabled
                  v-model="kg"
                  label="Udźwig"
                ></v-text-field>
              </v-card>
            </v-col>
            <v-col :key="2" col="8">
              <v-card>
                <v-container fluid>
                  <v-row>
                    <v-col cols="12">
                      <v-combobox
                        v-model="select"
                        :items="months"
                        label="Data"
                        multiple
                        outlined
                        dense
                      ></v-combobox>
                    </v-col>
                  </v-row>
                </v-container>
                <v-data-table :items="raports" :headers="headers">
                  <template v-slot:item.date="{ item }">
                    {{item.date}}
                  </template>
                </v-data-table>
              </v-card>
            </v-col>
          </v-row>
        </v-container>
        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red lighten-1" text @click="dialog = false">
            Anuluj
          </v-btn>
          <v-btn color="primary" text @click="dialog = false"> Drukuj </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { db } from "@/fb.js";
import Snackbar from "./Snackbar.vue";

export default {
  data() {
    return {
      dialog: false,
      select: [],
        months: [
          'Styczeń',
          'Luty',
          'Marzec',
          'Kwiecień',
          'Maj',
          'Czewriec',
          'Lipiec',
          'Sierpień',
          'Wrzesień',
          'Październik',
          'Listopad',
          'Grudzień',
        ],
      // headers: [
      //   {
      //     text: "Data",
      //     align: "start",
      //     value: "date",
      //   },
      //   { text: "Stan", value: "malfunction" },
      //   { text: "Konserwator", value: "reviewerName" },
      //   { text: "Opis", value: "description" },
      // ],
      raports: [],
    };
  },
  methods: {
    getInfo: function () {
      console.log(this.liftId);
      this.raports.length = 0;
      db.collection("reviews")
        .where("liftId", "==", this.id)
        .get()
        .then((querySnapshot) => {
          querySnapshot.forEach((doc) => {
            // doc.data() is never undefined for query doc snapshots
            console.log(doc.id, " => ", doc.data());
            this.raports.push({
              date: doc.data().date.toDate()
              // .toDate().toLocaleString("pl-PL", {
              //   day: "2-digit",
              //   month: "long",
              //   year: "numeric",
              // }),
              ,
              malfunction: doc.data().malfunction ? "Awaria" : "Sprawny",
              reviewerName: doc.data().reviewerName,
              description: doc.data().description,
            });
          });
          console.log("hejka");
          
              
        })
        .catch((error) => {
          console.log("Error getting documents: ", error);
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
    kg: String,
  },
  // watch: {
  //     liftId: function () {
  //         this.raports = [];
  //         this.getInfo();
  //     },
  // },
  components: { Snackbar },
  created() {
    console.log("print mounted");
  },
  watch: {
    id: function () {
      this.getInfo();
    },
  },
  computed: {
    headers (){
      return[
        
          {
          text: "Data",
          align: "start",
          value: "date",
          filter: value => {
            if(!this.select ) return true
            return value +'bababoey'
          }
        },
        { text: "Stan", value: "malfunction" },
        { text: "Konserwator", value: "reviewerName" },
        { text: "Opis", value: "description" },
        
      ]
    }
  }
};
</script>
