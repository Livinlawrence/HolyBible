package ajl.com.bible.utility

import android.os.Environment
import java.io.File
import java.io.FileWriter
import java.io.IOException

object IOUtility {
    fun createFileWithContent(dir: String, fileName: String, stringBody: String): String? {
        try {
            val root = File(
                Environment.getExternalStorageDirectory(),
                dir
            )
            if (!root.exists()) {
                root.mkdirs()
            }
            val textFile = File(root, "$fileName.txt")
            val writer = FileWriter(textFile)
            writer.append(stringBody)
            writer.flush()
            writer.close()
            return textFile.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

}