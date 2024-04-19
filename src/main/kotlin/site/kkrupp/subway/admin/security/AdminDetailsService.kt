package site.kkrupp.subway.admin.security

import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import site.kkrupp.subway.admin.repository.AdminRepository


@Service
class AdminDetailsService(
    val adminRepository: AdminRepository
) : UserDetailsService {

    private val logger = LoggerFactory.getLogger(AdminDetailsService::class.java)

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val admin = adminRepository.findById(username).orElseThrow { UsernameNotFoundException("Invalid username") }

        return AdminDetail(admin)
    }
}