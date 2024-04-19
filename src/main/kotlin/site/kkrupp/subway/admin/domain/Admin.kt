package site.kkrupp.subway.admin.domain

import jakarta.annotation.Nonnull
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.io.Serializable

@Entity
data class Admin(
    @Id
    val username: String,

    @Nonnull
    val password: String,

    @Nonnull
    val role: String = "ADMIN"
) : Serializable