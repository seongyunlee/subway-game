package site.kkrupp.subway.fillblank.service

import org.springframework.stereotype.Service
import site.kkrupp.subway.fillblank.dto.FillBlankResponseDto
import site.kkrupp.subway.fillblank.dto.FillBlankSubmitResponseDto
import site.kkrupp.subway.fillblank.dto.Problem


@Service
class FillBlankService {

    fun getProblems(level: Int): FillBlankResponseDto {

        return fillBlankResponseDto

    }

    fun submitAnswer(): FillBlankSubmitResponseDto {
        return fillBlankSubmitResponseDto
    }

    companion object MockUp {
        private val problems = listOf(
            Problem(1, "problem1.jpg", listOf()),
            Problem(2, "problem2.jpg", listOf()),
            Problem(3, "problem3.jpg", listOf()),
        )
        val fillBlankResponseDto = FillBlankResponseDto(1, problems)

        private val fillBlankSubmitResponseDto = FillBlankSubmitResponseDto(
            true, "1234-5678-9012-3456", "newToken"
        )
    }
}
