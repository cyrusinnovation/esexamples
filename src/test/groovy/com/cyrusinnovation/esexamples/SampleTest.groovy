package com.cyrusinnovation.esexamples

import spock.lang.Specification

class SampleTest extends Specification {
    def "sample test"() {
        given:
        def aVariable = true

        when:
        def result = !aVariable

        then:
        !result
    }
}
