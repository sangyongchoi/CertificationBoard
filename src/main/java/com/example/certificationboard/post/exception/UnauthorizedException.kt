package com.example.certificationboard.post.exception

import java.lang.RuntimeException

class UnauthorizedException(
    override val message: String
): RuntimeException() {
}