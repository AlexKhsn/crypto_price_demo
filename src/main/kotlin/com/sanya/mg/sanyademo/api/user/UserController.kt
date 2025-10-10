package com.sanya.mg.sanyademo.api.user

import com.sanya.mg.sanyademo.api.user.dto.CreateUserDto
import com.sanya.mg.sanyademo.api.user.dto.UserDto
import com.sanya.mg.sanyademo.api.user.dto.UserDto.Companion.toDto
import com.sanya.mg.sanyademo.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management API")
class UserController(
    val userService: UserService,
) {
    @Operation(summary = "Create user", description = "Creates a new user")
    @PostMapping
    fun addUser(
        @RequestBody user: CreateUserDto,
    ): ResponseEntity<UserDto> {
        val user = userService.createUser(
            username = user.username,
            email = user.email,
        )
        return ResponseEntity.ok().body(user.toDto())
    }

    @Operation(summary = "Get all users", description = "Returns a list of all users")
    @GetMapping
    fun getUsers(): ResponseEntity<List<UserDto>> {
        val users = userService.getAllUsers().map { it.toDto() }
        return ResponseEntity.ok().body(users)
    }

    @Operation(summary = "Get user by ID", description = "Returns a single user by their ID")
    @GetMapping("/{id}")
    fun getUserById(
        @PathVariable id: Long,
    ): ResponseEntity<UserDto> {
        try {
            val user = userService.getUserById(id)
            return ResponseEntity.ok().body(user.toDto())
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }

    @Operation(summary = "Get user by username", description = "Returns a user by their username")
    @GetMapping("/name/{username}")
    fun getUserByName(
        @PathVariable username: String,
    ): ResponseEntity<UserDto> {
        try {
            val user = userService.getUserByUsername(username)
            return ResponseEntity.ok().body(user.toDto())
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }

    @Operation(summary = "Get user by email", description = "Returns a user by their email address")
    @GetMapping("/email/{email}")
    fun getUserByEmail(
        @PathVariable email: String,
    ): ResponseEntity<UserDto> {
        try {
            val user = userService.getUserByEmail(email)
            return ResponseEntity.ok().body(user.toDto())
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }

    @Operation(summary = "Delete user", description = "Deletes a user by their ID")
    @DeleteMapping("/{id}")
    fun deleteUserById(
        @PathVariable id: Long,
    ): ResponseEntity<UserDto> {
        try {
            val deletedUser = userService.deleteUserById(id)
            return ResponseEntity.ok().body(deletedUser.toDto())
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }
}
