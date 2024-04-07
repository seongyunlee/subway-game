package site.kkrupp.subway.player.domain

import jakarta.persistence.*
import site.kkrupp.subway.utill.GameType
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Entity
@Table(name = "player")
data class Player(

    @Id
    @Column(name = "PLAYER_ID")
    val playerId: String = UUID.randomUUID().toString(),

    @Column(name = "GAME_TYPE")
    @Enumerated(EnumType.STRING)
    val gameType: GameType,

    @Column(name = "GAME_SCORE")
    var gameScore: Int = 0,

    @Column(name = "GAME_LIFE")
    var gameLife: Int,

    @Column(name = "CURRENT_CONTEXT")
    var currentContext: String = "",

    @Column(name = "START_TIME")
    val startTime: LocalDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime(),

    @Column(name = "END_TIME")
    var endTime: LocalDateTime? = null,
)