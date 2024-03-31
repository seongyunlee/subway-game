package site.kkrupp.subway.station.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "alias_name")
@Entity
class AliasName(
    @Id
    @Column(name = "ID")
    val id: String,

    @Column(name = "ALIAS_NAME")
    val name: String,

    @Column(name = "STATION_ID")
    val stationId: String
)