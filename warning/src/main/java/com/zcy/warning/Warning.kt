package com.zcy.warning

import android.app.Activity
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import java.lang.ref.WeakReference

/**
 * @author:         zhaochunyu
 * @description:    Warn helper class
 * @date:           2019/3/15
 */
class Warning private constructor() {

    private var warn: Warn? = null

    private val activityDecorView: ViewGroup?
        get() {
            return activityWeakReference?.get()?.window?.decorView as? ViewGroup
        }

    // after build
    fun show() {
        // why should run on ui thread ?
        activityDecorView?.addView(warn)
    }

    private fun setActivity(activity: Activity) {
        activityWeakReference = WeakReference(activity)
    }

    companion object {
        private var activityWeakReference: WeakReference<Activity>? = null

        @JvmStatic
        fun create(activity: Activity?): Warning {
            if (activity == null) {
                throw IllegalArgumentException("Activity cannot be null!")
            }
            val warning = Warning()
            Warning.removeBefore(activity)
            warning.setActivity(activity)
            warning.warn = Warn(activity)

            return warning
        }

        // TODO i want config it to support a show queue

        @JvmStatic
        fun removeBefore(activity: Activity?) {
            (activity?.window?.decorView as? ViewGroup)?.let {
                var child: Warn?
                for (i in 0 until it.childCount) {
                    child = if (it.getChildAt(i) is Warn) it.getChildAt(i) as Warn else null
                    if (child != null && child.windowToken != null) {
                        ViewCompat.animate(child).alpha(0f).withEndAction(getRemoveViewRunnable(child))
                    }
                }
            }
        }

        private fun getRemoveViewRunnable(childView: Warn?): Runnable {
            return Runnable {
                childView?.let {
                    (childView.parent as? ViewGroup)?.removeView(childView)
                }
            }
        }
    }
}