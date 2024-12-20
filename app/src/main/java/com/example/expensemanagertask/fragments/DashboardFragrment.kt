package com.example.expensemanagertask.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanagertask.R
import com.example.expensemanagertask.TransactionAdapter
import com.example.expensemanagertask.databinding.FragmentDashboardBinding
import com.example.expensemanagertask.models.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val transactionList = mutableListOf<Transaction>()
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        transactionAdapter = TransactionAdapter(transactionList) { transaction ->
            navigateToTransactionDetail(transaction)
        }
        binding.recentTransactionsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transactionAdapter
        }

        addSwipeToDelete()

        fetchUserTransactions()

        binding.addButton.setOnClickListener {
            navigateToAddTransaction()
        }
    }

    private fun addSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val transaction = transactionList[position]
                val currentUser = FirebaseAuth.getInstance().currentUser

                if (currentUser != null) {
                    // Delete from Firestore
                    firestore.collection("users").document(currentUser.uid).collection("transactions")
                        .document(transaction.id)
                        .delete()
                        .addOnSuccessListener {
                            transactionList.removeAt(position)
                            transactionAdapter.notifyItemRemoved(position)
                            Toast.makeText(requireContext(), "Transaction deleted", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            transactionAdapter.notifyItemChanged(position)
                            Toast.makeText(requireContext(), "Failed to delete: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    transactionAdapter.notifyItemChanged(position)
                    Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recentTransactionsRecycler)
    }

    private fun fetchUserTransactions() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val userTransactionsRef = firestore.collection("users")
                .document(uid)
                .collection("transactions")
                .orderBy("date")

            userTransactionsRef.get()
                .addOnSuccessListener { result ->
                    transactionList.clear()
                    for (document in result) {
                        val transaction = document.toObject(Transaction::class.java)
                        transactionList.add(transaction)
                    }
                    transactionAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to fetch transactions: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "User not authenticated!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToTransactionDetail(transaction: Transaction) {
        val bundle = Bundle().apply {
            putString("transaction_id", transaction.id)
            putString("transaction_type", transaction.type)
            putString("transaction_date", transaction.date)
            putDouble("transaction_amount", transaction.amount)
            putString("transaction_description", transaction.description)
        }
        findNavController().navigate(R.id.transactionDetailFragment, bundle)
    }

    private fun navigateToAddTransaction() {
        findNavController().navigate(R.id.addTransactionFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
