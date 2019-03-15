package com.zcy.warning

import android.app.Activity
import android.view.ViewGroup
import java.lang.ref.WeakReference

/**
 * @author:         zhaochunyu
 * @description:    ${DESP}
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
        fun create(activity: Activity): Warning {
            if (activity == null) {
                throw IllegalArgumentException("Activity cannot be null!")
            }
            val warning = Warning()

            warning.setActivity(activity)
            warning.warn = Warn(activity)

            return warning
        }
    }
}