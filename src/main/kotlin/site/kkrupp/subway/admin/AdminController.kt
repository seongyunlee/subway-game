package site.kkrupp.subway.admin

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import site.kkrupp.subway.admin.service.AdminService

@Controller
@RequestMapping("/admin")
class AdminController(
    val adminService: AdminService
) {
    val logger = LoggerFactory.getLogger(this.javaClass)!!

    @GetMapping()
    fun mainPage(): String {
        return "home"
    }

    @GetMapping("/station-list")
    fun stationList(model: Model, @RequestParam page: Int = 0): String {
        val stations = adminService.getStationList(page)

        model.addAttribute("stations", stations)
        model.addAttribute("currentPage", page)

        return "stationList"
    }

    @PostMapping("/station-list/save")
    fun saveStation(model: Model, @RequestBody dto: SaveStationRequestDto): String {
        //100 items per page
        val station = adminService.saveStation(dto)
        model.addAttribute("stations", listOf(station))
        model.addAttribute("currentPage", 0)


        return "stationList"
    }

    @GetMapping("/fillblank/problemList")
    fun fillBlankProblemList(model: Model, @RequestParam page: Int = 0): String {

        val problems = adminService.getAllFillBlankProblems(page)
        model.addAttribute("problems", problems)
        model.addAttribute("currentPage", page)
        val problemNumbers = adminService.getNumberOfFillBlankProblems()
        model.addAttribute("totalProblem", problemNumbers)
        val numberOfStations = adminService.getNumberOfStations()
        model.addAttribute("totalStation", numberOfStations)

        return "fillBlankProblem"
    }

    @PostMapping("fillblank/editProblem")
    fun editProblem(
        model: Model,
        @RequestParam id: Int?,
        @RequestParam answer: Long,
        @RequestParam problemImage: MultipartFile
    ): String {

        val problem = adminService.editFillBlankProblem(id, answer, problemImage)
        model.addAttribute("problems", listOf(problem))
        model.addAttribute("currentPage", 0)

        return "fillBlankProblem"
    }

    @GetMapping("/fillblank/problemList/search")
    fun fillBlankProblem(model: Model, @RequestParam searchName: String): String {
        val problems = adminService.searchFillBlankProblemsByStationName(searchName)
        model.addAttribute("problems", problems)
        model.addAttribute("currentPage", 0)
        return "fillBlankProblem"
    }

    @GetMapping("/station-list/search")
    fun searchStation(model: Model, @RequestParam searchName: String): String {
        val stations = adminService.searchStationByName(searchName)
        model.addAttribute("stations", stations)
        model.addAttribute("currentPage", 0)
        return "stationList"
    }
}