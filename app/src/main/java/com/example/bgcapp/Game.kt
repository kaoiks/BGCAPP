

package com.example.bgcapp
import java.util.*

class Game {

    var title:String? = null
    var originalTitle:String? = null
    var releaseYear: Int? = null
    var bggId: Long = 0
    var rankingPosition: Int = 0
    var thumbnail: String? = null

    constructor(title:String,  originalTitle:String, releaseYear: Int,  bggId: Long, rankingPosition: Int, thumbnail: String){
        this.title = title
        this.originalTitle = originalTitle
        this.releaseYear = releaseYear
        this.bggId = bggId
        this.rankingPosition = rankingPosition
        this.thumbnail = thumbnail
    }

}