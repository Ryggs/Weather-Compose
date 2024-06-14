package com.dvt.weather.interview.base

import org.junit.Rule

open class BaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
}
