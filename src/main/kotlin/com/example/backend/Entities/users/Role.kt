package com.example.backend.Entities.users


import org.springframework.security.core.authority.SimpleGrantedAuthority

enum class Role(val permissions: Set<Permission>) {
    USER(emptySet()),
    ADMIN(
        setOf(
            Permission.ADMIN_READ,
            Permission.ADMIN_UPDATE,
            Permission.ADMIN_DELETE,
            Permission.ADMIN_CREATE
        )
    );

    fun getAuthorities(): List<SimpleGrantedAuthority> {
        val authorities = permissions
            .map { SimpleGrantedAuthority(it.permission) }
            .toMutableList()
        authorities.add(SimpleGrantedAuthority("ROLE_${this.name}"))
        return authorities
    }
}
