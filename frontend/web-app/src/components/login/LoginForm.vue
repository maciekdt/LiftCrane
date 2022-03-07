<template>
  <form 
    class="loginForm" 
    v-on:submit.prevent='login' 
    >

    <div v-if=notAuthenticateError class="alert">
      <span  class="closebtn" v-on:click='closeMessage'>&times;</span> 
      <strong> Niepoprawna nazwa użytkownika lub hasło</strong>
    </div> 

    <div class="imgcontainer">
        <img src='../../assets/LiftCraneLogo.png' class="logo"/>
    </div>

    <div class="container">
        <label><b>Nazwa Użytkownika</b></label>
        <input 
          type="text" 
          placeholder="Podaj nazwę użytkownika..." 
          name = "name"
          required>

        <label><b>Hasło </b></label>
        <input 
          type="password" 
          placeholder="Podaj hasło..." 
          name = "pass"
          minlength="5"
          required>
            
        <button type="submit">Zaloguj się</button>
    </div>

  </form>
</template>

<script>
import userRepo from '../../users/usersRepo'
export default {
  name: "LoginForm",

  data() {
    return {
      notAuthenticateError: false,
      serverError: false,
    };
  },

  methods: {

    login(submitEvent) {
      let loginData = {
        name : submitEvent.target.elements.name.value,
        pass : submitEvent.target.elements.pass.value
      };
      
      this.notAuthenticateError = !userRepo.authenticateUser(loginData)
    },

    closeMessage(){
      this.notAuthenticateError = false ;
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
body {font-family: Arial, Helvetica, sans-serif;}

input{
  width: 100%;
  padding: 12px 20px;
  margin: 8px 0;
  display: inline-block;
  border: 1px solid #ccc;
  box-sizing: border-box;
}

button {
  background-color: #04AA6D;
  color: white;
  padding: 14px 20px;
  margin: 8px 0;
  border: none;
  cursor: pointer;
  width: 100%;
  font-size: 1em;
}

button:hover {
  opacity: 0.8;
}

.loginForm {
  background-color: whitesmoke;
}

.imgcontainer {
  text-align: center;
  padding: 1em 0 1em 0;
}

.logo {
  width: 15%;
  height: 15%;
}

img.avatar {
  width: 40%;
  border-radius: 50%;
}

.container {
  padding: 16px;
}

span.psw {
  float: right;
  padding-top: 16px;
}

.alert {
  padding: 20px;
  background-color: #f44336;
  color: white;
}

.closebtn {
  margin-left: 15px;
  color: white;
  font-weight: bold;
  float: right;
  font-size: 22px;
  line-height: 20px;
  cursor: pointer;
  transition: 0.3s;
}

</style>
