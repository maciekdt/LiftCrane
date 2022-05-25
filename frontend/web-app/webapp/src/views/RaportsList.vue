<template>
  <v-card>
    <v-card-title>
      Urządzenia
      <v-spacer></v-spacer>
      <v-text-field
        v-model="search"
        append-icon="mdi-magnify"
        label="Szukaj"
        single-line
        hide-details
      ></v-text-field>
    </v-card-title>
    <v-data-table
      :headers="headers"
      :items="device"
      :search="search"
    >
    <template v-slot:item.status="{ item }">
    <v-simple-checkbox v-model="item.status" disabled></v-simple-checkbox>
    </template>
    </v-data-table>
  </v-card>
</template>

<script>
  export default {
    name: 'Table',
    data () {
      return {
        search: '',
        headers: [
          {
            text: 'Nazwa',
            align: 'start',
            sortable: false,
            value: 'name',
          },
          { text: 'ID', value: 'id' },
          { text: 'Lokalizacja', value: 'loc' },
          { text: 'Status', value: 'status' },
        ],
        device: [
          {
            name: 'Zielona 43',
            id: 'N7485947182',
            loc: 'Cieplice',
            status: true
          },
          {
            name: 'OW Bombel',
            id: 'N7589258343',
            loc: 'Karpacz',
            status: false
          },
          {
            name: 'Biedronka Zabobrze',
            id: 'N4412892583',
            loc: 'Jelenia Góra Kiepury 34',
            status: false
          },
        ],
      }
    },
  }
</script>