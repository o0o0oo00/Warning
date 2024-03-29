package com.zcy.warning.lib

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import kotlinx.android.synthetic.main.layout_warn.view.*

/**
 * @author:         zhaochunyu
 * @description:    custom warn view
 *
 *                    todo rename Pudding
 * @date:           2019/3/15
 */
class Warn @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var animEnter: ObjectAnimator
    private val animEnterInterceptor = OvershootInterpolator()

    init {
        inflate(context, R.layout.layout_warn, this)
    }

    /**
     * 初始化配置，如loading 的显示 与 icon的动画 触摸反馈等
     */
    private fun initConfiguration() {
        icon?.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alerter_pulse))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.e(TAG, "onAttachedToWindow")
        initConfiguration()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.e(TAG, "onDetachedFromWindow")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.e(TAG, "onMeasure")
        animEnter = ObjectAnimator.ofFloat(this@Warn, "translationY", -this@Warn.measuredHeight.toFloat(), -80F)
        animEnter.interpolator = animEnterInterceptor
        animEnter.duration = ANIMATION_DURATION
        animEnter.start()
    }

    fun hide(windowManager: WindowManager, removeNow: Boolean = false) {
        if (!this@Warn.isAttachedToWindow) {
            return
        }
        if (removeNow) {
            windowManager.removeViewImmediate(this@Warn)
            return
        }
        warn_body.isClickable = false
        val anim = ObjectAnimator.ofFloat(this@Warn, "translationY", -80F, -this@Warn.measuredHeight.toFloat())
        anim.interpolator = AnticipateOvershootInterpolator()
        anim.duration = ANIMATION_DURATION
        anim.start()
        Handler().postDelayed({
            if (this@Warn.isAttachedToWindow) {
                windowManager.removeViewImmediate(this@Warn)
            }
        }, ANIMATION_DURATION)
    }


    fun setWarnBackgroundColor(@ColorInt color: Int) {
        warn_body.setBackgroundColor(color)
    }

    /**
     * Sets the Warn Background Drawable Resource
     *
     * @param resource The qualified drawable integer
     */
    fun setWarnBackgroundResource(@DrawableRes resource: Int) {
        warn_body.setBackgroundResource(resource)
    }

    /**
     * Sets the Warn Background Drawable
     *
     * @param drawable The qualified drawable
     */
    fun setwarnBackgroundDrawable(drawable: Drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            warn_body.background = drawable
        } else {
            warn_body.setBackgroundDrawable(drawable)
        }
    }

    /**
     * Sets the Title of the Warn
     *
     * @param titleId String resource id of the Warn title
     */
    fun setTitle(@StringRes titleId: Int) {
        setTitle(context.getString(titleId))
    }

    /**
     * Sets the Title of the Warn
     *
     * @param title String object to be used as the Warn title
     */
    fun setTitle(title: String) {
        if (!TextUtils.isEmpty(title)) {
            text.visibility = View.VISIBLE
            text.text = title
        }
    }

    /**
     * Set the Title's text appearance of the Title
     *
     * @param textAppearance The style resource id
     */
    fun setTitleAppearance(@StyleRes textAppearance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            text.setTextAppearance(textAppearance)
        } else {
            text.setTextAppearance(text.context, textAppearance)
        }
    }

    /**
     * Set the Title's typeface
     *
     * @param typeface The typeface to use
     */
    fun setTitleTypeface(typeface: Typeface) {
        text.typeface = typeface
    }

    /**
     * Sets the Text of the Warn
     *
     * @param textId String resource id of the Warn text
     */
    fun setText(@StringRes textId: Int) {
        setText(context.getString(textId))
    }

    /**
     * Set the Text's typeface
     *
     * @param typeface The typeface to use
     */
    fun setTextTypeface(typeface: Typeface) {
        text.typeface = typeface
    }

    /**
     * Sets the Text of the Warn
     *
     * @param text String resource id of the Warn text
     */
    fun setText(text: String) {
        if (!TextUtils.isEmpty(text)) {
            this.text.visibility = View.VISIBLE
            this.text.text = text
        }
    }

    /**
     * Set the Text's text appearance of the Title
     *
     * @param textAppearance The style resource id
     */
    fun setTextAppearance(@StyleRes textAppearance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            text.setTextAppearance(textAppearance)
        } else {
            text.setTextAppearance(text.context, textAppearance)
        }
    }

    /**
     * Set the inline icon for the Warn
     *
     * @param iconId Drawable resource id of the icon to use in the Warn
     */
    fun setIcon(@DrawableRes iconId: Int) {
        icon.setImageDrawable(AppCompatResources.getDrawable(context, iconId))
    }

    /**
     * Set the icon color for the Warn
     *
     * @param color Color int
     */
    fun setIconColorFilter(@ColorInt color: Int) {
        icon.setColorFilter(color)
    }

    /**
     * Set the icon color for the Warn
     *
     * @param colorFilter ColorFilter
     */
    fun setIconColorFilter(colorFilter: ColorFilter) {
        icon.colorFilter = colorFilter
    }

    /**
     * Set the icon color for the Warn
     *
     * @param color Color int
     * @param mode  PorterDuff.Mode
     */
    fun setIconColorFilter(@ColorInt color: Int, mode: PorterDuff.Mode) {
        icon.setColorFilter(color, mode)
    }

    /**
     * Set the inline icon for the Warn
     *
     * @param bitmap Bitmap image of the icon to use in the Warn.
     */
    fun setIcon(bitmap: Bitmap) {
        icon.setImageBitmap(bitmap)
    }

    /**
     * Set the inline icon for the Warn
     *
     * @param drawable Drawable image of the icon to use in the Warn.
     */
    fun setIcon(drawable: Drawable) {
        icon.setImageDrawable(drawable)
    }

    /**
     * Set whether to show the icon in the warn or not
     *
     * @param showIcon True to show the icon, false otherwise
     */
    fun showIcon(showIcon: Boolean) {
        icon.visibility = if (showIcon) View.VISIBLE else View.GONE
    }

    /**
     * Set if the Icon should pulse or not
     *
     * @param shouldPulse True if the icon should be animated
     */
    fun pulseIcon(shouldPulse: Boolean) {
//        this.enableIconPulse = shouldPulse
    }

    /**
     * Set if the duration of the warn is infinite
     *
     * @param enableInfiniteDuration True if the duration of the warn is infinite
     */
    fun setEnableInfiniteDuration(enableInfiniteDuration: Boolean) {
//        this.enableInfiniteDuration = enableInfiniteDuration
    }

    /**
     * Enable or disable progress bar
     *
     * @param enableProgress True to enable, False to disable
     */
    fun setEnableProgress(enableProgress: Boolean) {
//        this.enableProgress = enableProgress
    }

    /**
     * Set the Progress bar color from a color resource
     *
     * @param color The color resource
     */
    fun setProgressColorRes(@ColorRes color: Int) {
//        pbProgress?.progressDrawable?.colorFilter = LightingColorFilter(MUL, ContextCompat.getColor(context, color))
    }

    /**
     * Set the Progress bar color from a color resource
     *
     * @param color The color resource
     */
    fun setProgressColorInt(@ColorInt color: Int) {
//        pbProgress?.progressDrawable?.colorFilter = LightingColorFilter(MUL, color)
    }

    /**
     * Enable or Disable haptic feedback
     *
     * @param vibrationEnabled True to enable, false to disable
     */
    fun setVibrationEnabled(vibrationEnabled: Boolean) {
//        this.vibrationEnabled = vibrationEnabled
    }

    /**
     * Show a button with the given text, and on click listener
     *
     * @param text The text to display on the button
     * @param onClick The on click listener
     */
//    fun addButton(text: String, @StyleRes style: Int, onClick: View.OnClickListener) {
//        Button(ContextThemeWrapper(context, style), null, style).apply {
//            this.text = text
//            this.setOnClickListener(onClick)
//
//            buttons.add(this)
//        }
//
//        // Alter padding
//        flwarnBackground?.apply {
//            this.setPadding(this.paddingLeft, this.paddingTop, this.paddingRight, this.paddingBottom / 2)
//        }
//    }


    companion object {
        private val TAG = Warn::class.java.simpleName
        const val DISPLAY_TIME: Long = 3000
        const val ANIMATION_DURATION: Long = 500

    }

}