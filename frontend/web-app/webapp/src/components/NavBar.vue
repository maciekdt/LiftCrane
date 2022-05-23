<template>
  <nav>
    <v-toolbar>
    <v-app-bar-nav-icon class="gray-text" @click="drawer = !drawer"></v-app-bar-nav-icon>
      <v-toolbar-title class="text-uppercase grey--text">
        <div class="grey lighten-3 pa-2 text-center rounded-xl">
          <span class="font-weight-light">Lift</span>
        <span>Crane</span>
        </div>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn text>
        <v-icon left>logout</v-icon>
        <span>Wyloguj</span>
      </v-btn>
    </v-toolbar>

    <v-navigation-drawer v-model="drawer" absolute bottom temporary class="indigo">
    <v-list nav dense>
      <v-list-item v-for="link in links" :key="link.text" router :to="link.route">
        <v-list-tile-action>
          <v-icon class="white--text pa-3">{{ link.icon }}</v-icon>
        </v-list-tile-action>
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
        {icon: 'view_list', text: 'Lista urządzeń', route:'/RaportsList'},
        {icon: 'contact_support', text: 'Kontakt/pomoc', route:'/About'},


      ]
      }
    }
      
    }
</script>