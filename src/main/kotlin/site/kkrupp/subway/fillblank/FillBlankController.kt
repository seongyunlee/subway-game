package site.kkrupp.subway.fillblank

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import site.kkrupp.subway.bestroute.dto.BestRouteAnswerDto
import site.kkrupp.subway.fillblank.service.FillBlankService
import site.kkrupp.subway.player.annotation.RequiredUser
import site.kkrupp.subway.player.domain.Player

@Controller
@RestController
@RequestMapping("/fillblank")
class FillBlankController(
    private val fillBlankService: FillBlankService
) {

    @GetMapping("/start")
    fun initialGame(): ResponseEntity<String> {
        return ResponseEntity.internalServerError().body("NOT IMPLEMENTED YET")
    }

    @PostMapping("/submit")
    fun submitAnswer(
        @RequestBody bestRouteAnswerDto: BestRouteAnswerDto,
        @RequiredUser player: Player,
    ): ResponseEntity<String> {

        return ResponseEntity.internalServerError().body("NOT IMPLEMENTED YET")
    }
}