package io.micronaut.main

import io.micronaut.main.pages.HomePage
import spock.lang.Specification


class EnvironmentIsProdSpec extends Specification {

    def "checks url is set to http://micronaut.io"() {

        expect:
        new HomePage().micronautUrl().startsWith('http://micronaut.io')
    }

}