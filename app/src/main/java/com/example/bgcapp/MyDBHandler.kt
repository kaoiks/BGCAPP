package com.example.bgcapp

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues

class MyDBHandler(context: Context, name: String?, factory: SQLiteDatabase. CursorFactory?, version: Int): SQLiteOpenHelper (context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object{
        private val DATABASE_VERSION =1
        private val DATABASE_NAME = "gamesDB.db"
        val TABLE_GAMES = "games"
        val COLUMN_ID = "_id"
        val COLUMN_TITLE = "title"
        val COLUMN_OGTITLE = "originalTitle"
        val COLUMN_YEAR = "year"
        val COLUMN_RANKING = "ranking"
        val COLUMN_THUMBNAIL = "thumbnail"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_GAMES_TABLE = ("CREATE TABLE" + TABLE_GAMES +
                "(" + COLUMN_ID + "INTEGER PRIMARY KEY,"+
                COLUMN_TITLE + " TEXT,"+
                COLUMN_OGTITLE + " TEXT,"+
                COLUMN_YEAR + " INTEGER,"+
                COLUMN_RANKING + " INTEGER,"+
                COLUMN_THUMBNAIL + " TEXT" + ")")

        db.execSQL(CREATE_GAMES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS"+ TABLE_GAMES )
        onCreate(db)

    }

    fun addGame(game:Game){
        val values = ContentValues()
        values.put(COLUMN_ID, game.bggId)
        values.put(COLUMN_TITLE, game.title)
        values.put(COLUMN_OGTITLE, game.originalTitle)
        values.put(COLUMN_YEAR, game.releaseYear)
        values.put(COLUMN_RANKING, game.rankingPosition)
        values.put(COLUMN_THUMBNAIL, game.thumbnail)
        val db = this.writableDatabase
        db.insert(TABLE_GAMES,null,values)
        db.close()
    }

    fun findGame(id: Long):Game?{
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_ID = $id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var game: Game? = null
        if (cursor.moveToFirst()){
            val title = cursor.getString(1)
            val originalTitle = cursor.getString(2)
            val year = cursor.getInt(3)
            val ranking = cursor.getInt(4)
            val thumbnail = cursor.getString(5)
            game = Game(title,originalTitle,year,id,ranking,thumbnail)
        }

        db.close()
        return game
    }

    fun deleteGame(id: Long):Boolean {
        var result = false
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_ID = $id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            db.delete(TABLE_GAMES, COLUMN_ID + " = ?", arrayOf(id.toString()))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }

}