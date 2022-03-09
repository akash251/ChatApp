package com.kamatiaakash.chatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {
    private lateinit var etPassword: EditText
    private lateinit var etEmail: EditText
    private lateinit var etUserName: EditText
    private lateinit var btnSignUp: Button
    private lateinit var uAuthorize: FirebaseAuth
    private lateinit var uDataBaseRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etUserName = findViewById(R.id.etUserName)
        btnSignUp = findViewById(R.id.btnSignUp)
        uAuthorize = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            val name = etUserName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            signUp(name,email,password)
        }

    }

    private fun signUp( name:String,email: String, password: String) {
        uAuthorize.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    addUserToDatabase(name,email, uAuthorize.currentUser?.uid!!)
                   val intent = Intent(this@SignUp,MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this@SignUp,"Error While Signing Up Please Try Again!!",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        uDataBaseRef = FirebaseDatabase.getInstance().getReference()

        uDataBaseRef.child("user").child(uid).setValue(UserModel(name,email,uid))
    }
}