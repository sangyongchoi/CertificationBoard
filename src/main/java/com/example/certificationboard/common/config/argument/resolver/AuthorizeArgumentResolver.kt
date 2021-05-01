package com.example.certificationboard.common.config.argument.resolver

import com.example.certificationboard.member.domain.Member
import com.example.certificationboard.member.domain.MemberRepository
import com.example.certificationboard.member.exception.UserNotFoundException
import org.springframework.core.MethodParameter
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

annotation class AuthUser

@Component
class AuthorizeArgumentResolver(
    private val memberRepository: MemberRepository
): HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(AuthUser::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?
    ): Member {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val userId = authentication.name

        return memberRepository.findById(userId).orElseThrow { UserNotFoundException("존재하지 않는 유저입니다.") }
    }
}