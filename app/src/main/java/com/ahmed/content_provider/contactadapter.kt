package com.ahmed.content_provider

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ahmed.content_provider.databinding.CustomLayoutBinding

class contactadapter : RecyclerView.Adapter<contactadapter.viewholder> {

    private var contacts = ArrayList<contact>()
    constructor( contacts : ArrayList<contact>){
        this.contacts = contacts
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val binding : CustomLayoutBinding = CustomLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        val v : viewholder = viewholder(binding)
        return v
    }

    override fun getItemCount(): Int {

        return contacts.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val contact : contact = contacts.get(position)
        if (contact.getemail()!=null) {
            holder.binding.emailContact.text = contact.getemail()
        }
        if (contact.getname()!=null){
            holder.binding.nameContact.text = contact.getname()
        }
        if (contact.getnumber()!=null){
            holder.binding.numberContact.text = contact.getnumber()
        }
        if(contact.getother_details()!=null){
            holder.binding.otherContact.text = contact.getother_details()
        }
        var image:Bitmap? = null
        if (!contact.getphoto().equals("") && contact.getphoto() != null){
            image = BitmapFactory.decodeFile(contact.getphoto())
            if (image == null) {
                holder.binding.contactImg.setImageBitmap(image)
            }else{
                    image = BitmapFactory.decodeResource(holder.binding.root.context.resources,R.drawable.ic_launcher_contacts_foreground)
                holder.binding.contactImg.setImageBitmap(image)
            }
        }else{
            image = BitmapFactory.decodeResource(holder.binding.root.context.resources,R.drawable.ic_launcher_contacts_foreground)
            holder.binding.contactImg.setImageBitmap(image)
        }

    }
    inner class viewholder(itemView: CustomLayoutBinding) : ViewHolder(itemView.root){
        val binding:CustomLayoutBinding = itemView

    }
}