package org.cloudchallenge.codex.security.repository

import org.cloudchallenge.codex.core.model.Instance
import org.springframework.data.mongodb.repository.MongoRepository

interface InstanceRepository : MongoRepository<Instance, String>