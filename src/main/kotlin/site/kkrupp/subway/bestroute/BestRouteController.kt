package site.kkrupp.subway.bestroute

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import site.kkrupp.subway.bestroute.dto.request.BestRouteSubmitAnswerRequestDto
import site.kkrupp.subway.bestroute.dto.response.BestRouteStartGameResponseDto
import site.kkrupp.subway.bestroute.dto.response.BestRouteSubmitAnswerResponseDto
import site.kkrupp.subway.common.monitoring.activity.UserActivityMonitor
import site.kkrupp.subway.fillblank.service.BestRouteService
import site.kkrupp.subway.player.annotation.RequiredUser
import site.kkrupp.subway.player.domain.Player

@Controller
@RestController
@RequestMapping("/bestroute")
class BestRouteController(
    private val bestRouteService: BestRouteService
) {


    @UserActivityMonitor("Start best route game")
    @GetMapping("/start")
    fun startGame(): ResponseEntity<BestRouteStartGameResponseDto> {
        return ResponseEntity.ok(bestRouteService.startGame())
    }

    @PostMapping("/submit")
    fun submitAnswer(
        @RequestBody bestRouteSubmitAnswerRequestDto: BestRouteSubmitAnswerRequestDto,
        @RequiredUser player: Player,
    ): ResponseEntity<BestRouteSubmitAnswerResponseDto> {
        return ResponseEntity.ok(bestRouteService.submitAnswer(bestRouteSubmitAnswerRequestDto, player))
    }
}