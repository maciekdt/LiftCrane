<template>
  <v-data-table
    :headers="headers"
    :items="review"
    :sort-by="['date', 'malfunction']"
    multi-sort
    items-per-page=50
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
      {{ item.malfunction ? 'Awaria' : 'Sprawne' }}
    </v-chip>
    </template>
  </v-data-table>
</template>

<script>
import { db } from "../fb.js";
import { mapState } from "vuex";

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

      loader: true,
      editedIndex: -1,
    };
  },
  methods: {
     getColor (mal) {
        if (mal == false) return 'green'
        else if (mal == true) return 'red'
        else return 'orange'
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
