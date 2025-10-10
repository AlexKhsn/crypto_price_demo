package com.sanya.mg.sanyademo.repository.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    val username: String,
    @Column(nullable = false)
    val email: String,
    @Column(nullable = false)
    val createdAt: LocalDateTime,
    @OneToMany(mappedBy = "user", cascade = [(CascadeType.ALL)])
    val assets: MutableList<AssetEntity> = mutableListOf(),
    @OneToMany(mappedBy = "user", cascade = [(CascadeType.ALL)])
    val transactions: MutableList<TransactionEntity> = mutableListOf(),
)
