<template>
  <v-app>
    <v-main>
      <About/>
      <Table></Table>
    </v-main>
  </v-app>
</template>

<script>
import Table from './components/Table';
import About from './components/About';
export default {
  name: 'App',

  components: {
    About,
    Table,
  },

  data: () => ({
    //
  }),
};
</script>
