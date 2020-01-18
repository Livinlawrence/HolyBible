package ajl.com.data.db

import ajl.com.data.entity.BookmarkEntity
import ajl.com.data.entity.VerseBookmarkEntity
import ajl.com.data.entity.VerseEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VerseDao {

    @Query("SELECT * FROM verses WHERE book_no == :bookId")
    fun getVerse(bookId: Int): List<VerseEntity>

    @Query("SELECT v.*,b._id as bookmark_id,b.verse_id as bookmarked,n.verse_id as noted,f.verse_id as hasFootnote,bf.foldername,ta.color_tag as colorTag FROM verses v LEFT JOIN  bookmarks b ON  v._id=b.verse_id LEFT JOIN notes n ON v._id=n.verse_id LEFT JOIN footnotes f ON v._id=f.verse_id LEFT JOIN bookmark_folder bf ON b.folder_id = bf.id LEFT JOIN tags ta ON v._id = ta.verseid WHERE book_no == :bookId AND chapter ==:chapterNo ORDER BY v.verse_no ASC")
    fun getVerseOfChapter(bookId: Int, chapterNo: Int): List<VerseBookmarkEntity>

    @Query("SELECT v.*,b._id as bookmark_id,b.verse_id as bookmarked,n.verse_id as noted,f.verse_id as hasFootnote,bf.foldername,ta.color_tag as colorTag  FROM verses v LEFT JOIN  bookmarks b ON  v._id=b.verse_id LEFT JOIN notes n ON v._id=n.verse_id LEFT JOIN footnotes f ON v._id=f.verse_id LEFT JOIN bookmark_folder bf ON b.folder_id = bf.id LEFT JOIN tags ta ON v._id = ta.verseid WHERE  verse_no>1 AND (verse LIKE '%' ||:characters || '%' OR book LIKE '%' ||:characters || '%')  ORDER BY v.book_no ASC LIMIT 1000")
    fun searchVerse(characters: String): List<VerseBookmarkEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookmark(entity: BookmarkEntity): Long


    //SELECT v.*,b.verse_no as bookMarked FROM verses v LEFT JOIN  bookmarks b ON  v._id=b.verse_no ORDER BY v.book_no ASC
}