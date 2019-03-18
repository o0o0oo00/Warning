package com.zcy.warning.lib

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.layout_warn.view.*
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

    private lateinit var warn: Warn
    private var hasPermission = false

    private var windowManager: WindowManager? = null

    // after build
    fun show() {
        windowManager?.also {
            try {
                activityWeakReference?.get()?.let { activity ->
                    if (activity.isFinishing || activity.isDestroyed) {
                        return@also
                    } else {
                        it.addView(warn, initLayoutParameter())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // time over dismiss
        warn.postDelayed({
            activityWeakReference?.get()?.let { activity ->
                if (activity.isFinishing || activity.isDestroyed) {
                    return@postDelayed
                } else {
                    warn.hide(windowManager ?: return@postDelayed)
                }
            }
        }, Warn.DISPLAY_TIME)

        // click dismiss
        warn.warn_body.setOnClickListener {
            warn.hide(windowManager ?: return@setOnClickListener)
        }


    }

    fun registerLifeCycle() {

    }

    // todo 暂时先不写
    fun checkPermission() {
        activityWeakReference?.get()?.let { activity ->
            val check = ActivityCompat.checkSelfPermission(activity, Manifest.permission.SYSTEM_ALERT_WINDOW)
            if (check != PackageManager.PERMISSION_GRANTED) {
                /**
                 * 判断该权限请求是否已经被 Denied(拒绝)过。  返回：true 说明被拒绝过 ; false 说明没有拒绝过
                 *
                 * 注意：
                 * 如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。
                 * 如果设备规范禁止应用具有该权限，此方法也会返回 false。
                 */
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {
                    log("onViewClicked: 该权限请求已经被 Denied(拒绝)过。");
                    //弹出对话框，告诉用户申请此权限的理由，然后再次请求该权限。
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CALL_PHONE), 1);

                } else {
                    log("onViewClicked: 该权限请未被denied过");

                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CALL_PHONE), 1);
                }

            } else {
                hasPermission = true
            }

        }
    }

    private fun initLayoutParameter(): WindowManager.LayoutParams {
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
        //        TODO("adjust permission to choice type")


        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL

        return layoutParams
    }

    // must invoke first
    fun setActivity(activity: Activity) {
        activityWeakReference = WeakReference(activity)
        warn = Warn(activity)
        windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    }

    companion object {
        private var activityWeakReference: WeakReference<Activity>? = null

        @JvmStatic
        fun create(activity: Activity?): Warning {
            if (activity == null) {
                throw IllegalArgumentException("Activity cannot be null!")
            }
            val warning = Warning()
            warning.setActivity(activity)
            return warning
        }
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