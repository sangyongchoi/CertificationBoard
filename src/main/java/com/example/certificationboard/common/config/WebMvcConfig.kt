package com.example.certificationboard.common.config

import com.example.certificationboard.common.config.argument.resolver.AuthorizeArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport

@Configuration
open class WebMvcConfig(
    private val authorizeArgumentResolver: AuthorizeArgumentResolver
): WebMvcConfigurationSupport() {

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(authorizeArgumentResolver)
        argumentResolvers.add(PageableHandlerMethodArgumentResolver());
    }
}