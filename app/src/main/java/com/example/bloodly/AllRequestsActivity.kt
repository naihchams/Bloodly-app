package com.example.bloodly

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodly.Adapter.RequestAdapter
import com.example.bloodly.models.Request
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class AllRequestsActivity : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private var recyclerView: RecyclerView? = null
    private var adapter: RequestAdapter? = null
    private var requestList: MutableList<Request> = mutableListOf()
    private var databaseRequest: DatabaseReference? = null
    private var db: FirebaseFirestore? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_all_request_page, container, false)

        linearLayoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = linearLayoutManager
        recyclerView?.setHasFixedSize(true)

        adapter = RequestAdapter(requestList, requireContext())
        recyclerView?.adapter = adapter

        db = FirebaseFirestore.getInstance();
        getData();

        return view;
    }

    private fun getData() {
        db!!.collection("Request")
            .get()
            .addOnCompleteListener { task: Task<QuerySnapshot> ->
                if (task.isSuccessful) {
                    requestList.clear()
                    for (document in task.result) {
                        val request =
                            document.toObject(
                                Request::class.java
                            )
                        requestList.add(request)
                    }
                    adapter!!.notifyDataSetChanged()
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    companion object {
        fun newInstance(): AllRequestsActivity = AllRequestsActivity()
    }
}


