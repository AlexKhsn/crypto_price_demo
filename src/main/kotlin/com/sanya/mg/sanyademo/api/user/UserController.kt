package com.sanya.mg.sanyademo.api.user

import com.sanya.mg.sanyademo.api.user.dto.CreateUserDto
import com.sanya.mg.sanyademo.api.user.dto.UserDto
import com.sanya.mg.sanyademo.service.UserService
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
class UserController(
    val userService: UserService,
) {
    @PostMapping
    fun addUser(
        @RequestBody user: CreateUserDto,
    ): ResponseEntity<UserDto> {
        val user = userService.createUser(user)
        return ResponseEntity.ok().body(user)
    }

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserDto>> {
        val users = userService.getAllUsers()
        return ResponseEntity.ok().body(users)
    }

    @GetMapping("/{id}")
    fun getUserById(
        @PathVariable id: Long,
    ): ResponseEntity<UserDto> {
        try {
            val userById = userService.getUserById(id)
            return ResponseEntity.ok().body(userById)
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/name/{username}")
    fun getUserByName(
        @PathVariable username: String,
    ): ResponseEntity<UserDto> {
        try {
            val user = userService.getUserByUsername(username)
            return ResponseEntity.ok().body(user)
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/email/{email}")
    fun getUserByEmail(
        @PathVariable email: String,
    ): ResponseEntity<UserDto> {
        try {
            val user = userService.getUserByEmail(email)
            return ResponseEntity.ok().body(user)
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteUserById(
        @PathVariable id: Long,
    ): ResponseEntity<UserDto> {
        try {
            val deletedUser = userService.deleteUserById(id)
            return ResponseEntity.ok().body(deletedUser)
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }
}
