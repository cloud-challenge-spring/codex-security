package org.cloudchallenge.codex.security.model

data class UserLogin(var login: String = "",
                     var password: String = "",
                     var instanceId:String = "",
                     var type: UserLoginType = UserLoginType.USER){

    override fun toString() = "UserLogin($login on $instanceId')"
}