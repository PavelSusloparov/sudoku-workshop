package com.example.workshop.services

import org.springframework.stereotype.Component

@Component
class SimpleCalculatorService {

    fun sum(argument1: Int, argument2: Int) = argument1 + argument2

    fun diff(argument1: Int, argument2: Int) = argument1 - argument2

    fun sin(x: Double) = java.lang.Math.sin(x)
}
