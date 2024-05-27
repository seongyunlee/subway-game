package site.kkrupp.subway.travel.service

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
import site.kkrupp.subway.travel.dto.response.ChatItemDto
import site.kkrupp.subway.travel.dto.response.TravelReportAnswerResponseDto
import site.kkrupp.subway.travel.dto.response.TravelStartGameResponseDto
import site.kkrupp.subway.travel.dto.response.TravelSubmitAnswerResponseDto
import site.kkrupp.subway.utill.GameType
import java.time.ZoneId
import java.time.ZonedDateTime


@Service
class TravelService(
    private val playerRepository: PlayerRepository,
    private val stationRepository: StationRepository
) {

    val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun startGame(startLineId: String): TravelStartGameResponseDto {
        val playerInfo = initialPlayer()
        val nextStation = makeDealerAnswer(
            startLineId,
            listOf()
        )!!
        val newContext = ChatContextDto(
            currentLine = startLineId,
            previousStationIds = mutableListOf(nextStation.id)
        )

        playerInfo.currentContext = newContext.toString()

        playerRepository.save(playerInfo)


        return TravelStartGameResponseDto(
            playerId = playerInfo.playerId,
            chatContext = ChatContextDto(
                currentLine = startLineId,
                previousStationIds = listOf(nextStation.id),
            ),
            gameLife = playerInfo.gameLife,
            gameScore = playerInfo.gameScore,
            firstAnswer = ChatItemDto(
                stationName = nextStation.name,
                stationId = nextStation.id,
                originalLine = startLineId,
                transferTo = null,
            )
        )
    }

    /*
     * Pick right station based on the line
     */
    private fun pickCorrectStation(currentLineID: String, previousStationIds: List<Long>?): Station? {
        val stationInLine =
            stationRepository.findByLines_lineId(currentLineID)
        if (previousStationIds.isNullOrEmpty()) {
            return stationInLine.randomOrNull()
        }
        return stationInLine.filter { it.id !in previousStationIds }.randomOrNull()
    }

    private fun makeDealerAnswer(currentLineID: String, previousStationIds: List<Long>?): Station? {
        // 1/10 확률로 오답을 제공
        return if ((0..10).random() != 1) {
            pickCorrectStation(currentLineID, previousStationIds)
        } else {
            pickWrongStation(currentLineID, previousStationIds)
        }
    }

    private fun pickWrongStation(currentLineID: String, previousStationIds: List<Long>?): Station? {
        val stationInLine =
            stationRepository.findByLines_lineIdNot(currentLineID)
        if (previousStationIds.isNullOrEmpty()) {
            return stationInLine.random()
        }
        return stationInLine.filter { it.id !in previousStationIds }.randomOrNull()
    }

    @Transactional
    fun reportAnswer(
        dto: TravelReportAnswerRequestDto,
        player: Player
    ): TravelReportAnswerResponseDto {
        if (player.currentContext != dto.chatContext.toString()) {
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
            val changeStation = makeDealerAnswer(
                currentLine,
                dto.chatContext.previousStationIds
            )
            if (changeStation == null) {
                player.gameLife = 0
                playerRepository.save(player)
                return gameOverDto(player)
            }
            dto.chatContext.previousStationIds.removeLast()
            dto.chatContext.previousStationIds.addLast(changeStation.id)
            player.currentContext = dto.chatContext.toString()
            playerRepository.save(player)
            return TravelReportAnswerResponseDto(
                isSuccess = true,
                gameLife = player.gameLife,
                gameScore = player.gameScore,
                changeStation = ChatItemDto(
                    stationName = changeStation.name,
                    stationId = changeStation.id,
                    originalLine = currentLine,
                    transferTo = null
                ),
            )
        } else {
            return gameOverDto(player)
        }
    }

    private fun gameOverDto(
        player: Player,
    ): TravelReportAnswerResponseDto {
        player.endTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()
        player.gameLife = 0
        playerRepository.save(player)
        return TravelReportAnswerResponseDto(
            isSuccess = false,
            gameLife = 0,
            gameScore = player.gameScore,
            changeStation = null,
        )
    }

    @Transactional
    fun submitAnswer(
        dto: TravelSubmitAnswerRequestDto,
        player: Player
    ): TravelSubmitAnswerResponseDto {
        if (player.currentContext != dto.chatContext.toString()) {
            logger.info("Malicious request detected. saved:${player.currentContext} incoming:${dto.chatContext}")
            throw BadRequestException("Malicious request detected. Player: ${player.playerId}")
        }
        if (player.gameLife <= 0) {
            throw BadRequestException("Game Over")
        }
        if (player.gameType != GameType.TRAVEL) {
            throw BadRequestException("Invalid game type")
        }
        val isCorrect = checkAnswer(dto.chatContext, dto.answer, dto.transferTo)
        if (isCorrect != null) {
            val currentLine = dto.transferTo ?: dto.chatContext.currentLine
            player.gameScore += 1
            val nextStation = makeDealerAnswer(
                currentLine,
                dto.chatContext.previousStationIds
            )
            if (nextStation == null) {
                // 더이상 역이 없을때
                player.gameLife = 0
                player.endTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()
                playerRepository.save(player)
                return TravelSubmitAnswerResponseDto(
                    isCorrect = true,
                    gameLife = 0,
                    gameScore = player.gameScore,
                    chatContext = dto.chatContext,
                    submittedAnswer = ChatItemDto(
                        stationName = isCorrect.name,
                        stationId = isCorrect.id,
                        originalLine = currentLine,
                        transferTo = dto.transferTo,
                    ),
                    dealerAnswer = null,
                )
            }

            dto.chatContext.previousStationIds.let{
                it.add(isCorrect.id)
                it.add(nextStation.id)
            }

            val newContext = ChatContextDto(
                currentLine = currentLine,
                previousStationIds = dto.chatContext.previousStationIds
            )

            player.currentContext = newContext.toString()
            playerRepository.save(player)
            return TravelSubmitAnswerResponseDto(
                isCorrect = true,
                gameLife = player.gameLife,
                gameScore = player.gameScore,
                chatContext = newContext,
                submittedAnswer = ChatItemDto(
                    stationName = isCorrect.name,
                    stationId = isCorrect.id,
                    originalLine = currentLine,
                    transferTo = dto.transferTo,
                ),
                dealerAnswer = ChatItemDto(
                    stationName = nextStation.name,
                    stationId = nextStation.id,
                    originalLine = currentLine,
                    transferTo = dto.transferTo,
                ),
            )
        } else {
            //틀렸을때
            player.gameLife = 0
            player.endTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()
            playerRepository.save(player)
            return TravelSubmitAnswerResponseDto(
                isCorrect = false,
                gameLife = player.gameLife,
                gameScore = player.gameScore,
                chatContext = dto.chatContext,
                submittedAnswer = ChatItemDto(
                    stationName = dto.answer,
                    stationId = -1,
                    originalLine = dto.chatContext.currentLine,
                    transferTo = dto.transferTo ?: dto.chatContext.currentLine
                ),
                dealerAnswer = null,
            )
        }
    }


    private fun checkAnswer(chatContext: ChatContextDto, answer: String, transferTo: String?): Station? {
        val enteredStations = stationRepository.findByNameOrAliasName_Name(answer, answer)
        logger.info("Entered Stations: $enteredStations")
        if (enteredStations.isNullOrEmpty()) {
            return null
        }
        return enteredStations.find { checkStation(it, transferTo, chatContext) }
    }

    private fun checkStation(station: Station, transferTo: String?, chatContext: ChatContextDto): Boolean {
        transferTo?.let {
            if (!station.lines.map { it.lineId }.contains(transferTo)) {
                return false
            }
        }
        val lineIds = station.lines.map { it.lineId }
        return lineIds.contains(chatContext.currentLine) &&
                !chatContext.previousStationIds.contains(station.id)
    }


    private fun initialPlayer(): Player {
        val playerInfo = Player(gameType = GameType.TRAVEL, gameLife = 1, gameScore = 0)
        return playerInfo
    }
}