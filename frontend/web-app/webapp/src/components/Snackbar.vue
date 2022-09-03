<v-snackbar
  v-model="snackbar"
  :timeout="timeout"
  transition="fade-transition"
  class="d-flex justify-center"
>
      {{ text }}
    </v-snackbar>

<script>
export default {
  data: () => ({
    timeout: 2000,
  }),
  props: {
      text: String,
      snackbar: Boolean

  }
};
</script>
