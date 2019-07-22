package com.example.workshop.controllers

import com.example.workshop.collections.CalculatorRequest
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
@RequestMapping("/example/")
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
    fun sumTwoArguments(@Validated @RequestBody req: CalculatorRequest): ResponseEntity<CalculatorResponse> {
        logger.info("Sum 2 arguments: ${req.argument1}+${req.argument2}")
        val result = simpleCalculatorService.sum(req.argument1!!, req.argument2!!)
        logger.info("Result = $result")
        return ResponseEntity(CalculatorResponse(result = result.toString()), HttpStatus.OK)
    }
}
