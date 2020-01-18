package ajl.com.data.entity


data class VerseNoteEntity(
    var _id: Int,
    var note_id: Int,
    var verse_id: Int,
    var testament: String,
    var book: String,
    var book_no: Int,
    var chapter: Int,
    var actual_chapter_no: Int,
    var verse_no: Int,
    var verse: String,
    var heading: String,
    var note: String,
    var foldername: String

)