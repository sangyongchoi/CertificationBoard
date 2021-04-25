package com.example.certificationboard.post.exception

import java.lang.RuntimeException

class NotAllowedValueException (
        override val message: String
): RuntimeException(){
}