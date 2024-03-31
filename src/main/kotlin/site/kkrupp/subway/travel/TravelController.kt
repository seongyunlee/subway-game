package site.kkrupp.subway.travel

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import site.kkrupp.subway.fillblank.service.TravelService
import site.kkrupp.subway.player.annotation.RequiredUser
import site.kkrupp.subway.player.domain.Player
import site.kkrupp.subway.travel.dto.request.TravelReportAnswerRequestDto
import site.kkrupp.subway.travel.dto.request.TravelSubmitAnswerRequestDto
import site.kkrupp.subway.travel.dto.response.TravelReportAnswerResponseDto
import site.kkrupp.subway.travel.dto.response.TravelStartGameResponseDto
import site.kkrupp.subway.travel.dto.response.TravelSubmitAnswerResponseDto

@Controller
@RestController
@RequestMapping("/travel")
class TravelController(
    private val travelService: TravelService
) {


    @GetMapping("/start")
    fun startGame(
        @RequestParam startLineId: String
    ): ResponseEntity<TravelStartGameResponseDto> {
        return ResponseEntity.ok(travelService.startGame(startLineId))
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