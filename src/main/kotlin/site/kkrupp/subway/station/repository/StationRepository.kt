package site.kkrupp.subway.station.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import site.kkrupp.subway.station.domain.Station

interface StationRepository : JpaRepository<Station, String> {

    @Query("SELECT s FROM Station s JOIN s.lines l WHERE l.lineId = :lineId")
    fun findByLineId(lineId: String): List<Station>

    fun findByNameOrAliasName_Name(name: String, aliasName: String): Station?

    @Query("SELECT s FROM Station s JOIN s.lines l WHERE l.lineId != :lineId")
    fun findByLindIdNot(lineId: String): List<Station>


}