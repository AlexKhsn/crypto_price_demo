package com.sanya.mg.sanyademo.notes.dto

data class UpdateNoteDto(
    val title: String?,
    val content: String?,
    val pinned: Boolean?,
)
