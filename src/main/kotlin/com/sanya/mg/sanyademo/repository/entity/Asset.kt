package com.sanya.mg.sanyademo.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "assets")
data class Asset(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    val baseTicker: String,
    @Column(nullable = false)
    val quoteTicker: String,
    @Column(nullable = false, precision = 20, scale = 8)
    val quantity: BigDecimal,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
)
