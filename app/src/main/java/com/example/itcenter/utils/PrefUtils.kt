package com.example.itcenter.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.itcenter.model.AllStudentModel
import com.example.itcenter.model.DarslarModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PrefUtils(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    private val gson = Gson()

    // Int ma'lumotni arrayga qo'shib saqlash funksiyasi
    fun saveFavorite(key: String, value: DarslarModel) {
        val intArray = getFavorite(key)?.toMutableList() ?: mutableListOf()
        if (intArray.filter { it.id == value.id }.firstOrNull() != null) {
            intArray.remove(value)
            val json = gson.toJson(intArray)
            editor.putString(key, json)
            editor.apply()
        } else {
            intArray.add(value)
            val json = gson.toJson(intArray)
            editor.putString(key, json)
            editor.apply()
        }

    }

    // Saqlangan arrayni olish funksiyasi
    fun clearFavorite() {
        val intArray = getFavorite(Constants.favorite)?.toMutableList() ?: mutableListOf()
        intArray.clear()
        val json = gson.toJson(intArray)
        editor.putString(Constants.favorite, json)
        editor.apply()
    }

    // Saqlangan arrayni olish funksiyasi
    fun getFavorite(key: String): ArrayList<DarslarModel>? {
        val json = sharedPreferences.getString(key, null) ?: return null
        val type = object : TypeToken<ArrayList<DarslarModel>>() {}.type
        val intList: ArrayList<DarslarModel> = gson.fromJson(json, type)
        return ArrayList(intList)
    }

    fun checkFavorite(key: String, value: Int): Boolean {
        val intArray = getFavorite(key)?.toMutableList() ?: mutableListOf()
        return intArray.firstOrNull { it.id == value } != null
    }

    fun setStudent(student: AllStudentModel) {
        editor.putInt(Constants.ID, student.id)
        editor.putString(Constants.fullName, student.fullName)
        editor.putString(Constants.group, student.group)
        editor.putString(Constants.g, student.group)
        editor.apply()
    }

    fun setGroup(group: String) {
        editor.putString(Constants.g, group)
        editor.apply()
    }

    fun getID(): Int {
        return sharedPreferences.getInt(Constants.ID, -1)
    }

    fun get_ID(): String? {
        return sharedPreferences.getString(Constants._ID, "123")
    }

    fun getStudent(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun setLevel(value: String) {
        editor.putString(Constants.level, value)
        editor.apply()
    }

    fun setQuizLevel(value: Int) {
        editor.putInt(Constants.quizLevel, value)
        editor.apply()
    }

    fun setLesson(value: Int) {
        editor.putInt(Constants.lesson, value)
        editor.apply()
    }

    fun getLevel(): String? {
        return sharedPreferences.getString(Constants.level, "begin")
    }

    fun getQuizLevel(): Int {
        return sharedPreferences.getInt(Constants.quizLevel, 1)
    }

    fun getLesson(): Int {
        return sharedPreferences.getInt(Constants.lesson, 0)
    }

    fun clear() {
        editor.clear()
        editor.apply()
    }

    fun setNotification(value: List<AllStudentModel>) {
        val intArray = getNotification()?.toMutableList() ?: mutableListOf()
        intArray.addAll(value)
        val json = gson.toJson(intArray)
        editor.putString(Constants.notification, json)
        editor.apply()
    }

    fun getNotification(): ArrayList<AllStudentModel>? {
        val json = sharedPreferences.getString(Constants.notification, null) ?: return null
        val type = object : TypeToken<ArrayList<AllStudentModel>>() {}.type
        val intList: ArrayList<AllStudentModel> = gson.fromJson(json, type)
        return ArrayList(intList)
    }
}