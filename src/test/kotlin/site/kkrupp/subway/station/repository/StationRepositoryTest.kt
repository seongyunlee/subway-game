package site.kkrupp.subway.station.repository

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class StationRepositoryTest {

    @Autowired
    private lateinit var stationRepository: StationRepository

    @Test
    @DisplayName("지하철 이름으로 역을 조회한다.")
    fun findByNameOrAliasName_Name() {
        val station = stationRepository.findByNameOrAliasName_Name("요용산", "요용산")
        Assertions.assertEquals("용산역", station?.name)
    }

    @Test
    @DisplayName("모든 역을 가져온다.")
    @Transactional
    fun findAll() {
        val stations = stationRepository.findAll()
        stations.forEach { station ->
            station.apply {
                println("id: $id, name: $name, aliasName: $aliasName , lines: $lines")
            }
        }
        Assertions.assertTrue(stations.isNotEmpty())
    }

    @Test
    @Transactional
    @DisplayName("지하철 노선 ID로 역을 조회한다.")
    fun findByLineId() {
        val stations = stationRepository.findByLineId("LINE_1")
        stations.forEach { station ->
            station.apply {
                println("id: $id, name: $name, aliasName: $aliasName , lines: $lines")
            }
        }
        Assertions.assertTrue(stations.isNotEmpty())
    }

}
