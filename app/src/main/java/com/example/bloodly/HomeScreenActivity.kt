package com.example.bloodly

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodly.Adapter.RequestAdapter
import com.example.bloodly.models.Request
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.bloodly.AddRequestActivity
import com.example.bloodly.AllRequestsActivity

class HomeScreenActivity : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private var recyclerView: RecyclerView? = null
    private var adapter: RequestAdapter? = null
    private var user: FirebaseUser? = null
    private var requestList = mutableListOf<Request>()
    private lateinit var db: FirebaseFirestore
    private lateinit var nameTextView: TextView
    private lateinit var logoutButton: ImageView

    private fun getData() {
        db.collection("Request")
            .get()
            .addOnSuccessListener { documents: QuerySnapshot ->
                requestList.clear()
                for (document in documents) {
                    val request = document.toObject(Request::class.java)
                    requestList.add(request)
                }
                adapter = RequestAdapter(requestList, requireContext())
                recyclerView?.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents: ", exception)
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = linearLayoutManager
        recyclerView?.setHasFixedSize(true)

        db = Firebase.firestore
        user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            getData()
        }
        nameTextView = view.findViewById(R.id.name)
        logoutButton = view.findViewById(R.id.logoutButton)
        val needBloodButton: Button = view.findViewById(R.id.needBloodButton)
        needBloodButton.setOnClickListener {
            val addrquestFragment = AddRequestActivity.newInstance()
            openFragment(addrquestFragment)
        }

        val DonateBloodButton: Button = view.findViewById(R.id.DonateBloodButton)
        DonateBloodButton.setOnClickListener {
            val addrquestFragment = AllRequestsActivity.newInstance()
            openFragment(addrquestFragment)
        }

        user?.let {
            db.collection("userInfo").document(it.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        nameTextView.text = document.getString("userName")
                    } else {
                        Log.w("Firestore", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("Firestore", "Error getting documents: ", exception)
                }
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val loginIntent = Intent(context, LoginActivity::class.java)
            startActivity(loginIntent)
            activity?.finish()
        }

        return view
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.content1, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        fun newInstance(): HomeScreenActivity = HomeScreenActivity()
    }
}

