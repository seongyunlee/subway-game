package site.kkrupp.subway.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class CommonController {

    @GetMapping("/result")
    fun result(): String {
        return "result"
    }
}