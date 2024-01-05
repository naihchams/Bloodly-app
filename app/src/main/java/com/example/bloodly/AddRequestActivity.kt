package com.example.bloodly

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.bloodly.MainScreenActivity
import com.example.bloodly.R
import com.example.bloodly.RegisterActivity
import com.example.bloodly.models.Request
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


private var mDatabase: FirebaseFirestore? = null


class AddRequestActivity : Fragment(), RadioGroup.OnCheckedChangeListener {

    private var isClearingCheck: Boolean = false

    var radioGroups: RadioGroup? = null
    var radioGroups2: RadioGroup? = null
    var edit_name: EditText?=null
    var edit_age: EditText?=null
    var edit_phone: EditText?=null
    var edit_hospital: EditText?=null
    var edit_userLocation: EditText?=null

    var bloodGrp:String?=null
    var unitNedded:Int?=0
    var name:String?=null ;var age:String?=null;var phone:String?=null;var hospital:String?=null;var userLocation:String?=null

    //user details
    var firebaseUser :FirebaseUser?=null
    var firebaseAuth:FirebaseAuth?=null
    var userId:String?=null
    var userEmail:String?=null


    override fun onStart() {
        super.onStart()

        mDatabase = FirebaseFirestore.getInstance()

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth?.currentUser
        if(firebaseUser != null){
            userId = firebaseUser?.uid
            userEmail = firebaseUser?.email
        } else {
            val intent = Intent(context, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_add__request__page, container, false)
        // Get radio group selected item using on checked change listener

        //init all view
        edit_name=view.findViewById<EditText>(R.id.edit_name)
        edit_age=view.findViewById<EditText>(R.id.edit_age)
        edit_phone=view.findViewById<EditText>(R.id.edit_phone)
        edit_hospital=view.findViewById<EditText>(R.id.edit_hospital)
        edit_userLocation=view.findViewById<EditText>(R.id.edit_userLocation)

        radioGroups = view.findViewById(R.id.radio_group) as RadioGroup
        radioGroups?.setOnCheckedChangeListener(this)

        radioGroups2 = view.findViewById(R.id.radio_group2) as RadioGroup
        radioGroups2?.setOnCheckedChangeListener(this)

        val publishBtn =view.findViewById<Button>(R.id.publish)

        val unitText =view.findViewById<TextView>(R.id.unitText)
        val btnAddUnit=view.findViewById<Button>(R.id.btn_addUnit)
        btnAddUnit.setOnClickListener {
            if (unitNedded!! <=20){
                unitNedded= unitNedded!! +1
                unitText.text=unitNedded.toString()
            }
        }
        val btn_removeUnit=view.findViewById<Button>(R.id.btn_removeUnit)
        btn_removeUnit.setOnClickListener {
            if (unitNedded!!>0){
                unitNedded= unitNedded!! -1
                unitText.text=unitNedded.toString()
            }
        }

        publishBtn.setOnClickListener {
            validateAllView()
        }
        return view
    }

    private fun validateAllView() {
        name=edit_name?.text.toString()
        age=edit_age?.text.toString()
        phone=edit_phone?.text.toString()
        hospital=edit_hospital?.text.toString()
        userLocation=edit_userLocation?.text.toString()
        if(name.equals("")){
            edit_name?.error = "name is  empty"
        }
        else if(age.equals("")){
            edit_age?.error = "age is  empty"
        }
        else if(phone.equals("")){
            edit_phone?.error="phone is empty"
        }
        else if(hospital.equals("")){
            edit_hospital?.error="hospital is empty"
        }
        else if(userLocation.equals("")){
            edit_userLocation?.error="location is empty"
        }
        else if(unitNedded==0){
            Toast.makeText(context,"Select how many unitNeeded?",Toast.LENGTH_SHORT).show()
        }
        else if(bloodGrp==null){
            Toast.makeText(context,"Select blood group",Toast.LENGTH_SHORT).show()
        }
        else{
            sendRequestFirebase()
        }

    }

    private fun sendRequestFirebase() {
        // Create request model
        val requestModel = Request(name, age, phone, userLocation, hospital, unitNedded.toString(), bloodGrp, userId, userEmail)

        // Add a new document with a generated ID
        mDatabase?.collection("Request")?.add(requestModel)
            ?.addOnSuccessListener { documentReference ->
                Toast.makeText(context, "Request Submitted", Toast.LENGTH_SHORT).show()
                navigateToHome()
            }
            ?.addOnFailureListener { e ->
                Log.e("FirebaseError", "Failed to submit request", e)
                Toast.makeText(context, "Failed to submit request: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun navigateToHome() {
        startActivity(Intent(context, MainScreenActivity::class.java))
    }

    companion object {
        fun newInstance(): AddRequestActivity = AddRequestActivity()
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

        if (isClearingCheck) {
            return; // Prevent further processing if we are just clearing checks
        }

        val localRadioGroups = radioGroups
        val localRadioGroups2 = radioGroups2

        try {
            isClearingCheck = true;
            if (group === localRadioGroups) {
                localRadioGroups2?.clearCheck();
            } else if (group === localRadioGroups2) {
                localRadioGroups?.clearCheck();
            }
        } finally {
            isClearingCheck = false;
        }

        if (checkedId != -1) {
            val checkedRadioButton = group?.findViewById<RadioButton>(checkedId)
            checkedRadioButton?.let {
                if (it.isChecked) {
                    bloodGrp = it.text.toString()
                }
            }
        }
    }

}