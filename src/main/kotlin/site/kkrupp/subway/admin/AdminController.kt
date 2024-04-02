package site.kkrupp.subway.admin

import jakarta.transaction.Transactional
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

    @Transactional
    @PostMapping("/station-list/save")
    fun saveStation(@RequestBody dto: SaveStationRequestDto): String {
        //100 items per page
        adminService.saveStation(dto)

        return "redirect:station-list"
    }

    @GetMapping("/fillblank/problemList")
    fun fillBlankProblemList(model: Model, @RequestParam page: Int = 0): String {

        val problems = adminService.getAllFillBlankProblems(page)
        model.addAttribute("problems", problems)
        model.addAttribute("currentPage", page)

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
}