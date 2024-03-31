package site.kkrupp.subway.station.domain

import jakarta.persistence.*

@Entity
@Table(name = "station")
class Station(
    @Id
    @Column(name = "ID")
    val id: String,

    @Column(name = "NAME")
    val name: String,

    @OneToMany(mappedBy = "stationId")
    @Enumerated(EnumType.STRING)
    val lines: List<StationLine>,

    @OneToMany(mappedBy = "stationId")
    val aliasName: List<AliasName>? = emptyList()
)

