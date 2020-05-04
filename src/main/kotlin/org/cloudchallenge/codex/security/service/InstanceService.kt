package org.cloudchallenge.codex.security.service

import org.cloudchallenge.codex.core.config.CodexMongoProperties
import org.cloudchallenge.codex.core.context.InstanceContextHolder
import org.cloudchallenge.codex.core.context.withInstance
import org.cloudchallenge.codex.core.model.Instance
import org.cloudchallenge.codex.security.repository.InstanceRepository
import org.cloudchallenge.codex.core.lang.findOne
import org.springframework.stereotype.Service

@Service
class InstanceService(private val instanceRepository: InstanceRepository,
                      private val codexMongoProperties: CodexMongoProperties) {

    fun findOne(id: String): Instance? {
        withInstance(codexMongoProperties.globalDatabaseName) {
            return instanceRepository.findOne(id)
        }
    }

    fun getCurrentInstance(): Instance {
        val currentInstance = InstanceContextHolder.getInstanceId()
        try {
            InstanceContextHolder.setInstanceId(codexMongoProperties.globalDatabaseName)
            return instanceRepository.findOne(currentInstance)
                    ?: throw IllegalArgumentException("Instance $currentInstance is not found")
        } finally {
            InstanceContextHolder.setInstanceId(currentInstance)
        }
    }
}