package site.kkrupp.subway.common.util

import org.slf4j.LoggerFactory
import kotlin.math.exp
import kotlin.math.pow
import kotlin.random.Random

class RandomUtil {

    companion object {
        private fun sigmoid(x: Double): Double {
            val epsilon = 0.5
            val lambda = 13.81
            return 1 / (1 + exp(-lambda * (x - epsilon)))
        }

        private fun exponential(x: Double): Double {
            val a = 0.0095
            val b = 105.149
            return a * b.pow(x)
        }

        /**
         * Generate a random number between 0 and 1 and apply sigmoid function to it
         * @return a random number between 0 and 1
         */
        fun randomExponential(): Double {
            return exponential(Random.nextDouble())
        }
    }
}