# Задача 006: Пользователи и привязка данных

## Цель
Создать сущность User и привязать к ней активы (Asset) и транзакции (Transaction). Каждый пользователь должен иметь свой портфель активов и историю транзакций.

## Описание
Необходимо создать полноценную систему пользователей и установить связи между User, Asset и Transaction.

## Шаг 1: Создать Entity User

Создать сущность `User` в пакете `entity`:
- id: Long (автогенерация)
- username: String (уникальное)
- email: String (уникальное)
- createdAt: LocalDateTime

## Шаг 2: Добавить связи в Asset и Transaction

### Модификация Asset:
- Добавить поле `user: User` с аннотацией `@ManyToOne`
- Один пользователь может иметь много активов
- При удалении пользователя активы должны удаляться (cascade)

### Модификация Transaction:
- Добавить поле `user: User` с аннотацией `@ManyToOne`
- Один пользователь может иметь много транзакций
- При удалении пользователя транзакции должны удаляться (cascade)

## Шаг 3: Создать UserRepository

Создать репозиторий `UserRepository`:
- Наследуется от JpaRepository
- Метод для поиска по username
- Метод для поиска по email

## Шаг 4: Создать DTO для User

### UserCreateRequest:
- username: String
- email: String

### UserResponse:
- id: Long
- username: String
- email: String
- createdAt: LocalDateTime

## Шаг 5: Создать UserService

Методы:
- `createUser(request: UserCreateRequest): UserResponse` - создание пользователя
- `getUserById(id: Long): UserResponse` - получение по ID
- `getAllUsers(): List<UserResponse>` - получение всех пользователей
- `deleteUser(id: Long)` - удаление пользователя

## Шаг 6: Создать UserController

Эндпоинты:
- `POST /api/users` - создать пользователя
- `GET /api/users/{id}` - получить пользователя
- `GET /api/users` - получить всех пользователей
- `DELETE /api/users/{id}` - удалить пользователя

## Шаг 7: Модифицировать AssetService и AssetController

### Изменения в DTO:
- `AssetCreateRequest` добавить поле `userId: Long`
- `AssetResponse` добавить поле `userId: Long`

### Изменения в AssetService:
- При создании актива привязывать его к пользователю через userId
- При создании транзакции также привязывать к тому же пользователю

### Новые эндпоинты в AssetController:
- `GET /api/users/{userId}/assets` - получить все активы пользователя

## Шаг 8: Модифицировать TransactionController

### Новые эндпоинты:
- `GET /api/users/{userId}/transactions` - получить все транзакции пользователя
- `GET /api/users/{userId}/transactions/type/{type}` - транзакции пользователя по типу

## Пример кода

### User Entity:
```kotlin
@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    val username: String,

    @Column(unique = true, nullable = false)
    val email: String,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val assets: MutableList<Asset> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val transactions: MutableList<Transaction> = mutableListOf()
)
```

### Связь в Asset:
```kotlin
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", nullable = false)
val user: User
```

## Проверка работы

### Сценарий 1: Создание пользователя
1. POST /api/users с body:
```json
{
  "username": "john_doe",
  "email": "john@example.com"
}
```
2. Получить userId из ответа

### Сценарий 2: Создание актива для пользователя
1. POST /api/assets с body:
```json
{
  "userId": 1,
  "baseTicker": "BTC",
  "quoteTicker": "USD",
  "quantity": 5.0
}
```
2. Проверить что создался актив и BUY транзакция, оба привязаны к пользователю

### Сценарий 3: Получение данных пользователя
1. GET /api/users/{userId}/assets - должен вернуть все активы пользователя
2. GET /api/users/{userId}/transactions - должен вернуть все транзакции

### Сценарий 4: Удаление пользователя
1. Создать пользователя с активами и транзакциями
2. DELETE /api/users/{userId}
3. Проверить что пользователь, его активы и транзакции удалены из БД

## Технические требования
- Используй `@Transactional` для методов модификации данных
- Обрабатывай случаи когда пользователь не найден
- Валидируй уникальность username и email
- При создании актива проверяй существование пользователя с данным userId
