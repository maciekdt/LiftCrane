<template>
  <v-simple-table>
    <template v-slot:default>
      <thead>
        <tr>
          <th class="text-left">Data</th>
          <th class="text-left">Status</th>
          <th class="text-left">Serwisant</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in raports" :key="item.name">
          <td>{{ item.date.toDate()
                .toLocaleString("pl-PL", {
                  day: "2-digit",
                  month: "long",
                  year: "numeric",
                }) }}</td>
          <td>{{ item.malfunction }}</td>
          <td>{{ item.reviewerId }}</td>
        </tr>
      </tbody>
    </template>
  </v-simple-table>
</template>

<script>
import { db } from "@/fb.js";


export default {
  data() {
    return {
      raports: [],
    };
  },
  props: {
    liftId: String,
  },
  methods: {
    getInfo: function () {
      console.log(this.liftId);
      db.collection("reviews")
        .where("liftId", "==", this.liftId)
        .get()
        .then((querySnapshot) => {
          querySnapshot.forEach((doc) => {
            // doc.data() is never undefined for query doc snapshots
            console.log(doc.id, " => ", doc.data());
            this.raports.push({
              date: doc
                .data()
                .date.toDate()
                .toLocaleString("pl-PL", {
                  day: "2-digit",
                  month: "long",
                  year: "numeric",
                }),
              malfunction: doc.data().malfunction ? "Awaria" : "Sprawny",
              reviewerId: doc.data().reviewerId,
            });
          });
        })
        .catch((error) => {
          console.log("Error getting documents: ", error);
        });
    },
  },
  firestore() {
    return {
      currentTodo: db.collection('reviews').where("liftId", "==", this.liftId),
    }
  },
  watch: {
    liftId: function () {
      this.raports = [];
      this.getInfo();
    },
  },
};
</script>
