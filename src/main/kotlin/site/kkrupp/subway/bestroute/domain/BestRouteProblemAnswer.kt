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

    // 한 역이 여러 문제의 출발역이 될 수 있다.
    // @OneToOne 이면 Hibernate 가 START_STATION 에 UNIQUE 를 걸어 역당 1문제로 제한된다.
    @ManyToOne
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

    @Column(name = "CORRECT_CNT")
    var correctCnt: Long = 0,

    @Column(name = "WRONG_CNT")
    var wrongCnt: Long = 0,

    @Column(name = "DIFFICULTY_INDEX")
    var difficultyIndex: Long
)

