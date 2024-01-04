package com.example.bloodly

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Question(val text: String, val answers: List<String>)

class QuestionnaireActivity : AppCompatActivity() {

    private lateinit var scrollView: ScrollView
    private lateinit var questionLayout: LinearLayout

    private var currentQuestionIndex = 0
    var questions = listOf(
        Question("Are you aged between 18 and 65?", listOf("Yes", "No")),
        Question("Is your weight less than 50kg?",  listOf("Yes","No")),
        Question("Are you suffering from any of the below?\n•\tTransmittable disease\n•\tAsthma\n•\tCardiac Arrest\n•\tHypertension\n•\tBlood tension\n•\tDiabetes\n•\tCancer\n", listOf("Yes","No")),
        Question("Have you undergone a tattoo?",  listOf("Yes, in the last 6months","Yes","No")),
        Question( "Have you undergone immunization in the past month?", listOf("Yes","No")),
        Question("What is your sex?" , listOf("Male","Female")),
        Question("What is your blood type?" , listOf("A+","B+","O+","AB+","A-","B-","O-","AB-")),
        Question( "When is the last time you have donated?" , listOf("Less than 3 months ago","Less than 6 months ago","Less than 1 year ago","More than 1 year ago")),
        Question( "Have you contracted any infectious disease recently (past 3 months)?" , listOf("Yes","No")),
    )
    private val userAnswers = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        scrollView = findViewById(R.id.scrollView)
        questionLayout = findViewById(R.id.questionLayout)
        loadQuestion(0)
    }


    private fun loadQuestion(index: Int) {
        if (index >= questions.size) {
            finishQuestionnaire()
            return
        }

        questionLayout.removeAllViews()
        val question = questions[index]
        val textView = TextView(this).apply {
            text = question.text
            textSize = 24f
        }
        questionLayout.addView(textView)

        val radioGroup = RadioGroup(this).apply { orientation = RadioGroup.VERTICAL }
        for (answer in question.answers) {
            val radioButton = RadioButton(this).apply {
                text = answer
                textSize = 18f
            }
            radioGroup.addView(radioButton)
        }
        questionLayout.addView(radioGroup)

        val nextButton = Button(this).apply {
            text = "Next"
            setOnClickListener {
                val selectedRadioButton = radioGroup.findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
                if (selectedRadioButton != null) {
                    userAnswers.add(selectedRadioButton.text.toString())
                    loadQuestion(index + 1)
                } else {
                    Toast.makeText(applicationContext, "Please select an answer", Toast.LENGTH_SHORT).show()
                }
            }
        }
        questionLayout.addView(nextButton)
    }

    private fun finishQuestionnaire() {
        // Get the current logged-in user
        val user = FirebaseAuth.getInstance().currentUser

        // Check if the user is logged in
        if (user != null) {
            // Get a reference to the Firestore database
            val db = FirebaseFirestore.getInstance()

            // Prepare the answers to be saved
            val answersMap = questions.mapIndexed { index, question ->
                question.text to userAnswers[index]
            }.toMap()

            // Create a new document in the userinfo collection for the user
            db.collection("userInfo").document(user.uid)
                .set(answersMap)
                .addOnSuccessListener {
                    val intent = Intent(this, HomeScreenActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Questionnaire data saved successfully to userinfo.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    // Handle failure
                    Toast.makeText(this, "Failed to save questionnaire data to userinfo.", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
        }

        // End the Activity
        finish()
    }

}