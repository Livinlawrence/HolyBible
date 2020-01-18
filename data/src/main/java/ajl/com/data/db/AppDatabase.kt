package ajl.com.data.db

import ajl.com.data.entity.*
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.runBlocking
import java.io.IOException

@Database(
    entities = [BookEntity::class, BookChapterDetailEntity::class, VerseEntity::class,
        BookmarkEntity::class, NoteEntity::class, FootnoteEntity::class,
        BookmarkFolderEntity::class, NotesFolderEntity::class, ColorTagEntity::class, SearchHistoryEntity::class, VerseEntityFTS::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun bookChapterDetailDao(): BookChapterDetailsDao
    abstract fun verseDao(): VerseDao
    abstract fun bookMarkDao(): BookmarkDao
    abstract fun noteDao(): NoteDao
    abstract fun footNoteDao(): FoteNoteDao
    abstract fun folderDao(): FolderDao
    abstract fun colorTagDao(): ColorTagDao
    abstract fun searchHistoryDao(): SearchHistoryDao


    companion object {

        @JvmField
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.e("DB", "MIGRATING FROM SQLITE TO ROOM 1-->2")
            }
        }
        val MIGRATION_2_3: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

            }
        }

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context?) = instance
            ?: synchronized(LOCK) {
                runBlocking {
                    // launch a new coroutine in background and continue
                    checkDb(context)
                }

                Log.e("DB", "CREATING ROOM DB")
                instance ?: buildDatabase(
                    context!!
                ).also { instance = it }
            }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "${DBOpenHelper.DATABASE_NAME}.db"
        ).addMigrations(
            MIGRATION_1_2, MIGRATION_2_3
        )
            .build()


        private fun checkDb(context: Context?) {
            val helper = DBOpenHelper(context)
            try {
                helper.importDB()
            } catch (ioe: IOException) {
                throw Error("Unable to create database")
            }

        }
    }
}