package com.sanya.mg.sanyademo

import com.sanya.mg.sanyademo.common.BaseIT
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient

@AutoConfigureWebTestClient
class ExampleIT() : BaseIT() {
    init {

        test("first test") {
        }
    }
}
