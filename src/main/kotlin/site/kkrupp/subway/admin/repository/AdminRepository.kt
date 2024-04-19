package site.kkrupp.subway.admin.repository

import org.springframework.data.jpa.repository.JpaRepository
import site.kkrupp.subway.admin.domain.Admin

interface AdminRepository : JpaRepository<Admin, String>