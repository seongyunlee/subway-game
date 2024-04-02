package site.kkrupp.subway.station.repository

import org.springframework.data.jpa.repository.JpaRepository
import site.kkrupp.subway.station.domain.Station

interface StationRepository : JpaRepository<Station, String> {

    fun findByLines_lineId(lineId: String): List<Station>

    fun findByNameOrAliasName_Name(name: String, aliasName: String): List<Station>?

    fun findByLines_lineIdNot(lineId: String): List<Station>

    fun findById(id: Long): Station?

}