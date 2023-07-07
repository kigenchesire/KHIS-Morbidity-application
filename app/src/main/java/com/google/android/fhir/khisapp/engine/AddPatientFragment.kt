package com.google.android.fhir.khisapp.engine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.fhir.codelabs.engine.R

class AddPatientFragment : Fragment() {

    companion object {
        fun newInstance() = AddPatientFragment()
    }

    private lateinit var viewModel: AddPatientViewMode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_patient, container, false)
    }


}