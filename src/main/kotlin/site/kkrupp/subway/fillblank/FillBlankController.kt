package site.kkrupp.subway.fillblank

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.kkrupp.subway.fillblank.dto.BestProblemResponseDto
import site.kkrupp.subway.fillblank.dto.BestSubmitAnswerResponseDto
import site.kkrupp.subway.fillblank.service.FillBlankService

@Controller
@RestController
@RequestMapping("/fillblank")
class FillBlankController(
        private val fillBlankService: FillBlankService) {


    @GetMapping("/problem")
    fun getProblem(): ResponseEntity<BestProblemResponseDto> {
        return ResponseEntity.ok(fillBlankService.getProblem())
    }

    @PostMapping("/submit")
    fun submitAnswer(): ResponseEntity<BestSubmitAnswerResponseDto> {
        return ResponseEntity.ok(fillBlankService.submitAnswer())
    }
}