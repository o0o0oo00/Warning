package com.zcy.warning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.zcy.warning.lib.Warning

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        Handler().postDelayed({
            Warning.create(this).show()
        }, 200)
    }
}
