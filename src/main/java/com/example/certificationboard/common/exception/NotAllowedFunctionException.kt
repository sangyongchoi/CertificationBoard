package com.example.certificationboard.common.exception

class NotAllowedFunctionException(
    override val message: String
): RuntimeException(message) {
}