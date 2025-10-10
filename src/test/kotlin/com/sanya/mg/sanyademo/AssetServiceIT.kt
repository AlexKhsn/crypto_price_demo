package com.sanya.mg.sanyademo

import com.sanya.mg.sanyademo.common.BaseIT
import com.sanya.mg.sanyademo.repository.AssetRepository
import com.sanya.mg.sanyademo.repository.TransactionRepository
import com.sanya.mg.sanyademo.repository.UserRepository
import com.sanya.mg.sanyademo.service.UserService
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient

/**
 * Integration tests for AssetController
 * @constructor Creates AssetServiceIT
 */
@AutoConfigureWebTestClient
class AssetServiceIT(
    private val userRepository: UserRepository,
    private val assetRepository: AssetRepository,
    private val transactionRepository: TransactionRepository,
    private val userService: UserService
) : BaseIT() {
    init {

        beforeEach {
            assetRepository.deleteAll()
            userRepository.deleteAll()
            transactionRepository.deleteAll()
        }

        test("EXAMPLE PLAN successfully create asset for existing user") {
//            // Arrange
//            // создать юзера + заасейвать его
//            // подготовить данные для создания ассета (создать сущбность риквеста)
//
//            // Act
//            // вызвать метод контроллера и получить риспонс
//
//            // Assert
//            // проверить риспонс
//            // проверить что ассет создался в базе
//            // проверить что ассет привязался к юзеру
//            // ...
        }
    }
}
