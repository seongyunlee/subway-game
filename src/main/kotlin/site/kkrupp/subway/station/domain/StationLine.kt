package site.kkrupp.subway.station.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "station_line")
class StationLine(

    @Id
    @Column(name = "ID")
    val id: String,


    @Column(name = "STATION_ID")
    val stationId: String,

    @Column(name = "LINE_ID")
    val lineId: String
)