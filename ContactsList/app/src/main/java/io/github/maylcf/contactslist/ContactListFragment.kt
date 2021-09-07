package io.github.maylcf.contactslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.github.maylcf.contactslist.databinding.FragmentContactListBinding

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {

    lateinit var binding: FragmentContactListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentContactListBinding.bind(view)

        setupViews()
        setupToolbar()
    }

    private fun setupToolbar() {
        // TODO: Implement
        //(activity as MainActivity).supportActionBar?.title = ""
    }

    private fun setupViews() {
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_list_to_new_contact)
        }

        binding.btnSearch.setOnClickListener {
            // TODO: Implement
        }
    }
}