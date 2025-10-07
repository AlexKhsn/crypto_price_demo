package com.sanya.mg.sanyademo.service

import com.sanya.mg.sanyademo.api.user.dto.CreateUserDto
import com.sanya.mg.sanyademo.api.user.dto.UserDto
import com.sanya.mg.sanyademo.repository.UserRepository
import com.sanya.mg.sanyademo.repository.entity.User
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun createUser(request: CreateUserDto): UserDto {
        val newUser = User(
            username = request.username,
            email = request.email,
            createdAt = LocalDateTime.now(),
        )

        val savedUser = userRepository.save(newUser)
        return UserDto fromEntity savedUser
    }

    fun getAllUsers(): List<UserDto> {
        val users = userRepository.findAll()
        return users.map { user -> UserDto fromEntity user }
    }

    fun getUserById(id: Long): UserDto {
        val user = userRepository.findById(id).get()
        return UserDto fromEntity user
    }

    fun getUserByUsername(username: String): UserDto {
        val user = userRepository.findByUsername(username)
            ?: throw NoSuchElementException("User $username not found")

        return UserDto fromEntity user
    }

    fun getUserByEmail(email: String): UserDto {
        val user = userRepository.findByEmail(email)
            ?: throw NoSuchElementException("User $email not found")

        return UserDto fromEntity user
    }

    fun deleteUserById(id: Long): UserDto {
        val deletedUser = userRepository.findById(id).get()
        userRepository.deleteById(id)
        return UserDto fromEntity deletedUser
    }
}
