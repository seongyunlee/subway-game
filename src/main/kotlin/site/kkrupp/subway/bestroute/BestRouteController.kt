package site.kkrupp.subway.bestroute

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.kkrupp.subway.fillblank.dto.BestProblemResponseDto
import site.kkrupp.subway.fillblank.dto.BestSubmitAnswerResponseDto
import site.kkrupp.subway.fillblank.service.BestRouteService

@Controller
@RestController
@RequestMapping("/bestroute")
class BestRouteController(
        private val bestRouteService: BestRouteService
) {

    @GetMapping("/problem")
    fun getProblem(): ResponseEntity<BestProblemResponseDto> {
        return ResponseEntity.ok(bestRouteService.getProblem())
    }

    @PostMapping("/submit")
    fun submitAnswer(): ResponseEntity<BestSubmitAnswerResponseDto> {
        return ResponseEntity.ok(bestRouteService.submitAnswer())
    }
}