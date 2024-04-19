package site.kkrupp.subway.bestroute.domain

import jakarta.persistence.*
import site.kkrupp.subway.station.domain.Station

@Entity
@Table(name = "best_route_problem")
data class BestRouteProblemAnswer(

    @Id
    @Column(name = "PROBLEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @OneToOne
    @JoinColumn(name = "START_STATION")
    val startStation: Station,

    @ManyToOne
    @JoinColumn(name = "END_STATION")
    val endStation: Station,

    @ManyToOne
    @JoinColumn(name = "CHOICE1")
    val choice1: Station,


    @ManyToOne
    @JoinColumn(name = "CHOICE2")
    val choice2: Station,

    @ManyToOne
    @JoinColumn(name = "CHOICE3")
    val choice3: Station,

    @ManyToOne
    @JoinColumn(name = "CHOICE4")
    val choice4: Station,

    @ManyToOne
    @JoinColumn(name = "ANSWER")
    val answer: Station,

    @Column(name = "CHOICE1_TIME")
    val choice1Time: Int,

    @Column(name = "CHOICE2_TIME")
    val choice2Time: Int,

    @Column(name = "CHOICE3_TIME")
    val choice3Time: Int,

    @Column(name = "CHOICE4_TIME")
    val choice4Time: Int,
)

