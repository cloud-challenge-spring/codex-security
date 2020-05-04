package org.cloudchallenge.codex.security.jwt

import io.jsonwebtoken.JwtParserBuilder
import io.jsonwebtoken.Jwts.parserBuilder
import org.cloudchallenge.codex.security.config.SecurityProperties
import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec

@Component
class JwtParserFactory(private val securityProperties: SecurityProperties) {
    fun jwtParser(): JwtParserBuilder =  parserBuilder().apply {
        val spec = X509EncodedKeySpec(securityProperties.rsaPublicKey)
        val kf = KeyFactory.getInstance("RSA")
        setSigningKey(kf.generatePublic(spec))
    }
}