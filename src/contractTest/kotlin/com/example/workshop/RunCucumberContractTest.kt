package com.example.workshop

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    plugin = ["pretty", "html:target/cucumber/contract", "json:target/cucumber/contract/report.json"],
    features = ["src/contractTest/resources/features"]
)
class RunCucumberContractTest
