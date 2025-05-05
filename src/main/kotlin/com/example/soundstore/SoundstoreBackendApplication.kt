package com.example.soundstore

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SoundstoreBackendApplication

fun main(args: Array<String>) {
	runApplication<SoundstoreBackendApplication>(*args)
}
