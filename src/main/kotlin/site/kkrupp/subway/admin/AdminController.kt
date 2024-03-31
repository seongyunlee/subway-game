package site.kkrupp.subway.admin

import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import site.kkrupp.subway.station.domain.AliasName
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
    fun stationList(model: Model, @RequestParam page: Int = 0): String {
        val stations = stationRepository.findAll(PageRequest.of(page, 100, Sort.by("id"))).content

        model.addAttribute("stations", stations)
        model.addAttribute("currentPage", page)

        return "stationList"
    }

    @Transactional
    @PostMapping("/station-list/save")
    fun saveStation(@RequestBody dto: SaveStationRequestDto): String {
        //100 items per page
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
        val orgAliasNames = station.aliasName.map { it.name }
        station.aliasName.removeIf { !dto.aliasNames.contains(it.name) }
        dto.aliasNames.forEach {
            if (!orgAliasNames.contains(it)) {
                station.aliasName.add(AliasName(name = it, stationId = station.id))
            }
        }

        logger.info("station: ${station.lines.map { it.lineId }}")

        stationRepository.save(station)


        return "redirect:/admin/station-list"
    }
}