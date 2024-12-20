package com.example.expensemanagertask.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.expensemanagertask.R
import com.example.expensemanagertask.databinding.FragmentEditTransactionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class EditTransactionFragment : Fragment() {

    private var _binding: FragmentEditTransactionBinding? = null
    private val binding get() = _binding!!
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private var transactionId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionId = arguments?.getString("transaction_id")
        val transactionDate = arguments?.getString("transaction_date")
        val transactionAmount = arguments?.getDouble("transaction_amount")
        val transactionDescription = arguments?.getString("transaction_description")

        Log.d("EditTransactionFragment", "Editing transactionId: $transactionId")

        binding.dateInput.setText(transactionDate)
        binding.amountInput.setText(transactionAmount?.toString())
        binding.descriptionInput.setText(transactionDescription)
        binding.dateInput.setOnClickListener { openDatePicker() }
        binding.saveTransactionButton.setOnClickListener {
            saveTransactionChanges()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.dashboardFragment)
        }
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                binding.dateInput.setText(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun saveTransactionChanges() {
        val updatedDate = binding.dateInput.text.toString()
        val updatedAmount = binding.amountInput.text.toString().toDoubleOrNull()
        val updatedDescription = binding.descriptionInput.text.toString()

        if (updatedDate.isEmpty() || updatedAmount == null || updatedDescription.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val documentRef = firestore.collection("users")
                .document(uid)
                .collection("transactions")
                .document(transactionId ?: "")
            documentRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val updatedTransaction = mapOf(
                            "date" to updatedDate,
                            "amount" to updatedAmount,
                            "description" to updatedDescription
                        )
                        documentRef.update(updatedTransaction)
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Transaction updated successfully", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.dashboardFragment)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(requireContext(), "Failed to update transaction: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(requireContext(), "Transaction not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error fetching transaction: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "User not authenticated!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
