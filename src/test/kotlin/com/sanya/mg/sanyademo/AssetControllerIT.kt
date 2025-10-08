package com.sanya.mg.sanyademo

import com.sanya.mg.sanyademo.api.asset.AssetController
import com.sanya.mg.sanyademo.api.asset.dto.AssetCreateRequest
import com.sanya.mg.sanyademo.common.BaseIT
import com.sanya.mg.sanyademo.repository.AssetRepository
import com.sanya.mg.sanyademo.repository.UserRepository
import com.sanya.mg.sanyademo.repository.entity.User
import com.sanya.mg.sanyademo.service.AssetService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.time.LocalDateTime.now

/**
 * Integration tests for AssetController
 *
 * Test cases to cover:
 *
 * POST /api/assets (create)
 * ✅ 1. Successfully create asset for existing user
 * ❌ СЕЙЧАС НЕ БУДУТ РАБОТАТЬ Creating 2 assets with  same base ticker and quote ticker for the same user should return 400
 * ❌ СЕЙЧАС НЕ БУДУТ РАБОТАТЬ Creating 2 assets with  same base ticker but different quote ticker for the same user returns 200
 * ❌ СЕЙЧАС НЕ БУДУТ РАБОТАТЬ Creating 2 assets with  different base ticker but same quote ticker for the same user returns 200
 *
 * LATER Create asset with non-existing user ID - should return 404
 * LATER Create asset with negative quantity - should throw validation exception
 * LATER Create asset with zero quantity - should throw validation exception
 * LATER Create asset with empty baseTicker - should throw validation exception
 * LATER Create asset with empty quoteTicker - should throw validation exception
 *
 * GET /api/assets/{id} (get)
 * ✅ Successfully get asset by existing ID
 * ✅ Get asset by non-existing ID - should return 404
 *
 * GET /api/assets (getAll)
 * ❌ Successfully get all assets when multiple assets exist
 * ❌ Get all assets when no assets exist - should return empty list
 * ❌ Successfully get all assets when 1 asset exist return list with 1 asset
 *
 *
 * GET /api/assets/users/{userId} (getUserAssetsById)
 * ❌ Successfully get all assets for existing user with multiple assets
 * ❌ Get assets for existing user with no assets - should return empty list
 * ❌ Get assets for non-existing user ID - should return 404
 *
 * PUT /api/assets/{id} (update)
 * ❌ Successfully update asset quantity for existing asset
 * ❌ Update asset with non-existing ID - should return 404
 * ❌ LATER  Update asset with negative quantity - should throw validation exception
 * ❌ LATER  Update asset with zero quantity - should throw validation exception
 *
 * DELETE /api/assets/{id} (delete)
 * ❌ Successfully delete existing asset
 * ❌ Delete asset by non-existing ID - should return 404
 * ❌ Delete asset and verify it's removed from database
 *
 * Edge cases:
 * ❌ Create asset with very large quantity (BigDecimal precision)
 * ❌ Verify asset-user relationship is maintained correctly (when deleted user - assets deleted too)
 * ❌ Verify user not deleted when asset deleted
 * ❌ LATER Verify transactions are created when assets are modified (if applicable)
 */
@AutoConfigureWebTestClient
class AssetControllerIT(
    val assetController: AssetController,
    private val userRepository: UserRepository,
    private val assetRepository: AssetRepository,
    @Autowired private val assetService: AssetService,
) : BaseIT() {
    init {

        beforeEach {
            assetRepository.deleteAll()
            userRepository.deleteAll()
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

        test("Successfully create asset for existing user") {
            // Arrange
            val userForSave = User(
                id = null,
                username = "Test User",
                email = "jonh.doe@example.com",
                createdAt = now(),
            )
            val user = userRepository.save(userForSave)
            val request = AssetCreateRequest(
                baseTicker = "BTC",
                quoteTicker = "USDT",
                quantity = BigDecimal("1.5"),
                userId = user.id!!,
            )

            // Act
            val response = assetController.create(request)

            // Assert
            response.statusCode shouldBe HttpStatus.CREATED
            val responseBody = response.body!!
            responseBody.baseTicker shouldBe request.baseTicker
            responseBody.quoteTicker shouldBe request.quoteTicker
            responseBody.quantity shouldBe request.quantity
            responseBody.userId shouldBe request.userId
            responseBody.id shouldBeGreaterThan 0

            val assetFromDb = assetRepository.findById(responseBody.id).get()
            assetFromDb.baseTicker shouldBe request.baseTicker
            assetFromDb.quoteTicker shouldBe request.quoteTicker
            assetFromDb.quantity.stripTrailingZeros() shouldBe request.quantity
            assetFromDb.user.id shouldBe request.userId
        }

        test("Should return 400 when creating 2 assets with  same base ticker and quote ticker for the same user") {
            // Arrange
            // создать юзера + заасейвать его
            // подготовить данные для создания ассетов (создать сущности реквестов)

            val userForSave = User(
                id = null,
                username = "Test User",
                email = "jonh.doe@example.com",
                createdAt = now(),
            )

            val user = userRepository.save(userForSave)

            val request1 = AssetCreateRequest(
                baseTicker = "BTC",
                quoteTicker = "USDT",
                quantity = BigDecimal("1.5"),
                userId = user.id!!,
            )

            val request2 = AssetCreateRequest(
                baseTicker = "BTC",
                quoteTicker = "USDT",
                quantity = BigDecimal("1.5"),
                userId = user.id!!,
            )

            // FIXME Почему подсвечивает !! сверху, но когда уберу то не работает?

            // Act
            // вызвать метод контроллера и получить респонс1 и ошибку от второго реквеста

            val response1 = assetController.create(request1)
            val exception = shouldThrow<ResponseStatusException> {
                assetController.create(request2)
            }

            // Assert
            // проверить статус код респонс1
            // проверить статус код отловленной ошибки

            response1.statusCode shouldBe HttpStatus.CREATED
            exception.statusCode shouldBe HttpStatus.BAD_REQUEST
        }

        test("Should return 200 when creating 2 assets with  same base ticker but different quote ticker for the same user") {
            // Arrange
            val userForSave = User(
                id = null,
                username = "Test User",
                email = "jonh.doe@example.com",
                createdAt = now(),
            )
            val user = userRepository.save(userForSave)
            val request1 = AssetCreateRequest(
                baseTicker = "BTC",
                quoteTicker = "USDT",
                quantity = BigDecimal("1.5"),
                userId = user.id!!,
            )
            val request2 = AssetCreateRequest(
                baseTicker = "BTC",
                quoteTicker = "ETH",
                quantity = BigDecimal("1.5"),
                userId = user.id!!,
            )

            // Act

            val response1 = assetController.create(request1)
            val response2 = assetController.create(request2)

            // Assert

            response1.statusCode shouldBe HttpStatus.CREATED
            response2.statusCode shouldBe HttpStatus.CREATED
        }

        test("Should return 200 when creating 2 assets with  different base ticker but same quote ticker for the same user") {
            // Arrange
            val userForSave = User(
                id = null,
                username = "Test User",
                email = "jonh.doe@example.com",
                createdAt = now(),
            )
            val user = userRepository.save(userForSave)
            val request1 = AssetCreateRequest(
                baseTicker = "BTC",
                quoteTicker = "USDT",
                quantity = BigDecimal("1.5"),
                userId = user.id!!,
            )
            val request2 = AssetCreateRequest(
                baseTicker = "ETH",
                quoteTicker = "USDT",
                quantity = BigDecimal("1.5"),
                userId = user.id!!,
            )

            // Act

            val response1 = assetController.create(request1)
            val response2 = assetController.create(request2)

            // Assert

            response1.statusCode shouldBe HttpStatus.CREATED
            response2.statusCode shouldBe HttpStatus.CREATED
        }

        test("Should successfully get asset by existing ID") {
            // Arrange
            val userForSave = User(
                id = null,
                username = "Test User",
                email = "jonh.doe@example.com",
                createdAt = now(),
            )

            val user = userRepository.save(userForSave)

            val request = AssetCreateRequest(
                baseTicker = "BTC",
                quoteTicker = "USDT",
                quantity = BigDecimal("1.5"),
                userId = user.id!!,
            )
            val response = assetController.create(request)

            // Act
            val foundedAsset = assetService.getAssetById(response.body?.id!!)

            // Assert

            foundedAsset.id shouldBe response.body?.id
        }

        test("Should return not found when getting non existing asset by id") {
            // Arrange
            val nonExistingAssetId = 1L

            // Act
            val response = assetController.get(nonExistingAssetId)

            // Assert
            response.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }
}
