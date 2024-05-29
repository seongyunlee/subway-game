package site.kkrupp.subway.fillblank

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import site.kkrupp.subway.common.monitoring.activity.UserActivityMonitor
import site.kkrupp.subway.fillblank.dto.request.FillBlankSubmitAnswerRequestDto
import site.kkrupp.subway.fillblank.dto.response.FillBlankStartGameResponseDto
import site.kkrupp.subway.fillblank.dto.response.FillBlankSubmitAnswerResponseDto
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
    @UserActivityMonitor("Start fill blank game")
    fun startGame(): ResponseEntity<FillBlankStartGameResponseDto> {
        return ResponseEntity.ok(fillBlankService.startGame())
    }

    @PostMapping("/submit")
    fun submitAnswer(
        @RequestBody fillBlankSubmitAnswerRequestDto: FillBlankSubmitAnswerRequestDto,
        @RequiredUser player: Player,
    ): ResponseEntity<FillBlankSubmitAnswerResponseDto> {

        return ResponseEntity.ok(fillBlankService.submitAnswer(fillBlankSubmitAnswerRequestDto, player))
    }
}