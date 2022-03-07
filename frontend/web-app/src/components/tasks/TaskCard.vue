<template>
  <div class="box">
    <table>
      <tr>
        <th>
          <h3>{{ liftName }}</h3>
        </th>
      </tr>
      <tr>
        <th>
          <h3>{{ location }}</h3>
        </th>
        <th>
          <h4>{{ type }}</h4>
        </th>
      </tr>
    </table>
  </div>
</template>


<script>
export default {
  name: "TaskCard",
  props: {
    liftName: String,
    location: String,
    date: String,
    type: String,
  },

  computed: {
    taskData() {
      return {
        liftName: this.liftName,
        location: this.location,
        date: this.date,
        type: this.type
      }
    }
  }
};
</script>


<style scoped>
.box {
  background-color: whitesmoke;
}
</style>