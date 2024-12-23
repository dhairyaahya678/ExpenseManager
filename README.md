# 💰 ExpenseManager - Manage Your Finances Efficiently 💰

## 🌟 Table of Contents
- [Overview](#-overview-)
- [Workflow](#-workflow-)
- [Technologies Used](#-technologies-used-)
- [Important Implementation Areas](#-important-implementation-areas-)
- [Key Features](#-key-features-)
- [Screenshots](#-screenshots-)
- [Ending Note](#-ending-note-)

---

<img src="https://user-images.githubusercontent.com/your_image_url/expense-manager.gif" width="900">

## 🌟 Overview 🌟
*ExpenseManager* is an Android application built with **Kotlin** and integrated with **Firestore**, a real-time database, for managing financial transactions efficiently. This app helps users track their income and expenses in real-time, offering a seamless and updated financial overview. It includes features such as:

- **Authentication**: Secure login and signup using email.
- **Expense and Income Tracking**: View your transactions with categorized summaries.
- **Search Feature**: Quickly find specific transactions.
- **Detailed Transaction View**: Edit or delete transactions directly from the detail screen.
- **Add Transactions**: Input details such as date, amount, description, and type (expense/income).
- **Real-Time Updates**: All transaction data is instantly updated and synced across the app.
- **Modern UI**: Clean and intuitive design for ease of use.

---

## 🛠️ Workflow 🛠️
### 1. **Login and Signup**
- The app opens with a **login screen** where users can sign in using their email.
- New users can sign up via the **signup screen** and proceed to the dashboard after account creation.

### 2. **Dashboard**
- The **dashboard** displays an overview of all transactions (expenses and income).
- Users can see a summary of their financial activity and search for specific transactions.
- A **search bar** helps users filter transactions by keywords.

### 3. **Real-Time Data Sync**
- Transactions are stored in **Firestore**, enabling real-time updates across the app. 
- Any addition, edit, or deletion of a transaction is instantly reflected on the dashboard.

### 4. **Transaction Details**
- By clicking on a transaction card, users navigate to the **detail screen**.
- From here, users can:
  - **Edit** the transaction: Update details like amount, date, description, or type.
  - **Delete** the transaction: Permanently remove it from the database.

### 5. **Add Transaction**
- Use the **add button** (+) on the dashboard to add a new transaction.
- Input fields include:
  - **Date**: Select the transaction date.
  - **Amount**: Enter the transaction amount.
  - **Description**: Provide a brief description.
  - **Type**: Specify whether it is an *expense* or *income*.

---

## ⚙️ Technologies Used ⚙️
1. **Kotlin**
   - Primary programming language for Android app development.
2. **Firestore**
   - Cloud-based real-time database for storing and syncing transaction data instantly.
3. **Firebase Authentication**
   - Provides secure user authentication using email.
4. **Material Design Components**
   - Ensures the app has a modern and intuitive UI.
5. **LiveData and ViewModel**
   - Manages UI-related data for lifecycle awareness and better performance.

---

## 💡 Important Implementation Areas 💡
1. **Real-Time Database**
   - Transactions are instantly synced across the app using Firestore.
2. **Authentication**
   - Firebase Authentication for secure login and signup processes.
3. **Firestore Integration**
   - Efficient CRUD operations (Create, Read, Update, Delete) for managing transactions.
4. **RecyclerView with Adapter**
   - Displays transaction data in an organized list format on the dashboard.
5. **Navigation Components**
   - Seamless navigation between screens (login, signup, dashboard, details).
6. **Date Picker**
   - Allows users to select dates when adding or editing transactions.

---

## ✨ Key Features ✨
- **Secure Authentication**: Login and signup using email credentials.
- **Real-Time Data Updates**: Transaction data is updated instantly across all views.
- **Transaction Overview**: View all expenses and income with brief summaries.
- **Search Functionality**: Locate specific transactions easily.
- **Edit/Delete Transactions**: Modify or remove records from the detail screen.
- **Add New Transactions**: Input and manage detailed transaction information.
- **Modern UI/UX**: Intuitive design for easy navigation and interaction.

---

## 📸 Screenshots 📸
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/6fb82abe-a42c-4a3e-a49a-e946f8e3a061" width="300"/></td>
    <td><img src="https://github.com/user-attachments/assets/f74b13d8-ee84-4c12-9f04-9d2eaf36abc2" width="300"/></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/bc5cbf13-23c6-4c26-84a1-1555d986d6bd" width="300"/></td>
    <td><img src="https://github.com/user-attachments/assets/dd822121-61eb-4598-bada-639d59eb390b" width="300"/></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/e3b49945-adb4-4a9e-b99f-c83bcb41ae74" width="300"/></td>
  </tr>
</table>

---

## 📄 Ending Note
*ExpenseManager* is a comprehensive solution for managing your personal finances on the go. With its real-time updates and simple yet powerful features, it makes tracking expenses and income effortless. Focus on delivering a seamless user experience, ensure data security, and optimize the app’s performance to make it a must-have tool for every user. 🚀
