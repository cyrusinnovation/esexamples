package com.cyrusinnovation.esexamples

import spock.lang.Specification

class TestFailingSpec extends Specification {
    def "a test that fails"() {
        expect:
        false == true
    }
}
