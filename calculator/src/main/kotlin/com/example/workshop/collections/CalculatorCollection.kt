package com.example.workshop.collections

import org.jetbrains.annotations.NotNull

class CalculatorTwoArgumentsRequest {

    @NotNull(value = "argument1 is required")
    val argument1: Int? = null

    @NotNull(value = "argument2 is required")
    val argument2: Int? = null
}

class CalculatorOneArgumentRequest {

    @NotNull(value = "argument1 is required")
    val argument1: Double? = null
}

class CalculatorResponse(var result: String)
