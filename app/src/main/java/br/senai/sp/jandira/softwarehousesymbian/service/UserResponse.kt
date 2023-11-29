package br.senai.sp.jandira.softwarehousesymbian.service

data class UserResponse(
    var email: String = " ",
    var password: String = " ",
){
    override fun toString() : String {
        return "UserResponse(email = '$email', password = '$password')"
    }
}