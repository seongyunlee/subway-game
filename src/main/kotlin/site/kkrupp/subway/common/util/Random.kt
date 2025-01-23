package site.kkrupp.subway.common.util

import org.apache.commons.math3.random.RandomDataGenerator
import org.apache.commons.math3.special.Beta


class RandomUtil {

    companion object {
        var randomData: RandomDataGenerator = RandomDataGenerator()

        fun randomBeta(normalizeNumber: Double): Double {

            val alpha = 2 * normalizeNumber + 1
            val beta = 2 * (1 - normalizeNumber) + 1

            return randomData.nextBeta(alpha, beta);
        }
    }
}