package com.workshop.sudoku

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    plugin = ["pretty", "html:target/cucumber/component", "json:target/cucumber/component/report.json"],
    features = ["src/componentTest/resources/features"]
)
class RunCucumberComponentTest
