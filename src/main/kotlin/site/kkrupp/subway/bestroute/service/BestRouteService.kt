package site.kkrupp.subway.fillblank.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import site.kkrupp.subway.bestroute.dto.*
import site.kkrupp.subway.bestroute.repository.BestRouteRepository
import site.kkrupp.subway.fillblank.dto.*
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
     *  @param playerInfo :
     *  플레이어 정보 , (- gameId (게임에 대한 unique Id)
     * - gameType (어떤 게임을 하는지)
     * - gameScore (현재 점수)
     * - gameLife (남은 목숨))  를 가지고 있음
     */
    fun startGame(): BestRouteResponseDto {
        val playerInfo = initialPlayer()
        getProblem(playerInfo.gameScore).let {
            playerInfo.currentContext = it.id.toString()
            playerRepository.save(playerInfo)
            return BestRouteResponseDto(playerInfo.playerId!!, it)
        }
    }

    /**
     * Pick right problem based on the score
     */
    fun getProblem(score: Int): BestRouteProblem {
        val problem = bestRouteRepository.findById("1").orElseThrow()
        problem.apply {
            return BestRouteProblem(
                id = problem.id,
                startStation = startStation.name,
                endStation = endStation.name,
                choices = listOf(choice1, choice2, choice3, choice4).map { StationChoice(it.id, it.name) }
            )
        }
    }

    private fun initialPlayer(): Player {
        val playerInfo = Player(gameType = GameType.FILLBLANK, gameLife = 3)
        return playerRepository.save(playerInfo)
    }

    fun submitAnswer(bestRouteAnswerDto: BestRouteAnswerDto, player: Player): BestRouteSubmitResponseDto {
        // TODO: 정답 확인 로직
        if (player.currentContext != bestRouteAnswerDto.problemId.toString()) {
            logger.info("Malicious request detected. Player: ${player.playerId}")
            throw IllegalArgumentException("Invalid request")
        }
        if (checkAnswer(bestRouteAnswerDto.answer, bestRouteAnswerDto.problemId)) {
            player.gameScore += 1
        } else {
            player.gameLife -= 1
        }
        val newProblem = getProblem(player.gameScore)
        player.currentContext = newProblem.id.toString()
        playerRepository.save(player)
        getProblem(player.gameScore).let {
            return BestRouteSubmitResponseDto(true, it.id, newProblem)
        }
    }

    private fun checkAnswer(answer: String, problemId: Int): Boolean {
        val problem = bestRouteRepository.findById(problemId.toString()).orElseThrow()
        return problem.answer == answer
    }
}