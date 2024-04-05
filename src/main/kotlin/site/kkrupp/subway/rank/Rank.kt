package site.kkrupp.subway.rank

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Duration
import java.time.LocalDate

@Entity
@Table(name = "RANK")
data class Rank(
    @Id
    val playerId: String,

    @Column
    val gameType: String,

    @Column
    val duration: Duration,

    @Column
    val score: Int,

    @Column(length = 8)
    val nickName: String,

    @Column
    val createdAt: LocalDate,
)