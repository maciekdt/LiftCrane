<template>
  <v-data-table
    :headers="headers"
    :items="devices"
    :sort-by="['date', 'malfunction']"
    multi-sort
    class="elevation-1"
  >
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>Serwisy</v-toolbar-title>

        <v-spacer></v-spacer>
        <v-dialog v-model="dialogDelete" max-width="500px">
          <v-card>
            <v-card-title class="text-h5 grey lighten-2"
              >Usuń wpis</v-card-title
            >
            <v-card-text class="pa-2"
              >Czy napewno chcesz usunąć ten wpis?</v-card-text
            >
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="closeDelete">Nie</v-btn>
              <v-btn color="blue darken-1" text @click="deleteItemConfirm"
                >Tak</v-btn
              >
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>
    <template v-slot:item.actions="{ item }">
      <v-icon @click="deleteItem(item)"> delete_forever </v-icon>
    </template>
  </v-data-table>
</template>

<script>
import { db } from "../fb.js";
// import deletePopup from "../components/deletePopup";
export default {
  // components: { deletePopup },
  data() {
    return {
      dialogDelete: false,
      headers: [
        {
          text: "Wpis",
          align: "start",
          value: "liftId",
          sortable: false,
        },
        { text: "Miesiac ", value: "date", align: "right" },
        { text: "Status ", value: "malfunction", align: "right" },
        { text: "Usuń ", value: "actions", align: "right", sortable: false },

        // { text: "Serwisant ", value: "reviewerId", align: "right" },
      ],
      devices: [],
      loader: true,
      editedIndex: -1,
    };
  },
  methods: {
    getAll: function () {
      db.collection("reviews")
        .get()
        .then((querySnapshot) => {
          querySnapshot.forEach((doc) => {
            // console.log(`${doc.id} => ${doc.data().id}`);
            this.devices.push({
              liftId: doc.data().liftId,
              raportId: doc.id,
              date: doc
                .data()
                .date.toDate()
                .toLocaleString("pl-PL", {day:'2-digit', month: "long", year:'numeric' }),
              malfunction: doc.data().malfunction ? "Awaria" : "Sprawny",
              reviewerId: doc.data().reviewerId,
            });
          });
        })
        .finally(() => {
          this.loader = false;
        });
    },
    deleteItem(item) {
      this.editedIndex = item.raportId;
      console.log(item.raportId + " deleted");
      this.dialogDelete = true;
    },

    deleteItemConfirm() {
      this.devices.splice(this.editedIndex, 1);
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
  },
  mounted() {
    this.getAll();
    console.log("Mounted, getAll data from firebase");
  },
  watch: {
    dialogDelete(val) {
      val || this.closeDelete();
    },
  },
};
</script>
