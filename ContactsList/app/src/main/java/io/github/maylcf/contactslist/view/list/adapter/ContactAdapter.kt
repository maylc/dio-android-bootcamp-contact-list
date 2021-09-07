package io.github.maylcf.contactslist.view.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.maylcf.contactslist.databinding.ListContactItemBinding
import io.github.maylcf.contactslist.model.Contact

class ContactAdapter(
    private val context: Context,
    private val list: List<Contact>,
    private val onClick: ((Int) -> Unit)
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListContactItemBinding.inflate(inflater, parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = list[position]
        holder.bind(contact)
    }

    override fun getItemCount(): Int = list.size

    inner class ContactViewHolder(val biding: ListContactItemBinding) : RecyclerView.ViewHolder(biding.root) {
        fun bind(contact: Contact) {
            biding.contactName.text = contact.name
            biding.contactPhone.text = contact.phone
            biding.contactListItem.setOnClickListener { onClick(contact.id) }
        }
    }
}
