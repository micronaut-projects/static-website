package io.micronaut.airtable

import io.micronaut.core.beans.BeanIntrospection
import spock.lang.Specification

class RecordSpec extends Specification {

    void "Record is annotated with @Introspected"() {
        when:
        BeanIntrospection.getIntrospection(Record)

        then:
        noExceptionThrown()
    }
}
