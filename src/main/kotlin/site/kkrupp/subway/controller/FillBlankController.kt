package site.kkrupp.subway.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/fillblank")
class FillBlankController {
    @GetMapping("/guide")
    fun fillBlank(): String {
        return "fillblank/guide"
    }

    @GetMapping("/game")
    fun game(): String {
        return "fillblank/game"
    }
}