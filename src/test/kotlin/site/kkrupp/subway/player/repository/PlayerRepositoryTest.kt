package site.kkrupp.subway.player.repository

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import site.kkrupp.subway.player.domain.Player
import site.kkrupp.subway.utill.GameType

@SpringBootTest
class PlayerRepositoryTest {
    @Autowired
    private lateinit var playerRepository: PlayerRepository

    @Test
    @DisplayName("TRAVEL 플레이어를 저장한다.")
    fun saveTravelPlayer() {
        // given
        val player = Player(
            gameType = GameType.TRAVEL,
            gameLife = 3,
            gameScore = 0,
        )

        // when
        val savedPlayer = playerRepository.save(player)

        // then
        Assertions.assertTrue(savedPlayer.playerId != null)
    }

}