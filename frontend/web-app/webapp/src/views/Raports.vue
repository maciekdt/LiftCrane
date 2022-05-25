<template>
  <v-data-table
    :headers="headers"
    :items="devices"
    item-key="liftId"
    sort-by="liftId"
    group-by="mounth"
    class="elevation-1"
    show-group-by
    :loading='loader'
    loading-text="Pobieranie danych, proszę czekać"
  ></v-data-table>
</template>
<script>
import db from '../fb.js'

  export default {
    data () {
      return {
        headers: [
          {
            text: 'Wpis',
            align: 'start',
            value: 'liftId',
            groupable: false,
          },
          { text: 'Miesiac ', value: 'date', align: 'right' },
          { text: 'Status ', value: 'malfunction', align: 'right' },
          { text: 'Serwisant ', value: 'reviewerId', align: 'right' },

        ],
        devices: [],
        loader: true,
      }
    },
    methods: {
    getAll: function(){
          db.collection("reviews").get().then((querySnapshot) => {
          querySnapshot.forEach((doc) => {
          // console.log(`${doc.id} => ${doc.data().id}`);          
          this.devices.push({ 
            liftId: doc.data().liftId,
            date: doc.data().date.toDate().toLocaleString("pl-PL", { month: "long" }),
            malfunction: doc.data().malfunction ? "Awaria" : "Sprawny",
            reviewerId: doc.data().reviewerId,
            });
          });
          }).finally(()=>{this.loader = false});
        }
    },
    mounted() {
        this.getAll()
        console.log('Mounted, getAll data from firebase')
      }
}
</script>