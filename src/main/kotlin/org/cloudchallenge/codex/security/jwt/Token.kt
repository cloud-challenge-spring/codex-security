package org.cloudchallenge.codex.security.jwt

import io.jsonwebtoken.JwtBuilder
import org.cloudchallenge.codex.security.model.Authenticable
import org.cloudchallenge.codex.security.model.UserLogin
import java.time.Instant
import java.util.*

class Token {

    companion object {

        const val INSTANCE_ID = "ins"
        const val LOCALE = "loc"
        const val AUTH_TYPE = "aty"
        const val AUTHENTICABLE_ID = "ati"
        const val FIRSTNAME = "fnm"
        const val LASTNAME = "lnm"

        fun createJWTToken(jwtBuilder: JwtBuilder, userLogin: UserLogin, expirationDate: Instant, authenticable: Authenticable): JwtBuilder =
            jwtBuilder
                    .setSubject(authenticable.identifier)
                    .setId(authenticable.identifier)
                    .claim(AUTHENTICABLE_ID, authenticable.identifier)
                    .setExpiration(Date.from(expirationDate))
                    .setIssuedAt(Date.from(Instant.now()))
                    .claim(LOCALE, authenticable.locale.toString())
                    .claim(INSTANCE_ID, userLogin.instanceId)
                    .claim(AUTH_TYPE, authenticable.authType)
                    .claim(FIRSTNAME, authenticable.firstName)
                    .claim(LASTNAME, authenticable.lastName)
    }
}