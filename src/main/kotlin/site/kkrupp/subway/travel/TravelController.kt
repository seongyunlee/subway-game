package site.kkrupp.subway.travel

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import site.kkrupp.subway.common.monitoring.activity.UserActivityMonitor
import site.kkrupp.subway.player.annotation.RequiredUser
import site.kkrupp.subway.player.domain.Player
import site.kkrupp.subway.travel.dto.request.TravelReportAnswerRequestDto
import site.kkrupp.subway.travel.dto.request.TravelSubmitAnswerRequestDto
import site.kkrupp.subway.travel.dto.response.TravelReportAnswerResponseDto
import site.kkrupp.subway.travel.dto.response.TravelStartGameResponseDto
import site.kkrupp.subway.travel.dto.response.TravelSubmitAnswerResponseDto
import site.kkrupp.subway.travel.service.TravelService

@Controller
@RestController
@RequestMapping("/travel")
class TravelController(
    private val travelService: TravelService
) {


    @UserActivityMonitor("Start travel game")
    @GetMapping("/start")
    fun startGame(
        @RequestParam startLine: String
    ): ResponseEntity<TravelStartGameResponseDto> {
        return ResponseEntity.ok(travelService.startGame(startLine))
    }

    @PostMapping("/submit")
    fun submitAnswer(
        @RequiredUser player: Player,
        @RequestBody dto: TravelSubmitAnswerRequestDto
    ): ResponseEntity<TravelSubmitAnswerResponseDto> {
        return ResponseEntity.ok(travelService.submitAnswer(dto, player))
    }

    @PostMapping("/report")
    fun report(
        @RequiredUser player: Player,
        @RequestBody reportAnswerRequestDto: TravelReportAnswerRequestDto
    ): ResponseEntity<TravelReportAnswerResponseDto> {
        return ResponseEntity.ok(travelService.reportAnswer(reportAnswerRequestDto, player))
    }


}