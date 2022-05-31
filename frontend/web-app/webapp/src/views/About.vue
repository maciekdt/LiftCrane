<template>
  <div class="about">
    <h1 class="font-weight-medium text--primary">System CMMS LiftCrane</h1>
    <h2 class="font-weight-light text--secondary">
      Elektroniczny dziennik serwisanta
    </h2>
    <h2 class="font-weight-light text--disabled">Konakty:</h2>
    <v-list-item
      three-line
      v-for="contact in team"
      :key="contact.name"
      class="my-3"
    >
      <v-sheet elevation="2" rounded width="760">
        <v-list-item-content>
          <v-list-item-title class="font-weight-medium mx-2">{{
            contact.name
          }}</v-list-item-title>
          <v-list-item-subtitle>
            <v-icon left class="mx-2">alternate_email</v-icon>
            email: {{ contact.email }}
          </v-list-item-subtitle>
          <v-list-item-subtitle>
            <v-icon left class="mx-2">call</v-icon>
            telefon: {{ contact.phone }}
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-sheet>
    </v-list-item>
    <div></div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      team: [
        {
          name: "Łukasz Krzywicki",
          email: "lukaszkrzywicki@autograf.pl",
          phone: "533 371 705",
        },
        {
          name: "Maciej Dutkowski",
          email: "maciek.fi88@gmail.com",
          phone: "531 359 640",
        },
      ],
    };
  },
};
</script>
