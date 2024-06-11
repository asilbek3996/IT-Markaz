package com.example.itcenter.model

import java.io.Serializable

data class QuestionModel(
    val id: Int,
    val question: String?,
    val language: String?,
    var level: String,
    val a: String?,
    val b: String?,
    val c: String?,
    val d: String?,
    val right: String?,
    val count: Int,
    var clickedAnswer: String?,
    ):Serializable