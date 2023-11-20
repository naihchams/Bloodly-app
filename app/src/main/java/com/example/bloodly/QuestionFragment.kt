package com.example.bloodly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class QuestionFragment : Fragment() {

    private var questionIndex: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        questionIndex = arguments?.getInt("questionIndex") ?: 0
        displayQuestion(questionIndex)

        val btnNext = view.findViewById<Button>(R.id.btnNext)
        btnNext?.setOnClickListener {
            (activity as? QuestionnaireActivity)?.goToNextQuestion()
        }
    }

    private fun displayQuestion(index: Int) {
        val question = (activity as? QuestionnaireActivity)?.questions?.get(index) ?: Question("", emptyList())

        val questionTextView = view?.findViewById<TextView>(R.id.tvQuestion)
        questionTextView?.text = question.text

        val answersContainer = view?.findViewById<LinearLayout>(R.id.answersContainer)
        answersContainer?.removeAllViews()

        question.answers.forEach { answer ->
            val button = Button(context).apply {
                text = answer
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                    val margin = resources.getDimension(R.dimen.answer_button_margin).toInt()
                    setMargins(margin, margin, margin, margin)
                }
                setOnClickListener {
                    // Implement logic for what happens when an answer is selected
                }
            }
            answersContainer?.addView(button)
        }
    }
}
