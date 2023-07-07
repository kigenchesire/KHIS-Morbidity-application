package com.google.android.fhir.khisapp.engine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.fhir.codelabs.engine.R

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }
}