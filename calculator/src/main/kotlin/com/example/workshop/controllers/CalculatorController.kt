package com.example.workshop.controllers

import com.example.workshop.collections.CalculatorTwoArgumentsRequest
import com.example.workshop.collections.CalculatorOneArgumentRequest
import com.example.workshop.collections.CalculatorResponse
import com.example.workshop.services.SimpleCalculatorService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@RestController
@RequestMapping("/calculator/v1/")
class CalculatorController(
    private val simpleCalculatorService: SimpleCalculatorService
) {

    companion object {
        val logger: Logger = Logger.getLogger(this::class.java.simpleName)
    }

    /**
     * Sum of 2 arguments
     */
    @PostMapping("/sum", consumes = [ MediaType.APPLICATION_JSON_UTF8_VALUE ], produces = [ MediaType.APPLICATION_JSON_UTF8_VALUE ])
    fun sumTwoArguments(@Validated @RequestBody req: CalculatorTwoArgumentsRequest): ResponseEntity<CalculatorResponse> {
        val result = simpleCalculatorService.sum(req.argument1!!, req.argument2!!)
        logger.info("Sum 2 arguments: ${req.argument1} + ${req.argument2} = $result")
        return ResponseEntity(CalculatorResponse(result = result.toString()), HttpStatus.OK)
    }

    /**
     * Difference between 2 arguments
     */
    @PostMapping("/diff", consumes = [ MediaType.APPLICATION_JSON_UTF8_VALUE ], produces = [ MediaType.APPLICATION_JSON_UTF8_VALUE ])
    fun diffTwoArguments(@Validated @RequestBody req: CalculatorTwoArgumentsRequest): ResponseEntity<CalculatorResponse> {
        val result = simpleCalculatorService.diff(req.argument1!!, req.argument2!!)
        logger.info("Difference between 2 arguments: ${req.argument1} - ${req.argument2} = $result")
        return ResponseEntity(CalculatorResponse(result = result.toString()), HttpStatus.OK)
    }

    /**
     * Sin of an argument
     */
    @PostMapping("/sin", consumes = [ MediaType.APPLICATION_JSON_UTF8_VALUE ], produces = [ MediaType.APPLICATION_JSON_UTF8_VALUE ])
    fun sin(@Validated @RequestBody req: CalculatorOneArgumentRequest): ResponseEntity<CalculatorResponse> {
        val result = simpleCalculatorService.sin(req.argument1!!)
        logger.info("Sinus of arguments: sin(${req.argument1}) = $result")
        return ResponseEntity(CalculatorResponse(result = result.toString()), HttpStatus.OK)
    }
}
