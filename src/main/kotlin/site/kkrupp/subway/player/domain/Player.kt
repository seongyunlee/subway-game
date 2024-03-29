package site.kkrupp.subway.player.domain

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import site.kkrupp.subway.utill.GameType

@Entity
@Table(name = "player")
class Player(

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "PLAYER_ID")
    val playerId: String? = null,

    @Column(name = "GAME_TYPE")
    @Enumerated(EnumType.STRING)
    val gameType: GameType,

    @Column(name = "GAME_SCORE")
    var gameScore: Int = 0,

    @Column(name = "GAME_LIFE")
    var gameLife: Int,
)