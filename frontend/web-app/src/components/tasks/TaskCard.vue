<template>
  <div class="card">
    <table>
      <tr>
        <th><p class="liftName"> {{task.liftName}} </p></th>
        <th><p class="date"> {{task.date}} </p></th>
      </tr>
      <tr>
        <th><p class="location">{{task.location}} </p></th>
        <th></th>
      </tr>
    </table>
  </div>
</template>


<script>
import Task from '../../data-model/Task'
export default {
  name: "TaskCard",
  props: {
    task : Task
  }
};
</script>


<style scoped>
table {
  width: 100%;
  height: 100%;
  padding-left: .5em;
  padding-right: .5em;
}
.card {
  border-radius: .4em;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}

.liftName {
  font-size: 1em;
  font-weight: bold;
  text-align: left;
  
}

.date {
  font-size: .7em;
  font-weight: bold;
  opacity: .8;
  text-align: right;
}

.location {
  font-size: .8em;
  font-weight: bold;
  opacity: .8;
  text-align: left;
}

.logo {
  max-width: 100%;
}
</style>