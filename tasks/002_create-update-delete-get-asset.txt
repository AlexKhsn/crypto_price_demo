# Задача 002: CRUD операции для Asset

## Цель
Создать REST API для управления криптовалютными активами (Asset) с полным набором CRUD операций.

## Описание
В проекте уже существует:
- Сущность `Asset` (src/main/kotlin/com/sanya/mg/sanyademo/entity/Asset.kt)
- Репозиторий `AssetRepository` (src/main/kotlin/com/sanya/mg/sanyademo/repository/AssetRepository.kt)

Необходимо реализовать:

### 1. Создать DTO классы
Создать в пакете `com.sanya.mg.sanyademo.dto`:
- `AssetCreateRequest` - для создания нового актива
  - baseTicker: String
  - quoteTicker: String
  - quantity: BigDecimal
- `AssetUpdateRequest` - для обновления актива
    тут уебанство!
  - quantity: BigDecimal
- `AssetResponse` - для возврата данных об активе
  - id: Long
  - baseTicker: String
  - quoteTicker: String
  - quantity: BigDecimal

### 2. Создать AssetService
Создать сервис `AssetService` в пакете `com.sanya.mg.sanyademo.service`:
- `createAsset(request: AssetCreateRequest): AssetResponse` - создание актива
- `getAssetById(id: Long): AssetResponse` - получение актива по ID
- `getAllAssets(): List<AssetResponse>` - получение всех активов
- `updateAsset(id: Long, request: AssetUpdateRequest): AssetResponse` - обновление количества актива
- `deleteAsset(id: Long)` - удаление актива

Сервис должен:
- Использовать `AssetRepository` для работы с БД
- Обрабатывать случай, когда актив не найден (выбросить исключение)
- Маппить Entity в DTO и обратно

### 3. Создать AssetController
Создать контроллер `AssetController` в пакете `com.sanya.mg.sanyademo.api`:

Эндпоинты:
- `POST /api/assets` - создать новый актив
  - Request body: AssetCreateRequest
  - Response: AssetResponse (HTTP 201)
- `GET /api/assets/{id}` - получить актив по ID
  - Response: AssetResponse (HTTP 200)
- `GET /api/assets` - получить все активы
  - Response: List<AssetResponse> (HTTP 200)
- `PUT /api/assets/{id}` - обновить количество актива
  - Request body: AssetUpdateRequest
  - Response: AssetResponse (HTTP 200)
- `DELETE /api/assets/{id}` - удалить актив
  - Response: HTTP 204 (No Content)

### 4. Обработка ошибок (опционально)
Создать простой обработчик исключений для случая, когда актив не найден (может вернуть HTTP 404).

## Технические требования
- Использовать Kotlin
- Следовать существующей структуре проекта
- Использовать Spring Boot аннотации (@RestController, @Service, @RequestMapping, и т.д.)
- Использовать dependency injection через constructor

## Подсказки по именованию

### Файлы и классы
- DTO классы создать в: `src/main/kotlin/com/sanya/mg/sanyademo/dto/`
  - `AssetCreateRequest.kt`
  - `AssetUpdateRequest.kt`
  - `AssetResponse.kt`
- Сервис создать в: `src/main/kotlin/com/sanya/mg/sanyademo/service/AssetService.kt`
- Контроллер создать в: `src/main/kotlin/com/sanya/mg/sanyademo/api/AssetController.kt`

### Пример структуры DTO (data class)
```kotlin
data class AssetCreateRequest(
    val baseTicker: String,
    val quoteTicker: String,
    val quantity: BigDecimal
)
```

### Пример методов репозитория (уже существует)
```kotlin
assetRepository.save(asset)           // сохранить
assetRepository.findById(id)          // найти по ID
assetRepository.findAll()             // найти все
assetRepository.deleteById(id)        // удалить
```

### Пример аннотаций для контроллера
```kotlin
@RestController
@RequestMapping("/api/assets")
class AssetController(private val assetService: AssetService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAsset(@RequestBody request: AssetCreateRequest): AssetResponse {
        return assetService.createAsset(request)
    }
    // остальные методы...
}
```

### Пример аннотаций для сервиса
```kotlin
@Service
class AssetService(private val assetRepository: AssetRepository) {
    // методы...
}
```

## Проверка
После реализации:
1. Запустите приложение
2. Откройте Swagger UI по адресу: http://localhost:8080/swagger-ui/index.html
3. Проверьте работу всех эндпоинтов через Swagger интерфейс
