package ajl.com.data.db

import ajl.com.data.entity.BookmarkEntity
import ajl.com.data.entity.VerseBookmarkEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface BookmarkDao {

    @Query("SELECT v.*,b._id as bookmark_id,b.verse_id as bookmarked,n.verse_id as noted,f.verse_id as hasFootnote,bf.foldername   FROM bookmarks b INNER JOIN  verses v ON  v._id=b.verse_id LEFT JOIN notes n ON v._id=n.verse_id LEFT JOIN footnotes f ON v._id=f.verse_id LEFT JOIN bookmark_folder bf ON b.folder_id = bf.id WHERE b.folder_id = :folderId ORDER BY b._id DESC")
    fun getVerseDescendingBookmarks(folderId: Int): List<VerseBookmarkEntity>

    @Query("SELECT v.*,b._id as bookmark_id,b.verse_id as bookmarked,n.verse_id as noted,f.verse_id as hasFootnote,bf.foldername   FROM bookmarks b INNER JOIN  verses v ON  v._id=b.verse_id LEFT JOIN notes n ON v._id=n.verse_id LEFT JOIN footnotes f ON v._id=f.verse_id LEFT JOIN bookmark_folder bf ON b.folder_id = bf.id WHERE b.folder_id = :folderId ORDER BY b._id ASC")
    fun getVerseAscendingBookmarks(folderId: Int): List<VerseBookmarkEntity>

    @Query("SELECT v.*,b._id as bookmark_id,b.verse_id as bookmarked,n.verse_id as noted,f.verse_id as hasFootnote,bf.foldername   FROM bookmarks b INNER JOIN  verses v ON  v._id=b.verse_id LEFT JOIN notes n ON v._id=n.verse_id LEFT JOIN footnotes f ON v._id=f.verse_id LEFT JOIN bookmark_folder bf ON b.folder_id = bf.id WHERE b.folder_id = :folderId ORDER BY b.addedon DESC")
    fun getDateDescendingBookmarks(folderId: Int): List<VerseBookmarkEntity>

    @Query("SELECT v.*,b._id as bookmark_id,b.verse_id as bookmarked,n.verse_id as noted,f.verse_id as hasFootnote,bf.foldername   FROM bookmarks b INNER JOIN  verses v ON  v._id=b.verse_id LEFT JOIN notes n ON v._id=n.verse_id LEFT JOIN footnotes f ON v._id=f.verse_id LEFT JOIN bookmark_folder bf ON b.folder_id = bf.id WHERE b.folder_id = :folderId ORDER BY b.addedon ASC")
    fun getDateAscendingBookmarks(folderId: Int): List<VerseBookmarkEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: BookmarkEntity): Long

    @Query("DELETE FROM bookmarks WHERE _id = :id")
    fun deleteBookmark(id: Int)

    @Query("SELECT COUNT(verse_id) FROM bookmarks")
    fun getNumberOfBookmarks(): Int


    @Query("DELETE FROM bookmarks WHERE folder_id = :id")
    fun deleteFolderBookmark(id: Int)
}