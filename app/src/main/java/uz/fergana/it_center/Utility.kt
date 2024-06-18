package uz.fergana.it_center

import android.content.Context
import android.widget.Toast


object Utility {
    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}
