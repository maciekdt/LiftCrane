<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="review"
      :sort-by="['date']"
      sort-desc
      multi-sort
      :items-per-page=50
      class="elevation-1"
      :item-class="itemClass"
      :search="search"
    >
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>Serwisy</v-toolbar-title>
          <div v-if="newReviews > 0">
            <v-btn @click="setSeen()" class="px-2 mx-3"
              >{{ newReviews }} odczytaj</v-btn
            >
          </div>
          <div v-else>
            <v-btn disabled class="px-2 mx-3">Brak nowych zgłoszeń</v-btn>
          </div>
          <v-spacer></v-spacer>
          <!-- Download do XLS -->
          <v-btn class="ma-2">
            <export-excel :data="review" :fields="fields" name="Zgloszenia.xls">
              Eksportuj do xls
              <v-icon>file_download</v-icon>
            </export-excel>
          </v-btn>
          <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            label="Szukaj"
            single-line
            hide-details
          ></v-text-field>
          <v-dialog v-model="dialogDelete" max-width="500px">
            <v-card>
              <v-card-title class="text-h5 white--text primary"
                >Usuń wpis</v-card-title
              >
              <v-card-text class="pa-2"
                >Czy napewno chcesz usunąć ten wpis?</v-card-text
              >
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeDelete">Anuluj</v-btn>
                <v-btn color="blue darken-1" @click="deleteItemConfirm">Usuń</v-btn>
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>
      <template v-slot:item.photos="{ item }">
        <label> {{ item.images.length }}</label>
        <v-icon v-if="item.images.length == 0" disabled> image </v-icon>
        <v-icon v-else @click="getImages(item)"> image </v-icon>
      </template>
      <template v-slot:item.actions="{ item }">
        <v-icon @click="deleteItem(item)"> delete_forever </v-icon>
      </template>
      <template v-slot:item.date="{ item }">
        {{item.date
      .toDate().toLocaleString("pl-PL", {
          day: "2-digit",
          month: "long",
          year: "2-digit",
          hour: "numeric",
          minute: "numeric"
        })
        }}
      </template>
      <template v-slot:item.malfunction="{ item }">
        <v-chip :color="getColor(item.malfunction)">
          {{ item.malfunction ? "Naprawa" : "" }}
        </v-chip>
      </template>
      <template v-slot:item.liftName="{ item }">
        <p :class="itemClass(item)">{{ item.liftName }}</p>
      </template>
      <template v-slot:item.dtr="{ item }">
        <v-chip>
          {{ item.dtr ? "DTR" : "" }}
        </v-chip>
      </template>
      <!-- <template v-slot:item.udt="{ item }">
        <v-chip>
          {{ item.udt ? "✓" : "" }}
        </v-chip>
      </template> -->
      <template v-slot:item.udt="{ item }">
        <v-chip :color="serviceTypeField(item)[1]">
          {{ serviceTypeField(item)[0]}}
        </v-chip>
      </template>
      <template v-slot:item.seen="{ item }">
        <v-chip @click="setSeenOne(item)">
          {{ item.seen ? "✓" : "★" }}
        </v-chip>
      </template>
    </v-data-table>
    <v-overlay :value="imagesLoadingOverlay">
      <v-progress-circular indeterminate size="64"></v-progress-circular>
    </v-overlay>
  </div>
</template>

<script>
import { db, imagesRef } from "../fb.js";
import { mapState } from "vuex";
import excel from "vue-excel-export";
import Vue from "vue";

import "viewerjs/dist/viewer.css";
// import excel from "vue-excel-export";
Vue.use(excel);
export default {
  // components: { deletePopup },
  data() {
    return {
      dialogDelete: false,
      search: "",
      headers: [
        { text: "Odczytano", value: "seen", align: "start" },
        { text: "Info", value: "info", align: "start" },
        {
          text: "Wpis",
          align: "left",
          value: "liftName",
          sortable: false,
        },

        { text: "Miesiac ", value: "date", align: "right" },
        // { text: "DTR", value: "dtr", align: "right" },
        { text: "Typ", value: "udt", align: "right" },
        { text: "Naprawa", value: "malfunction", align: "right" },
        { text: "Serwisant ", value: "reviewerName", align: "right" },
        { text: "Opis ", value: "description", align: "right" },
        { text: "Zdjęcia ", value: "photos", align: "right" },
        { text: "Usuń ", value: "actions", align: "right", sortable: false },
      ],
      fields: {
        Nazwa: "liftName",
        Data: "date",
        Przegląd: "dtr",
        Awaria: "malfunction",
        UDT: "udt",
        Serwisant: "reviewerName",
        Opis: "description",
      },

      loader: false,
      editedIndex: -1,
      imagesLoadingOverlay: false,
      infoItems: [{ name: "Nowy", value: "red" },
    {name:"Ważne",value:"orange"}],
    };
  },
  methods: {
    //Zmien zeby nie zmienialo wszystkich!!!!
    // itemRowColor (item){
    //   return item.info == "red" ? 'style-1' : 'style-2'
    // },
    serviceTypeField(item){
      let text = "";
      let color = ""
      if(item.dtr){
        text = "DTR"
        color = "blue"
      }
      if(item.udt){
        text = "UDT"
        color = "pink"
      }
      return [text, color];
    },
    setSeen() {
      this.review.forEach((element) => {
        if (element.seen == false) {
          db.collection("reviews").doc(element.id).update({
            seen: true,
          });
        } else {
          console.log(element.id);
        }
      });
    },
    setSeenOne(item) {
      let seen = item.seen;
      db.collection("reviews")
        .doc(item.id)
        .update({
          seen: !seen,
        })
        .then(console.log(item.id + "set seen!"));
    },
    setInfo(item, info) {
      db.collection("reviews")
        .doc(item.id)
        .set({
          info: info,
        })
        .then(console.log(item.id + "set info" + this.info));
    },
    getColor(mal) {
      if (mal == false) return "light-green";
      else if (mal == true) return "orange";
      else return "orange";
    },
    // font-weight-regular font-weight-black
    itemClass(item) {
      var returnedClass = "";
      if (item.info == "red")
        returnedClass = returnedClass + "background-color: red lighten-2 ";
      if (item.seen == false)
        returnedClass = returnedClass + "font-weight-bold ";
      else if (item.seen == true)
        returnedClass = returnedClass + "font-weight-regular ";
      return returnedClass;
    },
    deleteItem(item) {
      this.editedIndex = item.id;
      this.dialogDelete = true;
    },

    deleteItemConfirm() {
      db.collection("reviews")
        .doc(this.editedIndex)
        .delete()
        .then(() => {
          console.log("Document successfully deleted!" + this.editedIndex);
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
    getImages(item) {
      console.log(item.images);
      var images = [];
      this.imagesLoadingOverlay = true;
      async function getList() {
        for await (const element of item.images) {
          await imagesRef
            .child(element)
            .getDownloadURL()
            .then((url) => {
              images.push({ src: url, href: url, alt: "" });
            });
          console.log("1");
        }
        console.log("2");

        // this.$viewerApi({
        //   images: this.images,
        // });
        return 1;
      }

      getList()
        .then((response) => {
          console.log(response + "hej");
          this.imagesLoadingOverlay = false;

          this.$viewerApi({
            images: images,
          });
        })
        .catch((e) => {
          console.log(e);
        });
      console.log(images);
      ///)

      // item.images.forEach(element => {
      //   imagesRef.child(element).getDownloadURL().then((url) => {images.push({'src':url,"href":url,"alt":''})});
      //   this.$viewerApi({
      //     images: images,
      //   })
      // });
    },
  },
  mounted() {},
  created() {},
  computed: {
    ...mapState(["review"]),
    newReviews() {
      let val = 0;
      this.review.forEach((element) => {
        if (element.seen == false) {
          val++;
        }
      });
      console.log(val);
      return val;
    },
  },
  watch: {
    dialogDelete(val) {
      val || this.closeDelete();
    },
  },
};
</script>

<style>
tr:nth-child(even) {
  background-color: #e3f2fd;
}

tr:hover {
  /* background-color: #90CAF9 !important; */
  background-color: rgb(201, 201, 201) !important;
  filter: brightness(90%);
}
.v-select__selections{
  width: 16px !important;
}
</style>
