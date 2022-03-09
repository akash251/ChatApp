package com.kamatiaakash.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var UserListRecycler : RecyclerView
    private lateinit var userList:ArrayList<UserModel>
    private lateinit var adapter: UserAdapter
    private lateinit var uAuthorize : FirebaseAuth
    private lateinit var UserDataBaseRef:DatabaseReference
    val name:String = "Start Chat With"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = name

        userList = ArrayList()
        uAuthorize = FirebaseAuth.getInstance()
        adapter = UserAdapter(this,userList)
        UserListRecycler = findViewById(R.id.UserListRecycler)
        UserListRecycler.layoutManager = LinearLayoutManager(this)
        UserListRecycler.adapter = adapter
        UserDataBaseRef = FirebaseDatabase.getInstance().getReference()

        UserDataBaseRef.child("user").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for(postSnapShot in snapshot.children){
                    val currentUser = postSnapShot.getValue(UserModel::class.java)

                    if(uAuthorize.currentUser?.uid != currentUser?.uid)
                        userList.add(currentUser!!)

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logOut){
            uAuthorize.signOut()
            val intent = Intent(this@MainActivity,Login::class.java)
            startActivity(intent)
            finish()
            return true
        }
        return true
    }
}