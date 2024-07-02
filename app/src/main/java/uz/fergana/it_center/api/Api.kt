package uz.fergana.it_center.api


import io.reactivex.Observable
import retrofit2.http.GET
import uz.fergana.it_center.model.AllCategoryModel
import uz.fergana.it_center.model.AllStudentModel
import uz.fergana.it_center.model.CategoryModel
import uz.fergana.it_center.model.CourceModel
import uz.fergana.it_center.model.DarslarModel
import uz.fergana.it_center.model.ImageItem
import uz.fergana.it_center.model.Notification
import uz.fergana.it_center.model.QuestionModel

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
    @GET("courses")
    fun getCourses(): Observable<ArrayList<CourceModel>>
}