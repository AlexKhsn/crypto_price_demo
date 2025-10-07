package com.sanya.mg.sanyademo.api.user.dto

import io.swagger.v3.oas.annotations.media.Schema

data class CreateUserDto(
    @field:Schema(description = "Username", example = "john_doe")
    val username: String,
    @field:Schema(description = "Email address", example = "john.doe@example.com")
    val email: String,
)
