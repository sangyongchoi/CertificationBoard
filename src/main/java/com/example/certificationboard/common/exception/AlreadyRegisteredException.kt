package com.example.certificationboard.common.exception

class AlreadyRegisteredException(
    override val message: String
) : RuntimeException(message) {
}