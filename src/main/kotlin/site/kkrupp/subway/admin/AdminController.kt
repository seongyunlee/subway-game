package site.kkrupp.subway.admin

import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import site.kkrupp.subway.station.domain.StationLine
import site.kkrupp.subway.station.repository.StationRepository

@Controller
@RequestMapping("/admin")
class AdminController(
    val stationRepository: StationRepository
) {
    val logger = LoggerFactory.getLogger(this.javaClass)!!

    @GetMapping()
    fun mainPage(): String {
        logger.info("Admin main page")
        return "home"
    }

    @GetMapping("/station-list")
    fun stationList(model: Model): String {
        model.addAttribute("stations", stationRepository.findAll())

        return "stationList"
    }

    @Transactional
    @PostMapping("/station-list/save")
    fun saveStation(@RequestBody dto: SaveStationRequestDto): String {
        val station =
            stationRepository.findById(dto.id.toString()).orElse(null) ?: return "redirect:/admin/station-list"
        station.name = dto.name
        val orgLines = station.lines.map { it.lineId }
        station.lines.removeIf { !dto.lines.contains(it.lineId) }
        dto.lines.forEach {
            if (!orgLines.contains(it)) {
                station.lines.add(StationLine(stationId = station.id, lineId = it))
            }
        }
        logger.info("station: ${station.lines.map { it.lineId }}")

        stationRepository.save(station)


        return "redirect:/admin/station-list"
    }
}