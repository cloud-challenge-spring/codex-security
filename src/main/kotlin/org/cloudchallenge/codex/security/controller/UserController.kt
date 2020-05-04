package org.cloudchallenge.codex.security.controller

import org.cloudchallenge.codex.core.exception.NotFoundException
import org.cloudchallenge.codex.security.model.User
import org.cloudchallenge.codex.security.model.UserCriteria
import org.cloudchallenge.codex.security.service.UserService
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
@ConditionalOnClass(RestController::class)
class UserController(private val userService: UserService) {

    @GetMapping
    fun findAll(pageable: Pageable?, request: UserCriteria) = userService.findAll(pageable, request)

    @GetMapping("/{id}")
    fun findOne(@PathVariable id: String) = userService.findOne(id)?: throw NotFoundException("user id:'$id' not found")

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun insert(@RequestBody user: User): User =
        userService.insert(user.copy(email = user.email?.toLowerCase()))

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun update(@PathVariable id: String, @RequestBody user: User): User? {
        user.id = id
        return userService.update(user)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) {
        userService.delete(id)
    }
}