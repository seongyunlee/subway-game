package site.kkrupp.subway.player.service

import org.springframework.stereotype.Service
import site.kkrupp.subway.player.domain.Player
import site.kkrupp.subway.player.repository.PlayerRepository

@Service
class PlayerService(
    private val playerRepository: PlayerRepository
) {
    fun savePlayer(player: Player) {
        playerRepository.save(player)
    }
}