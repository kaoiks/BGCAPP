package com.example.bgcapp

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MyDBHandler(context: Context, name: String?, factory: SQLiteDatabase. CursorFactory?, version: Int): SQLiteOpenHelper (context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object{
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "gamesDB.db"

        val TABLE_USERDATA = "useradata"
        val TABLE_GAMES = "games"
        val TABLE_GAMES_RANKING = "gamesranking"

        val TABLE_EXTENSIONS = "extensions"
        val TABLE_EXTENSIONS_RANKING = "extensionsranking"
        val COLUMN_ID = "_id"
        val COLUMN_TITLE = "title"
        val COLUMN_OGTITLE = "originalTitle"
        val COLUMN_YEAR = "year"
        val COLUMN_RANKING = "ranking"
        val COLUMN_THUMBNAIL = "thumbnail"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_GAMES_TABLE = ("CREATE TABLE " + TABLE_GAMES +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"+
                COLUMN_TITLE + " TEXT,"+
                COLUMN_OGTITLE + " TEXT,"+
                COLUMN_YEAR + " INTEGER,"+
                COLUMN_RANKING + " INTEGER,"+
                COLUMN_THUMBNAIL + " TEXT" + ")")

        db.execSQL(CREATE_GAMES_TABLE)


        val CREATE_EXTENSIONS_TABLE = ("CREATE TABLE " + TABLE_EXTENSIONS +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"+
                COLUMN_TITLE + " TEXT,"+
                COLUMN_OGTITLE + " TEXT,"+
                COLUMN_YEAR + " INTEGER,"+
                COLUMN_RANKING + " INTEGER,"+
                COLUMN_THUMBNAIL + " TEXT" + ")")

        db.execSQL(CREATE_EXTENSIONS_TABLE)

        val CREATE_GAMES_RANKING_TABLE = ("CREATE TABLE " + TABLE_GAMES_RANKING +

                "(" + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COLUMN_ID + " INTEGER,"+
                COLUMN_RANKING + " INTEGER,"+
                "date" + " DATE" + ")")

        db.execSQL(CREATE_GAMES_RANKING_TABLE)


        val CREATE_EXTENSIONS_RANKING_TABLE = ("CREATE TABLE " + TABLE_EXTENSIONS_RANKING +

                "(" + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COLUMN_ID + " INTEGER,"+
                COLUMN_RANKING + " INTEGER,"+
                "date" + " DATE" + ")")

        db.execSQL(CREATE_EXTENSIONS_RANKING_TABLE)


        val CREATE_DATA_TABLE = ("CREATE TABLE " + TABLE_USERDATA +

                "(" + "user" + " TEXT,"+
                "lastsync" + " DATE"+
                 ")")

        db.execSQL(CREATE_DATA_TABLE)


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS"+ TABLE_GAMES )
        db.execSQL("DROP TABLE IF EXISTS"+ TABLE_GAMES_RANKING )
        onCreate(db)

    }

    fun addGame(game:Game){
        try {
            var values = ContentValues()
            values.put(COLUMN_ID, game.bggId)
            values.put(COLUMN_TITLE, game.title)
            values.put(COLUMN_OGTITLE, game.originalTitle)
            values.put(COLUMN_YEAR, game.releaseYear)
            values.put(COLUMN_RANKING, game.rankingPosition)
            values.put(COLUMN_THUMBNAIL, game.thumbnail)
            val db = this.writableDatabase
            db.insert(TABLE_GAMES, null, values)

            values = ContentValues()
            values.put(COLUMN_ID, game.bggId)
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            val currentDate = sdf.format(Date())


            values.put( "date", currentDate)
            values.put(COLUMN_RANKING, game.rankingPosition)
            val res =  db.insert(TABLE_GAMES_RANKING, null, values)

            //db.close()
        }
        catch (e: Exception){

        }
    }

    fun addExpansion(game:Game){
        try {
            var values = ContentValues()
            values.put(COLUMN_ID, game.bggId)
            values.put(COLUMN_TITLE, game.title)
            values.put(COLUMN_OGTITLE, game.originalTitle)
            values.put(COLUMN_YEAR, game.releaseYear)
            values.put(COLUMN_RANKING, game.rankingPosition)
            values.put(COLUMN_THUMBNAIL, game.thumbnail)
            val db = this.writableDatabase
            db.insert(TABLE_EXTENSIONS, null, values)


        }
        catch (e: Exception){

        }
    }

    fun getHistoryofGame(id: Long):MutableList<String>{
        var count = 0
        val query = "SELECT date, $COLUMN_RANKING FROM $TABLE_GAMES_RANKING WHERE $COLUMN_ID = $id ORDER BY date desc"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        val rankingList: MutableList<String> = mutableListOf()
        if (cursor.moveToFirst()){
            val date = cursor.getString(0)
            val rankingPosition = cursor.getInt(1)
            rankingList.add("$date%$rankingPosition")
            //game = Game(title,originalTitle,year,cursor.getLong(0),ranking,thumbnail)
            //listofGames.add(game)
        }
        while (cursor.moveToNext()){
            val date = cursor.getString(0)
            val rankingPosition = cursor.getInt(1)
            rankingList.add("$date%$rankingPosition")
            //game = Game(title,originalTitle,year,cursor.getLong(0),ranking,thumbnail)
            //listofGames.add(game)
        }

        cursor.close()

        return rankingList


    }

    fun countGames():Int{
        var count = 0
        val query = "SELECT * FROM $TABLE_GAMES"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)


        return cursor.count
    }

    fun countExtensions():Int{
        var count = 0
        val query = "SELECT * FROM $TABLE_EXTENSIONS"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

//        while(cursor.moveToNext()){
//            ++count
//        }

        return cursor.count
    }

    fun getGameList(mode: Int):MutableList<Game>{


        val query = "SELECT * FROM $TABLE_GAMES"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var game: Game? = null

        val listofGames: MutableList<Game> = mutableListOf()
        if (cursor.moveToFirst()){
            val title = cursor.getString(1)
            val originalTitle = cursor.getString(2)
            val year = cursor.getInt(3)
            val ranking = cursor.getInt(4)
            val thumbnail = cursor.getString(5)
            game = Game(title,originalTitle,year,cursor.getLong(0),ranking,thumbnail)
            listofGames.add(game)
        }
        while (cursor.moveToNext()){
            val title = cursor.getString(1)
            val originalTitle = cursor.getString(2)
            val year = cursor.getInt(3)
            val ranking = cursor.getInt(4)
            val thumbnail = cursor.getString(5)
            game = Game(title,originalTitle,year,cursor.getLong(0),ranking,thumbnail)
            listofGames.add(game)
        }

        cursor.close()
        //db.close()
        return listofGames
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

        cursor.close()
        //db.close()
        return game
    }

    fun findExpansion(id: Long):Game?{
        val query = "SELECT * FROM $TABLE_EXTENSIONS WHERE $COLUMN_ID = $id"
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

        cursor.close()
        //db.close()
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
        cursor.close()
        //db.close()
        return result
    }


    fun deleteExpansion(id: Long):Boolean {
        var result = false
        val query = "SELECT * FROM $TABLE_EXTENSIONS WHERE $COLUMN_ID = $id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            db.delete(TABLE_EXTENSIONS, COLUMN_ID + " = ?", arrayOf(id.toString()))
            cursor.close()
            result = true
        }
        cursor.close()
        //db.close()
        return result
    }

    fun setUser(username: String) {



        var values = ContentValues()
        values.put("user", username)

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val currentDate = sdf.format(Date())
        values.put("lastsync", currentDate)

        val db = this.writableDatabase
        db.insert(TABLE_USERDATA, null, values)

    }


    fun updateSync() {



        var values = ContentValues()

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val currentDate = sdf.format(Date())
        values.put("lastsync", currentDate)

        val db = this.writableDatabase
        db.update(TABLE_USERDATA,values,null,null)


    }


    fun checkSync(): String? {
        var result = false
        val query = "SELECT * FROM $TABLE_USERDATA"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val user = cursor.getString(1)
            cursor.close()
            return user
        }
        //db.close()
        return null
    }

    fun checkUser(): String? {
        var result = false
        val query = "SELECT * FROM $TABLE_USERDATA"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val user = cursor.getString(0)
            cursor.close()
            return user
        }

        //db.close()
        return null
    }



}