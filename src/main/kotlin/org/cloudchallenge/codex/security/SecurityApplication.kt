package org.cloudchallenge.codex.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(CodexSecurityConfiguration::class)
@EnableConfigurationProperties
@EnableDiscoveryClient
class SecurityApplication

fun main(args: Array<String>) {
	runApplication<SecurityApplication>(*args)
}
