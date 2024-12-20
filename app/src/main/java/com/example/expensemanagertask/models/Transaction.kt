package com.example.expensemanagertask.models

data class Transaction(
    val id: String = "",
    val type: String = "",
    val date: String = "",
    val amount: Double = 0.0,
    val description: String = "")
