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
      <v-btn text>
        <v-icon left>account_circle</v-icon>
        <span>Profil</span>
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
      ]
      }
    },
    computed: {
     currentRouteName() {
       return this.$route.name;
     }
    }
  }
</script>