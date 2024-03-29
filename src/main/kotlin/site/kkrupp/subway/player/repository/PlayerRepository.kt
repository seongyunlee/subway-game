package site.kkrupp.subway.player.repository

import org.springframework.data.jpa.repository.JpaRepository
import site.kkrupp.subway.player.domain.Player

interface PlayerRepository : JpaRepository<Player, String>