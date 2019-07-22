package com.example.workshop.collections

import org.jetbrains.annotations.NotNull

class CalculatorRequest {

    @NotNull(value = "argument1 is required")
    val argument1: String? = null

    @NotNull(value = "argument2 is required")
    val argument2: String? = null
}

class CalculatorResponse(var result: String)
