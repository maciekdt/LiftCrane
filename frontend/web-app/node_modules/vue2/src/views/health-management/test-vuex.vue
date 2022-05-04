<template>
  <div class="form-group">
    <label class="col-sm-2 control-label">用户名</label>
    <div class="col-sm-10">
      <input type="text" v-model="username" v-on:change="userNameChange" class="form-control" :placeholder="username">
    </div>
  </div>
</template>
<script>
// import Utils from '../../src/utils'
export default {
  props: ['placeholder'],
  data: function() {
    return {
      username: '111'
    }
  },
  methods: {
    userNameChange() {
      // this.$emit("childChange","username",this.username)
      this.$store.state.user_name = this.username
      // Utils.post()
    }
  }
}
</script>
