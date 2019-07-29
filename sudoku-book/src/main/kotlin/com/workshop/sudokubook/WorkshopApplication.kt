package com.workshop.sudokubook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WorkshopApplication

fun main(args: Array<String>) {
    initLogging()
    runApplication<WorkshopApplication>(*args)
}

/**
 * Initialize logging (just some system props).
 */
fun initLogging() {
    // Before we start anything, set up the JULI->Log4J2 Adapter
    // -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager
    // See http://logging.apache.org/log4j/2.x/log4j-jul/index.html
    // System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager")
    // JBoss Logging should use SLF4J.
    System.setProperty("org.jboss.logging.provider", "slf4j")
}
