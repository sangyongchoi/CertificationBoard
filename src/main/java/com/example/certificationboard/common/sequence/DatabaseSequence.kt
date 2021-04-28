package com.example.certificationboard.common.sequence

import org.springframework.data.mongodb.core.mapping.Document
import javax.persistence.Id

@Document
class DatabaseSequence(
  @field:Id var id: String,
  var seq: Long
) {
}