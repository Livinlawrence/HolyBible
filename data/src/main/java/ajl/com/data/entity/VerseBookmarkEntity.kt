package ajl.com.data.entity


data class VerseBookmarkEntity(

    var _id: Int,
    var bookmark_id: Int,
    var bookmarked: Int,
    var hasFootnote: Int,
    var noted: Int,
    var testament: String,
    var book: String,
    var book_no: Int,
    var chapter: Int,
    var actual_chapter_no: Int,
    var verse_no: Int,
    var verse: String,
    var heading: String,
    var foldername: String?,
    var colorTag: Int?
)