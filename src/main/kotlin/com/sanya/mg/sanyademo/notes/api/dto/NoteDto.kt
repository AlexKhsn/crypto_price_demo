package com.sanya.mg.sanyademo.notes.api.dto

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class NoteDto(
    val id: Long,
    val title: String,
    val content: String?,
    val pinned: Boolean?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val daysAgo: Long = ChronoUnit.DAYS.between(createdAt, LocalDateTime.now()),
    val preview: String? = content?.take(100),
)
