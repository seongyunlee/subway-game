package site.kkrupp.subway.fillblank.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import site.kkrupp.subway.player.repository.PlayerRepository

@SpringBootTest
class FillBlankServiceTest {

    @Autowired
    private lateinit var fillBlankService: FillBlankService

    @Autowired
    private lateinit var playerRepository: PlayerRepository

    @Test
    @DisplayName("0점일 때_문제_하나_가져오기")
    fun testGetProblem() {
        val problem = fillBlankService.getProblem(0);
        println(problem)
    }

    @Test
    @DisplayName("문제 100개 가져와서 문제 번호 그룹핑")
    fun testGetProblems() {
        val problems = (1..100).map { fillBlankService.getProblem(30) }
        val ids = problems.map {
            it.id
        }.sorted()

    }
}
