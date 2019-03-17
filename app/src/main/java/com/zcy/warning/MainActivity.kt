package com.zcy.warning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zcy.warning.lib.Warning

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun show(view: View) {
//        Warning.create(this).show()
        val warning = Warning()
        warning.setActivity(this)
        warning.show()
    }
}
