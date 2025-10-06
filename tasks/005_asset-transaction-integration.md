# Задача 005: Интеграция операций с активами и транзакциями

## Цель
Автоматически создавать транзакции при манипуляциях с активами (Asset). Каждое изменение актива должно отражаться в истории транзакций.

## Описание
Необходимо модифицировать `AssetService` так, чтобы операции с активами автоматически создавали соответствующие записи в таблице транзакций (Transaction).

## Бизнес-логика

### 1. POST /api/assets (Создание актива)
При создании нового актива создается транзакция типа **BUY**:
- type: "BUY"
- symbol: baseTicker (например "BTC")
- quantity: количество из AssetCreateRequest
- price: текущая рыночная цена (можно захардкодить или получить из внешнего API)
- date: текущая дата/время

### 2. DELETE /api/assets/{id} (Удаление актива)
При удалении актива создается транзакция типа **SELL**:
- type: "SELL"
- symbol: baseTicker удаляемого актива
- quantity: количество актива на момент удаления
- price: текущая рыночная цена
- date: текущая дата/время

### 3. PUT /api/assets/{id} (Обновление количества актива)
При изменении количества актива:

**Если количество увеличилось** (новое > старого):
- Создать транзакцию типа **BUY**
- quantity: разница между новым и старым количеством
- Пример: было 5 BTC, стало 8 BTC → BUY транзакция на 3 BTC

**Если количество уменьшилось** (новое < старого):
- Создать транзакцию типа **SELL**
- quantity: разница между старым и новым количеством
- Пример: было 8 BTC, стало 5 BTC → SELL транзакция на 3 BTC

**Если количество не изменилось**:
- Не создавать транзакцию

## Технические требования

### 1. Модификация AssetService
- Добавить `TransactionRepository` в конструктор через dependency injection
- Модифицировать методы:
  - `createAsset()` - добавить создание BUY транзакции
  - `updateAsset()` - добавить логику сравнения количества и создание соответствующей транзакции
  - `deleteAsset()` - добавить создание SELL транзакции перед удалением

### 3. Транзакционность
- Используй аннотацию `@Transactional` на методах сервиса
- Это гарантирует что если не удастся создать транзакцию, то и актив не будет изменен

## Примеры кода


### Пример создания транзакции в методе createAsset
```kotlin
fun createAsset(request: AssetCreateRequest): AssetResponse {
    // Создание актива
    val asset = Asset(
        baseTicker = request.baseTicker,
        quoteTicker = request.quoteTicker,
        quantity = request.quantity
    )
    val savedAsset = assetRepository.save(asset)

    // Создание BUY транзакции
    val transaction = Transaction(
        type = "BUY",
        symbol = request.baseTicker,
        quantity = request.quantity,
        price = BigDecimal("50000.00"), // захардкоженная цена
        date = LocalDateTime.now()
    )
    transactionRepository.save(transaction)

    return mapToResponse(savedAsset)
}
```

## Проверка работы

### Сценарий 1: Создание актива
1. POST /api/assets с body:
```json
{
  "baseTicker": "BTC",
  "quoteTicker": "USD",
  "quantity": 2.5
}
```
2. Проверь через GET /api/transactions что создалась BUY транзакция на 2.5 BTC

### Сценарий 2: Увеличение количества
1. Создай актив с quantity = 5
2. PUT /api/assets/{id} с body: `{"quantity": 8}`
3. Проверь что создалась BUY транзакция на 3 единицы

### Сценарий 3: Уменьшение количества
1. Создай актив с quantity = 10
2. PUT /api/assets/{id} с body: `{"quantity": 7}`
3. Проверь что создалась SELL транзакция на 3 единицы

### Сценарий 4: Удаление актива
1. Создай актив с quantity = 5
2. DELETE /api/assets/{id}
3. Проверь что создалась SELL транзакция на 5 единиц
4. Проверь что актив действительно удален

