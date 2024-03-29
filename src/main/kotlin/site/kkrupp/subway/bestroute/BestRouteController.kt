package site.kkrupp.subway.bestroute

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import site.kkrupp.subway.bestroute.dto.BestRouteAnswerDto
import site.kkrupp.subway.bestroute.dto.BestRouteResponseDto
import site.kkrupp.subway.bestroute.dto.BestRouteSubmitResponseDto
import site.kkrupp.subway.fillblank.service.BestRouteService
import site.kkrupp.subway.player.annotation.RequiredUser
import site.kkrupp.subway.player.domain.Player

@Controller
@RestController
@RequestMapping("/bestroute")
class BestRouteController(
    private val bestRouteService: BestRouteService
) {


    @GetMapping("/start")
    fun startGame(): ResponseEntity<BestRouteResponseDto> {
        return ResponseEntity.ok(bestRouteService.startGame())
    }

    @PostMapping("/submit")
    fun submitAnswer(
        @RequestBody bestRouteAnswerDto: BestRouteAnswerDto,
        @RequiredUser player: Player,
    ): ResponseEntity<BestRouteSubmitResponseDto> {
        return ResponseEntity.ok(bestRouteService.submitAnswer(bestRouteAnswerDto, player))
    }
}