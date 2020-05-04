package org.cloudchallenge.codex.security.repository

import org.bson.types.ObjectId
import org.cloudchallenge.codex.security.model.User
import org.cloudchallenge.codex.security.model.UserCriteria
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.util.StringUtils
import java.time.Instant

interface UserRepository : MongoRepository<User, ObjectId>, UserRepositoryCustom {
    fun findByEmail(email:String): User?
}

interface UserRepositoryCustom {
    fun findAll(pageable: Pageable?, request: UserCriteria): Page<User>
    fun update(user: User): User?
}

@Suppress("unused")
class UserRepositoryImpl(private val mongoTemplate: MongoTemplate) : UserRepositoryCustom {

    override fun findAll(pageable: Pageable?, request: UserCriteria): Page<User> {
        val criteria = Criteria()
        if (StringUtils.hasText(request.email)) {
            criteria.and("email").`is`(request.email?.toLowerCase())
        }

        val query = Query(criteria)
        val count = mongoTemplate.count(query, User::class.java)
        pageable?.let { query.with(pageable) }
        val events = mongoTemplate.find(query, User::class.java)
        pageable?.let { return PageImpl(events, pageable, count) }
        return PageImpl(events)
    }

    override fun update(user: User): User? {
        val criteria = Criteria.where("_id").`is`(user.id)
        val update = Update()
        user.password?.let { update["password"] = it }
        user.email?.let { update["email"] = it }
        user.firstName?.let { update["firstName"] = it }
        user.lastName?.let { update["lastName"] = it }
        user.active.let { update["active"] = it }
        user.locale.let { update["locale"] = it }

        update.set("modificationDate", Instant.now())

        return mongoTemplate.findAndModify(Query.query(criteria), update, FindAndModifyOptions.options().returnNew(true), User::class.java)
    }
}