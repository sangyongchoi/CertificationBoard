package com.example.certificationboard.member.exception

class UserNotFoundException(
    override val message: String
): IllegalArgumentException(message) {
}