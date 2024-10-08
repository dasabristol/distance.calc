package com.example.activityfare

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerOrigin: Spinner
    private lateinit var spinnerDestination: Spinner
    private lateinit var rgDiscount: RadioGroup
    private lateinit var btnCalculate: Button

    // Fare map from origin to destination
    private val fareMap = mapOf(
        Pair("Urdaneta", "Baguio") to 200.0,
        Pair("Urdaneta", "Manila") to 400.0,
        Pair("Urdaneta", "Alaminos") to 250.0,
        Pair("Urdaneta", "Bolinao") to 300.0,
        Pair("Manila", "Baguio") to 500.0,
        Pair("Manila", "Alaminos") to 400.0,
        Pair("Manila", "Urdaneta") to 400.0,
        Pair("Manila", "Bolinao") to 650.0,
        Pair("Baguio", "Urdaneta") to 200.0,
        Pair("Baguio", "Manila") to 500.0,
        Pair("Baguio", "Bolinao") to 350.0,
        Pair("Baguio", "Alaminos") to 300.0,
        Pair("Alaminos", "Urdaneta") to 250.0,
        Pair("Alaminos", "Baguio") to 300.0,
        Pair("Alaminos", "Manila") to 400.0,
        Pair("Alaminos", "Bolinao") to 50.0,
        Pair("Bolinao", "Urdaneta") to 250.0,
        Pair("Bolinao", "Baguio") to 350.0,
        Pair("Bolinao", "Alaminos") to 50.0,
        Pair("Bolinao", "Manila") to 650.0

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        spinnerOrigin = findViewById(R.id.spinnerOrigin)
        spinnerDestination = findViewById(R.id.spinnerDestination)
        rgDiscount = findViewById(R.id.radioGroup)
        btnCalculate = findViewById(R.id.btnCalculate)

        // Set the button click listener for fare calculation
        btnCalculate.setOnClickListener {
            calculateFare()
        }
    }

    private fun calculateFare() {
        // Get the selected origin and destination
        val origin = spinnerOrigin.selectedItem.toString()
        val destination = spinnerDestination.selectedItem.toString()

        // Get the base fare for the selected route
        val baseFare = fareMap[Pair(origin, destination)] ?: 0.0

        // Get the selected discount type
        val selectedDiscountId = rgDiscount.checkedRadioButtonId
        val discountType: String
        val discount: Double = when (selectedDiscountId) {
            R.id.rbStudent -> {
                discountType = "Student"
                0.20 // 20% discount for students
            }
            R.id.rbPWD -> {
                discountType = "PWD"
                0.20 // 20% discount for PWD
            }
            R.id.rbSenior -> {
                discountType = "Senior Citizen"
                0.20 // 20% discount for senior citizens
            }
            else -> {
                discountType = "None"
                0.0 // No discount
            }
        }

        // Calculate the final fare
        val discountedFare = baseFare - (baseFare * discount)

        // Show the receipt dialog
        showReceiptDialog(origin, destination, baseFare, discountedFare, discountType)
    }

    private fun showReceiptDialog(origin: String, destination: String, baseFare: Double, discountedFare: Double, discountType: String) {
        // Inflate the custom layout for the dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_receipt, null)

        // Create the AlertDialog
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Set the receipt details in the dialog
        val tvReceiptOrigin = dialogView.findViewById<TextView>(R.id.tvReceiptOrigin)
        val tvReceiptDestination = dialogView.findViewById<TextView>(R.id.tvReceiptDestination)
        val tvReceiptBaseFare = dialogView.findViewById<TextView>(R.id.tvReceiptBaseFare)
        val tvReceiptDiscount = dialogView.findViewById<TextView>(R.id.tvReceiptDiscount)
        val tvReceiptTotalFare = dialogView.findViewById<TextView>(R.id.tvReceiptTotalFare)

        // Update the dialog with fare information
        tvReceiptOrigin.text = "Origin: $origin"
        tvReceiptDestination.text = "Destination: $destination"
        tvReceiptBaseFare.text = "Base Fare: ₱${String.format("%.2f", baseFare)}"
        tvReceiptDiscount.text = "Discount: $discountType"
        tvReceiptTotalFare.text = "Total Fare: ₱${String.format("%.2f", discountedFare)}"

        // Close button in the dialog
        val btnCloseReceipt = dialogView.findViewById<Button>(R.id.btnCloseReceipt)
        btnCloseReceipt.setOnClickListener {
            alertDialog.dismiss() // Close the dialog when clicked
        }

        // Show the dialog
        alertDialog.show()
    }
}
