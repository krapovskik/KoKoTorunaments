package com.sorsix.koko

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KoKoApplication

fun main(args: Array<String>) {
    runApplication<KoKoApplication>(*args)
}
