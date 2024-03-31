package site.kkrupp.subway.station.domain

import jakarta.persistence.*

@Table(name = "alias_name")
@Entity
class AliasName(
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "ALIAS_NAME")
    val name: String,

    @Column(name = "STATION_ID")
    val stationId: Long
)