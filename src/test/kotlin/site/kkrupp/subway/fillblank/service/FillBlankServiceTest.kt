package site.kkrupp.subway.fillblank.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import site.kkrupp.subway.bestroute.dto.BestRouteProblem
import site.kkrupp.subway.bestroute.dto.BestRouteResponseDto
import site.kkrupp.subway.player.domain.Player
import site.kkrupp.subway.player.repository.PlayerRepository
import site.kkrupp.subway.utill.GameType

@SpringBootTest
class FillBlankServiceTest {

    @Autowired
    private lateinit var fillBlankService: FillBlankService

    @Autowired
    private lateinit var playerRepository: PlayerRepository

    @Test
    @DisplayName("Test startGame method")
    fun testStartGame() {
        val expectedPlayerId = "1234-5678-9012-3456"
        val expectedProblem = BestRouteProblem("1", "problem1.jpg", listOf())


        val player = Player(expectedPlayerId, GameType.FILLBLANK, 0, 3)

        val result: BestRouteResponseDto = fillBlankService.startGame()

        assertEquals(expectedPlayerId, result.playerId)
        assertEquals(expectedProblem, result.problem)
    }
}
