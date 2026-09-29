package site.kkrupp.subway.fillblank.service

import org.apache.coyote.BadRequestException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import site.kkrupp.subway.common.util.RandomUtil
import site.kkrupp.subway.fillblank.domain.FillBlankProblemAnswer
import site.kkrupp.subway.fillblank.dto.request.FillBlankSubmitAnswerRequestDto
import site.kkrupp.subway.fillblank.dto.response.FillBlankProblemDto
import site.kkrupp.subway.fillblank.dto.response.FillBlankStartGameResponseDto
import site.kkrupp.subway.fillblank.dto.response.FillBlankSubmitAnswerResponseDto
import site.kkrupp.subway.fillblank.repository.FillBlankRepository
import site.kkrupp.subway.player.domain.Player
import site.kkrupp.subway.player.repository.PlayerRepository
import site.kkrupp.subway.utill.GameType
import java.time.ZoneId
import java.time.ZonedDateTime


@Service
class FillBlankService(
    private val playerRepository: PlayerRepository,
    private val fillBlankRepository: FillBlankRepository,
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)!!

    companion object {
        /** 이 점수에 도달하면 가장 어려운 구간의 문제가 나온다. */
        private const val MAX_DIFFICULTY_SCORE = 30
    }

    fun startGame(): FillBlankStartGameResponseDto {
        val playerInfo = initialPlayer()
        val problem = getProblem(playerInfo.gameScore)
        playerInfo.currentContext = problem.id.toString()
        playerRepository.save(playerInfo)
        return FillBlankStartGameResponseDto(playerInfo.playerId, problem, playerInfo.gameLife, playerInfo.gameScore)
    }

    /**
     * 점수가 높을수록 난이도 높은(= 승하차 인원이 적은) 문제가 나오도록 베타분포로 인덱스를 뽑는다.
     */
    fun getProblem(score: Int): FillBlankProblemDto {
        val numberOfProblems = fillBlankRepository.count()
        if (numberOfProblems == 0L) throw IllegalStateException("No fill-blank problems available")

        // 난이도는 "점수 / 목표점수" 로 정규화한다. 문제 개수로 나누면 (541개 기준) 40점을 받아도
        // p 가 0.07 에 그쳐 난이도가 사실상 오르지 않는다.
        val difficulty = (score.toDouble() / MAX_DIFFICULTY_SCORE).coerceIn(0.0, 1.0)

        // randomBeta 가 1.0 에 가까우면 인덱스가 전체 개수와 같아져 OFFSET 이 범위를 벗어난다.
        val raw = Math.round(RandomUtil.randomBeta(difficulty) * numberOfProblems)
        val index = raw.coerceIn(0, numberOfProblems - 1).toInt()

        val problem = fillBlankRepository.findNthSortedByBoardingCnt(index)
            ?: throw IllegalStateException("No problem at index $index of $numberOfProblems")

        return FillBlankProblemDto(
            id = problem.id,
            problemImage = problem.problemImage,
        )
    }

    private fun initialPlayer(): Player {
        val playerInfo = Player(gameType = GameType.FILLBLANK, gameLife = 3)
        return playerRepository.save(playerInfo)
    }

    fun submitAnswer(
        dto: FillBlankSubmitAnswerRequestDto,
        player: Player
    ): FillBlankSubmitAnswerResponseDto {
        if (player.gameLife <= 0) {
            throw BadRequestException("Game Over")
        }

        if (player.currentContext != dto.problemId.toString()) {
            logger.info("Malicious request detected. Player: ${player.playerId}")
            throw IllegalArgumentException("Invalid request")
        }

        val problem = fillBlankRepository.findById(dto.problemId.toString()).orElseThrow {
            InternalError("Invalid problem id")
        }

        val isCorrect = checkAnswerAndUpdateStatics(dto.answer, problem)

        if (!isCorrect) {
            player.gameLife -= 1
        } else {
            player.gameScore += 1
        }
        if (player.gameLife <= 0) {
            player.endTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()
            logger.info("Game Over. Player: ${player.playerId} ${player.gameScore} ${player.endTime}")
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

    private fun checkAnswerAndUpdateStatics(answer: String, problem: FillBlankProblemAnswer): Boolean {
        val correctAnswers = problem.answer.aliasName.map { it.name }.toMutableSet()
        correctAnswers.add(problem.answer.name)

        val isCorrect = correctAnswers.contains(answer.trim())

        if (isCorrect) {
            problem.correctCnt += 1
        } else {
            problem.wrongCnt += 1
        }
        fillBlankRepository.save(problem)

        return isCorrect
    }

}
