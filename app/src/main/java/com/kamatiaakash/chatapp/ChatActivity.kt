package com.kamatiaakash.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox:EditText
    private lateinit var sendButton:ImageView
    private lateinit var MessageAdapter:MessageAdapter
    private lateinit var messageList:ArrayList<Message>
    private lateinit var UserDatabaseRef : DatabaseReference

    var receiverRoom:String? = null
    var senderRoom:String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        UserDatabaseRef = FirebaseDatabase.getInstance().getReference()
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid
        supportActionBar?.title = name

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sendButton)
        messageList = ArrayList()
        MessageAdapter = MessageAdapter(this,messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = MessageAdapter

//        Logic for adding data to recyclerView
        UserDatabaseRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                   messageList.clear()
                   for(postSnapshot in snapshot.children){
                       val message = postSnapshot.getValue(Message::class.java)
                       messageList.add(message!!)
                   }
                    MessageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

//        Adding message to database and displaying on the ui
        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObject = Message(message,senderUid)

            UserDatabaseRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    UserDatabaseRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.text = null
        }
    }
}