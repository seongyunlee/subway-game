package site.kkrupp.subway.common.util

import org.apache.commons.math3.random.RandomDataGenerator
import org.apache.commons.math3.special.Beta


class RandomUtil {

    companion object {
        var randomData: RandomDataGenerator = RandomDataGenerator()

        /**
         * 난이도 집중도. 값이 클수록 표본이 normalizeNumber 근처에 모인다.
         * 기존 값(2)은 분포가 너무 평평해서 0점에서도 중앙값이 전체의 20% 지점까지 갔다.
         */
        const val DEFAULT_CONCENTRATION = 8.0

        /**
         * [normalizeNumber] (0~1) 를 중심으로 하는 베타분포 표본을 뽑는다.
         * 0 이면 쉬운 쪽(0 근처), 1 이면 어려운 쪽(1 근처)에 몰린다.
         */
        fun randomBeta(normalizeNumber: Double, concentration: Double = DEFAULT_CONCENTRATION): Double {
            val p = normalizeNumber.coerceIn(0.0, 1.0)

            val alpha = 1 + concentration * p
            val beta = 1 + concentration * (1 - p)

            return randomData.nextBeta(alpha, beta)
        }
    }
}