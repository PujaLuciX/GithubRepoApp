package com.assignment.githubrepoapp.cache
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.assignment.githubrepoapp.data.model.RepoListModel

class DatabaseOpenHelper(context: Context?) :  SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    override fun onOpen(db: SQLiteDatabase?) {
        onCreate(db)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $REPO_TABLE (" +
            "$COLUMN_NAME TEXT," +
                " $COLUMN_AUTHOR TEXT," +
                " $COLUMN_AVATAR TEXT," +
                " $COLUMN_DESCRIPTION TEXT,"+
                " $COLUMN_LANGUAGE TEXT,"+
                " $COLUMN_LANGUAGE_COLOR TEXT,"+
                " $COLUMN_STARS INT,"+
                " $COLUMN_FORKS INT);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun saveReposToDb(repoListModel: RepoListModel) {
        val db = writableDatabase
        db.insert(
            REPO_TABLE,
            null,
            ContentValues().apply {
                put(COLUMN_NAME, repoListModel.name)
                put(COLUMN_AUTHOR, repoListModel.author)
                put(COLUMN_AVATAR, repoListModel.avatar)
                put(COLUMN_DESCRIPTION, repoListModel.description)
                put(COLUMN_LANGUAGE, repoListModel.language)
                put(COLUMN_LANGUAGE_COLOR, repoListModel.languageSymbolColor)
                put(COLUMN_STARS, repoListModel.stars)
                put(COLUMN_FORKS, repoListModel.forks)
            }
        )
        db.close()
    }

    fun getReposFromDb(): ArrayList<RepoListModel> {
        val queryParams = "SELECT * FROM $REPO_TABLE"
        val db = readableDatabase
        val cursor = db.rawQuery(queryParams, null)
        val repoListModels = ArrayList<RepoListModel>()
        try {
            if (cursor.moveToFirst()) {
                do {
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                    val author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR))
                    val avatar = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AVATAR))
                    val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                    val language = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LANGUAGE))
                    val languageColor = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LANGUAGE_COLOR))
                    val stars = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STARS))
                    val forks = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FORKS))
                    repoListModels.add(RepoListModel(name, author, avatar, description, language, languageColor, stars, forks))
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            // Nothing to do.
        }
        cursor.close()
        db.close()
        return repoListModels
    }

    fun deleteDb() {
        val db = writableDatabase
        db.delete(REPO_TABLE, null, null)
        db.close()
    }


    companion object {
        private val DATABASE_NAME = "REPO_DATA"
        private val DATABASE_VERSION = 1
        private val REPO_TABLE = "REPO_TABLE"
        private const val COLUMN_NAME = "Col_Name"
        private const val COLUMN_AUTHOR = "Col_Author"
        private const val COLUMN_AVATAR = "Col_Avatar"
        private const val COLUMN_DESCRIPTION = "Col_Description"
        private const val COLUMN_LANGUAGE = "Col_Language"
        private const val COLUMN_LANGUAGE_COLOR = "Col_Language_Color"
        private const val COLUMN_STARS = "Col_Stars"
        private const val COLUMN_FORKS = "Col_Forks"
    }
}