package site.kkrupp.subway.common.util

import kotlin.math.exp
import kotlin.random.Random

class Sigmoid {
    companion object {
        private fun sigmoid(x: Double): Double {
            val epsilon = 0.5
            val lambda = 13.81
            return 1 / (1 + exp(-lambda * (x - epsilon)))
        }

        /**
         * Generate a random number between 0 and 1 and apply sigmoid function to it
         * @return a random number between 0 and 1
         */
        fun randomSigmoid(): Double {
            return sigmoid(Random.nextDouble())
        }
    }
}