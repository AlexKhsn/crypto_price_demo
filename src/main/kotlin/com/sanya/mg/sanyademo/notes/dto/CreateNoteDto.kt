package com.sanya.mg.sanyademo.notes.dto

data class CreateNoteDto(
    val title: String,
    val content: String?,
    val pinned: Boolean?,
)
