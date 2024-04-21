package site.kkrupp.subway.fillblank.domain

import jakarta.persistence.*
import org.joda.time.DateTime
import site.kkrupp.subway.station.domain.Station

@Entity
@Table(name = "fill_blank_problem")
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

    @Column
    var createdAt: DateTime = DateTime.now()
)
