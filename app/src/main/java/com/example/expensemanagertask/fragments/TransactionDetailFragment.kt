package com.example.expensemanagertask.fragments

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
import com.example.expensemanagertask.databinding.FragmentTransactionDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TransactionDetailFragment : Fragment() {

    private var _binding: FragmentTransactionDetailBinding? = null
    private val binding get() = _binding!!
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private var transactionId: String? = null
    private var transactionDate: String? = null
    private var transactionAmount: Double? = null
    private var transactionDescription: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionId = arguments?.getString("transaction_id")
        transactionDate = arguments?.getString("transaction_date")
        transactionAmount = arguments?.getDouble("transaction_amount")
        transactionDescription = arguments?.getString("transaction_description")
        Log.d("TransactionDetailFragment", "Transaction ID: $transactionId")
        binding.transactionId.text = "ID: $transactionId"
        binding.transactionDate.text = transactionDate
        binding.transactionAmount.text = "₹$transactionAmount"
        binding.transactionDescription.text = transactionDescription
        binding.editIcon.setOnClickListener {
            Log.d("EditButton", "Edit button clicked")
            navigateToEditTransaction(transactionId, transactionDate, transactionAmount, transactionDescription)
        }
        binding.deleteIcon.setOnClickListener {
            deleteTransaction()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.dashboardFragment)
        }
    }

    private fun navigateToEditTransaction(
        transactionId: String?,
        transactionDate: String?,
        transactionAmount: Double?,
        transactionDescription: String?
    ) {
        val bundle = Bundle().apply {
            putString("transaction_id", transactionId)
            putString("transaction_date", transactionDate)
            putDouble("transaction_amount", transactionAmount ?: 0.0)
            putString("transaction_description", transactionDescription)
        }
        findNavController().navigate(R.id.editTransactionFragment, bundle)
    }

    private fun deleteTransaction() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val documentRef = firestore.collection("users")
                .document(uid)
                .collection("transactions")
                .document(transactionId ?: "")
            documentRef.delete()
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Transaction deleted successfully!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.dashboardFragment)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to delete transaction: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("DeleteTransaction", "Error deleting transaction", e)
                }
        } else {
            Toast.makeText(requireContext(), "User not authenticated!", Toast.LENGTH_SHORT).show()
        }
    }
    //For PULL Request
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
