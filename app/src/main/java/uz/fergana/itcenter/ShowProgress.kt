package uz.fergana.itcenter

interface ShowProgress {
    interface View {
        fun showProgressBar()
        fun hideProgressBar()
        fun refresh()
        fun again()
    }
}