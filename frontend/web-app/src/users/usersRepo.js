let usersRepo = {
    authenticateUser: function(loginData) {
        return loginData.name == "maciek" && loginData.pass == "password";
    }
}

export default usersRepo