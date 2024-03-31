package site.kkrupp.subway.travel.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import site.kkrupp.subway.fillblank.service.TravelService
import site.kkrupp.subway.player.domain.Player
import site.kkrupp.subway.player.repository.PlayerRepository
import site.kkrupp.subway.station.domain.AliasName
import site.kkrupp.subway.station.domain.Station
import site.kkrupp.subway.station.domain.StationLine
import site.kkrupp.subway.station.repository.StationRepository
import site.kkrupp.subway.travel.dto.ChatContextDto
import site.kkrupp.subway.travel.dto.request.TravelSubmitAnswerRequestDto
import site.kkrupp.subway.utill.GameType


@ExtendWith(MockitoExtension::class)
class TravelServiceTest {

    @Mock
    private lateinit var playerRepository: PlayerRepository

    @Mock
    private lateinit var stationRepository: StationRepository

    @InjectMocks
    private lateinit var travelService: TravelService

    @Test
    @DisplayName("submitAnswer 정답 + 환승X 제출 테스트")
    fun submitCorrectAnswerTest() {
        // given
        val dto = TravelSubmitAnswerRequestDto(
            answer = "강남역",
            transferTo = null,
            chatContext = ChatContextDto(
                currentLine = "LINE_1",
                previousStationIds = listOf("S001")
            )
        )
        val storedPlayer = Player(
            playerId = "test-player-id",
            gameType = GameType.TRAVEL,
            gameLife = 1,
            gameScore = 0,
            currentContext = dto.chatContext.hashCode().toString()
        )

        // when


        Mockito.`when`(playerRepository.save(storedPlayer)).thenReturn(storedPlayer)
        Mockito.`when`(stationRepository.findByNameOrAliasName_Name("강남역", "강남역")).thenReturn(
            Station(
                id = "S002",
                name = "강남역",
                aliasName = mutableListOf(
                    AliasName(id = 1, name = "강남역", stationId = "S002")
                ),
                lines = mutableListOf(
                    StationLine(id = 1, stationId = "S002", lineId = "LINE_1")
                )
            )
        )
        Mockito.lenient().`when`(stationRepository.findByLineId("LINE_1")).thenReturn(
            listOf(
                Station(
                    id = "S001",
                    name = "서울역",
                    aliasName = mutableListOf(
                        AliasName(id = 1, name = "서울역", stationId = "S001")
                    ),
                    lines = mutableListOf(
                        StationLine(id = 1, stationId = "S001", lineId = "LINE_1")
                    )
                ),
                Station(
                    id = "S002",
                    name = "강남역",
                    aliasName = mutableListOf(
                        AliasName(id = 1, name = "강남역", stationId = "S002")
                    ),
                    lines = mutableListOf(
                        StationLine(id = 1, stationId = "S002", lineId = "LINE_1")
                    )
                )
            )
        )
        Mockito.lenient().`when`(stationRepository.findByLindIdNot("LINE_1")).thenReturn(
            listOf(
                Station(
                    id = "A1234",
                    name = "홍대입구역",
                    aliasName = mutableListOf(
                        AliasName(id = 1, name = "홍대입구역", stationId = "A1234")
                    ),
                    lines = mutableListOf(
                        StationLine(id = 1, stationId = "A1234", lineId = "LINE_2")
                    )
                ),
                Station(
                    id = "B1234",
                    name = "신촌역",
                    aliasName = mutableListOf(
                        AliasName(id = 1, name = "신촌역", stationId = "B1234")
                    ),
                    lines = mutableListOf(
                        StationLine(id = 1, stationId = "B1234", lineId = "LINE_2")
                    )
                )
            )
        )

        // then
        val response = travelService.submitAnswer(dto, storedPlayer)
        Assertions.assertTrue(response.isCorrect)
        Assertions.assertEquals(1, response.gameLife)
        Assertions.assertEquals(1, response.gameScore)
    }

}