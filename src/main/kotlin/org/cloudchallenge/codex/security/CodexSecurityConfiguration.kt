package org.cloudchallenge.codex.security

import org.cloudchallenge.codex.core.CodexCoreConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
@Import(CodexCoreConfiguration::class)
@EnableMongoRepositories
class CodexSecurityConfiguration