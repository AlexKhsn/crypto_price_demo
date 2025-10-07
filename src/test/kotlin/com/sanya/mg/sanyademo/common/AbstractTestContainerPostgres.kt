package com.sanya.mg.sanyademo.common

import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import org.junit.jupiter.api.TestInstance
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractTestContainerPostgres : FunSpec() {
    companion object {
        @JvmField
        val postgresContainer =
            PostgreSQLContainer<Nothing>("postgres:14.5").apply {
                withDatabaseName("testdb")
                withUsername("test")
                withPassword("test")
                withExposedPorts(5432)
                withCommand("postgres", "-c", "max_connections=200")
            }

        init {
            postgresContainer.start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
            registry.add("spring.datasource.password", postgresContainer::getPassword)
            registry.add("spring.datasource.driver-class-name") { "org.postgresql.Driver" }
        }
    }

    override fun extensions() = listOf(SpringExtension)
}
