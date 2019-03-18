package com.zcy.warning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.os.postDelayed
import com.zcy.warning.lib.Warning

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun show(view: View) {
        Warning.create(this).show()
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 800)
    }

    fun show2(view: View){
        Warning.create(this).config {
            setTitle("title")
            setText("text")
            setWarnBackgroundColor(resources.getColor(R.color.colorAccent))
        }.show()
    }
}
