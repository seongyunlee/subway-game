package site.kkrupp.subway.fillblank.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import site.kkrupp.subway.fillblank.repository.FillBlankRepository
import site.kkrupp.subway.player.repository.PlayerRepository


@Service
class FillBlankService(
    private val playerRepository: PlayerRepository,
    private val fillBlankRepository: FillBlankRepository,
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)!!

}
