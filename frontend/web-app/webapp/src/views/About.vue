<template>
  <div class="about">
    <h1 class="font-weight-medium text--primary">System CMMS LiftCrane</h1>
    <h2 class="font-weight-light text--secondary">Elektroniczny dziennik serwisanta</h2>
    <h2 class="font-weight-light text--disabled">Konakty:</h2>
    <v-list-item three-line 
    v-for="contact in team" 
    :key="contact.name"
    class="my-3"
    >
    <v-sheet elevation="6" rounded>

      <v-list-item-content>
        <v-list-item-title class="font-weight-medium ma-1">{{ contact.name }}</v-list-item-title>
        <v-list-item-subtitle>
          <v-icon left>alternate_email</v-icon>
          email: {{ contact.email }}
        </v-list-item-subtitle>
        <v-list-item-subtitle>
          <v-icon left>call</v-icon>
          telefon: {{contact.phone}}
        </v-list-item-subtitle>
      </v-list-item-content>
      </v-sheet>

    </v-list-item>
  </div>
</template>

<script>
  export default {
    data(){
      return{
      team: [
        {name: 'Łukasz Krzywicki', email: 'lukaszkrzywicki@autograf.pl', phone: '660 894 863'},
        {name: 'Maciek Dutkowski', email: 'null', phone: 'null'},
      ]
      }
    }
      
    }
</script>
