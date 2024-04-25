package site.kkrupp.subway.common.util

import kotlin.math.exp
import kotlin.random.Random

class RandomUtil {
    companion object {
        private fun sigmoid(x: Double): Double {
            val epsilon = 0.5
            val lambda = 13.81
            return 1 / (1 + exp(-lambda * (x - epsilon)))
        }

        private fun exponential(x: Double): Double {
            val y0 = 0.00197
            val k = 2.786
            val v0 = -2.9724
            return y0 - k/v0* (1 - exp(-k*x))
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