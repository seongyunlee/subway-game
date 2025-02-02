package site.kkrupp.subway.common.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RandomUtilTest {

    @Test
    fun testRandomExponential() {
        val randomNumbers = (1..10).map { e -> RandomUtil.randomBeta(e.toDouble() / 123) }

        Assertions.assertTrue(randomNumbers.all { it >= 0.0 && it <= 1.0 })
    }
}