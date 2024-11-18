package com.note.note.domain.enums

enum class BranchState(val code: Int) {
    TODO(0),
    COMMIT(1),
    MERGE(2);

    fun toNoteState(): NoteState {
        return if(this == MERGE) NoteState.DONE else NoteState.TODO
    }
}

fun Int.toBranchState(): BranchState {
    return BranchState.entries.find { it.code == this } ?: BranchState.TODO
}
