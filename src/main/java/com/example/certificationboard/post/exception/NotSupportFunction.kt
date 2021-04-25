package com.example.certificationboard.post.exception

import java.lang.RuntimeException

class NotSupportFunction(
        override val message: String
): RuntimeException() {
}