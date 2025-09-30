package com.sanya.mg.sanyademo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class SanyaDemoApplication

fun main(args: Array<String>) {
    runApplication<SanyaDemoApplication>(*args)
}
