package com.sanya.mg.sanyademo.notes.repository

import com.sanya.mg.sanyademo.notes.repository.entity.Note
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NoteRepository : JpaRepository<Note, Long> {
    fun findByPinnedTrue(): List<Note>

    fun findNotesByTitleContainsIgnoreCase(subtitle: String): List<Note>

    fun findNotesByOrderByCreatedAtDesc(): List<Note>
}
