package io.github.maylcf.contactslist.view.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.github.maylcf.contactslist.R
import io.github.maylcf.contactslist.application.ContactApplication
import io.github.maylcf.contactslist.databinding.FragmentContactBinding
import io.github.maylcf.contactslist.model.Contact
import io.github.maylcf.contactslist.view.details.ContactFragmentArgs.fromBundle

class ContactFragment : Fragment(R.layout.fragment_contact) {

    lateinit var binding: FragmentContactBinding
    private var contactId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentContactBinding.bind(view)

        setupContact()
        setupViews()
    }

    private fun setupContact() {
        arguments?.let {
            contactId = fromBundle(it).contactId
        }

        if (isNewContact()) return

        binding.progressBar.visibility = View.VISIBLE

        Thread(Runnable {
            Thread.sleep(1500)
            val list = ContactApplication.instance.helperDB?.searchContact("$contactId", true) ?: return@Runnable
            val contact = list.getOrNull(0) ?: return@Runnable
            requireActivity().runOnUiThread {
                binding.progressBar.visibility = View.GONE
                binding.contactName.editText?.setText(contact.name)
                binding.contactPhone.editText?.setText(contact.phone)
            }
        }).start()
    }

    private fun isNewContact(): Boolean {
        return contactId == -1
    }

    private fun setupViews() {
        if (isNewContact()) {
            binding.btnDelete.visibility = View.GONE
        } else {
            binding.btnDelete.visibility = View.VISIBLE
        }

        binding.btnSave.setOnClickListener { onClickSaveContact() }
        binding.btnDelete.setOnClickListener { onClickDeleteContact() }
    }

    private fun onClickDeleteContact() {
        if (contactId > -1) {
            binding.progressBar.visibility = View.VISIBLE
            Thread {
                Thread.sleep(1500)
                ContactApplication.instance.helperDB?.deleteContact(contactId)
                requireActivity().runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    navigateToContactList()
                }
            }.start()
        }
    }

    private fun onClickSaveContact() {
        val contact = Contact(
            contactId,
            binding.contactName.editText?.text.toString(),
            binding.contactPhone.editText?.text.toString()
        )
        binding.progressBar.visibility = View.VISIBLE

        Thread {
            Thread.sleep(1500)
            if (contactId == -1) {
                ContactApplication.instance.helperDB?.saveContact(contact)
            } else {
                ContactApplication.instance.helperDB?.updateContact(contact)
            }
            requireActivity().runOnUiThread {
                binding.progressBar.visibility = View.GONE
                navigateToContactList()
            }
        }.start()
    }

    private fun navigateToContactList() {
        findNavController().navigate(ContactFragmentDirections.actionContactToList())
    }
}