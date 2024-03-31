package site.kkrupp.subway.fillblank.service

import org.apache.coyote.BadRequestException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import site.kkrupp.subway.fillblank.domain.FillBlankProblemAnswer
import site.kkrupp.subway.fillblank.dto.request.FillBlankSubmitAnswerRequestDto
import site.kkrupp.subway.fillblank.dto.response.FillBlankProblemDto
import site.kkrupp.subway.fillblank.dto.response.FillBlankStartGameResponseDto
import site.kkrupp.subway.fillblank.dto.response.FillBlankSubmitAnswerResponseDto
import site.kkrupp.subway.fillblank.repository.FillBlankRepository
import site.kkrupp.subway.player.domain.Player
import site.kkrupp.subway.player.repository.PlayerRepository
import site.kkrupp.subway.utill.GameType


@Service
class FillBlankService(
    private val playerRepository: PlayerRepository,
    private val fillBlankRepository: FillBlankRepository,
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)!!

    fun startGame(): FillBlankStartGameResponseDto {
        val playerInfo = initialPlayer()
        val problem = getProblem(playerInfo.gameScore)
        playerInfo.currentContext = problem.id.toString()
        playerRepository.save(playerInfo)
        return FillBlankStartGameResponseDto(playerInfo.playerId!!, problem, playerInfo.gameLife, playerInfo.gameScore)
    }

    /**
     * Pick right problem based on the score
     */
    fun getProblem(score: Int): FillBlankProblemDto {
        val problem = fillBlankRepository.findById("1").orElseThrow()
        problem.apply {
            return FillBlankProblemDto(
                id = problem.id,
                problemImage = problem.problemImage,
            )
        }
    }

    private fun initialPlayer(): Player {
        val playerInfo = Player(gameType = GameType.FILLBLANK, gameLife = 3)
        return playerRepository.save(playerInfo)
    }

    fun submitAnswer(
        fillBlankSubmitAnswerRequestDto: FillBlankSubmitAnswerRequestDto,
        player: Player
    ): FillBlankSubmitAnswerResponseDto {
        // TODO: 정답 확인 로직
        if (player.gameLife <= 0) {
            throw BadRequestException("Game Over")
        }

        if (player.currentContext != fillBlankSubmitAnswerRequestDto.problemId.toString()) {
            logger.info("Malicious request detected. Player: ${player.playerId}")
            throw IllegalArgumentException("Invalid request")
        }

        val problem = fillBlankRepository.findById(fillBlankSubmitAnswerRequestDto.problemId.toString()).orElseThrow()

        val isCorrect = checkAnswer(fillBlankSubmitAnswerRequestDto.answer, problem)

        if (!isCorrect) {
            player.gameLife -= 1
        } else {
            player.gameScore += 1
        }

        val newProblem = getProblem(player.gameScore)

        val result = FillBlankSubmitAnswerResponseDto(
            newProblem = newProblem,
            answer = problem.answer.name,
            isCorrect = isCorrect,
            gameLife = player.gameLife,
            gameScore = player.gameScore,
        )

        player.currentContext = newProblem.id.toString()
        playerRepository.save(player)

        return result
    }

    private fun checkAnswer(answer: String, problem: FillBlankProblemAnswer): Boolean {
        val correctAnswers = problem.answer.name + problem.answer.aliasName
        logger.info("correctAnswers: $correctAnswers")
        return correctAnswers.contains(answer.trim())
    }

}
