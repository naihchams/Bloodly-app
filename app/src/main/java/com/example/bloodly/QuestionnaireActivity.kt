package com.example.bloodly

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

data class Question(
    val text: String,
    val answers: List<String>
)

class QuestionnaireActivity : AppCompatActivity() {

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

    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        setupProgressIndicators()
        loadQuestion(currentQuestionIndex)
    }

    fun goToNextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            updateProgressIndicator(currentQuestionIndex)
            loadQuestion(currentQuestionIndex)
        } else {
            // Handle completion of questionnaire
        }
    }

    private fun loadQuestion(index: Int) {
        val fragment = QuestionFragment().apply {
            arguments = Bundle().apply {
                putInt("questionIndex", index)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun setupProgressIndicators() {
        val indicatorContainer = findViewById<LinearLayout>(R.id.progressIndicatorContainer)
        questions.forEachIndexed { index, _ ->
            val view = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(20, 20) // Adjust size as needed
                background = if (index == 0) getDrawable(R.drawable.selected_dot_background) else getDrawable(R.drawable.unselected_dot_background)
            }
            indicatorContainer.addView(view)
        }
    }

    private fun updateProgressIndicator(currentIndex: Int) {
        val indicatorContainer = findViewById<LinearLayout>(R.id.progressIndicatorContainer)
        for (i in 0 until indicatorContainer.childCount) {
            val view = indicatorContainer.getChildAt(i)
            view.background = if (i == currentIndex) getDrawable(R.drawable.selected_dot_background) else getDrawable(R.drawable.unselected_dot_background)
        }
    }
}

