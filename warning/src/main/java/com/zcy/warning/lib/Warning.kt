package com.zcy.warning.lib

import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.ViewCompat
import java.lang.Exception
import java.lang.ref.WeakReference
import kotlin.math.log

/**
 * @author:         zhaochunyu
 * @description:    Warn helper class
 * @date:           2019/3/15
 */
class Warning {

    private fun log(e: String) {
        Log.e(this::class.java.simpleName, "${this::class.java.simpleName} $e")
    }

    private var warn: Warn? = null
    private val activityDecorView: ViewGroup?
        get() {
            return activityWeakReference?.get()?.window?.decorView as? ViewGroup
        }

    private var windowManager: WindowManager? = null

    init {


    }

    // after build
    fun show() {
        log("show")
        // why should run on ui thread ?
//        activityDecorView?.addView(warn)
        configWarn()

        windowManager?.also {
            log("addView")
            try {
                Handler().postDelayed({

                    // init layout params
                    val layoutParams = WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        0, 0,
                        PixelFormat.TRANSPARENT
                    )
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
                    layoutParams.gravity = Gravity.TOP

                    layoutParams.flags =
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or // 不获取焦点，以便于在弹出的时候 下层界面仍然可以进行操作
                                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR // 确保你的内容不会被装饰物(如状态栏)掩盖.
                    // popWindow的层级为 TYPE_APPLICATION_PANEL
////        TODO("adjust permission")
                    layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
                    layoutParams.windowAnimations = R.style.windowManager
                    it.addView(warn, layoutParams)
                }, 100)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        warn?.postDelayed({
            log("2${warn?.hasShowed}")
            if (warn?.hasShowed == true) {
                log("post delay remove view")
                windowManager?.removeViewImmediate(warn)
            }
        }, Warn.DISPLAY_TIME)


    }

    private fun configWarn() {
        warn?.setOnClickListener {
            log("1${warn?.hasShowed}")
            if (warn?.hasShowed == true) {
                log("click to remove view")
                windowManager?.removeViewImmediate(warn)
            }
        }
    }

    fun setActivity(activity: Activity) {
        activityWeakReference = WeakReference(activity)
        warn = Warn(activity)
        windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    }

    companion object {
        private var activityWeakReference: WeakReference<Activity>? = null

//        @JvmStatic
//        fun create(activity: Activity?): Warning {
//            if (activity == null) {
//                throw IllegalArgumentException("Activity cannot be null!")
//            }
//            val warning = Warning()
////            Warning.removeBefore(activity)
//            warning.setActivity(activity)
//            warning.warn = Warn(activity)
//
//            return warning
//        }
//
//        // TODO i want config it to support a show queue
//
//        @JvmStatic
//        fun removeBefore(activity: Activity?) {
//            (activity?.window?.decorView as? ViewGroup)?.let {
//                var child: Warn?
//                for (i in 0 until it.childCount) {
//                    child = if (it.getChildAt(i) is Warn) it.getChildAt(i) as Warn else null
//                    if (child != null && child.windowToken != null) {
//                        ViewCompat.animate(child).alpha(0f).withEndAction(
//                            getRemoveViewRunnable(
//                                child
//                            )
//                        )
//                    }
//                }
//            }
//        }
//
//        private fun getRemoveViewRunnable(childView: Warn?): Runnable {
//            return Runnable {
//                childView?.let {
//                    (childView.parent as? ViewGroup)?.removeView(childView)
//                }
//            }
//        }
    }
}