package com.example.expensemanagertask.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.expensemanagertask.R
import com.example.expensemanagertask.databinding.FragmentAddTransactionBinding
import com.example.expensemanagertask.models.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddTransactionFragment : Fragment() {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigateToDashboard()
        }


        binding.dateBox.setOnClickListener {
            showDatePicker()
        }


        binding.addTransactionButton.setOnClickListener {
            val selectedId = binding.radioGroupTransactionType.checkedRadioButtonId
            val transactionType = when (selectedId) {
                R.id.radio_expense -> "Expense"
                R.id.radio_income -> "Income"
                else -> {
                    Toast.makeText(requireContext(), "Please select Expense or Income", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val date = binding.dateBox.text.toString()
            val amount = binding.amountInput.text.toString()
            val description = binding.descriptionInput.text.toString()

            if (date.isEmpty() || date == "Choose Date") {
                Toast.makeText(requireContext(), "Please select a date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (amount.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter an amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (description.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a description", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val uid = currentUser.uid
                val userTransactionsRef = firestore.collection("users")
                    .document(uid)
                    .collection("transactions")


                val documentRef = userTransactionsRef.document()

                val transaction = Transaction(
                    id = documentRef.id,
                    type = transactionType,
                    date = date,
                    amount = amount.toDouble(),
                    description = description.lowercase()
                )

                // Save transaction under the user's UID
                documentRef.set(transaction)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Transaction added successfully!", Toast.LENGTH_SHORT).show()
                        navigateToDashboard()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Failed to add transaction: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "User not authenticated!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val formattedDate = String.format("%02d/%02d/%04d", day, month + 1, year)
                binding.dateBox.text = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun navigateToDashboard() {
        findNavController().navigate(R.id.dashboardFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
