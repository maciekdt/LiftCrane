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
          <td>{{ item.date }}</td>
          <td>
            <v-chip :color="getColor(item.malfunction)">
              {{ item.malfunction }}
            </v-chip>
          </td>
          <td>{{ item.reviewerName }}</td>
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
    getColor(mal) {
      if (mal == 'Sprawny') return "green";
      else if (mal == 'Awaria') return "red";
      else return "orange";
    },
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
              date: doc.data().date.toDate().toLocaleString("pl-PL", {
                day: "2-digit",
                month: "long",
                year: "numeric",
              }),
              malfunction: doc.data().malfunction ? "Awaria" : "Sprawny",
              reviewerName: doc.data().reviewerName,
            });
          });
        })
        .catch((error) => {
          console.log("Error getting documents: ", error);
        });
    },
    // getReviewerName: function () {
    //   db.collection("users")
    //     .doc(reviewerId)
    //     .get()
    //     .then((querySnapshot) => {
    //       querySnapshot.forEach((doc) => {
    //         // doc.data() is never undefined for query doc snapshots
    //         console.log(doc.id, " => ", doc.data());
    //         this.raports.push({
    //           date: doc.data().date.toDate().toLocaleString("pl-PL", {
    //             day: "2-digit",
    //             month: "long",
    //             year: "numeric",
    //           }),
    //           malfunction: doc.data().malfunction ? "Awaria" : "Sprawny",
    //           reviewerId: doc.data().reviewerId,
    //         });
    //       });
    //     });
    // },
  },

  watch: {
    liftId: function () {
      this.raports = [];
      this.getInfo();
    },
  },
};
</script>
