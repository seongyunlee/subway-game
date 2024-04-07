package site.kkrupp.subway.rank

import lombok.extern.slf4j.Slf4j
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import site.kkrupp.subway.fillblank.service.RankService
import site.kkrupp.subway.rank.dto.EnrollRankRequestDto
import site.kkrupp.subway.rank.dto.EnrollRankResponseDto
import site.kkrupp.subway.rank.dto.GetRankResponseDto

@Slf4j
@Controller
@RestController
@RequestMapping("/rank")
class RankController(
    private val rankService: RankService
) {
    @PostMapping("/enroll")
    fun enrollRank(@RequestBody dto: EnrollRankRequestDto): ResponseEntity<EnrollRankResponseDto> {
        return ResponseEntity.ok(rankService.enrollRank(dto))
    }

    @GetMapping("/{gameType}")
    fun getRank(@PathVariable gameType: String, @RequestParam playerId: String?): ResponseEntity<GetRankResponseDto> {
        return ResponseEntity.ok(rankService.getRank(gameType, playerId))
    }
}