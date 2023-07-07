package com.google.android.fhir.khisapp.engine

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.codelabs.engine.R
import com.google.android.fhir.datacapture.QuestionnaireFragment
import com.google.android.fhir.datacapture.mapping.ResourceMapper
import com.google.android.fhir.datacapture.validation.Invalid
import com.google.android.fhir.datacapture.validation.QuestionnaireResponseValidator
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Questionnaire
import java.util.UUID



class MainActivity : AppCompatActivity() {


  var questionnaireJsonString: String? = null
  private val viewModel: MainActivityViewModel by viewModels()
  private val questionnaireResource: Questionnaire
    get() =
      FhirContext.forCached(FhirVersionEnum.R4).newJsonParser().parseResource(questionnaireJsonString)
              as Questionnaire

  override fun onCreate(savedInstanceState: android.os.Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    questionnaireJsonString = getStringFromAssets("quetionnaire3.json")
    if (savedInstanceState == null) {
      supportFragmentManager.commit {
        setReorderingAllowed(true)
        add(
          R.id.fragment_container_view,
          QuestionnaireFragment.builder().setQuestionnaire(questionnaireJsonString!!).build()
        )
      }
    }
  }

  fun submitQuestionnaire() {
    val isPatientSaved = MutableLiveData<Boolean>()

    // Get a questionnaire response
    val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view)
            as QuestionnaireFragment
    val questionnaireResponse = fragment.getQuestionnaireResponse()

    val jsonParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
    val questionnaireResponseString =
      jsonParser.encodeResourceToString(questionnaireResponse)
    Log.d("response", questionnaireResponseString)

    lifecycleScope.launch {
      val questionnaire =
        jsonParser.parseResource(questionnaireJsonString) as Questionnaire
      val bundle = ResourceMapper.extract(questionnaire, questionnaireResponse)
      Log.d("extraction result", jsonParser.encodeResourceToString(bundle))
      //  upload qusetonnaire to FHIR engine

      ////// new code
      //var fhirEngine: FhirEngine = FhirEngineProvider.getInstance(this@MainActivity)
      //val fhirEngine: FhirEngine by lazy { FhirEngineProvider.getInstance(this@MainActivity) }
      var fhirEngine: FhirEngine = FhirApplication.fhirEngine(applicationContext)
      if (QuestionnaireResponseValidator.validateQuestionnaireResponse(
          questionnaireResource,
          questionnaireResponse,
          application
        )
          .values
          .flatten()
          .any { it is Invalid }
      ) {
        isPatientSaved.value = false
        Toast.makeText(this@MainActivity, "failed to submit", Toast.LENGTH_SHORT).show()
        return@launch
      }
      val entry = ResourceMapper.extract(questionnaireResource, questionnaireResponse).entryFirstRep

      if (entry.resource !is Patient) {
        val resourse_entry = entry.resource
        Toast.makeText(this@MainActivity, "resource type = $resourse_entry", Toast.LENGTH_SHORT)
          .show()
        return@launch
      }
      val questionnaireresponse = entry.resource as Patient
      questionnaireresponse.id = UUID.randomUUID().toString()
      fhirEngine.create(questionnaireresponse)
      isPatientSaved.value = true
      Toast.makeText(this@MainActivity, "data submitted suuccessfully", Toast.LENGTH_SHORT).show()
      fun submitPatientDataToServer(patientData: Patient): Boolean {
        isPatientSaved.value = true
        return true
      }


    }


  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.submit) {
      submitQuestionnaire()
      return true


    }
    if (item.itemId == R.id.sync) {

      Toast.makeText(this@MainActivity, "Sync started ", Toast.LENGTH_SHORT).show()
      viewModel.triggerOneTimeSync()
      Toast.makeText(this@MainActivity, "Sync was successfull ", Toast.LENGTH_SHORT).show()
      return true}
    return super.onOptionsItemSelected(item)
  }


  private fun getStringFromAssets(fileName: String): String {
    return assets.open(fileName).bufferedReader().use { it.readText() }

  }
  private fun generateUuid(): String {

    return UUID.randomUUID().toString()

  }
}

