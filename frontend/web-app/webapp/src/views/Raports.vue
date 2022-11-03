<template>
  <div>
  <v-data-table
  :headers="headers"
  :items="review"
  :sort-by="['date']"
  sort-desc
  multi-sort
  items-per-page="50"
  class="elevation-1"
  :item-class="isNew"
  >
  <template v-slot:top>
    <v-toolbar flat>
      <v-toolbar-title>Serwisy</v-toolbar-title>
      <div v-if="newReviews > 0">
      <v-btn @click="setSeen()" class="px-2 mx-3">{{newReviews}} odczytaj</v-btn>
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
              <v-btn color="blue darken-1" @click="closeDelete">Nie</v-btn>
              <v-btn color="blue darken-1" text @click="deleteItemConfirm"
              >Tak</v-btn
              >
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
      {{item.date.toDate().toLocaleString("pl-PL", {
        day: "2-digit",
        month: "long",
        year: "2-digit",
        hour: "numeric",
        minute: "numeric"
      }),
    }}
    </template>
    <template v-slot:item.malfunction="{ item }">
      <v-chip :color="getColor(item.malfunction)">
        {{ item.malfunction ? "Awaria!" : "Brak" }}
      </v-chip>
    </template>
    <template v-slot:item.liftName="{ item }">
      <p :class="isNew(item)">{{ item.liftName }}</p>
    </template>
    <template v-slot:item.dtr="{ item }">
      <v-chip>
        {{ item.dtr ? "Tak" : "Nie" }}
      </v-chip>
    </template>
    <template v-slot:item.udt="{ item }">
      <v-chip>
        {{ item.udt ? "✓" : "" }}
      </v-chip>
    </template>
    <template v-slot:item.seen="{ item }">
      <v-chip @click="setSeenOne(item)">
        {{ item.seen ? "✓" : "★" }}
      </v-chip>
    </template>
    
  </v-data-table>
  <v-overlay :value="imagesLoadingOverlay">
      <v-progress-circular
        indeterminate
        size="64"
      ></v-progress-circular>
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

      headers: [
        { text: "Odczytano", value: "seen", align: "start" },
        {
          text: "Wpis",
          align: "left",
          value: "liftName",
          sortable: false,
        },

        { text: "Miesiac ", value: "date", align: "right" },
        { text: "DTR", value: "dtr", align: "right" },
        { text: "Awaria", value: "malfunction", align: "right" },
        { text: "UDT", value: "udt", align: "right" },
        { text: "Serwisant ", value: "reviewerName", align: "right" },
        { text: "Opis ", value: "description", align: "right" },
        { text: "Zdjęcia ", value: "photos", align: "right", sortable: false },
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

      loader: true,
      editedIndex: -1,
      imagesLoadingOverlay: false,
    };
  },
  methods: {
    //Zmien zeby nie zmienialo wszystkich!!!!
    setSeen() {
      this.review.forEach((element) => {
        if (element.seen == false) {
          db.collection("reviews").doc(element.id).update({
            seen: true,
          });
        } else {
          console.log(element.id);
        }
        // db.collection("reviews")
        //   .get()
        //   .then(function (querySnapshot) {
        //     querySnapshot.forEach(function (doc) {
        //       doc.ref.update({ seen: true });
        //     });
        //   });
      });
    },
    setSeenOne(item){
      let seen = item.seen
      db.collection("reviews").doc(item.id).update({
            seen: !seen,
          }).then(
            console.log(item.id + "set seen!")
          );
    },
    getColor(mal) {
      if (mal == false) return "green";
      else if (mal == true) return "red";
      else return "orange";
    },
    // font-weight-regular font-weight-black
    isNew(item) {
      if (item.seen == false) return "font-weight-bold";
      else if (item.seen == true) return "font-weight-regular";
      else return "orange";
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
  created() {
    this.$store.dispatch("bindReviewRef").then(() => {
      console.log("Created and dispatched");
      this.loader = false;
    });
  },
  computed: {
    ...mapState(["review"]),
    newReviews(){
      let val = 0;
      this.review.forEach(element => {
        if(element.seen == false){val++}
      });
      console.log(val)
      return val;
    }
  },
  watch: {
    dialogDelete(val) {
      val || this.closeDelete();
    },
  },
};
</script>
