package com.sanya.mg.sanyademo.repository.entity

import com.sanya.mg.sanyademo.common.TransactionType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "transactions")
data class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val type: TransactionType,
    @Column(nullable = false)
    val symbol: String,
    @Column(nullable = false)
    val quantity: BigDecimal,
    @Column(nullable = false)
    val date: LocalDate = LocalDate.now(),
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,
)
