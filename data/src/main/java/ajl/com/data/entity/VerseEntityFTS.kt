package ajl.com.data.entity

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.FtsOptions

@Fts4(contentEntity = VerseEntity::class, tokenizer = FtsOptions.TOKENIZER_PORTER)
@Entity(tableName = "verseFts")
data class VerseEntityFTS(
    var verse: String
)