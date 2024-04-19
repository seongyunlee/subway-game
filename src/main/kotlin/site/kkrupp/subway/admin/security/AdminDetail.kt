package site.kkrupp.subway.admin.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import site.kkrupp.subway.admin.domain.Admin


class AdminDetail(val admin: Admin) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(
            GrantedAuthority { "ROLE_${admin.role}" }
        )
    }

    override fun getPassword(): String {
        return admin.password
    }

    override fun getUsername(): String {
        return admin.username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}