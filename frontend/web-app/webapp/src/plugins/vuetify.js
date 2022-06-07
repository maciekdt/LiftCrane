import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';
import '@mdi/font/css/materialdesignicons.css'
Vue.use(Vuetify);

export default new Vuetify({
    theme: {
        themes: {
          light: {
            primary: '#1564bf',
            secondary: '#5e91f2', // #FFCDD2
            accent:  '#003b8e', // #3F51B5
          }
        }}

});
