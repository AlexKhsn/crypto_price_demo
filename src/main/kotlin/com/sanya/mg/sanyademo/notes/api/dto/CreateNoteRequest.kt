package com.sanya.mg.sanyademo.notes.api.dto

data class CreateNoteRequest(
    val title: String,
    val content: String?,
    val pinned: Boolean?,
)
