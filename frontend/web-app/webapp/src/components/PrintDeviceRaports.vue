<template>
  <div class="">
    <v-dialog v-model="dialog" width="90%">
      <template v-slot:activator="{ on, attrs }">
        <!-- <v-btn color="red lighten-2" dark v-bind="attrs" v-on="on">
          Click Me
        </v-btn> -->
        <!-- <v-icon small @click="getInfo()" v-bind="attrs" v-on="on">
          {{name}}
        </v-icon> -->
        <v-btn @click="getInfo()" v-bind="attrs" v-on="on">{{ name }}</v-btn>
      </template>

      <v-card>
        <v-card-title class="text-h5 primary white--text">
          Drukuj dziennik dla {{ name }}
        </v-card-title>
        <v-container class="ma-2">
          <v-row>
            <v-col :key="1" cols="6">
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
            <v-col :key="2" col="6" id="printMe">
              <v-card>
                <v-container fluid>
                  <v-row>
                    <v-col cols="6">
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
                <v-data-table
                  v-model="selected"
                  show-select
                  :items="raports"
                  :headers="headers"
                  item-key="id"
                >
                  <!-- <template v-slot:item.date="{ item }">
                    {{item.date}}
                  </template> -->
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
          <v-btn color="primary" text @click="generatePdf"> Drukuj </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { db } from "@/fb.js";
import jsPDF from "jspdf";
import "jspdf-autotable";
import { font } from "@/calibri-normal.js";

export default {
  data() {
    return {
      dialog: false,
      select: [],
      months: [
        "Styczeń",
        "Luty",
        "Marzec",
        "Kwiecień",
        "Maj",
        "Czewriec",
        "Lipiec",
        "Sierpień",
        "Wrzesień",
        "Październik",
        "Listopad",
        "Grudzień",
      ],
      headers: [
        {
          text: "Data",
          align: "start",
          value: "date",
        },
        { text: "Data", value: "date", align: "right" },
        { text: "DTR", value: "dtr", align: "right" },
        { text: "Awaria", value: "malfunction", align: "right" },
        { text: "UDT", value: "udt", align: "right" },
        { text: "Serwisant ", value: "reviewerName", align: "right" },
        { text: "Opis ", value: "description", align: "right" },
      ],
      pdfHeaders: [
        { title: "Id", dataKey: "id" },
        { title: "Data", dataKey: "date" },
        { title: "Awaria", dataKey: "malfunction" },
        { title: "Przegląd", dataKey: "dtr" },
        { title: "UDT", dataKey: "udt" },
        { title: "Konserwator", dataKey: "reviewerName" },
        { title: "Opis", dataKey: "description" },
      ],
      raports: [],
      selected: [],
      info: [],
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
              date: doc.data().date.toDate().toLocaleString("pl-PL", {
                day: "2-digit",
                month: "long",
                year: "numeric",
              }),

              malfunction: doc.data().malfunction ? "Awaria" : "Sprawny",
              dtr: doc.data().dtr ? "Przegląd" : "Brak",
              udt: doc.data().udt ? "Przegląd" : "",
              reviewerName: doc.data().reviewerName,
              description: doc.data().description,
              id: doc.id,
            });
          });
          console.log("hejka");
        })
        .catch((error) => {
          console.log("Error getting documents: ", error);
        });
    },
    generatePdf() {
      let i = 0;
      this.selected.forEach((element) => {
        element["id"] = (i + 1).toString();
        i = i + 1;
        this.info.push([
          element.id,
          element.date,
          element.dtr,
          element.malfunction,
          element.udt,
          element.reviewerName,
          element.description,
        ]);
      });
      console.log(this.selected);
      let pdfName = "raport_" + (Date.now() % 10000);
      var doc = new jsPDF({
        putOnlyUsedFonts: true,
        orientation: "p",
        format: "a4",
      });

      doc.addFileToVFS("calibri-normal.ttf", font);
      doc.addFont("calibri-normal.ttf", "calibri", "normal");
      doc.setFont("calibri");

      doc.text("Raporty dla " + this.name, 10, 10);
      doc.setLineWidth(0.1);
      doc.setDrawColor(0, 0, 0);
      doc.setLineDash([2.5]);
      doc.line(5, 15, 180, 15);
      doc.setLineWidth(0.25);
      doc.setLineDash([0]);
      doc.autoTable({
        head: [
          [
            "ID",
            "Data",
            "Serwis z. z DTR",
            "Awaria",
            "UDT",
            "Konserwator",
            "Opis",
          ],
        ],
        body: this.info,
        startY: 30,
        styles: {
          font: "calibri", // <-- place name of your font here
          fontStyle: "normal",
        },
      });
      doc.save(pdfName + ".pdf");
    },
  },
  props: {
    id: String,
    nrfab: String,
    name: String,
    loc: String,
    udt: String,
    malfunction: String,
    dtr: String,
    prod: String,
    kg: String,
  },
  // watch: {
  //     liftId: function () {
  //         this.raports = [];
  //         this.getInfo();
  //     },
  // },
  created() {
    console.log("print mounted");
  },
  watch: {
    id: function () {
      this.getInfo();
    },
  },
  computed: {
    // headers (){
    //   return[
    //       {
    //       text: "Data",
    //       align: "start",
    //       value: "date",
    //       // filter: value => {
    //       //   if(!this.select ) return true
    //       //   return value +'bababoey'
    //       // }
    //     },
    //     { text: "Stan", value: "malfunction" },
    //     { text: "Konserwator", value: "reviewerName" },
    //     { text: "Opis", value: "description" },
    //   ]
    // }
  },
};
</script>
