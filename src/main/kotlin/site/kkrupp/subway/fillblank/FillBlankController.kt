package site.kkrupp.subway.fillblank

import jakarta.servlet.http.HttpSession
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import site.kkrupp.subway.fillblank.dto.FillBlankAnswerDto
import site.kkrupp.subway.fillblank.dto.FillBlankResponseDto
import site.kkrupp.subway.fillblank.dto.FillBlankSubmitResponseDto
import site.kkrupp.subway.fillblank.service.FillBlankService

@Controller
@RestController
@RequestMapping("/fillblank")
class FillBlankController(
    private val fillBlankService: FillBlankService
) {


    @GetMapping("/problem")
    fun getProblem(@RequestParam level: Int): ResponseEntity<FillBlankResponseDto> {
        return ResponseEntity.ok(fillBlankService.getProblems(level))
    }

    @PostMapping("/submit")
    fun submitAnswer(
        @RequestBody fillBlankAnswerDto: FillBlankAnswerDto, session: HttpSession
    ): ResponseEntity<FillBlankSubmitResponseDto> {

        session.setAttribute("user", "newToken")

        return ResponseEntity.ok(fillBlankService.submitAnswer())
    }
}