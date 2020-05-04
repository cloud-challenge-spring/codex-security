package org.cloudchallenge.codex.security.service

import org.cloudchallenge.codex.core.config.CodexMongoProperties
import org.cloudchallenge.codex.core.context.withInstance
import org.cloudchallenge.codex.security.config.SecurityProperties
import org.cloudchallenge.codex.security.exception.DisabledUserException
import org.cloudchallenge.codex.security.exception.InvalidCredentialsException
import org.cloudchallenge.codex.security.exception.InvalidInstanceIdException
import org.cloudchallenge.codex.security.jwt.JwtBuilderFactory
import org.cloudchallenge.codex.security.model.Auth
import org.cloudchallenge.codex.security.model.Authenticable
import org.cloudchallenge.codex.security.model.UserLogin
import org.cloudchallenge.codex.security.model.UserLoginType
import org.cloudchallenge.codex.security.repository.InstanceRepository
import org.cloudchallenge.codex.security.jwt.Token
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.time.Instant

@Service
class UserAuthenticationService(private val codexMongoProperties: CodexMongoProperties,
                                private val instanceRepository: InstanceRepository,
                                private val userService: UserService,
                                private val securityProperties: SecurityProperties,
                                private val jwtBuilderFactory: JwtBuilderFactory): AuthenticationService {

    companion object {
        val logger = LoggerFactory.getLogger(UserAuthenticationService::class.java)!!
    }

    override fun rank(): Int = 1

    override fun accept(userLogin: UserLogin) = true

    override fun authenticate(userLogin: UserLogin): Auth {
        withInstance(codexMongoProperties.globalDatabaseName) {
            if (!instanceRepository.existsById(userLogin.instanceId))
                throw InvalidInstanceIdException("Instance ${userLogin.instanceId} is not found")
        }

        var authenticable: Authenticable? = null

        when (userLogin.type) {
            UserLoginType.USER -> authenticable = searchAuthenticable(userLogin.login, userLogin.instanceId)
        }

        authenticable?.let { it ->
            validateAuthenticable(it, userLogin)
            //completeAuthenticable(it)

            val expirationDate = Instant.now() + securityProperties.tokenExpiration
            val jwtBuilder = Token.createJWTToken(jwtBuilderFactory.jwtBuilder(), userLogin, expirationDate, it)
            return Auth(it, jwtBuilder.compact())
        } ?: throw InvalidCredentialsException("Invalid login")
    }

    private fun validateAuthenticable(authenticable: Authenticable, userLogin: UserLogin){
        if (!authenticable.active)
            throw DisabledUserException("Authenticable ${userLogin.login} is disabled")

        if (!StringUtils.hasText(userLogin.password))
            throw InvalidCredentialsException("Invalid login")

        if (StringUtils.hasText(userLogin.password) && !checkPassword(authenticable, userLogin.password)) {
            logger.info("Invalid login {} ", userLogin.login)
            throw InvalidCredentialsException("Invalid login")
        }
    }

    /*
    private fun completeAuthenticable(authenticable: Authenticable){
        //complete groups
    }*/

    private fun searchAuthenticable(login: String, instanceId: String): Authenticable? {
        var authenticable: Authenticable? = null
        withInstance(instanceId) {
            var user = userService.findByEmail(login)
            if (user == null) {
                withInstance(codexMongoProperties.globalDatabaseName) {
                    user = userService.findByEmail(login)
                }
            }
            authenticable = user?.let {
                Authenticable(
                    identifier = it.id.toString(), login= it.email, password= it.password,
                    firstName = it.firstName, lastName = it.lastName,
                    active = it.active, authType = UserLoginType.USER, locale = it.locale
                )
            }
        }
        return authenticable
    }

    fun checkPassword(authenticable: Authenticable, password: String): Boolean {
        var valid = false
        try {
            valid = BCrypt.checkpw(password, authenticable.password)
        } catch (exception: IllegalArgumentException) {
            logger.error("Check password failed", exception)
        }
        return valid
    }
}