package site.kkrupp.subway.rank

import lombok.extern.slf4j.Slf4j
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import site.kkrupp.subway.fillblank.service.RankService
import site.kkrupp.subway.rank.dto.EnrollRankResponseDto
import site.kkrupp.subway.rank.dto.GetRankResponseDto
import site.kkrupp.subway.utill.GameType
import java.util.*

@Slf4j
@Controller
@RestController
@RequestMapping("/rank")
class RankController(
        private val rankService: RankService) {


    @PostMapping("/{gameType}/enroll")
    fun enrollRank(@PathVariable gameType: String): ResponseEntity<EnrollRankResponseDto> {
        return ResponseEntity.ok(rankService.enrollRank(GameType.valueOf(gameType.uppercase(Locale.getDefault()))))
    }

    @GetMapping("/{gameType}")
    fun getRank(@PathVariable gameType: String): ResponseEntity<GetRankResponseDto> {
        return ResponseEntity.ok(rankService.getRank(GameType.valueOf(gameType.uppercase(Locale.getDefault()))))
    }
}