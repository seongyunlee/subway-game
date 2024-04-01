package site.kkrupp.subway.admin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AdminControllerTest {

    @Autowired
    lateinit var adminController: AdminController


    @Test
    @DisplayName("역 정보를 수정한다.")
    fun testSaveStation() {
        val dto = SaveStationRequestDto(
            id = 6,
            name = "김포공항",
            lines = listOf(
                "LINE_AIR",
                "LINE_INCHON",
                "LINE_GIMPO",
                "LINE_5",
                "LINE_9",
                "LINE_SEOHEA",
            ),
            aliasNames = listOf("김포공항역")
        )

        val result = adminController.saveStation(dto)

        assertEquals("redirect:/admin/station-list", result)
    }
}
