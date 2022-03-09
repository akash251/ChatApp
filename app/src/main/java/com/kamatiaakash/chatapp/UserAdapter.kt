package com.kamatiaakash.chatapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class UserAdapter (val context: Context,val UserList:ArrayList<UserModel>): RecyclerView.Adapter<UserAdapter.UserViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view:View = LayoutInflater.from(context).inflate(R.layout.user_item_layout,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = UserList[position]
        holder.UserName.text = currentUser.name
        holder.itemView.setOnClickListener {
            val intent  = Intent(context,ChatActivity::class.java)

            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)

            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return UserList.size
    }

    class UserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val UserName = itemView.findViewById<TextView>(R.id.UserName)
    }
}