package uz.fergana.it_center.model

data class GroupModel(
    val name: String,
    val students: ArrayList<AllStudentModel>,
    val group: String
)
