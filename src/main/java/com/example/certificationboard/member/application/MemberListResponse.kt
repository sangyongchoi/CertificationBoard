package com.example.certificationboard.member.application

data class MemberListResponse(
    val membersInfo: List<MemberResponse>,
    val hasNext: Boolean
) {
}