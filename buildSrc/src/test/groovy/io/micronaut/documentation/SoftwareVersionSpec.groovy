package io.micronaut.documentation

import spock.lang.Specification
import spock.lang.Unroll

class SoftwareVersionSpec extends Specification {

    @Unroll
    void "it can parse version: #semver"(String semver, SoftwareVersion expected) {
        when:
        expected.versionText = semver
        SoftwareVersion softwareVersion = SoftwareVersion.build(semver)

        then:
        softwareVersion == expected

        where:
        semver                  || expected
        "1.2.3"                 || new SoftwareVersion(major: 1, minor: 2, patch: 3)

        "1.2.3.M1"              || new SoftwareVersion(major: 1, minor: 2, patch: 3, snapshot: new Snapshot("M1"))
        "1.2.3.RC1"             || new SoftwareVersion(major: 1, minor: 2, patch: 3, snapshot: new Snapshot("RC1"))
        "1.2.3.BUILD-SNAPSHOT"  || new SoftwareVersion(major: 1, minor: 2, patch: 3, snapshot: new Snapshot("BUILD-SNAPSHOT"))

        "1.2.3-M1"              || new SoftwareVersion(major: 1, minor: 2, patch: 3, snapshot: new Snapshot("M1"))
        "1.2.3-RC1"             || new SoftwareVersion(major: 1, minor: 2, patch: 3, snapshot: new Snapshot("RC1"))
        "1.2.3-BUILD-SNAPSHOT"  || new SoftwareVersion(major: 1, minor: 2, patch: 3, snapshot: new Snapshot("BUILD-SNAPSHOT"))
        "1.2.3-SNAPSHOT"        || new SoftwareVersion(major: 1, minor: 2, patch: 3, snapshot: new Snapshot("SNAPSHOT"))

    }
}
