package uz.fergana.itcenter.model

data class GroupModel(
    val name: String,
    val students: ArrayList<AllStudentModel>,
    val group: String
)
