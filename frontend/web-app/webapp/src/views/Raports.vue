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
      <v-icon @click="getImages(item)"> image </v-icon>
      <label> {{ item.images.length }}</label>
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
  </v-data-table>
</template>

<script>
import { db, imagesRef } from "../fb.js";
import { mapState } from "vuex";
import "viewerjs/dist/viewer.css";
//  import VueViewer from 'v-viewer'
// import deletePopup from "../components/deletePopup";
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
          value: "liftId",
          sortable: false,
        },
        { text: "Miesiac ", value: "date", align: "right" },
        { text: "Status ", value: "malfunction", align: "right" },
        { text: "Serwisant ", value: "reviewerName", align: "right" },
        { text: "Zdjęcia ", value: "photos", align: "right", sortable: false },
        { text: "Usuń ", value: "actions", align: "right", sortable: false },
      ],

      loader: true,
      editedIndex: -1,

      images: [
        // { src: 'https://firebasestorage.googleapis.com/v0/b/liftcrane-bfea5.appspot.com/o/reviews_images%2F15c97b18-d4b4-421f-83da-e620e9df0654?alt=media&token=dbdcf947-20b5-4a41-b781-3c7814160c74', href: 'https://firebasestorage.googleapis.com/v0/b/liftcrane-bfea5.appspot.com/o/reviews_images%2F15c97b18-d4b4-421f-83da-e620e9df0654?alt=media&token=dbdcf947-20b5-4a41-b781-3c7814160c74', alt: ' ' },
        {
          src: "https://picsum.photos/id/2/150/150",
          href: "https://picsum.photos/id/2/800/800",
          alt: " ",
        },
        {
          src: "https://picsum.photos/id/3/150/150",
          href: "https://picsum.photos/id/3/800/800",
          alt: " ",
        },
        {
          src: "https://picsum.photos/id/4/150/150",
          href: "https://picsum.photos/id/4/800/800",
          alt: " ",
        },
        {
          src: "https://picsum.photos/id/5/150/150",
          href: "https://picsum.photos/id/5/800/800",
          alt: " ",
        },
        { src: this.image2, href: this.image2, alt: " " },
        {
          src: "https://picsum.photos/id/6/150/150",
          href: "https://picsum.photos/id/6/800/800",
          alt: " ",
        },
      ],
    };
  },
  methods: {
    getColor(mal) {
      if (mal == false) return "green";
      else if (mal == true) return "red";
      else return "orange";
    },
    deleteItem(item) {
      this.editedIndex = item.raportId;
      this.dialogDelete = true;
    },

    deleteItemConfirm() {
      db.collection("reviews")
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
        return 1

      }

      getList()
        .then(response => {console.log(response+"hej");this.$viewerApi({
           images: images,
         })})
        .catch((e) => {
          console.log(e);
        });
      console.log(images)
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
