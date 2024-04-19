package site.kkrupp.subway.station.domain

import jakarta.persistence.*

@Entity
@Table(name = "station")
class Station(
    @Id
    @Column(name = "ID")
    val id: Long,

    @Column(name = "NAME")
    var name: String,

    @Column(name = "BOARDING_CNT")
    var boardingCnt: Int?,

    @OneToMany(mappedBy = "stationId", cascade = [CascadeType.ALL], orphanRemoval = true)
    @Enumerated(EnumType.STRING)
    var lines: MutableList<StationLine>,

    @OneToMany(mappedBy = "stationId", cascade = [CascadeType.ALL], orphanRemoval = true)
    var aliasName: MutableList<AliasName> = mutableListOf()

)

