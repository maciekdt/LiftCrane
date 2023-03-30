<template app>
  <div>
    <template>
      <template>
        <v-container class="pa-4 text-center">
          <v-row class="fill-height" align="center" justify="center">
            <template v-for="(item, i) in items">
              <v-col :key="i" cols="12" md="4">
                <v-hover v-slot="{ hover }">
                  <v-card
                    :elevation="hover ? 12 : 2"
                    :class="{ 'on-hover': hover }"
                    :to="item.to"
                    color="blue"
                  >
                    <v-img :src="item.img" height="225px"> </v-img>
                    <p
                      class="ma-2 pb-2 subheading text-left text-h6 white--text"
                    >
                      <v-icon class="mb-1 text-left white--text">{{
                        item.icon
                      }}</v-icon>
                      {{ item.title }}
                    </p>
                  </v-card>
                </v-hover>
              </v-col>
            </template>
          </v-row>
        </v-container>
      </template>
    </template>
    <div>
      <br /><br />
      <hr />
      <h3>Aktualizacja 16.08.2023</h3>

      <div class="py-3">
        <ul>
          <li>Poprawienie funkcjonowania strony</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import { db } from "@/fb";
export default {
  data() {
    return {
      userName: null,
      newRaports: 0,
      items: [
        {
          title: "Urządzenia",
          icon: "list_alt",
          text: "Przeglądaj wszystkie urządzenia",
          img: "https://images.unsplash.com/photo-1585293878107-569e3ebdab53?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
          to: "/Devices",
        },
        {
          title: "Dziennik konserwacji",
          icon: "fact_check",
          text: "Przeglądaj konserwacje i awarie",
          img: "https://firebasestorage.googleapis.com/v0/b/liftcrane-bfea5.appspot.com/o/raports.jpg?alt=media&token=c14dd804-85b8-4fb2-8acc-75e846ea7bb2",
          to: "/Raports",
        },
        {
          title: "Statystyki",
          icon: "bar_chart",
          text: "Sprawdź jak przeprowadzane są konserwacje",
          img: "https://images.unsplash.com/photo-1599658880436-c61792e70672?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
          to: "/Statistics",
        },
      ],
      transparent: "rgba(255, 255, 255, 0)",
    };
  },
  methods: {
    showNewRaports() {
      console.log("clicked");
      db.collection("reviews")
        .where("seen", "==", false)
        .get()
        .then((querySnapshot) => {
          querySnapshot.forEach(() => {
            this.newRaports = this.newRaports + 1;
            console.log("hej");
          });
        });
    },
  },
  watch: {},
  // mounted() {
  //   db.collection("raports")
  //     .where("seen", "==", false)
  //     .get()
  //     .then((querySnapshot) => {
  //       querySnapshot.forEach(() => {
  //         this.newRaports = this.newRaports + 1;
  //       });
  //     });
  // },
};
</script>
<style scoped>
hr {
  color: #2196f3;
  opacity: 0.3;
}
.v-card {
  transition: opacity 0.4s ease-in-out;
}

.v-card:not(.on-hover) {
  opacity: 0.6;
}

.show-btns {
  color: rgba(255, 255, 255, 1) !important;
}
</style>
