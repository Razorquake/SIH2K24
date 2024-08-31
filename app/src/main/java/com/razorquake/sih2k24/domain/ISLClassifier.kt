package com.razorquake.sih2k24.domain

import android.graphics.Bitmap

interface ISLClassifier {
    fun classify(bitmap: Bitmap, rotation: Int): List<Classification>
}