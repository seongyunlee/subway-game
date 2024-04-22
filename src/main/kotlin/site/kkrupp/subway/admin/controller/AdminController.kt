package site.kkrupp.subway.admin.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import site.kkrupp.subway.admin.SaveStationRequestDto
import site.kkrupp.subway.admin.dto.BestRouteProblemModel
import site.kkrupp.subway.admin.dto.ChoiceInfoDto
import site.kkrupp.subway.admin.dto.LoginReqDto
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

    @PostMapping("/fillblank/problemList/save")
    fun saveFillBlankProblem(
        model: Model,
        @RequestParam stationName: String,
        @RequestParam problemImage: MultipartFile
    ): String {

        val problem = adminService.findStationAndEnrollFillblankProblem(stationName, problemImage)
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

    @GetMapping("/addCnt/search")
    fun searchStationAddCnt(model: Model, @RequestParam searchName: String): String {
        val stations = adminService.searchStationByName(searchName)
        model.addAttribute("stations", stations)
        model.addAttribute("currentPage", 0)
        return "stationAddCnt"
    }

    @GetMapping("/addCnt")
    fun stationListAddCnt(model: Model, @RequestParam page: Int = 0): String {
        val stations = adminService.getStationList(page)

        model.addAttribute("stations", stations)
        model.addAttribute("currentPage", page)

        return "stationAddCnt"
    }

    @PostMapping("/addCnt")
    fun saveCnt(model: Model, @RequestParam stationId: Long, @RequestParam boardingCnt: Long): String {
        adminService.saveBoardingCnt(stationId, boardingCnt)
        return "redirect:/admin/addCnt"
    }

    @PostMapping("/saveCnt")
    fun saveCnt(@RequestParam stationName: String, @RequestParam boardingCnt: Long): ResponseEntity<String> {
        adminService.saveBoardingCntByName(stationName, boardingCnt)
        return ResponseEntity("success", HttpStatus.OK)
    }


    @GetMapping("/bestroute/problemList")
    fun bestRouteProblemList(model: Model, @RequestParam page: Int = 0): String {
        val problems = adminService.getBestRouteProblems(page)
        val problemCnt = adminService.getNumberOfBestRouteProblems()
        val problemModel: List<BestRouteProblemModel> = problems.map {
            BestRouteProblemModel(
                id = it.id,
                startStation = it.startStation.name,
                endStation = it.endStation.name,
                answer = it.answer.id,
                choices = listOf(
                    ChoiceInfoDto(it.choice1.name, it.choice1.id, it.choice1Time),
                    ChoiceInfoDto(it.choice2.name, it.choice2.id, it.choice2Time),
                    ChoiceInfoDto(it.choice3.name, it.choice3.id, it.choice3Time),
                    ChoiceInfoDto(it.choice4.name, it.choice4.id, it.choice4Time),
                ),
            )
        }
        model.addAttribute("problems", problemModel)
        model.addAttribute("currentPage", page)
        model.addAttribute("totalProblem", problemCnt)

        return "bestRouteProblem"
    }

    @PostMapping("/join")
    @ResponseBody
    fun join(@RequestBody dto: LoginReqDto): String {
        logger.error("join : $dto")
        adminService.join(dto)
        return "success"
    }

    @GetMapping("/login")
    fun loginForm(): String {
        return "login"
    }

    @GetMapping("/fillblank/makeProblem")
    fun makeProblem(): String {
        return "makeProblem"
    }


}