package com.example.bloodly

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bloodly.ui.theme.BloodlyTheme
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) // Set the content view to the registration layout

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val passwordConfirmEditText = findViewById<EditText>(R.id.passwordConfirmEditText)
        val signUpButton = findViewById<Button>(R.id.signInButton)
        val haveAccountButton = findViewById<Button>(R.id.createAccountButton)

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = passwordConfirmEditText.text.toString().trim()

            if (isValidEmail(email) && isValidPassword(password, confirmPassword)) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val intent = Intent(this, QuestionnaireActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(baseContext, "Registration failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(baseContext, "Invalid email or password",
                    Toast.LENGTH_SHORT).show()
            }
        }

        haveAccountButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String, confirmPassword: String): Boolean {
        return password.length >= 6 && password == confirmPassword
    }
}