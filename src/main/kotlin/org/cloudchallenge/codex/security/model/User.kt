package org.cloudchallenge.codex.security.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlRootElement(name = "user")
data class User(
        @Id
        var id: String? = null,
        var email: String? = null,
        @JsonIgnore
        @get:Transient
        var password: String? = null,
        var firstName: String? = null,
        var lastName: String? = null,
        var locale: Locale = Locale.getDefault(),

        var creationDate: Instant = Instant.now(),
        var modificationDate: Instant? = Instant.now(),

        var active: Boolean = true,
        var activationDate: Instant? = null,
        var activationCode: String? = null,
        var activationValidity: Instant? = null
){
        // Needed for spring data mongo, @get:SpringTransient annotation is responsible for the
        // error "No property children found on entity class to bind constructor parameter to"
        @Suppress("unused")
        @PersistenceConstructor
        constructor() : this(password=null)
}