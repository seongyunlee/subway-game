package site.kkrupp.subway.station.repository

import org.springframework.data.jpa.repository.JpaRepository
import site.kkrupp.subway.station.domain.Station

interface StationRepository : JpaRepository<Station, String>