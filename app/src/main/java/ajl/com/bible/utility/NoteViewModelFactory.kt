package ajl.com.bible.utility

import ajl.com.bible.ui.note.NoteViewModel
import ajl.com.domain.usecases.GetNoteUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NoteViewModelFactory(private val getNoteUseCase: GetNoteUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteViewModel(getNoteUseCase) as T
    }


    /* fun create(): BooksViewModel {
         return create(getBooksUseCase)
     }*/
    /* fun create(getBooksUseCase: GetBooksUseCase): BooksViewModel*/
}