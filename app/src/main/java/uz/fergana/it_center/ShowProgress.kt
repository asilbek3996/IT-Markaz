package uz.fergana.it_center

interface ShowProgress {
    interface View {
        fun showProgressBar()
        fun hideProgressBar()
        fun refresh()
        fun again()
    }
}