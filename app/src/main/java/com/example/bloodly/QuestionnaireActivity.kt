package com.example.bloodly

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bloodly.MainScreenActivity
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

    private lateinit var userNameEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        // Initialize the EditText
        userNameEditText = EditText(this)
        userNameEditText.hint = "Enter your name"

        scrollView = findViewById(R.id.scrollView)
        questionLayout = findViewById(R.id.questionLayout)
        // Start with the user name question
        loadUserNameQuestion()
    }

    private fun loadUserNameQuestion() {
        questionLayout.removeAllViews()
        questionLayout.addView(userNameEditText)

        val nextButton = Button(this).apply {
            text = "Next"
            setOnClickListener {
                val userName = userNameEditText.text.toString().trim()
                if (userName.isNotEmpty()) {
                    // Save the user's name and move to the next question
                    userAnswers.add(userName)
                    loadQuestion(currentQuestionIndex)
                } else {
                    Toast.makeText(applicationContext, "Please enter your name", Toast.LENGTH_SHORT).show()
                }
            }
        }
        questionLayout.addView(nextButton)
    }

    private fun loadQuestion(index: Int) {
        if (index >= questions.size) {
            // Save the user's name from the first input
            val userName = userAnswers.firstOrNull() ?: ""
            if (userName.isNotEmpty()) {
                finishQuestionnaire(userName)
            } else {
                Toast.makeText(this, "User name is required.", Toast.LENGTH_SHORT).show()
                // Return to the user name input
                loadUserNameQuestion()
            }
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

    private fun finishQuestionnaire(userName: String) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val db = FirebaseFirestore.getInstance()
            val userDocRef = db.collection("userInfo").document(it.uid)

            // Prepare the answers to be saved, with the userName included
            val answersMap = userAnswersToMap(userName)

            userDocRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null && document.exists()) {
                        // If the document exists, update it with new data
                        userDocRef.update(answersMap)
                            .addOnSuccessListener {
                                navigateToHomeScreen()
                                showToast("Questionnaire data updated successfully.")
                            }
                            .addOnFailureListener { e ->
                                showToast("Failed to update questionnaire data.")
                            }
                    } else {
                        // If the document doesn't exist, create a new one
                        userDocRef.set(answersMap)
                            .addOnSuccessListener {
                                navigateToHomeScreen()
                                showToast("Questionnaire data saved successfully.")
                            }
                            .addOnFailureListener { e ->
                                showToast("Failed to save questionnaire data.")
                            }
                    }
                } else {
                    showToast("Failed to check for existing questionnaire data.")
                }
            }
        } ?: showToast("User not logged in.")
    }

    private fun userAnswersToMap(userName: String): Map<String, Any> {
        // Since we already have the userName, we'll start adding questions and answers from the second question
        val answersMap = mutableMapOf<String, Any>()

        // Skip the first entry (userName) when creating the map
        questions.zip(userAnswers.drop(1)).forEach { (question, answer) ->
            answersMap[question.text] = answer
        }

        // Now, add the userName entry
        answersMap["userName"] = userName

        return answersMap
    }



    private fun navigateToHomeScreen() {
        val intent = Intent(this, MainScreenActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}