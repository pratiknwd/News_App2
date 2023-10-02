package com.example.myapplication

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.ViewPager


class VerticalViewPager(context: Context, attrs: AttributeSet?) :
    ViewPager(context, attrs) {
    init {
        setPageTransformer(true, VerticalPageTransformer())
        overScrollMode = OVER_SCROLL_NEVER
    }

    private inner class VerticalPageTransformer : PageTransformer {
        override fun transformPage(page: View, position: Float) {

            //now here we will check positions...
            if (position < -1) {
                //[-infinity,-1]
                //if this page is way off screen to the left..
                page.alpha = 0f
            } else if (position <= 0) {
                //[-1,0]
                //use the default slide transition when moving to the left page..
                page.alpha = 1f
                //counteract the default slide transition...
                page.translationX = page.width * -position

                //set Y position to swipe in from top..
                val yPosition = position * page.height
                page.translationY = yPosition
                page.scaleX = 1f
                page.scaleY = 1f
            } else if (position <= 1) {
                //[0,1]

                //counteract the default slide transition..
                page.translationX = page.width * -position

                //to scale the page down
                val scale = 0.75f + (1 - 0.75f) * (1 - Math.abs(position))
                page.scaleX = scale
                page.scaleY = scale
            } else {
                //[1,+Infinity]
                //this page is way off screen to the right
                page.alpha = 0f
            }
        }
    }

    private fun swapXYCordinates(event: MotionEvent): MotionEvent {
        //now we will swap x and y coordinates using this,...
        val width = width.toFloat()
        val height = height.toFloat()
        val newX = event.y / height * width
        val newY = event.x / width * height
        event.setLocation(newX, newY)
        return event
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val intercepted = super.onInterceptTouchEvent(swapXYCordinates(ev))
        swapXYCordinates(ev) //return touch coordinates to original reference frame for any child value..
        return intercepted
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return super.onTouchEvent(swapXYCordinates(ev))
    }
}
