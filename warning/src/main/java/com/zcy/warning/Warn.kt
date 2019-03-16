package com.zcy.warning

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.layout_warn.view.*

/**
 * @author:         zhaochunyu
 * @description:    custom warn view
 *
 *                     rename Pudding
 * @date:           2019/3/15
 */
class Warn @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr), View.OnClickListener, Animation.AnimationListener {

    private var enterAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.warning_slide_in_from_top)
    private var exitAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.warning_slide_out_to_top)
    private var duration = DISPLAY_TIME
    private var measureOnce = false

    init {
        View.inflate(context, R.layout.layout_warn, this)
//        isHapticFeedbackEnabled = true  触力反馈
        ViewCompat.setTranslationZ(this, Integer.MAX_VALUE.toFloat())

        warn_body.setOnClickListener(this)
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.e(TAG, "onMeasure")

        // todo this code is not cool , i decided to remove it
        if (!measureOnce) {
            measureOnce = true
            // Add a negative top margin to compensate for overshoot enter animation
            val params = layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = context.resources.getDimensionPixelSize(R.dimen.warning_fix_margin_top)
            requestLayout()
        }
    }

    override fun onClick(v: View?) {
        hide()
    }

    // frameLayout 的点击事件传给 warn_body
    override fun setOnClickListener(l: OnClickListener?) {
        warn_body.setOnClickListener(l)
    }

    /* Setters and Getters */

    /**
     * Sets the Warn Background colour
     *
     * @param color The qualified colour integer
     */
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
     * Sets the Text of the Warn
     *
     * @param textId String resource id of the Warn text
     */
    fun setText(@StringRes textId: Int) {
        setText(context.getString(textId))
    }

    /**
     * Disable touches while the Warn is showing
     */
    fun disableOutsideTouch() {
        warn_body.isClickable = true
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
     * Set if the warner is isDismissable or not
     *
     * @param dismissible True if warn can be dismissed
     */
//    fun setDismissable(dismissable: Boolean) {
//        this.isDismissable = dismissable
//    }
//
//    /**
//     * Get if the warn is isDismissable
//     * @return
//     */
//    fun isDismissable(): Boolean {
//        return isDismissable
//    }

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
     * Set the warn's listener to be fired on the warn being fully shown
     *
     * @param listener Listener to be fired
     */
//    fun setOnShowListener(listener: OnShowwarnListener) {
//        this.onShowListener = listener
//    }

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



    override fun onAnimationEnd(animation: Animation?) {
        Log.e(TAG, "onAnimationEnd")
        // to prepare hide
        prepareHide()
    }

    override fun onAnimationStart(animation: Animation?) {
        Log.e(TAG, "onAnimationStart isInEditMode = $isInEditMode")
        if (isInEditMode) return
        visibility = View.VISIBLE
        // init config at here
        initConfiguration()
    }


    override fun onAnimationRepeat(animation: Animation?) {
        // do nothing
    }

    /**
     * 初始化配置，如loading 的显示 与 icon的动画 触摸反馈等
     */
    private fun initConfiguration() {

    }

    /**
     * duration 时间到了，启动消失动画
     */
    private fun prepareHide() {
        postDelayed({ hide() }, duration)
    }

    /**
     * 消失 启动消失动画
     */
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

    /**
     * 移除所有动画和WarnView
     */
    private fun cleanUp() {
        clearAnimation() //
        visibility = View.GONE
        postDelayed({
            (parent as? ViewGroup)?.removeView(this@Warn)
            //  todo call hideListener
        }, 100)

    }


    companion object {
        private val TAG = Warn::class.java.simpleName
        private const val DISPLAY_TIME: Long = 3000
    }

}