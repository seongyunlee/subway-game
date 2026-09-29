package site.kkrupp.subway.fillblank.service

import org.apache.coyote.BadRequestException
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import site.kkrupp.subway.bestroute.domain.BestRouteProblemAnswer
import site.kkrupp.subway.bestroute.dto.request.BestRouteSubmitAnswerRequestDto
import site.kkrupp.subway.bestroute.dto.response.*
import site.kkrupp.subway.bestroute.repository.BestRouteRepository
import site.kkrupp.subway.common.util.RandomUtil
import site.kkrupp.subway.player.domain.Player
import site.kkrupp.subway.player.repository.PlayerRepository
import site.kkrupp.subway.utill.GameType
import java.time.ZoneId
import java.time.ZonedDateTime

@Service
class BestRouteService(
    private val bestRouteRepository: BestRouteRepository,
    private val playerRepository: PlayerRepository
) {

    val logger = LoggerFactory.getLogger(this.javaClass)!!

    companion object {
        /** 이 점수에 도달하면 가장 어려운 구간의 문제가 나온다. */
        private const val MAX_DIFFICULTY_SCORE = 30
    }

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
            return BestRouteStartGameResponseDto(playerInfo.playerId, it, playerInfo.gameLife, playerInfo.gameScore)
        }
    }

    /** 문제 테이블을 재적재한 뒤 호출해야 캐시가 옛 문제를 내보내지 않는다. */
    @CacheEvict(value = ["bestRouteProblem"], allEntries = true)
    fun evictProblemCache() {
        logger.info("best route problem cache evicted")
    }

    /**
     * 점수가 높을수록 난이도 높은 문제가 나오도록 베타분포로 인덱스를 뽑는다.
     * DIFFICULTY_INDEX 는 "쉬움 점수"라 내림차순 0번이 가장 쉬운 문제다.
     */
    fun getProblem(score: Int): BestRouteProblemDto {
        val totalProblems = bestRouteRepository.count()
        if (totalProblems == 0L) throw IllegalStateException("No best-route problems available")

        // 난이도는 "점수 / 목표점수" 로 정규화한다. 문제 개수로 나누면 (2000개 기준)
        // 30점을 받아도 p 가 0.015 에 그쳐 난이도가 오르지 않는다.
        val difficulty = (score.toDouble() / MAX_DIFFICULTY_SCORE).coerceIn(0.0, 1.0)

        val raw = Math.round(RandomUtil.randomBeta(difficulty) * totalProblems)
        val problemIndex = raw.coerceIn(0, totalProblems - 1)

        val problem = bestRouteRepository.getProblemSortedByDifficultyIndexDesc(problemIndex.toInt())
            ?: throw IllegalStateException("No problem at index $problemIndex of $totalProblems")

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
        val playerInfo = Player(gameType = GameType.BESTROUTE, gameLife = 3)
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

        val isCorrect = checkAnswerAndUpdateStatics(bestRouteSubmitAnswerRequestDto.answer, problem)
        if (isCorrect) {
            player.gameScore += 1
        } else {
            player.gameLife -= 1
        }
        val result = BestRouteSubmitAnswerResponseDto(
            isCorrect = isCorrect,
            answer = problem.answer.id,
            correctMinute = listOf(
                CorrectMinute(problem.choice1.id, problem.choice1Time),
                CorrectMinute(problem.choice2.id, problem.choice2Time),
                CorrectMinute(problem.choice3.id, problem.choice3Time),
                CorrectMinute(problem.choice4.id, problem.choice4Time)
            ),
            newProblem = getProblem(player.gameScore),
            gameLife = player.gameLife,
            gameScore = player.gameScore
        )


        player.currentContext = result.newProblem.id.toString()
        if (player.gameLife <= 0) {
            player.endTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()
        }
        playerRepository.save(player)

        return result
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