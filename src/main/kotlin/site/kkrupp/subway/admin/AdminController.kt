package site.kkrupp.subway.admin

import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import site.kkrupp.subway.fillblank.domain.FillBlankProblemAnswer
import site.kkrupp.subway.fillblank.repository.FillBlankRepository
import site.kkrupp.subway.station.domain.AliasName
import site.kkrupp.subway.station.domain.StationLine
import site.kkrupp.subway.station.repository.StationRepository

@Controller
@RequestMapping("/admin")
class AdminController(
    val stationRepository: StationRepository,
    val fillBlankRepository: FillBlankRepository,
) {
    val logger = LoggerFactory.getLogger(this.javaClass)!!

    @GetMapping()
    fun mainPage(): String {
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
        station.aliasName.clear()
        station.aliasName.addAll(dto.aliasNames.map { AliasName(name = it, stationId = station.id) })


        stationRepository.save(station)


        return "redirect:station-list"
    }

    @GetMapping("/fillblank/problemList")
    fun fillBlankProblemList(model: Model, @RequestParam page: Int = 0): String {

        val problems = fillBlankRepository.findAll(PageRequest.of(page, 100, Sort.by("id"))).content
        model.addAttribute("problems", problems)
        model.addAttribute("currentPage", page)

        return "fillBlankProblem"
    }

    @PostMapping("fillblank/editProblem")
    fun editProblem(
        @RequestParam id: Long?,
        @RequestParam answer: Long,
        @RequestParam problemImage: MultipartFile
    ): String {
        logger.info("id: $id, answer: $answer, imgFile: $problemImage")

        return "redirect:/admin/fillblank/problemList"
    }

    @GetMapping("/fillblank/problemList/search")
    fun fillBlankProblem(model: Model, @RequestParam searchName: String): String {
        logger.info("stationName: $searchName")
        val station = stationRepository.findByNameOrAliasName_Name(searchName, searchName)
            ?: return "redirect:/admin/fillblank/problemList"
        logger.info("station found: $station")
        val problems = station.map {
            val orgProblem = fillBlankRepository.findByAnswer_id(it.id)
            //TODO : 한 개역에 문제가 여러 개 있을 때 로직 처리.
            FillBlankProblemAnswer(
                id = orgProblem.firstOrNull()?.id ?: -1,
                problemImage = orgProblem.firstOrNull()?.problemImage ?: "",
                answer = it,
            )
        }
        model.addAttribute("problems", problems)
        model.addAttribute("currentPage", 0)
        return "fillBlankProblem"
    }
}