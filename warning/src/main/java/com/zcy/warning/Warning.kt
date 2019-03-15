package com.zcy.warning

import android.app.Activity
import java.lang.ref.WeakReference

/**
 * @author:         zhaochunyu
 * @description:    ${DESP}
 * @date:           2019/3/15
 */
class Warning private constructor() {

    private var warning: Warning? = null

    companion object {
        private var activityWeakReference: WeakReference<Activity>? = null

        @JvmStatic
        fun create(activity: Activity) : Warning{
            if (activity == null) {
                throw IllegalArgumentException("Activity cannot be null!")
            }
            val warning = Warning()

            return warning
        }
    }
}