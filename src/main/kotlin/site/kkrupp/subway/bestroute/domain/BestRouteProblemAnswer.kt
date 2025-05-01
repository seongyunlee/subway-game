package site.kkrupp.subway.bestroute.domain

import jakarta.persistence.*
import site.kkrupp.subway.bestroute.dto.response.*
import site.kkrupp.subway.player.domain.Player
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

    @Column(name = "CORRECT_CNT")
    var correctCnt: Long = 0,

    @Column(name = "WRONG_CNT")
    var wrongCnt: Long = 0,

    @Column(name = "DIFFICULTY_INDEX")
    var difficultyIndex: Long
) {
    fun toStartDto(player: Player): BestRouteStartGameResponseDto {
        return BestRouteStartGameResponseDto(
            playerId = player.playerId,
            problem = BestRouteProblemDto(
                id = id,
                startStation = startStation.name,
                endStation = endStation.name,
                choices = listOf(
                    StationChoiceDto(choice1.id, choice1.name),
                    StationChoiceDto(choice2.id, choice2.name),
                    StationChoiceDto(choice3.id, choice3.name),
                    StationChoiceDto(choice4.id, choice4.name)
                )
            ),
            gameLife = player.gameLife,
            gameScore = player.gameScore
        )
    }

    fun toSubmitAnswerDto(
        isCorrect: Boolean,
        prevProblem: BestRouteProblemAnswer
    ): BestRouteSubmitAnswerResponseDto {
        return prevProblem.let {
            BestRouteSubmitAnswerResponseDto(
                isCorrect = isCorrect,
                answer = answer.id,
                correctMinute = listOf(
                    CorrectMinute(it.choice1.id, choice1Time),
                    CorrectMinute(it.choice2.id, choice2Time),
                    CorrectMinute(it.choice3.id, choice3Time),
                    CorrectMinute(it.choice4.id, choice4Time)
                ),
                newProblem = toProblemDto(),
                gameLife = 0,
                gameScore = 0
            )
        }
    }

    fun toProblemDto(): BestRouteProblemDto {
        return BestRouteProblemDto(
            id = id,
            startStation = startStation.name,
            endStation = endStation.name,
            choices = listOf(
                StationChoiceDto(choice1.id, choice1.name),
                StationChoiceDto(choice2.id, choice2.name),
                StationChoiceDto(choice3.id, choice3.name),
                StationChoiceDto(choice4.id, choice4.name)
            )
        )
    }
}

