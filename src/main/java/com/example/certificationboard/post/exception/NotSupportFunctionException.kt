package com.example.certificationboard.post.exception

import java.lang.RuntimeException

class NotSupportFunctionException(
        override val message: String
): RuntimeException() {
}