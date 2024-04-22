package site.kkrupp.subway.fillblank.domain

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import site.kkrupp.subway.station.domain.Station
import java.time.LocalDateTime

@Entity
@Table(name = "fill_blank_problem")
@EntityListeners(AuditingEntityListener::class)
data class FillBlankProblemAnswer(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROBLEM_ID")
    val id: Int,

    @Column(name = "PROBLEM_IMAGE")
    var problemImage: String,

    @OneToOne
    @JoinColumn(name = "ANSWER")
    var answer: Station,

    @Column(name = "CORRECT_CNT")
    var correctCnt: Long = 0,

    @Column(name = "WRONG_CNT")
    var wrongCnt: Long = 0,

    @CreatedDate
    var createdDate: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    var lastModifiedDate: LocalDateTime = LocalDateTime.now(),
)
