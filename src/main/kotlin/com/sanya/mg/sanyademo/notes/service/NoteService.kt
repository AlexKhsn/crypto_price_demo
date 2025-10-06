package com.sanya.mg.sanyademo.notes.service

import com.sanya.mg.sanyademo.notes.api.dto.CreateNoteRequest
import com.sanya.mg.sanyademo.notes.api.dto.NoteDto
import com.sanya.mg.sanyademo.notes.api.dto.UpdateNoteRequest
import com.sanya.mg.sanyademo.notes.repository.NoteRepository
import com.sanya.mg.sanyademo.notes.repository.entity.Note
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class NoteService(
    private val noteRepository: NoteRepository,
) {
    fun createNote(request: CreateNoteRequest): NoteDto {
        val note = Note(
            title = request.title,
            content = request.content,
            isPinned = request.pinned,
        )

        val saved = noteRepository.save(note)

        return NoteDto(
            saved.id!!,
            saved.title,
            saved.content,
            saved.isPinned,
            saved.createdAt,
            saved.updatedAt,
        )
    }

    fun getAllNotes(): List<NoteDto> {
        val notes = noteRepository.findAll()
        val sorted = notes.sortedWith(
            compareByDescending<Note> { it.isPinned }
                .thenByDescending { it.createdAt },
        )

        return sorted.map {
            NoteDto(
                it.id!!,
                it.title,
                it.content,
                it.isPinned,
                it.createdAt,
                it.updatedAt,
            )
        }
    }

    fun getById(id: Long): NoteDto {
        val note = noteRepository.findById(id).get()
        return NoteDto(
            note.id!!,
            note.title,
            note.content,
            note.isPinned,
            note.createdAt,
            note.updatedAt,
        )
    }

    fun updateNote(id: Long, request: UpdateNoteRequest): NoteDto {
        val note = noteRepository.findById(id).get()
        val updatedNote = Note(
            id = note.id,
            title = request.title ?: note.title,
            content = request.content ?: note.content,
            createdAt = note.createdAt,
            updatedAt = LocalDateTime.now(),
            isPinned = request.pinned ?: note.isPinned,
        )

        noteRepository.save(updatedNote)

        return NoteDto(
            updatedNote.id!!,
            updatedNote.title,
            updatedNote.content,
            updatedNote.isPinned,
            updatedNote.createdAt,
            updatedNote.updatedAt,
        )
    }
}
