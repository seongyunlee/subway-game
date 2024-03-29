package site.kkrupp.subway.station.domain

import jakarta.persistence.*
import site.kkrupp.subway.utill.LineID

@Entity
@Table(name = "station_line")
class StationLine(
    
    @Id
    val stationId: String,

    @Column(name = "LINE_ID")
    @Enumerated(EnumType.STRING)
    val lineId: LineID
)