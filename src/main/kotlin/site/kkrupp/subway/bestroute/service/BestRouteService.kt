package site.kkrupp.subway.fillblank.service

import jakarta.transaction.Transactional
import org.apache.coyote.BadRequestException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import site.kkrupp.subway.bestroute.domain.BestRouteProblemAnswer
import site.kkrupp.subway.bestroute.dto.request.BestRouteSubmitAnswerRequestDto
import site.kkrupp.subway.bestroute.dto.response.*
import site.kkrupp.subway.bestroute.repository.BestRouteRepository
import site.kkrupp.subway.common.util.RandomUtil
import site.kkrupp.subway.player.domain.Player
import site.kkrupp.subway.player.service.PlayerService
import site.kkrupp.subway.utill.Utils
import site.kkrupp.subway.utill.GameType

@Service
class BestRouteService(
    private val bestRouteRepository: BestRouteRepository,
    private val playerService: PlayerService,
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
        val player = initializePlayer()
        val problem = getProblem(player.gameScore)
        savePlayerContext(player, problem)
        return problem.toStartDto(player)
    }


    @Transactional
    fun submitAnswer(
        dto: BestRouteSubmitAnswerRequestDto,
        player: Player
    ): BestRouteSubmitAnswerResponseDto {

        verifyPlayer(player, dto)

        val prevProblem =
            bestRouteRepository.findById(dto.problemId.toString()).orElseThrow()

        val isCorrect = checkAnswerAndUpdateStatics(dto.answer, prevProblem)

        if (isCorrect) {
            player.increaseScore()
        } else {
            player.decreaseLife()
        }

        if (player.gameLife <= 0) {
            player.endTime = Utils.getKoreaLocalDateTime()
        }

        val newProblem = getProblem(player.gameScore)

        savePlayerContext(player, newProblem)

        return newProblem.toSubmitAnswerDto(isCorrect, prevProblem)

    }

    private fun verifyPlayer(
        player: Player,
        bestRouteSubmitAnswerRequestDto: BestRouteSubmitAnswerRequestDto
    ) {
        if (player.gameLife <= 0) {
            throw BadRequestException("Game Over")
        }

        if (player.currentContext != bestRouteSubmitAnswerRequestDto.problemId.toString()) {
            logger.info("Malicious request detected. Player: ${player.playerId}")
            throw IllegalArgumentException("Invalid request")
        }
    }

    private fun initializePlayer(): Player {
        val playerInfo = Player(gameType = GameType.BESTROUTE, gameLife = 3)
        playerService.savePlayer(playerInfo)
        return playerInfo
    }

    private fun savePlayerContext(
        playerInfo: Player,
        problem: BestRouteProblemAnswer
    ) {
        playerInfo.currentContext = problem.id.toString()
        playerService.savePlayer(playerInfo)
    }


    /**
     * Pick right problem based on the score
     */
    private fun getProblem(score: Int): BestRouteProblemAnswer {
        val totalProblems = bestRouteRepository.count()
        val problemIndex = Math.round(RandomUtil.randomBeta(score.toDouble() / totalProblems) * totalProblems)
        val problem = bestRouteRepository.getProblemSortedByDifficultyIndexDesc(problemIndex.toInt())

        return problem
    }


    private fun checkAnswerAndUpdateStatics(answer: Long, problem: BestRouteProblemAnswer): Boolean {
        val isCorrect = problem.answer.id == answer
        if (isCorrect) {
            problem.correctCnt += 1
        } else {
            problem.wrongCnt += 1
        }
        bestRouteRepository.save(problem)

        return isCorrect
    }
}