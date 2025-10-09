package com.sanya.mg.sanyademo

import com.sanya.mg.sanyademo.api.asset.AssetController
import com.sanya.mg.sanyademo.api.asset.dto.AssetUpdateRequest
import com.sanya.mg.sanyademo.api.user.UserController
import com.sanya.mg.sanyademo.common.BaseIT
import com.sanya.mg.sanyademo.common.TestUtil.createAndSaveDefaultUser
import com.sanya.mg.sanyademo.common.TestUtil.createDefaultAssetRequest
import com.sanya.mg.sanyademo.common.TestUtil.shouldMatch
import com.sanya.mg.sanyademo.repository.AssetRepository
import com.sanya.mg.sanyademo.repository.UserRepository
import com.sanya.mg.sanyademo.service.AssetService
import com.sanya.mg.sanyademo.service.TransactionService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal

/**
 * Integration tests for AssetController
 *
 * Test cases to cover:
 *
 * POST /api/assets (create)
 * ✅ 1. Successfully create asset for existing user
 * ✅ СЕЙЧАС НЕ БУДУТ РАБОТАТЬ Creating 2 assets with  same base ticker and quote ticker for the same user should return 400
 * ✅ СЕЙЧАС НЕ БУДУТ РАБОТАТЬ Creating 2 assets with  same base ticker but different quote ticker for the same user returns 200
 * ✅ СЕЙЧАС НЕ БУДУТ РАБОТАТЬ Creating 2 assets with  different base ticker but same quote ticker for the same user returns 200
 *
 * ❌ LATER Create asset with non-existing user ID - should return 404
 * ❌ LATER Create asset with negative quantity - should throw validation exception
 * ❌ LATER Create asset with zero quantity - should throw validation exception
 * ❌ LATER Create asset with empty baseTicker - should throw validation exception
 * ❌ LATER Create asset with empty quoteTicker - should throw validation exception
 *
 * GET /api/assets/{id} (get)
 * ✅ Successfully get asset by existing ID
 * ✅ Get asset by non-existing ID - should return 404
 *
 * GET /api/assets (getAll)
 * ✅ Successfully get all assets when multiple assets exist
 * ✅ Get all assets when no assets exist - should return empty list
 * ✅ Successfully get all assets when 1 asset exist return list with 1 asset
 *
 *
 * GET /api/assets/users/{userId} (getUserAssetsById)
 * ✅ Successfully get all assets for existing user with multiple assets
 * ✅ Get assets for existing user with no assets - should return empty list
 * ✅ Get assets for non-existing user ID - should return 404
 *
 * PUT /api/assets/{id} (update)
 * ✅ Successfully update asset quantity for existing asset
 * ✅ Update asset with non-existing ID - should return 404
 * ❌ LATER  Update asset with negative quantity - should throw validation exception
 * ❌ LATER  Update asset with zero quantity - should throw validation exception
 *
 * DELETE /api/assets/{id} (delete)
 * ✅ Successfully delete existing asset
 * ✅ Delete asset by non-existing ID - should return 404
 * ✅ Delete asset and verify it's removed from database
 *
 * Edge cases:
 * ✅ Create asset with very large quantity (BigDecimal precision)
 * ✅ Verify asset-user relationship is maintained correctly (when deleted user - assets deleted too)
 * ✅ Verify user not deleted when asset deleted
 * ✅ LATER Verify transactions are created when assets are modified (if applicable)
 */
@AutoConfigureWebTestClient
class AssetControllerIT(
    val assetController: AssetController,
    private val userRepository: UserRepository,
    private val assetRepository: AssetRepository,
    private val userController: UserController,
    private val transactionService: TransactionService,
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
            val user = createAndSaveDefaultUser(userRepository)
            val request = createDefaultAssetRequest(user = user)

            // Act
            val response = assetController.create(request)

            // Assert
            response.statusCode shouldBe HttpStatus.CREATED
            response.body shouldMatch request

            val assetFromDb = assetRepository.findById(response.body!!.id).get()
            assetFromDb.baseTicker shouldBe request.baseTicker
            assetFromDb.quoteTicker shouldBe request.quoteTicker
            assetFromDb.quantity.stripTrailingZeros() shouldBe request.quantity
            assetFromDb.user.id shouldBe request.userId
        }

        test("Should return 400 when creating 2 assets with  same base ticker and quote ticker for the same user") {
            // Arrange
            // создать юзера + заасейвать его
            // подготовить данные для создания ассетов (создать сущности реквестов)

            val user = createAndSaveDefaultUser(userRepository)

            val request1 = createDefaultAssetRequest(user = user)
            val request2 = createDefaultAssetRequest(quantity = 2.0, user = user)

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
            response1.body shouldMatch request1
            exception.statusCode shouldBe HttpStatus.BAD_REQUEST
            // TODO проверить что одна сохранилась в вторая нет
            val assetFromDb = assetRepository.findById(response1.body!!.id).get()
            assetFromDb.id shouldBe response1.body!!.id
            assetFromDb.baseTicker shouldBe response1.body!!.baseTicker
            assetFromDb.quoteTicker shouldBe response1.body!!.quoteTicker
            assetFromDb.user.id shouldBe response1.body!!.userId

            /**
             * Спросить следующее:
             * 1) Насколько имеет смысл проверка сохраненной сущности из бд с респонсом
             * если респонс который мы получаем от контроллера по факту внутри содержит сущность которую мы сохранили в бд
             * и она вернулась нам. Мы же респонс уже сравниваем с реквестом в shouldMatch. Является ли проверка
             * полученной именно из бд сущности излишней или так надо?
             *
             * 2) Когда я проверяю сохранился ли ассет в бд что я делаю:
             *  - получаю ассет из бд с помощью метода из ассетрепозитория
             *  Вопрос: я должен получать ассет из бд именно с помощью метода репозитория или же использовать
             *          метод из сервиса, так как там тоже есть метод поиска по id?
             *  - я сравниваю полученный из бд ассет с чем? Я должен сравнить его с реквестом или респонсом?
             *  - Опять же вопрос насколько имеет смысл данная проверка если перед этим мы проверяем данные из
             *  реквеста и респонса с помощью shouldMatch, а респонс по факту является сохраненной в бд сущностью asset
             *
             *  3) Как проверить сохранился ли первый ассет в таблице я понимаю, но как проверить сохранился ли второй такой же?
             *  Насколько это имеет смысл делать если в сервисе при создании ассета мы проверяем наличие такого же ассета в бд,
             *  и если он там есть, то выбрасывается ошибка и до создания и тем более сохранения в бд не доходит.
             *  В случае же если эта проверка необходима, то КАК ЕЕ СДЕЛАТЬ?!?!?
             */
        }

        test("Should return 200 when creating 2 assets with  same base ticker but different quote ticker for the same user") {
            // Arrange
            val user = createAndSaveDefaultUser(userRepository)
            val request1 = createDefaultAssetRequest(user = user)
            val request2 = createDefaultAssetRequest(quoteTicker = "ETH", quantity = 2.0, user = user)

            // Act

            val response1 = assetController.create(request1)
            val response2 = assetController.create(request2)

            // Assert

            response1.statusCode shouldBe HttpStatus.CREATED
            response1.body shouldMatch request1

            response2.statusCode shouldBe HttpStatus.CREATED
            response2.body shouldMatch request2
        }

        test("Should return 200 when creating 2 assets with  different base ticker but same quote ticker for the same user") {
            // Arrange
            val user = createAndSaveDefaultUser(userRepository)
            val request1 = createDefaultAssetRequest(user = user)
            val request2 = createDefaultAssetRequest(baseTicker = "ETH", quantity = 2.0, user = user)

            // Act

            val response1 = assetController.create(request1)
            val response2 = assetController.create(request2)

            // Assert

            response1.statusCode shouldBe HttpStatus.CREATED
            response1.body shouldMatch request1
            response2.statusCode shouldBe HttpStatus.CREATED
            response2.body shouldMatch request2
        }

        test("Should successfully get asset by existing ID") {
            // Arrange
            val user = createAndSaveDefaultUser(userRepository)
            val request = createDefaultAssetRequest(user = user)
            val response = assetController.create(request)

            // Act
            val foundedAsset = assetService.getAssetById(response.body?.id!!)
//        TODO То о чем я говорил, здесь я получаю сохраненный в бд ассет через метод сервиса а не метод репозитория

            // Assert
//        TODO Здесь я сравниваю и с респонсом и реквестом. Как правильно?

            foundedAsset.id shouldBe response.body?.id
            foundedAsset.baseTicker shouldBe request.baseTicker
            foundedAsset.quoteTicker shouldBe request.quoteTicker
            foundedAsset.quantity.stripTrailingZeros() shouldBe request.quantity
            foundedAsset.userId shouldBe request.userId
        }

        test("Should return not found 404 when getting asset by non existing id") {
            // Arrange
            val nonExistingAssetId = 1L

            // Act
            val response = assetController.get(nonExistingAssetId)

            // Assert
            response.statusCode shouldBe HttpStatus.NOT_FOUND
        }

        test("Successfully get all assets when multiple assets exist") {
            //  Arrange
            val user = createAndSaveDefaultUser(userRepository)
            val request1 = createDefaultAssetRequest(user = user)
            val request2 = createDefaultAssetRequest(baseTicker = "ETH", user = user)
            val response1 = assetController.create(request1)
            val response2 = assetController.create(request2)

            //  Act
            val response3 = assetController.getAll()

            //  Assert
            response1.statusCode shouldBe HttpStatus.CREATED
            response1.body shouldMatch request1
            response2.statusCode shouldBe HttpStatus.CREATED
            response2.body shouldMatch request2
            response3.statusCode shouldBe HttpStatus.OK
            response3.body?.shouldContainAll(listOf(response1.body, response2.body))
        }

        test("Get all assets when no assets exist - should return empty list") {
            //  Arrange
            val response = assetController.getAll()

            //  Act
            val assets = response.body

            //  Assert
            response.statusCode shouldBe HttpStatus.OK
            assets?.size shouldBe 0
        }

        test("Successfully get all assets when 1 asset exist return list with 1 asset") {
            //  Arrange
            val user = createAndSaveDefaultUser(userRepository)
            val request = createDefaultAssetRequest(user = user)
            val response = assetController.create(request)
            val allAssetsResponse = assetController.getAll()

            //  Act
            val assets = allAssetsResponse.body

            // Assert
            response.statusCode shouldBe HttpStatus.CREATED
            response.body shouldMatch request
            assets?.size shouldBe 1
            assets shouldContainOnly listOf(response.body)
        }

        test("Successfully get all assets for existing user with multiple assets") {
            //  Arrange
            val user = createAndSaveDefaultUser(userRepository)
            val request1 = createDefaultAssetRequest(user = user)
            val request2 = createDefaultAssetRequest("ETH", user = user)
            val response1 = assetController.create(request1)
            val response2 = assetController.create(request2)
            val allUserAssetsResponse = assetController.getUserAssetsById(user.id!!)

            //  Act
            val assets = allUserAssetsResponse.body

            //  Assert
            response1.statusCode shouldBe HttpStatus.CREATED
            response1.body shouldMatch request1
            response1.body?.userId shouldBe user.id
            response2.statusCode shouldBe HttpStatus.CREATED
            response2.body shouldMatch request2
            response2.body?.userId shouldBe user.id
            assets?.size shouldNotBe 0
            assets?.shouldContainAll(listOf(response1.body, response2.body))
        }

        test("Get assets for existing user with no assets - should return empty list") {
            //  Arrange
            val user = createAndSaveDefaultUser(userRepository)
            val allUserAssetsResponse = assetController.getUserAssetsById(user.id!!)

            //  Act
            val assets = allUserAssetsResponse.body

            //  Assert
            allUserAssetsResponse.statusCode shouldBe HttpStatus.OK
            assets?.size shouldBe 0
        }

        test("Get assets for non-existing user ID - should return 404") {
            //  Arrange
            val nonExistingUserId = 1L

            //  Act
            val allUserAssetsResponse = assetController.getUserAssetsById(nonExistingUserId)

            //  Assert
            allUserAssetsResponse.statusCode shouldBe HttpStatus.NOT_FOUND
        }

        test("Successfully update asset quantity for existing asset") {
            //  Arrange
            val user = createAndSaveDefaultUser(userRepository)
            val request = createDefaultAssetRequest(user = user)
            val response = assetController.create(request)

            //  Act
            val updateRequest = AssetUpdateRequest(quantity = BigDecimal("124"))
            val updateResponse = assetController.update(response.body?.id!!, updateRequest)

            //  Assert
            response.statusCode shouldBe HttpStatus.CREATED
            response.body shouldMatch request
            updateResponse.statusCode shouldBe HttpStatus.OK
            updateResponse.body?.quantity shouldBe updateRequest.quantity
            updateResponse.body?.quantity shouldNotBe response.body?.quantity
        }

        test("Update asset with non-existing ID - should return 404") {
            //  Arrange
            val nonExistingAssertId = 1L
            val updateRequest = AssetUpdateRequest(quantity = BigDecimal("124"))

            //  Act
            val updateResponse = assetController.update(nonExistingAssertId, updateRequest)

            //  Assert
            updateResponse.statusCode shouldBe HttpStatus.NOT_FOUND
        }

        test("Successfully delete existing asset and verify it's removed from DB") {
            //  Arrange
            val user = createAndSaveDefaultUser(userRepository)
            val request = createDefaultAssetRequest(user = user)
            val response = assetController.create(request)

            //  Act
            val deletedAssetResponse = assetController.delete(response.body?.id!!)
            val assets = assetController.getAll().body
            val foundDeleted = assetController.get(deletedAssetResponse.body?.id!!)

            //  Assert
            deletedAssetResponse.statusCode shouldBe HttpStatus.OK
            assets?.size shouldBe 0
            foundDeleted.statusCode shouldBe HttpStatus.NOT_FOUND
        }

        test("Delete asset by non-existing ID - should return 404") {
            //  Arrange
            val nonExistingAssetId = 1L

            //  Act

            val deleteResponse = assetController.delete(nonExistingAssetId)

            //  Assert
            deleteResponse.statusCode shouldBe HttpStatus.NOT_FOUND
        }

        test("Create asset with very large quantity (BigDecimal precision)") {
            //  Arrange
            val user = createAndSaveDefaultUser(userRepository)
            val request = createDefaultAssetRequest(quantity = 1.23E+1, user = user)

            //  Act
            val response = assetController.create(request)

            //  Assert
            response.statusCode shouldBe HttpStatus.CREATED
            response.body shouldMatch request
        }

        test("Verify asset-user relationship is maintained correctly (when deleted user - assets deleted too)") {
            //  Arrange
            val user = createAndSaveDefaultUser(userRepository)
            val request = createDefaultAssetRequest(user = user)
            val response = assetController.create(request)

            //  Act
            val deletedUser = userController.deleteUserById(user.id!!)

            //  Assert
            response.statusCode shouldBe HttpStatus.CREATED
            response.body shouldMatch request
            response.body?.userId shouldBe user.id
            user.assets shouldNotContain response.body
            user.assets.size shouldBe 0
            deletedUser.statusCode shouldBe HttpStatus.OK
            assetController.getUserAssetsById(deletedUser.body?.id!!).statusCode shouldBe HttpStatus.NOT_FOUND
            assetController.get(response.body?.id!!).statusCode shouldBe HttpStatus.NOT_FOUND
        }

        test("Verify user not deleted when asset deleted") {
            //  Arrange
            val user = createAndSaveDefaultUser(userRepository)
            val request = createDefaultAssetRequest(user = user)
            val response = assetController.create(request)

            //  Act
            val deletedAssetResponse = assetController.delete(response.body?.id!!)
            val foundUser = userController.getUserById(user.id!!)

            //  Assert
            response.statusCode shouldBe HttpStatus.CREATED
            response.body shouldMatch request
            deletedAssetResponse.statusCode shouldBe HttpStatus.OK
            assetController.get(response.body?.id!!).statusCode shouldBe HttpStatus.NOT_FOUND
            user.assets shouldNotContain response.body
            user.assets.size shouldBe 0
            foundUser.statusCode shouldBe HttpStatus.OK
        }

        test("LATER Verify transactions are created when assets are modified (if applicable)") {
            //  Arrange
            val user = createAndSaveDefaultUser(userRepository)
            val request = createDefaultAssetRequest(user = user)
            val response = assetController.create(request)

            //  Act
            val updateAssetRequest1 = AssetUpdateRequest(BigDecimal("234.34"))
            val updateAssetResponse1 = assetController.update(response.body?.id!!, updateAssetRequest1)
            val updateAssetRequest2 = AssetUpdateRequest(BigDecimal("1.34"))
            val updateAssetResponse2 = assetController.update(response.body?.id!!, updateAssetRequest2)
            val user2 = userRepository.findById(user.id!!).get()

            //  Assert
            response.statusCode shouldBe HttpStatus.CREATED
            response.body shouldMatch request
            updateAssetResponse1.statusCode shouldBe HttpStatus.OK
            updateAssetResponse1.body?.quantity?.stripTrailingZeros() shouldBe updateAssetRequest1.quantity
            updateAssetResponse1.body?.quantity?.stripTrailingZeros() shouldNotBe response.body?.quantity
            updateAssetResponse2.statusCode shouldBe HttpStatus.OK
            updateAssetResponse2.body?.quantity?.stripTrailingZeros() shouldBe updateAssetRequest2.quantity
            updateAssetResponse2.body?.quantity?.stripTrailingZeros() shouldNotBe response.body?.quantity
        }
    }
}
