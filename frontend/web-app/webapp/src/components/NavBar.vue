<template>
  <nav>
    <v-toolbar>
    <v-app-bar-nav-icon class="gray-text" @click="drawer = !drawer"><v-icon>menu</v-icon></v-app-bar-nav-icon>
    <v-toolbar-subtitle class="mx-3 font-weight-thin">{{ currentRouteName }}</v-toolbar-subtitle>
    <v-spacer></v-spacer>
      <v-toolbar-title class="text-uppercase grey--text d-flex justify-center">
        <div  class="grey lighten-3 pa-2 text-center rounded-xl">
          <span class="font-weight-light">Lift</span>
        <span class="">Crane</span>
        </div>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn v-if="!singedIn" @click="googleSignIn">
        <v-icon left>login</v-icon>
        <span>Zaloguj</span>
      </v-btn>
      <v-btn text v-else @click="googleSignOut">
        <v-icon left>logout</v-icon>
        <span>Wyloguj</span>
      </v-btn>
    </v-toolbar>

    <v-navigation-drawer v-model="drawer" absolute bottom temporary class="grey">
    <v-list nav dense>
      <v-list-item v-for="link in links" :key="link.text" router :to="link.route">
        <v-list-item-action>
          <v-icon class="white--text pa-3">{{ link.icon }}</v-icon>
        </v-list-item-action>
        <v-list-item-content>
          <v-list-item-title class="white--text">{{ link.text }}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list>
    </v-navigation-drawer>

  </nav>
</template>

<script>
import fb from 'firebase';

  export default {
    data(){
      return{
      drawer: false,
      links: [
        {icon: 'dashboard', text: 'Panel nawigacji', route:'/'},
        {icon: 'list_alt', text: 'Lista urządzeń', route:'/Devices'},
        {icon: 'fact_check', text: 'Dziennik konserwacji', route:'/Raports'},
        {icon: 'manage_accounts', text: 'Profile', route:'/Profiles'},
        {icon: 'contact_support', text: 'Kontakt/pomoc', route:'/About'},
      ],
      singedIn: false
      }
    },
    methods: {

        googleSignIn: function() {
        let provider = new fb.auth.GoogleAuthProvider();
        fb
        .auth()
        .signInWithPopup(provider)
        .then((result) => {
          let token = result.credential.accessToken;
          let user = result.user;
            console.log(token) // Token
            console.log(user) // User that was authenticated
            this.singedIn = true
        })
        .catch((err) => {
          console.log(err); // This will give you all the information needed to further debug any errors
          this.singedIn = false
        });
        },
        googleSignOut: function() {
            fb.auth().signOut().then(() => {
            // Sign-out successful.
            console.log('Logged Out')
            this.singedIn = false
            }).catch((error) => {
            // An error happened.
            console.log(error)
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
     }
    }
  }
</script>