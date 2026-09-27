package com.vetpet.petbeats.core.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.facebook.shimmer.ShimmerFrameLayout

/**
 * Bộ extension functions cho animation dùng chung toàn app VetPet.
 * Import: import com.vetpet.petbeats.core.utils.AnimationUtils.*
 */
object AnimationUtils {

    /**
     * Rung lắc view ngang - dùng cho validation error trên EditText.
     * Ví dụ: binding.inputName.shake()
     */
    fun View.shake() {
        ObjectAnimator.ofFloat(
            this, "translationX",
            0f, -10f, 10f, -8f, 8f, -5f, 5f, 0f
        ).apply {
            duration = 400
            start()
        }
    }

    /**
     * Fade in mượt mà - thay thế view.visibility = View.VISIBLE đột ngột.
     * Ví dụ: binding.nameError.fadeIn()
     */
    fun View.fadeIn(duration: Long = 250) {
        if (visibility == View.VISIBLE && alpha == 1f) return
        alpha = 0f
        visibility = View.VISIBLE
        animate().alpha(1f).setDuration(duration).start()
    }

    /**
     * Fade out mượt mà - thay thế view.visibility = View.GONE đột ngột.
     * Ví dụ: binding.nameError.fadeOut()
     */
    fun View.fadeOut(duration: Long = 200) {
        if (visibility == View.GONE) return
        animate().alpha(0f).setDuration(duration)
            .withEndAction {
                visibility = View.GONE
            }.start()
    }

    /**
     * Scale bounce khi nhấn nút - tạo cảm giác phản hồi xúc giác.
     * Ví dụ: binding.btnLogin.scaleBounce()
     */
    fun View.scaleBounce(scale: Float = 0.92f, duration: Long = 120) {
        animate().scaleX(scale).scaleY(scale).setDuration(duration)
            .withEndAction {
                animate().scaleX(1f).scaleY(1f)
                    .setDuration(duration)
                    .setInterpolator(OvershootInterpolator(2f))
                    .start()
            }.start()
    }

    /**
     * Hiệu ứng cascade entrance cho nhiều view (staggered slide-up + fade-in).
     * Ví dụ: staggeredEntrance(binding.logo, binding.title, binding.input, binding.btn)
     */
    fun staggeredEntrance(vararg views: View, delayStep: Long = 80) {
        views.forEachIndexed { index, view ->
            view.alpha = 0f
            view.translationY = 40f
            view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(350)
                .setStartDelay(index * delayStep)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
    }

    /**
     * Mở rộng view mượt mà - dùng khi toggle hiện/ẩn các field phụ (ví dụ: "Loại khác").
     * Ví dụ: binding.tvInputOther.animateExpand()
     */
    fun View.animateExpand() {
        val parent = parent as? ViewGroup ?: return
        TransitionManager.beginDelayedTransition(parent, ChangeBounds().apply { duration = 300 })
        visibility = View.VISIBLE
    }

    /**
     * Thu gọn view mượt mà.
     * Ví dụ: binding.tvInputOther.animateCollapse()
     */
    fun View.animateCollapse() {
        val parent = parent as? ViewGroup ?: return
        TransitionManager.beginDelayedTransition(parent, ChangeBounds().apply { duration = 300 })
        visibility = View.GONE
    }

    /**
     * Cross-fade từ Shimmer sang nội dung thật - thay thế toggle visibility đột ngột.
     * Ví dụ: crossFadeShimmerToContent(binding.shimmerFrameLayout, binding.recycle)
     */
    fun crossFadeShimmerToContent(shimmer: ShimmerFrameLayout, content: View) {
        shimmer.stopShimmer()
        shimmer.animate().alpha(0f).setDuration(200).withEndAction {
            shimmer.visibility = View.GONE
        }.start()
        content.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate().alpha(1f).setDuration(300).setStartDelay(100).start()
        }
    }

    /**
     * Trượt BottomNav xuống để ẩn - thay thế View.GONE đột ngột.
     * Ví dụ: binding.bottomUserNav.slideDown()
     */
    fun View.slideDown() {
        if (translationY > 0f) return
        animate().translationY(height.toFloat())
            .setDuration(250)
            .setInterpolator(AccelerateInterpolator())
            .start()
    }

    /**
     * Trượt BottomNav lên để hiện - thay thế View.VISIBLE đột ngột.
     * Ví dụ: binding.bottomUserNav.slideUp()
     */
    fun View.slideUp() {
        if (translationY == 0f) return
        animate().translationY(0f)
            .setDuration(300)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    /**
     * Hiệu ứng nhịp đập tim - dùng cho icon health trong SplashLocketFragment.
     * Ví dụ: binding.iconHealth.startHeartbeat()
     */
    fun View.startHeartbeat(): AnimatorSet {
        val scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.2f, 1f, 1.25f, 1f)
        val scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1f, 1.2f, 1f, 1.25f, 1f)
        return AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            duration = 1200
            interpolator = AccelerateDecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (this@startHeartbeat.isAttachedToWindow) {
                        start()
                    }
                }
            })
            start()
        }
    }

    /**
     * Hiệu ứng logo splash: phóng to từ nhỏ + fade in + breathing pulse loop.
     * Ví dụ: binding.logo.splashEntrance()
     */
    fun View.splashEntrance() {
        scaleX = 0.7f
        scaleY = 0.7f
        alpha = 0f
        animate()
            .scaleX(1f).scaleY(1f).alpha(1f)
            .setDuration(600)
            .setInterpolator(OvershootInterpolator(1.5f))
            .withEndAction {
                // Breathing pulse loop
                val pulse = ObjectAnimator.ofPropertyValuesHolder(
                    this,
                    PropertyValuesHolder.ofFloat("scaleX", 1f, 1.04f),
                    PropertyValuesHolder.ofFloat("scaleY", 1f, 1.04f)
                )
                pulse.duration = 900
                pulse.repeatMode = ValueAnimator.REVERSE
                pulse.repeatCount = ValueAnimator.INFINITE
                pulse.start()
            }.start()
    }

    /**
     * Hiệu ứng scale pop-in cho icon/view thành công.
     * Ví dụ: binding.logoSuccess.popIn()
     */
    fun View.popIn(delay: Long = 0) {
        scaleX = 0f
        scaleY = 0f
        alpha = 0f
        animate()
            .scaleX(1f).scaleY(1f).alpha(1f)
            .setDuration(500)
            .setStartDelay(delay)
            .setInterpolator(OvershootInterpolator(1.8f))
            .start()
    }

    /**
     * Scale down + fade out - dùng khi ẩn welcome screen chatbot.
     * Ví dụ: binding.logo.scaleOut()
     */
    fun View.scaleOut(duration: Long = 250) {
        animate()
            .alpha(0f)
            .scaleX(0.8f)
            .scaleY(0.8f)
            .setDuration(duration)
            .withEndAction { visibility = View.GONE }
            .start()
    }

    /**
     * Áp dụng RecyclerView layout animation để item xuất hiện cascade.
     * Ví dụ: binding.recycle.scheduleLayoutAnimation()
     */
    fun applyRecyclerViewAnimation(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        val context = recyclerView.context
        val animation = android.view.animation.AnimationUtils.loadLayoutAnimation(
            context, com.example.VetPet.R.anim.layout_animation_fall_down
        )
        recyclerView.layoutAnimation = animation
        recyclerView.scheduleLayoutAnimation()
    }
}
