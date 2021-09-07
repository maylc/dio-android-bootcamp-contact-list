package io.github.maylcf.contactslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.github.maylcf.contactslist.databinding.FragmentContactBinding

class ContactFragment : Fragment(R.layout.fragment_contact) {

    lateinit var binding: FragmentContactBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentContactBinding.bind(view)

        setupViews()
    }

    private fun setupViews() {
        binding.btnSave.setOnClickListener {
            // TODO: Implement
        }

        binding.btnDelete.setOnClickListener {
            // TODO: Implement
        }
    }
}