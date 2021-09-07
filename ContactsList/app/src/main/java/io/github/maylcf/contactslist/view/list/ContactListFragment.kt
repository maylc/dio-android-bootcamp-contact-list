package io.github.maylcf.contactslist.view.list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.maylcf.contactslist.R
import io.github.maylcf.contactslist.application.ContactApplication
import io.github.maylcf.contactslist.databinding.FragmentContactListBinding
import io.github.maylcf.contactslist.view.list.adapter.ContactAdapter
import io.github.maylcf.contactslist.model.Contact

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {

    lateinit var binding: FragmentContactListBinding
    private var adapter: ContactAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentContactListBinding.bind(view)

        setupViews()
        setupToolbar()
    }

    override fun onResume() {
        super.onResume()
        onClickSearch()
    }

    private fun setupToolbar() {
        // TODO: Implement
        //(activity as MainActivity).supportActionBar?.title = ""
    }

    private fun setupViews() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.fab.setOnClickListener { navigateToContactDetailsFragment() }

        binding.btnSearch.setOnClickListener { onClickSearch() }
    }

    private fun onClickItemRecyclerView(index: Int) {
        navigateToContactDetailsFragment(index)
    }

    private fun navigateToContactDetailsFragment(contactId: Int = -1) {
        val action = ContactListFragmentDirections.actionListToContact().setContactId(contactId)
        findNavController().navigate(action)
    }

    private fun onClickSearch() {
        val busca = binding.searchContactName.editText?.text.toString()

        binding.progressBar.visibility = View.VISIBLE
        Thread {
            Thread.sleep(1500)
            var filteredList: List<Contact> = mutableListOf()

            try {
                filteredList = ContactApplication.instance.helperDB?.searchContact(search = busca) ?: mutableListOf()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            requireActivity().runOnUiThread {
                adapter = ContactAdapter(requireContext(), filteredList) { onClickItemRecyclerView(it) }
                binding.recyclerView.adapter = adapter

                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, "Searching for $busca", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
}