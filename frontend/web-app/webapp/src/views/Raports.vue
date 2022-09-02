<template>
  <v-data-table
    :headers="headers"
    :items="review"
    :sort-by="['date', 'malfunction']"
    multi-sort
    items-per-page="50"
    class="elevation-1"
  >
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>Serwisy</v-toolbar-title>
        <v-btn @click="setSeen()">odczytano</v-btn>
        <v-spacer></v-spacer>
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
        {{ item.malfunction ? "Awaria" : "Sprawne" }}
      </v-chip>
    </template>
    <template v-slot:item.seen="{ item }">
      <v-chip>
        {{ item.seen ? "" : "!" }}
      </v-chip>
    </template>
  </v-data-table>
</template>

<script>
import { db, imagesRef } from "../fb.js";
import { mapState } from "vuex";
import "viewerjs/dist/viewer.css";

export default {
  // components: { deletePopup },
  data() {
    return {
      dialogDelete: false,

      image2: imagesRef
        .child("15c97b18-d4b4-421f-83da-e620e9df0654")
        .getDownloadURL()
        .then((url) => {
          this.images.push({ src: url, href: url, alt: "" });
        }),
      headers: [
        {
          text: "Wpis",
          align: "start",
          value: "liftName",
          sortable: false,
        },
        { text: "Alert", value: "seen", align: "left" },
        { text: "Miesiac ", value: "date", align: "right" },
        { text: "Status ", value: "malfunction", align: "right" },
        { text: "Serwisant ", value: "reviewerName", align: "right" },
        { text: "Opis ", value: "description", align: "right" },
        { text: "Zdjęcia ", value: "photos", align: "right", sortable: false },
        { text: "Usuń ", value: "actions", align: "right", sortable: false },
      ],

      loader: true,
      editedIndex: -1,
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
    getColor(mal) {
      if (mal == false) return "green";
      else if (mal == true) return "red";
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
  },
  watch: {
    dialogDelete(val) {
      val || this.closeDelete();
    },
  },
};
</script>
