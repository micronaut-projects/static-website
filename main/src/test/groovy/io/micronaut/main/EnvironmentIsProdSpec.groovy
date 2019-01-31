package io.micronaut.main

import io.micronaut.main.pages.HomePage
import spock.lang.Specification


class EnvironmentIsProdSpec extends Specification {

    def "checks url is set to https://micronaut.io"() {

        expect:
        new HomePage().micronautUrl().startsWith('https://micronaut.io')
    }

}