package site.kkrupp.subway.fillblank.service

import jakarta.transaction.Transactional
import org.apache.coyote.BadRequestException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import site.kkrupp.subway.player.domain.Player
import site.kkrupp.subway.player.repository.PlayerRepository
import site.kkrupp.subway.station.domain.Station
import site.kkrupp.subway.station.repository.StationRepository
import site.kkrupp.subway.travel.dto.ChatContextDto
import site.kkrupp.subway.travel.dto.request.TravelReportAnswerRequestDto
import site.kkrupp.subway.travel.dto.request.TravelSubmitAnswerRequestDto
import site.kkrupp.subway.travel.dto.response.TravelReportAnswerResponseDto
import site.kkrupp.subway.travel.dto.response.TravelStartGameResponseDto
import site.kkrupp.subway.travel.dto.response.TravelSubmitAnswerResponseDto
import site.kkrupp.subway.utill.GameType


@Service
class TravelService(
    private val playerRepository: PlayerRepository,
    private val stationRepository: StationRepository
) {

    val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun startGame(startLineId: String): TravelStartGameResponseDto {
        val playerInfo = initialPlayer()
        val nextStation = makeAnswer(
            startLineId,
            listOf()
        )
        val newContext = ChatContextDto(
            currentLine = startLineId,
            previousStationIds = listOf(nextStation.id)
        )

        playerInfo.currentContext = newContext.hashCode().toString()
        logger.info("hashcode: ${newContext.hashCode()} playerInfo: ${playerInfo}")

        playerRepository.save(playerInfo)


        return TravelStartGameResponseDto(
            playerId = playerInfo.playerId!!,
            chatContext = ChatContextDto(
                currentLine = startLineId,
                previousStationIds = listOf(nextStation.id),
            ),
            gameLife = playerInfo.gameLife,
            gameScore = playerInfo.gameScore
        )
    }

    /*
     * Pick right station based on the line
     */
    private fun pickCorrectStation(currentLineID: String, previousStationIds: List<Long>?): Station {
        val stationInLine =
            stationRepository.findByLineId(currentLineID)
        if (previousStationIds.isNullOrEmpty()) {
            return stationInLine.random()
        }
        return stationInLine.filter { it.id !in previousStationIds }.random()

    }

    private fun makeAnswer(currentLineID: String, previousStationIds: List<Long>?): Station {
        if ((0..10).random() != 10) {
            return pickCorrectStation(currentLineID, previousStationIds)
        } else {
            return pickWrongStation(currentLineID, previousStationIds)
        }
    }

    private fun pickWrongStation(currentLineID: String, previousStationIds: List<Long>?): Station {
        val stationInLine =
            stationRepository.findByLindIdNot(currentLineID)
        if (previousStationIds.isNullOrEmpty()) {
            return stationInLine.random()
        }
        return stationInLine.filter { it.id !in previousStationIds }.random()
    }

    @Transactional
    fun reportAnswer(
        dto: TravelReportAnswerRequestDto,
        player: Player
    ): TravelReportAnswerResponseDto {
        if (player.currentContext != dto.chatContext.hashCode().toString()) {
            throw BadRequestException("Invalid request")
        }
        if (player.gameLife <= 0) {
            throw BadRequestException("Game Over")
        }
        if (player.gameType != GameType.TRAVEL) {
            throw BadRequestException("Invalid game type")
        }
        if (dto.chatContext.previousStationIds.size % 2 == 0) {
            throw BadRequestException("Not your Turn")
        }
        val currentLine = dto.chatContext.currentLine
        val lastPickedStationId = dto.chatContext.previousStationIds.last()
        val lastPickedStation =
            stationRepository.findById(lastPickedStationId) ?: throw InternalError("Station not found")
        if (!lastPickedStation.lines.map { it.lineId }.contains(currentLine)) {
            // 오답 신고 성공
            player.gameScore += 10
            val changeStation = makeAnswer(
                currentLine,
                dto.chatContext.previousStationIds
            )
            dto.chatContext.previousStationIds.removeLast()
            dto.chatContext.previousStationIds.addLast(changeStation.id)
            player.currentContext = dto.chatContext.hashCode().toString()
            playerRepository.save(player)
            return TravelReportAnswerResponseDto(
                isSuccess = true,
                gameLife = player.gameLife,
                gameScore = player.gameScore,
                chatContext = dto.chatContext
            )
        } else {
            // 오답 신고 실패
            player.gameLife -= 1
            playerRepository.save(player)
            return TravelReportAnswerResponseDto(
                isSuccess = false,
                gameLife = 0,
                gameScore = player.gameScore,
                chatContext = dto.chatContext
            )
        }
    }

    @Transactional
    fun submitAnswer(
        dto: TravelSubmitAnswerRequestDto,
        player: Player
    ): TravelSubmitAnswerResponseDto {
        if (player.currentContext != dto.chatContext.hashCode().toString()) {
            throw BadRequestException("Invalid request")
        }
        if (player.gameLife <= 0) {
            throw BadRequestException("Game Over")
        }
        if (player.gameType != GameType.TRAVEL) {
            throw BadRequestException("Invalid game type")
        }
        val isCorrect = checkAnswer(dto.chatContext, dto.answer, dto.transferTo)
        if (isCorrect) {
            val currentLine = dto.transferTo ?: dto.chatContext.currentLine
            player.gameScore += 1
            val nextStation = makeAnswer(
                currentLine,
                dto.chatContext.previousStationIds
            )
            val newContext = ChatContextDto(
                currentLine = currentLine,
                previousStationIds = dto.chatContext.previousStationIds + nextStation.id
            )

            player.currentContext = newContext.hashCode().toString()
            playerRepository.save(player)
            return TravelSubmitAnswerResponseDto(
                isCorrect = true,
                gameLife = player.gameLife,
                gameScore = player.gameScore,
                chatContext = newContext
            )
        } else {
            player.gameLife = 0
            playerRepository.save(player)
            return TravelSubmitAnswerResponseDto(
                isCorrect = false,
                gameLife = player.gameLife,
                gameScore = player.gameScore,
                chatContext = dto.chatContext
            )
        }
    }


    private fun checkAnswer(chatContext: ChatContextDto, answer: String, transferTo: String?): Boolean {
        val enteredStation = stationRepository.findByNameOrAliasName_Name(answer, answer) ?: return false
        transferTo?.let {
            if (!enteredStation.lines.map { it.toString() }.contains(transferTo)) {
                return false
            }
        }
        val lineIds = enteredStation.lines.map { it.lineId }
        return lineIds.contains(chatContext.currentLine) &&
                !chatContext.previousStationIds.contains(enteredStation.id)
    }


    private fun initialPlayer(): Player {
        val playerInfo = Player(gameType = GameType.TRAVEL, gameLife = 1, gameScore = 0)
        return playerInfo
    }
}