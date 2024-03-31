package site.kkrupp.subway.fillblank.service

import org.apache.coyote.BadRequestException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import site.kkrupp.subway.bestroute.domain.BestRouteProblemAnswer
import site.kkrupp.subway.bestroute.dto.request.BestRouteSubmitAnswerRequestDto
import site.kkrupp.subway.bestroute.dto.response.*
import site.kkrupp.subway.bestroute.repository.BestRouteRepository
import site.kkrupp.subway.player.domain.Player
import site.kkrupp.subway.player.repository.PlayerRepository
import site.kkrupp.subway.utill.GameType

@Service
class BestRouteService(
    private val bestRouteRepository: BestRouteRepository,
    private val playerRepository: PlayerRepository
) {

    val logger = LoggerFactory.getLogger(this.javaClass)!!

    /**
     *  문제를 하나를 건네줌.
     *  session에서 score를 가져와야함.
     *  score 별로 문제 난이도를 올려서 제공해야함.
     *  플레이어 정보 , (- gameId (게임에 대한 unique Id)
     * - gameType (어떤 게임을 하는지)
     * - gameScore (현재 점수)
     * - gameLife (남은 목숨))  를 가지고 있음
     */
    fun startGame(): BestRouteStartGameResponseDto {
        val playerInfo = initialPlayer()
        getProblem(playerInfo.gameScore).let {
            playerInfo.currentContext = it.id.toString()
            playerRepository.save(playerInfo)
            return BestRouteStartGameResponseDto(playerInfo.playerId!!, it, playerInfo.gameLife, playerInfo.gameScore)
        }
    }

    /**
     * Pick right problem based on the score
     */
    fun getProblem(score: Int): BestRouteProblemDto {
        val problem = bestRouteRepository.findById("1").orElseThrow()
        problem.apply {
            return BestRouteProblemDto(
                id = problem.id,
                startStation = startStation.name,
                endStation = endStation.name,
                choices = listOf(choice1, choice2, choice3, choice4).map { StationChoiceDto(it.id, it.name) }
            )
        }
    }

    private fun initialPlayer(): Player {
        val playerInfo = Player(gameType = GameType.FILLBLANK, gameLife = 3)
        return playerRepository.save(playerInfo)
    }

    fun submitAnswer(
        bestRouteSubmitAnswerRequestDto: BestRouteSubmitAnswerRequestDto,
        player: Player
    ): BestRouteSubmitAnswerResponseDto {
        // TODO: 정답 확인 로직
        if (player.gameLife <= 0) {
            throw BadRequestException("Game Over")
        }

        if (player.currentContext != bestRouteSubmitAnswerRequestDto.problemId.toString()) {
            logger.info("Malicious request detected. Player: ${player.playerId}")
            throw IllegalArgumentException("Invalid request")
        }

        val problem = bestRouteRepository.findById(bestRouteSubmitAnswerRequestDto.problemId.toString()).orElseThrow()

        val isCorrect = checkAnswer(bestRouteSubmitAnswerRequestDto.answer, problem)
        val result = BestRouteSubmitAnswerResponseDto(
            isCorrect = isCorrect,
            answer = problem.answer.id,
            correctMinute = listOf(
                CurrentMinute(problem.choice1.id, problem.choice1Time),
                CurrentMinute(problem.choice2.id, problem.choice2Time),
                CurrentMinute(problem.choice3.id, problem.choice3Time),
                CurrentMinute(problem.choice4.id, problem.choice4Time)
            ),
            newProblem = getProblem(player.gameScore + isCorrect.let { 1 }),
            gameLife = player.gameLife,
            gameScore = player.gameScore
        )

        if (!result.isCorrect) {
            player.gameLife -= 1
        } else {
            player.gameScore += 1
        }
        player.currentContext = result.newProblem.id.toString()
        playerRepository.save(player)

        return result
    }

    private fun checkAnswer(answer: String, problem: BestRouteProblemAnswer): Boolean {
        return problem.answer.id == answer
    }
}