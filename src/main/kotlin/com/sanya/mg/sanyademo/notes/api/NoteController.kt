package com.sanya.mg.sanyademo.notes.api

import com.sanya.mg.sanyademo.notes.api.dto.CreateNoteRequest
import com.sanya.mg.sanyademo.notes.api.dto.NoteDto
import com.sanya.mg.sanyademo.notes.api.dto.UpdateNoteRequest
import com.sanya.mg.sanyademo.notes.service.NoteService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/notes")
class NoteController(
    private val noteService: NoteService,
) {
    @PostMapping
    fun addNote(
        @RequestBody
        request: CreateNoteRequest,
    ): NoteDto {
        val newNote = noteService.createNote(request)
        return newNote
    }

    @GetMapping
    fun getNotes(): List<NoteDto> {
        val notes = noteService.getAllNotes()
        return notes
    }

    @GetMapping("/{id}")
    fun getNoteById(
        @PathVariable id: Long,
    ): ResponseEntity<NoteDto> {
        try {
            val note = noteService.getById(id)
            return ResponseEntity.ok().body(note)
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    fun updateNote(
        @PathVariable id: Long,
        @RequestBody
        request: UpdateNoteRequest,
    ): ResponseEntity<NoteDto> {
        try {
            val updatedNote = noteService.updateNote(id, request)
            return ResponseEntity.ok().body(updatedNote)
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }
}
