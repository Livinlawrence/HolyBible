package ajl.com.data.db

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DBOpenHelper(val context: Context?) {

    companion object {

        val DATABASE_NAME = "bible"

    }


    @Throws(IOException::class)
    fun importDB(): Boolean {


        Log.e("DB ", "IMPORT TRIGGERED")
        return open()

    }


    private fun open(): Boolean {
        val dbFile = context?.getDatabasePath("$DATABASE_NAME.db")
        if (!dbFile!!.exists()) {
            Log.e("DB ", "FILE DOES NOT EXIST")
            try {
                copyDatabase(dbFile)
            } catch (e: IOException) {
                throw RuntimeException("Error opening db")
            }
        }
        return true
    }

    private fun copyDatabase(dbFile: File?) {
        val iss = context!!.assets.open("$DATABASE_NAME.db")
        val os = FileOutputStream(dbFile)

        //val buffer = ByteArray(1024)
        /*  generateSequence{  val i = iss.read(buffer); if (i<0) null else i }
              .forEach {
                  os.write(buffer, 0, i)
              }*/


        val buffer = ByteArray(1024)
        while (iss.read(buffer) > 0) {
            os.write(buffer)
        }
        os.flush()
        os.close()
        iss.close()

        Log.e("DB ", "COPY COMPLETED")
    }


}