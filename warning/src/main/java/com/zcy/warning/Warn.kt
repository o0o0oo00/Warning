package com.zcy.warning

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.annotation.StringRes
import kotlinx.android.synthetic.main.layout_warn.view.*

/**
 * @author:         zhaochunyu
 * @description:    ${DESP}
 * @date:           2019/3/15
 */
class Warn @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr), View.OnClickListener, Animation.AnimationListener {

    private var enterAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.warning_slide_in_from_top)
    private var exitAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.warning_slide_out_to_top)
    private var duration = DISPLAY_TIME

    init {
        View.inflate(context, R.layout.layout_warn, this)
//        isHapticFeedbackEnabled = true  触力反馈

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        enterAnimation.setAnimationListener(this)

        // Set Animation to be Run when View is added to Window
        animation = enterAnimation
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        enterAnimation.setAnimationListener(null)
    }

    override fun onClick(v: View?) {
        hide()
    }

    // frameLayout 的点击事件传给 warn_body
    override fun setOnClickListener(l: OnClickListener?) {
        warn_body.setOnClickListener(l)
    }


    /**
     * Sets the Title of the Warning
     *
     * @param title String object to be used as the Warning title
     */
    fun setTitle(title: String) {
        if (!TextUtils.isEmpty(title)) {
            text.visibility = View.VISIBLE
            text.text = title
        }
    }

    fun setTitle(@StringRes id: Int) {
        setTitle(context.getString(id))
    }

    override fun onAnimationEnd(animation: Animation?) {
        Log.e(TAG, "onAnimationEnd")
        // to prepare hide
        prepareHide()
    }

    override fun onAnimationStart(animation: Animation?) {
        Log.e(TAG, "onAnimationStart isInEditMode = $isInEditMode")
        if (isInEditMode) return
        visibility = View.VISIBLE
    }


    override fun onAnimationRepeat(animation: Animation?) {
        // do nothing
    }

    // duration 结束之后启动消失动画
    private fun prepareHide() {
        postDelayed({ hide() }, duration)
    }

    private fun hide() {
        startAnimation(exitAnimation)
        exitAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                // to remove all views
                cleanUp()
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
    }

    private fun cleanUp() {
        clearAnimation() //
        visibility = View.GONE
        postDelayed({
            (parent as? ViewGroup)?.removeView(this@Warn)
            // todo real hide and call hideListener
        }, 100)

    }


    companion object {
        private val TAG = Warn::class.java.simpleName
        private const val DISPLAY_TIME: Long = 3000
    }

}