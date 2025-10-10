package com.sanya.mg.sanyademo.service

import com.sanya.mg.sanyademo.repository.UserRepository
import com.sanya.mg.sanyademo.repository.entity.UserEntity
import com.sanya.mg.sanyademo.service.model.UserModel
import com.sanya.mg.sanyademo.service.model.UserModel.Companion.toModel
import java.time.LocalDateTime
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun createUser(
        username: String,
        email: String,
    ): UserModel {
        val newUser = UserEntity(
            username = username,
            email = email,
            createdAt = LocalDateTime.now(),
        )

        val savedUser = userRepository.save(newUser)
        return savedUser.toModel()
    }

    fun getAllUsers(): List<UserModel> {
        val users = userRepository.findAll()
        return users.map { user -> user.toModel() }
    }

    fun getUserById(id: Long): UserModel {
        val user = userRepository.findById(id).get().toModel()
        return user
    }

    fun getUserByUsername(username: String): UserModel {
        val user = userRepository.findByUsername(username)
            ?: throw NoSuchElementException("User $username not found")

        return user.toModel()
    }

    fun getUserByEmail(email: String): UserModel {
        val user = userRepository.findByEmail(email)
            ?: throw NoSuchElementException("User $email not found")

        return user.toModel()
    }

    fun deleteUserById(id: Long): UserModel {
        val deletedUser = userRepository.findById(id).get()
        userRepository.deleteById(id)
        return deletedUser.toModel()
    }

    fun getUserEntityById(id: Long): UserEntity {
        return userRepository.findById(id).get()
    }
}
