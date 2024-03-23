package site.kkrupp.subway.fillblank.service

import org.springframework.stereotype.Service
import site.kkrupp.subway.fillblank.dto.BestProblemResponseDto
import site.kkrupp.subway.fillblank.dto.BestSubmitAnswerResponseDto


@Service
class TravelService {

    fun getProblem(): BestProblemResponseDto {
        return BestProblemResponseDto(1, "https://avatars.githubusercontent.com/u/79950005?v=4")
    }

    fun submitAnswer(): BestSubmitAnswerResponseDto {
        return BestSubmitAnswerResponseDto()
    }
}