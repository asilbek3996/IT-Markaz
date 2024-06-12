package uz.fergana.itcenter.api


import io.reactivex.Observable
import retrofit2.http.GET
import uz.fergana.itcenter.model.AllCategoryModel
import uz.fergana.itcenter.model.AllStudentModel
import uz.fergana.itcenter.model.CategoryModel
import uz.fergana.itcenter.model.DarslarModel
import uz.fergana.itcenter.model.ImageItem
import uz.fergana.itcenter.model.Notification
import uz.fergana.itcenter.model.QuestionModel

interface Api {
    @GET("ads")
    fun getAds(): Observable<ArrayList<ImageItem>>
    @GET("category")
    fun getAllCategories(): Observable<List<AllCategoryModel>>
    @GET("category")
    fun getCategories(): Observable<List<CategoryModel>>
    @GET("students")
    fun getStudent(): Observable<List<AllStudentModel>>
    @GET("students")
    fun getStudent2(): Observable<ArrayList<AllStudentModel>>
    @GET("lessons")
    fun getLessons(): Observable<ArrayList<DarslarModel>>
    @GET("questions")
    fun getQuestions(): Observable<ArrayList<QuestionModel>>
    @GET("notification")
    fun getNotification(): Observable<ArrayList<Notification>>
}