package com.google.android.fhir.khisapp.engine

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.fhir.codelabs.engine.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var lastSyncedTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userImageButton: ImageButton = findViewById(R.id.userImageButton)
        val searchImageButton: ImageButton = findViewById(R.id.searchImageButton)
        val syncImageButton: ImageButton = findViewById(R.id.syncImageButton)
        val addRecordImageButton: ImageButton = findViewById(R.id.addRecordImageButton)

        val userTextView: TextView = findViewById(R.id.userTextView)
        val searchTextView: TextView = findViewById(R.id.searchTextView)
        val syncTextView: TextView = findViewById(R.id.syncTextView)
        val addRecordTextView: TextView = findViewById(R.id.addRecordTextView)
        sharedPreferences = getSharedPreferences("syncTime", MODE_PRIVATE)
        lastSyncedTextView = findViewById(R.id.lastSyncedTextView)

        //set onclick listeners
        userImageButton.setOnClickListener {
            userProfile()
        }

        userTextView.setOnClickListener {
            userProfile()
        }

        searchImageButton.setOnClickListener {
            search()
        }

        searchTextView.setOnClickListener {
            search()
        }

        syncImageButton.setOnClickListener {
            sync()
        }

        syncTextView.setOnClickListener {
            sync()
        }

        addRecordImageButton.setOnClickListener {
            addProfile()
        }

        addRecordTextView.setOnClickListener {
            addProfile()
        }
    }

    override fun onStart() {
        super.onStart()
        setLastSyncedTime()
    }

    private fun userProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun search() {
        Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show()
    }

    private fun sync() {
        viewModel.triggerOneTimeSync()
        saveLastSyncedTime()
        setLastSyncedTime()
        Toast.makeText(this, "Sync was successful", Toast.LENGTH_SHORT).show()
    }

    private fun addProfile() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun saveLastSyncedTime() {
        val currentTime = System.currentTimeMillis()
        val editor = sharedPreferences.edit()
        editor.putLong("lastSyncedTime", currentTime)
        editor.apply()
    }

    private fun setLastSyncedTime() {
        val lastSyncedTime = sharedPreferences.getLong("lastSyncedTime", 0)
        val formattedTime = formatTimestamp(lastSyncedTime)
        lastSyncedTextView.text = "Last Synced: $formattedTime"
    }

    private fun formatTimestamp(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }

}