package com.example.itcenter.model

import java.io.Serializable

data class DarslarModel(
    var _id           : String,
    var id           : Int,
    var lessonName   : String,
    var languageName : String,
    var videoLink    : String,
    var level        : String,
    var homework     : String,
    var createdAt    : String,
    var updatedAt    : String
): Serializable
