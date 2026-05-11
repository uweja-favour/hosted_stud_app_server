//package com.xapps.selftest.quiz.infrastructure.config
//
//import com.xapps.selftest.AuthServiceWebFilter
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
//import org.springframework.security.config.web.server.SecurityWebFiltersOrder
//import org.springframework.security.config.web.server.ServerHttpSecurity
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.security.web.server.SecurityWebFilterChain
//import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
//import org.springframework.web.cors.CorsConfiguration
//import org.springframework.web.cors.reactive.CorsConfigurationSource
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
//
//@Configuration
//@EnableReactiveMethodSecurity
//class SecurityConfig(
//    private val jwtAuthEntryPoint: JwtAuthEntryPoint,
//    private val authServiceWebFilter: AuthServiceWebFilter
//) {
//    private val logger = LoggerFactory.getLogger(javaClass)
//
//    @Value("\${spring.security.oauth2.resourceserver.jwt.accessSecret}")
//    private lateinit var accessSecret: String
//
//    @Bean
//    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
//
//    @Bean
//    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
//        return http
//            .cors { it.configurationSource(corsConfigurationSource()) }
//            .csrf { it.disable() }
//            .exceptionHandling { exceptions ->
//                exceptions.authenticationEntryPoint { exchange, exception ->
//                    jwtAuthEntryPoint.commence(exchange, exception)
//                }
//            }
//            // Disable default session-based security (stateless API)
//            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
//            .authorizeExchange { exchanges ->
//                exchanges
//                    .pathMatchers("/api/self_test_quiz/**").permitAll()
//                    .anyExchange().permitAll()
//            }
//            // Disable HTTP Basic (it may interfere)
//            .httpBasic { it.disable() }
//            // Disable form login
//            .formLogin { it.disable() }
//            .addFilterBefore(authServiceWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
//            .build()
//    }
//
//    /**
//     * CORS configuration bean for handling cross-origin requests.
//     * Important for allowing the Android emulator to call the API.
//     */
//    @Bean
//    fun corsConfigurationSource(): CorsConfigurationSource {
//        val config = CorsConfiguration()
//        config.allowedOrigins = listOf("http://10.0.2.2:5177") // Android emulator origin
//        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
//        config.allowedHeaders = listOf("*")
//        config.allowCredentials = true // required for sending Authorization headers or cookies
//
//        val source = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/**", config)
//        return source
//    }
//}
//
