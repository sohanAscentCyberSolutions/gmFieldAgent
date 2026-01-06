package com.example.md3.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RatingBar
import com.example.md3.R

class CustomRatingBar (context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatRatingBar(context, attrs) {
    private var starSize: Int = 0
    init {
        progressDrawable = null
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomRatingBar)
        starSize = typedArray.getDimensionPixelSize(R.styleable.CustomRatingBar_starSize, 40)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // Set the measured dimensions to accommodate the star size
        setMeasuredDimension(starSize * numStars, starSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Calculate the width of each star based on the RatingBar width and number of stars
        val starWidth = width / numStars

        // Draw each star drawable at the appropriate position
        for (i in 0 until numStars) {
            val starDrawable = if (rating >= i + 1) {
                resources.getDrawable(R.drawable.star_filled, null)
            } else {
                resources.getDrawable(R.drawable.star_outline, null)
            }

            // Calculate the left, top, right, and bottom positions for the star drawable
            val left = i * starWidth
            val top = 0
            val right = (i + 1) * starWidth
            val bottom = height

            // Set the bounds for the star drawable and draw it on the canvas
            starDrawable.setBounds(left, top, right, bottom)
            starDrawable.draw(canvas)
        }
    }
}