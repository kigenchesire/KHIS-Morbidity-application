package com.google.android.fhir.khisapp.engine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.fhir.codelabs.engine.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileActivity : AppCompatActivity() {

    private lateinit var emailTextView: TextView
    private lateinit var lastLoginTextView: TextView

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        emailTextView = findViewById(R.id.emailTextView)
        lastLoginTextView = findViewById(R.id.lastLoginTextView)

        auth = FirebaseAuth.getInstance()

        val currentUser: FirebaseUser? = auth.currentUser

        if (currentUser != null) {
            val email: String = currentUser.email ?: ""
            val lastLoginTimestamp: Long = currentUser.metadata?.lastSignInTimestamp ?: 0

            // Format the last login timestamp to a date string
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val lastLoginDate: String = dateFormat.format(Date(lastLoginTimestamp))

            // Set the retrieved data to the TextViews
            emailTextView.text = "Email: $email"
            lastLoginTextView.text = "Last Login: $lastLoginDate"
        }

    }
}