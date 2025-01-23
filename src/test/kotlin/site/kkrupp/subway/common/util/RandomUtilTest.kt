package site.kkrupp.subway.common.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RandomUtilTest {

    @Test
    fun testRandomExponential() {
        val randomNumbers = (0..1000).map { RandomUtil.randomBeta(1.0 / 123) }

        // show graph with 0.1 interval
        val histogram = randomNumbers.groupBy { (it * 10).toInt() }
            .mapValues { it.value.size }
            .toSortedMap()

        println(histogram)
    }
}