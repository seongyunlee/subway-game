package site.kkrupp.subway.admin

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import site.kkrupp.subway.station.repository.StationRepository

@Controller
@RequestMapping("/admin")
class AdminController(
    val stationRepository: StationRepository
) {
    @RequestMapping("/")
    fun login(): String {
        return "index"
    }

    @GetMapping("/station-list")
    fun stationList(model: Model): String {
        model.addAttribute("stations", stationRepository.findAll())

        return "stationList"
    }
}