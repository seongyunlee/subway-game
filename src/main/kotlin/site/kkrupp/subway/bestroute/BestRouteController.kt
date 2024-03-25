package site.kkrupp.subway.bestroute

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.kkrupp.subway.fillblank.dto.FillBlankResponseDto
import site.kkrupp.subway.fillblank.dto.FillBlankSubmitResponseDto
import site.kkrupp.subway.fillblank.service.BestRouteService

@Controller
@RestController
@RequestMapping("/bestroute")
class BestRouteController(
    private val bestRouteService: BestRouteService
) {

    @GetMapping("/problem")
    fun getProblem(): ResponseEntity<FillBlankResponseDto> {
        return ResponseEntity.ok(FillBlankResponseDto(1, listOf()))
    }

    @PostMapping("/submit")
    fun submitAnswer(): ResponseEntity<FillBlankSubmitResponseDto> {
        return ResponseEntity.ok(FillBlankSubmitResponseDto(true, "problemId", "newToken"))
    }
}