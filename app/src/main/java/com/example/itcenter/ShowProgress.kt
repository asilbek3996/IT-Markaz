package com.example.itcenter

interface ShowProgress {
    interface View {
        fun showProgressBar()
        fun hideProgressBar()
        fun refresh()
    }
}