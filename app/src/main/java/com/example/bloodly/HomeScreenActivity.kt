package com.example.bloodly

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import com.example.bloodly.ui.theme.BloodlyTheme
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material3.Button
import androidx.compose.material3.Text

class HomeScreenActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        setContent {
            BloodlyTheme {
                HomeScreen(auth = auth, onSignOut = {
                    updateUIAfterSignOut()
                })
            }
        }
    }

    private fun updateUIAfterSignOut() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


    @Composable
    fun Greeting2(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview2() {
        BloodlyTheme {
            Greeting2("Android")
        }
    }

    @Composable
    fun HomeScreen(auth: FirebaseAuth, onSignOut: () -> Unit) {
        Button(onClick = {
            auth.signOut()
            onSignOut()
        }) {
            Text("Sign Out")
        }
    }
}




