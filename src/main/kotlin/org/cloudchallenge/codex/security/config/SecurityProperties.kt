package org.cloudchallenge.codex.security.config

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*
import javax.annotation.PostConstruct

@Component
@ConfigurationProperties(prefix = "codex.security")
class SecurityProperties {
    companion object {
        val logger = LoggerFactory.getLogger(SecurityProperties::class.java)!!
    }

    var rsaPrivateKey: ByteArray? = null
    var rsaPublicKey: ByteArray? = null
    var tokenExpiration: Duration = Duration.parse("PT1H")
    var tokenAllowedExpiration: Duration = Duration.parse("PT48H")

    @PostConstruct
    fun showInfo() = rsaPublicKey?.let {
        logger.info("RSA Public Key: {}", Base64.getEncoder().encodeToString(rsaPublicKey))
    }
}