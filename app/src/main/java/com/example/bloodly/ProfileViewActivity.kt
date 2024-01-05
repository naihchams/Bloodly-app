package com.example.bloodly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewActivity : Fragment() {

    private lateinit var userNameTextView: TextView
    private lateinit var questionnaireDataTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile__page, container, false)

        // Initialize TextViews
        userNameTextView = view.findViewById(R.id.userNameTextView)
        questionnaireDataTextView = view.findViewById(R.id.questionnaireDataTextView)


        val DonateBloodButton: Button = view.findViewById(R.id.DonateBloodButton)
        DonateBloodButton.setOnClickListener {
            val addrquestFragment = AllRequestsActivity.newInstance()
            openFragment(addrquestFragment)
        }

        // Retrieve and display the user information
        displayUserInfo()

        return view
    }

    private fun displayUserInfo() {
        val user = FirebaseAuth.getInstance().currentUser

        user?.let {
            val db = FirebaseFirestore.getInstance()
            val userInfoRef = db.collection("userInfo").document(user.uid)

            userInfoRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Display user name
                    val userName = documentSnapshot.getString("userName") ?: "N/A"
                    userNameTextView.text = userName

                    // Since there's no dedicated TextView for each questionnaire answer, you can concatenate them
                    // Assuming you want to display them all in a single TextView
                    val questionnaireData = buildString {
                        documentSnapshot.data?.forEach { (key, value) ->
                            if (key != "userName") { // Assuming you don't want to repeat the user's name
                                append("$key: $value\n")
                            }
                        }
                    }
                    questionnaireDataTextView.text = questionnaireData
                } else {
                    // Handle the case where the user data doesn't exist
                    Toast.makeText(context, "User data not found.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                // Handle any errors
                Toast.makeText(context, "Error fetching user data.", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            // Handle the case where the user is not logged in
            Toast.makeText(context, "User not logged in.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.content1, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        fun newInstance(): ProfileViewActivity = ProfileViewActivity()
    }
}
