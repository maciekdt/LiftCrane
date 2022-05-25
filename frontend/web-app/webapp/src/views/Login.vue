<template>
<div>
    <p>Zarejestuj się</p>
    <v-btn @click="googleSignIn">Google</v-btn>
    <v-btn @click="googleSignOut">Wyloguj</v-btn>
    <p>{{displayName}}</p>

</div>
</template>


<script>
import fb from 'firebase';
export default {
    name:'Login',
    data() {
        return{
    displayName: null,
        }
    },
    methods: {

        googleSignIn: function() {
        let provider = new fb.auth.GoogleAuthProvider();
        fb
        .auth()
        .signInWithPopup(provider)
        .then((result) => {
          let token = result.credential.accessToken;
          let user = result.user;
            console.log(token) // Token
            console.log(user) // User that was authenticated
        })
        .catch((err) => {
          console.log(err); // This will give you all the information needed to further debug any errors
        });
        },
        googleSignOut: function() {
            fb.auth().signOut().then(() => {
            // Sign-out successful.
            console.log('Logged Out')
            }).catch((error) => {
            // An error happened.
            console.log(error)
            });
        },
        
        // login : function() {
        //     firebase.auth().signInWithEmailAndPassword(this.email, this.password).then(
        //         user => {
        //             console.log(user.data);
        //         },
        //         err => {
        //             alert(err);
        //         }
        //     )
        // }
    },
    created() {
    const user = fb.auth().currentUser;
     this.displayName = user.email;
        }
    }
</script>