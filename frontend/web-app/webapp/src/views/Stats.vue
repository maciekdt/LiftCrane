<template>
  <div>
    <h1 class="font-weight-medium text--primary">System CMMS LiftCrane</h1>
    <h2 class="font-weight-light text--secondary">
      Elektroniczny dziennik serwisanta
    </h2>
    <div>
      Strona stystyk - w trakcie prac
      <!-- <ul>
        <li>Zblizajace sie UDT</li>
        <li>Przeserwisowane urządzenia</li>
        <li>Ile który pracownik przeserwisował</li>
        <li>Liczba awarii</li>
        <li>Najbardziej awaryjne urządzenia</li>
        <li>wykres ile dziennie serwisów</li>
      </ul> -->
      <v-card>
        <v-card-text>
          <h2>Podstawowe informacje</h2>
          <p>
            Liczba urządzeń: {{ lifts.length }}<br/>
            Liczba serwisów: {{ review.length }}
          </p>
          Ilość serwisów:
          <ul>
            <li v-for="(val, key) in bestReviewer" :key="key">
              {{ key }}: {{ val }}
            </li>
          </ul>
          <br/>
          <p>
            Liczba awarii: {{ malfunctionCount }}
          </p>
          Urządzenia z największą liczbą awarii: {{ liftRank[1] }}
          <ul>
            <li v-for="(val, key) in liftRank[0]" :key="key">
              {{ val }}
            </li>
          </ul>
        </v-card-text>
      </v-card>
    </div>
  </div>
</template>

<script>
import {mapState} from "vuex";

export default {
  data() {
    return {
      rank: {},
    };
  },
  methods: {},
  computed: {
    ...mapState(["review", "lifts"]),
    bestReviewer() {
      const arr = this.review.map((a) => a.reviewerName);
      console.log(arr);
      const count = {}; //(count[el] || 0) +1 - jeżeli count[el] nie istnieje(undefined) to bierzemy 0 i dodajemy 1
      arr.forEach((el) => {
        count[el] = (count[el] || 0) + 1;
      });
      return count;
    },
    liftRank() {
      let arr = this.review.filter((a) => a.malfunction === true);
      arr = arr.map((a) => a.liftName);
      console.log(arr);
      const count = {}; //(count[el] || 0) +1 - jeżeli count[el] nie istnieje(undefined) to bierzemy 0 i dodajemy 1
      arr.forEach((el) => {
        count[el] = (count[el] || 0) + 1;
      });
      let temp = 0;
      const rank = [];
      for (const [key, value] of Object.entries(count)) {
        if (value > temp) temp = value;
        console.log(key)
      }
      for (const [key, value] of Object.entries(count)) {
        if (value === temp) rank.push(key)
      }
      console.log(rank)
      return [rank, temp];
    },
    malfunctionCount() {
      return this.review.filter((review) => review.malfunction).length;
    },
  },
};
</script>
