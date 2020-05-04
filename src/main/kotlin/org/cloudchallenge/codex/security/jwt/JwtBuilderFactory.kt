package org.cloudchallenge.codex.security.jwt

import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.cloudchallenge.codex.security.config.SecurityProperties
import org.springframework.stereotype.Service
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec

@Service
class JwtBuilderFactory(private val securityProperties: SecurityProperties) {
    fun jwtBuilder(): JwtBuilder = Jwts.builder()
            .apply {
                val keyFactory = KeyFactory.getInstance("RSA")
                val ks = PKCS8EncodedKeySpec(securityProperties.rsaPrivateKey)
                val privateKey = keyFactory.generatePrivate(ks) as RSAPrivateKey
                signWith(privateKey, SignatureAlgorithm.RS256)
            }
}