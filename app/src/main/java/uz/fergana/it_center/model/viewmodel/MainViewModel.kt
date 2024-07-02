package uz.fergana.it_center.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.fergana.it_center.api.repository.Repository
import uz.fergana.it_center.model.AllCategoryModel
import uz.fergana.it_center.model.AllStudentModel
import uz.fergana.it_center.model.CategoryModel
import uz.fergana.it_center.model.CourceModel
import uz.fergana.it_center.model.DarslarModel
import uz.fergana.it_center.model.ImageItem
import uz.fergana.it_center.model.Notification
import uz.fergana.it_center.model.QuestionModel
import uz.fergana.it_center.room.AppDatabase

class MainViewModel: ViewModel() {
    val repository = Repository()


    val error = MutableLiveData<String>()
    val progress = MutableLiveData<Boolean>()
    val shimmer = MutableLiveData<Int>()
    val adsData = MutableLiveData<ArrayList<ImageItem>>()
    val allCategoryData = MutableLiveData<List<AllCategoryModel>>()
    val categoriesData = MutableLiveData<List<CategoryModel>>()
    val courceData = MutableLiveData<List<CourceModel>>()
    val studentData = MutableLiveData<List<AllStudentModel>>()
    val userData = MutableLiveData<List<AllStudentModel>>()
    val lessonsData = MutableLiveData<ArrayList<DarslarModel>>()
    val questionData = MutableLiveData<ArrayList<QuestionModel>>()
    val notificationData = MutableLiveData<List<Notification>>()


    fun getOffers() {
        repository.getAds(error, adsData,progress,shimmer)
    }
    fun getCategories() {
        repository.getCategories(error, categoriesData,progress)
    }
    fun getCource() {
        repository.getCource(error, courceData,progress)
    }
    fun getStudent() {
        repository.getStudent(error, studentData,progress)
    }

    fun getAllStudent() {
        repository.getStudent(error, userData,progress)
    }
    fun getLessons() {
        repository.getLessons(error, lessonsData, progress)
    }
    fun getQuestions() {
        repository.getQuestions(error, questionData)
    }
    fun clear() {
        repository.clearDisposable()
    }

    fun insertAllDBStudents(items: List<AllStudentModel>) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getDatabase().getStudentDao().deleteAll()
            AppDatabase.getDatabase().getStudentDao().insertAllStudent(items)
        }
    }
    fun getAllStudents(){
        CoroutineScope(Dispatchers.Main).launch {
            studentData.value = withContext(Dispatchers.IO){AppDatabase.getDatabase().getStudentDao().getAllStudents()}
        }
    }
    fun insertAllDBCategories(items: List<CategoryModel>) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getDatabase().getCategoryDao().deleteAllCategory()
            AppDatabase.getDatabase().getCategoryDao().insertAll(items)
        }
    }
    fun getAllDBCategory(){
        CoroutineScope(Dispatchers.Main).launch {
            categoriesData.value = withContext(Dispatchers.IO){AppDatabase.getDatabase().getCategoryDao().getAllCategory()}
        }
    }
    fun insertAllDBCource(items: List<CourceModel>) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getDatabase().getCourceDao().deleteAllCource()
            AppDatabase.getDatabase().getCourceDao().insertAll(items)
        }
    }
    fun getAllDBCource(){
        CoroutineScope(Dispatchers.Main).launch {
            courceData.value = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase().getCourceDao()
                    .getAllCource()
            }
        }
    }

//
//    init {
//        startPolling()
//    }
//
//    private fun startPolling() {
//        viewModelScope.launch(Dispatchers.IO) {
//            while (true) {
//                getNotification()
//                delay(1000)
//            }
//        }
//    }
//    fun getNotification() {
//        repository.getNotification(error, notificationData)
//    }
}