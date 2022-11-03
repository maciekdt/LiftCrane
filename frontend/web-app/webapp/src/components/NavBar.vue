<template>
  <nav>
    <v-toolbar app class="primary">
      <v-app-bar-nav-icon class="gray-text" @click="drawer = !drawer"
        ><v-icon color="white ">menu</v-icon></v-app-bar-nav-icon
      >
      <div class="mx-3 font-weight-thin white--text">
        {{ currentRouteName }}
      </div>
      <v-spacer></v-spacer>
      <v-toolbar-title
        class="text-uppercase white--text d-flex justify-center rounded-xl"
      >
        <v-btn
          to="/"
          large
          retain-focus-on-click
          text
          plain
          class="secondary pa-2 text-center rounded-xl"
        >
          <span class="font-weight-light">Lift</span>
          <span class="">Crane</span>
        </v-btn>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <!-- Big screen devices -->
      <v-toolbar-items class="hidden-sm-and-down py-3">
        <div v-if="!isSignedIn">
          
          <v-tooltip bottom>
            <template v-slot:activator="{ on, attrs }">
              <v-icon class="mx-2 grey--text" v-bind="attrs" v-on="on">
                account_box
              </v-icon>
            </template>
            <span>Wylogowano</span>
          </v-tooltip>

          <v-btn @click="googleSignIn" class="secondary">
            <v-icon left>login</v-icon>
            <span>Zaloguj</span>
          </v-btn>
        </div>
        <div v-else>
          <v-chip class="ma-1" color="green" text-color="white" to="/Raports">
            <v-avatar left class="green darken-4" > {{newReviews}} </v-avatar>
            Zgłoszeń 
          </v-chip>
          <v-tooltip bottom>
            <template v-slot:activator="{ on, attrs }">
              <v-icon class="mx-2 green--text" v-bind="attrs" v-on="on">
                account_box
              </v-icon>
            </template>
            <span>Zalogowano {{ userName }}</span>
          </v-tooltip>
          <v-btn @click="googleSignOut" class="secondary">
            <v-icon left>logout</v-icon>
            <span>Wyloguj</span>
          </v-btn>
        </div>
      </v-toolbar-items>
      <!-- Mobile devices -->
      <v-toolbar-items class="hidden-md-and-up py-1">
        <div v-if="!isSignedIn">
          <v-tooltip bottom>
            <template v-slot:activator="{ on, attrs }">
              <v-icon class="mx-1 grey--text" v-bind="attrs" v-on="on">
                account_box
              </v-icon>
            </template>
          </v-tooltip>
          <v-btn @click="googleSignIn" class="secondary white--text" icon>
            <v-icon>login</v-icon>
          </v-btn>
        </div>
        <div v-else>
          
          <v-tooltip bottom>
            <template v-slot:activator="{ on, attrs }">
              <v-icon class="mx-1 green--text" v-bind="attrs" v-on="on">
                account_box
              </v-icon>
            </template>
            <span>Zalogowano {{ userName }}</span>
          </v-tooltip>
          <v-btn @click="googleSignOut" class="secondary white--text" icon>
            <v-icon>logout</v-icon>
          </v-btn>
          
        </div>
      </v-toolbar-items>
    </v-toolbar>
    <v-snackbar
      v-model="snackbar"
      :timeout="timeout"
      transition="fade-transition"
      class="d-flex justify-center"
    >
      {{ text }}
    </v-snackbar>
    <v-navigation-drawer v-model="drawer" absolute temporary class="secondary">
      <v-list nav dense>
        <v-list-item
          v-for="link in links"
          :key="link.text"
          router
          :to="link.route"
        >
          <v-list-item-action>
            <v-icon class="white--text pa-3">{{ link.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title class="white--text">
              {{ link.text }}</v-list-item-title
            >
          </v-list-item-content>
        </v-list-item>
        <v-divider></v-divider>
      </v-list>
    </v-navigation-drawer>
  </nav>
</template>

<script>
import fb from "firebase";
import router from "@/router/index";
import { mapMutations, mapState } from "vuex";
export default {
  data() {
    return {
      drawer: false,
      links: [
        { icon: "dashboard", text: "Panel nawigacji", route: "/" },
        { icon: "list_alt", text: "Lista urządzeń", route: "/Devices" },
        { icon: "fact_check", text: "Dziennik konserwacji", route: "/Raports" },
        { icon: "bar_chart", text: "Statystyki", route: "/Statistics" },
        { icon: "manage_accounts", text: "Profile", route: "/Profiles" },
        { icon: "contact_support", text: "Kontakt/pomoc", route: "/About" },
      ],
      singedIn: false,
      userNameTemp: null,
      snackbar: false,
      text: "Zalogowano!",
      timeout: 2000,
      user: "",
    };
  },
  methods: {
    ...mapMutations(["updateIsSignedIn", "updateUserName"]),

    googleSignIn: function () {
      let provider = new fb.auth.GoogleAuthProvider();

      fb.auth()
        .signInWithPopup(provider)
        .then((result) => {
          let user = result.user;
          console.log("Logged in"); // Token
          this.text = "Zalogowano!";
          this.snackbar = true;
          console.log(user); // User that was authenticated
          this.singedIn = true;
          this.userNameTemp = result.user.displayName;
          console.log(this.userNameTemp);
          this.updateIsSignedIn(true), this.updateUserName(this.userNameTemp);
          console.log("State userName: " + this.userName);
          console.log("Cy zalogowany " + this.isSignedIn);
          this.$emit("changeUserName");
        })
        .catch((err) => {
          console.log(err); // This will give you all the information needed to further debug any errors
          this.singedIn = false;
        });
    },
    googleSignOut: function () {
      fb.auth()
        .signOut()
        .then(() => {
          // Sign-out successful.
          console.log("Logged Out");
          this.singedIn = false;
          this.updateIsSignedIn(false),
            console.log("Czy zalogowany" + this.isSignedIn);
          this.text = "Wylogowano!";
          this.snackbar = true;
          router.push("/");
        })
        .catch((error) => {
          // An error happened.
          console.log(error);
        });
    },

    // login : function() {
    //     firebase.auth().signInWithEmailAndPassword(this.email, this.password).then(
    //         user => {
    //             console.log(user.data);
    //         },
    //         err => {
    //             alert(err);
    //         }
    //     )
    // }
  },
  computed: {
    currentRouteName() {
      return this.$route.name;
    },
    ...mapState(["isSignedIn", "userName", "review"]),
    newReviews(){
      let val = 0;
      this.review.forEach(element => {
        if(element.seen == false){val++}
      });
      return val;
    }
  },
  watch: {
    singedIn() {
      this.user = fb.auth().currentUser;
      if (this.user) {
        this.userstate = true; // If it exists
        console.log(this.userstate);
        return this.user.displayName;
      } else {
        this.userstate = false; // If it doesn't
        console.log(this.userstate);
        return this.userstate;
      }
    },
  },
};
</script>
