package uz.fergana.itcenter.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.fergana.itcenter.api.repository.Repository
import uz.fergana.itcenter.model.AllCategoryModel
import uz.fergana.itcenter.model.AllStudentModel
import uz.fergana.itcenter.model.CategoryModel
import uz.fergana.itcenter.model.DarslarModel
import uz.fergana.itcenter.model.ImageItem
import uz.fergana.itcenter.model.Notification
import uz.fergana.itcenter.model.QuestionModel
import uz.fergana.itcenter.room.AppDatabase

class MainViewModel: ViewModel() {
    val repository = Repository()


    val error = MutableLiveData<String>()
    val progress = MutableLiveData<Boolean>()
    val shimmer = MutableLiveData<Int>()
    val adsData = MutableLiveData<ArrayList<ImageItem>>()
    val allCategoryData = MutableLiveData<List<AllCategoryModel>>()
    val categoriesData = MutableLiveData<List<CategoryModel>>()
    val studentData = MutableLiveData<List<AllStudentModel>>()
    val userData = MutableLiveData<List<AllStudentModel>>()
    val lessonsData = MutableLiveData<ArrayList<DarslarModel>>()
    val questionData = MutableLiveData<ArrayList<QuestionModel>>()
    val notificationData = MutableLiveData<List<Notification>>()

    private val _buttonClicked = MutableLiveData<Event<Boolean>>()
    val buttonClicked: LiveData<Event<Boolean>> get() = _buttonClicked

    fun setButtonClicked(clicked: Boolean) {
        _buttonClicked.value = Event(clicked)
    }
    fun getOffers() {
        repository.getAds(error, adsData,progress,shimmer)
    }
    fun getAllCategories() {
        repository.getAllCategories(error, allCategoryData)
    }
    fun getCategories() {
        repository.getCategories(error, categoriesData)
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