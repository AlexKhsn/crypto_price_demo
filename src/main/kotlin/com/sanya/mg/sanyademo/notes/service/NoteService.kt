package com.sanya.mg.sanyademo.notes.service

import com.sanya.mg.sanyademo.notes.dto.CreateNoteDto
import com.sanya.mg.sanyademo.notes.dto.NoteResponseDto
import com.sanya.mg.sanyademo.notes.dto.UpdateNoteDto
import com.sanya.mg.sanyademo.notes.entity.Note
import com.sanya.mg.sanyademo.notes.repository.NoteRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class NoteService(
    private val noteRepository: NoteRepository,
) {
    fun createNote(request: CreateNoteDto): NoteResponseDto {
        val note = Note(
            title = request.title,
            content = request.content,
            pinned = request.pinned,
        )

        val saved = noteRepository.save(note)

        return NoteResponseDto(
            saved.id!!,
            saved.title,
            saved.content,
            saved.pinned,
            saved.createdAt,
            saved.updatedAt,
        )
    }

    fun getAllNotes(): List<NoteResponseDto> {
        val notes = noteRepository.findAll()
        val sorted = notes.sortedWith(
            compareByDescending<Note> { it.pinned }
                .thenByDescending { it.createdAt },
        )

        return sorted.map {
            NoteResponseDto(
                it.id!!,
                it.title,
                it.content,
                it.pinned,
                it.createdAt,
                it.updatedAt,
            )
        }
    }

    fun getById(id: Long): NoteResponseDto {
        val note = noteRepository.findById(id).get()
        return NoteResponseDto(
            note.id!!,
            note.title,
            note.content,
            note.pinned,
            note.createdAt,
            note.updatedAt,
        )
    }

    fun updateNote(id: Long, request: UpdateNoteDto): NoteResponseDto {
        val note = noteRepository.findById(id).get()
        val updatedNote = Note(
            id = note.id,
            title = request.title ?: note.title,
            content = request.content ?: note.content,
            createdAt = note.createdAt,
            updatedAt = LocalDateTime.now(),
            pinned = request.pinned ?: note.pinned,
        )

        noteRepository.save(updatedNote)

        return NoteResponseDto(
            updatedNote.id!!,
            updatedNote.title,
            updatedNote.content,
            updatedNote.pinned,
            updatedNote.createdAt,
            updatedNote.updatedAt,
        )
    }
}
