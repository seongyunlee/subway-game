package site.kkrupp.subway.station.domain

import jakarta.persistence.*

@Entity
@Table(name = "station_line")
class StationLine(

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,


    @Column(name = "STATION_ID")
    val stationId: Long,

    @Column(name = "LINE_ID")
    val lineId: String
)