<template>
    <form 
    class="acceptReviewForm" 
    v-on:submit.prevent='accept'>
        <div class="imgcontainer">
            <img src='../../assets/lift-crane-logo.png' class="logo"/>
        </div>

        <h4>Winda restauracyjna Prolift</h4>
        <h4>Numer seryjny : TYR465334</h4>
        <h4>Lokalizacja : Jelenia Góra, zielona 7A</h4>

        <button type="submit">Zatwierdź przegląd</button>
    </form>
</template>

<script>
</script>


<style scoped>
button {
    background-color: #04AA6D;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    cursor: pointer;
    max-width: 30em;
    font-size: 1em;
}


.acceptReviewForm {
  background-color: whitesmoke;
  padding: 16px;
}

.imgcontainer {
  text-align: center;
  padding: 1em 0 1em 0;
}

.logo {
  width: 15%;
  height: 15%;
  min-width: 10em;
}

img.avatar {
  width: 40%;
  border-radius: 50%;
}


span.psw {
  float: right;
  padding-top: 16px;
}

button:hover {
  opacity: 0.8;
}

</style>
