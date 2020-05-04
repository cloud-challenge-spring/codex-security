package org.cloudchallenge.codex.security.service

import org.bson.types.ObjectId
import org.cloudchallenge.codex.core.context.InstanceContextHolder
import org.cloudchallenge.codex.core.context.withInstance
import org.cloudchallenge.codex.core.lang.findOne
import org.cloudchallenge.codex.core.lang.randomString
import org.cloudchallenge.codex.security.model.User
import org.cloudchallenge.codex.security.model.UserCriteria
import org.cloudchallenge.codex.security.repository.UserRepository
import org.cloudchallenge.codex.security.service.validator.UserValidator
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.bcrypt.BCrypt.hashpw
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils.hasText
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class UserService(private val userRepository: UserRepository,
                  private val userValidator: UserValidator,
                  private val instanceService: InstanceService) {

    fun findAll(pageable: Pageable?, request: UserCriteria) : Page<User> = userRepository.findAll(pageable, request)

    fun findOne(id: String): User? = userRepository.findOne(ObjectId(id))

    fun findByEmail(email: String): User? = userRepository.findByEmail(email)

    fun insert(user: User): User {
        userValidator.validate(user)

        val hasPassword = hasText(user.password)

        val completedUser = if (!hasPassword) {
            val instanceKey = instanceService.getCurrentInstance().instanceKey
            val activationCode = "$instanceKey${randomString(60)}"
            user.copy(email = user.email!!.toLowerCase(), password = null, activationCode = activationCode,
                    activationValidity = Instant.now().plus(365, ChronoUnit.DAYS), active = false)
        } else {
            user.copy(email = user.email!!.toLowerCase(), password = hashpw(user.password, BCrypt.gensalt()))
        }

        return userRepository.insert(completedUser)
        /*if (!hasPassword) {
            userMailService.sendActivationEmail(createdUser)
        }*/
    }

    fun update(user: User): User? {
        userValidator.validate(user)

        if(hasText(user.password)) {
            user.password = hashpw(user.password, BCrypt.gensalt())
        }

        return userRepository.update(user)
    }

    fun delete(id: String) = userRepository.deleteById(ObjectId(id))
}