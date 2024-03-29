package site.kkrupp.subway.travel

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.kkrupp.subway.fillblank.service.TravelService

@Controller
@RestController
@RequestMapping("/travel")
class TravelController(
    private val fillBlankService: TravelService
) {


    /*    @PostMapping("/report")
        fun getProblem(): ResponseEntity<FillBlankResponseDto> {
            return ResponseEntity.ok(FillBlankResponseDto(1, listOf()))
        }

        @PostMapping("/submit")
        fun submitAnswer(): ResponseEntity<FillBlankSubmitResponseDto> {
            return ResponseEntity.ok(FillBlankSubmitResponseDto(true, "problemId", "newToken"))
        }*/
}