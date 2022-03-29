package com.sorsix.koko.security

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy


class SecurityConfig : WebSecurityConfigurerAdapter() {

    private val publicMatchers = arrayOf("/api", "/api")

    override fun configure(http: HttpSecurity) {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests().antMatchers(*publicMatchers).permitAll()
            .anyRequest().authenticated()
    }
}