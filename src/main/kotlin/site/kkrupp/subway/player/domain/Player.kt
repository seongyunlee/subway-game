package site.kkrupp.subway.player.domain

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import site.kkrupp.subway.utill.GameType
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "player")
data class Player(

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "PLAYER_ID")
    val playerId: String = "",

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