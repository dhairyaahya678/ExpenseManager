package com.example.expensemanagertask

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanagertask.models.Transaction

class TransactionAdapter(
    private val transactionList: List<Transaction>,
    private val onTransactionClick: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transactionId: TextView = itemView.findViewById(R.id.transaction_id)
        val transactionDescription: TextView = itemView.findViewById(R.id.transaction_description)
        val transactionDate: TextView = itemView.findViewById(R.id.transaction_date)
        val transactionType: TextView = itemView.findViewById(R.id.transaction_type)
        val transactionAmount: TextView = itemView.findViewById(R.id.transaction_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.transactionId.text = "${position + 1}"
        holder.transactionDescription.text = transaction.description
        holder.transactionDate.text = transaction.date
        holder.transactionAmount.text = "₹${transaction.amount}"


        holder.transactionType.text = transaction.type
        holder.transactionType.setTextColor(
            if (transaction.type == "Expense") Color.RED else Color.GREEN
        )


        holder.itemView.setOnClickListener {
            onTransactionClick(transaction)
        }
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
}
