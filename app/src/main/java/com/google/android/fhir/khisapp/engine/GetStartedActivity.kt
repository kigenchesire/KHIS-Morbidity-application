package com.google.android.fhir.khisapp.engine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.fhir.codelabs.engine.R
import com.google.firebase.auth.FirebaseAuth

class GetStartedActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var registerBtn: Button
    private lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        auth = FirebaseAuth.getInstance()

        registerBtn = findViewById(R.id.registerBtn)
        loginBtn = findViewById(R.id.loginBtn)

        registerBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}