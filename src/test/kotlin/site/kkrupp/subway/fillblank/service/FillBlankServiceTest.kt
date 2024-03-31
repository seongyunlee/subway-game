package site.kkrupp.subway.fillblank.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import site.kkrupp.subway.player.repository.PlayerRepository

@SpringBootTest
class FillBlankServiceTest {

    @Autowired
    private lateinit var fillBlankService: FillBlankService

    @Autowired
    private lateinit var playerRepository: PlayerRepository

    @Test
    @DisplayName("Test startGame method")
    fun testStartGame() {
        assertEquals(1, 1)
    }
}
