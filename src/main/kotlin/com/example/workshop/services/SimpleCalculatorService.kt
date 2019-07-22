package com.example.workshop.services

import org.springframework.stereotype.Component

@Component
class SimpleCalculatorService {

    fun sum(argument1: String, argument2: String) = argument1.toInt() + argument2.toInt()
}
