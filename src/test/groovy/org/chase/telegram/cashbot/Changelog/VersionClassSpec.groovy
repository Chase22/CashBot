package org.chase.telegram.cashbot.Changelog

import spock.lang.Specification
import spock.lang.Unroll

class VersionClassSpec extends Specification {

    @Unroll
    def "the fromString method should return a correct Version object for #input"() {
        when:
        Version version = new Version()
        version.fromString(input)

        then:
        version.major == major
        version.minor == minor
        version.patch == patch

        where:
        input | major | minor | patch
        "1.3.2" | 1 | 3 | 2
        "2.1"   | 2 | 1 | 0
    }

    def "the compareTo should return #expected for #version1 #version2"() {

        when:
        int compare = version1.compareTo(version2)

        then:
        compare == expected

        where:
        version1             | version2             | expected
        new Version(1, 1, 4) | new Version(1, 1, 4) | 0
        new Version(1, 2, 4) | new Version(1, 1, 4) | 1
        new Version(1, 1, 4) | new Version(2, 1, 4) | -1
    }

    def "toSting should return the version in pattern major.minor.patch"() {
        given:
        Version version = new Version(2,4,3)

        when:
        String verionString = version.toString()

        then:
        verionString == "2.4.3"
    }
}
