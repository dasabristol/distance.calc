package com.example.activityfare

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    private lateinit var rgTransportMode: RadioGroup
    private lateinit var btnCalculate: Button
    private lateinit var etUserMoney: EditText // Input for user money

    private val fareMap = mapOf(
        "Bus" to mapOf(
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
        ),
        "Jeep" to mapOf(
            Pair("Urdaneta", "Baguio") to 150.0,
            Pair("Urdaneta", "Manila") to 350.0,
            Pair("Urdaneta", "Alaminos") to 200.0,
            Pair("Urdaneta", "Bolinao") to 250.0,
            Pair("Manila", "Baguio") to 400.0,
            Pair("Manila", "Alaminos") to 400.0,
            Pair("Manila", "Urdaneta") to 350.0,
            Pair("Manila", "Bolinao") to 550.0,
            Pair("Baguio", "Urdaneta") to 150.0,
            Pair("Baguio", "Manila") to 400.0,
            Pair("Baguio", "Bolinao") to 350.0,
            Pair("Baguio", "Alaminos") to 300.0,
            Pair("Alaminos", "Urdaneta") to 200.0,
            Pair("Alaminos", "Baguio") to 300.0,
            Pair("Alaminos", "Manila") to 400.0,
            Pair("Alaminos", "Bolinao") to 50.0,
            Pair("Bolinao", "Urdaneta") to 250.0,
            Pair("Bolinao", "Baguio") to 350.0,
            Pair("Bolinao", "Alaminos") to 50.0,
            Pair("Bolinao", "Manila") to 550.0
        ),
        "Modern Jeep" to mapOf(
            Pair("Urdaneta", "Baguio") to 150.0,
            Pair("Urdaneta", "Manila") to 350.0,
            Pair("Urdaneta", "Alaminos") to 200.0,
            Pair("Urdaneta", "Bolinao") to 250.0,
            Pair("Manila", "Baguio") to 400.0,
            Pair("Manila", "Alaminos") to 400.0,
            Pair("Manila", "Urdaneta") to 350.0,
            Pair("Manila", "Bolinao") to 550.0,
            Pair("Baguio", "Urdaneta") to 150.0,
            Pair("Baguio", "Manila") to 400.0,
            Pair("Baguio", "Bolinao") to 350.0,
            Pair("Baguio", "Alaminos") to 300.0,
            Pair("Alaminos", "Urdaneta") to 200.0,
            Pair("Alaminos", "Baguio") to 300.0,
            Pair("Alaminos", "Manila") to 400.0,
            Pair("Alaminos", "Bolinao") to 50.0,
            Pair("Bolinao", "Urdaneta") to 250.0,
            Pair("Bolinao", "Baguio") to 350.0,
            Pair("Bolinao", "Alaminos") to 50.0,
            Pair("Bolinao", "Manila") to 550.0
        )
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
        rgTransportMode = findViewById(R.id.radioGroup2)
        btnCalculate = findViewById(R.id.btnCalculate)
        etUserMoney = findViewById(R.id.etUserMoney) // Initialize user money input

        btnCalculate.isEnabled = false

        setupSelectionListeners()

        btnCalculate.setOnClickListener {
            calculateFare()
        }

        // Add a TextWatcher to monitor changes in the etUserMoney input field
        etUserMoney.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateInputs() // Validate inputs every time the text changes
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupSelectionListeners() {
        spinnerOrigin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                validateInputs()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerDestination.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                validateInputs()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        rgTransportMode.setOnCheckedChangeListener { _, _ -> validateInputs() }
    }

    private fun validateInputs() {
        val origin = spinnerOrigin.selectedItem.toString()
        val destination = spinnerDestination.selectedItem.toString()
        val selectedTransportId = rgTransportMode.checkedRadioButtonId
        val moneyInput = etUserMoney.text.toString()

        // Enable the button only if origin != destination, transport mode is selected, and money is entered
        btnCalculate.isEnabled = (origin != destination && selectedTransportId != -1 && moneyInput.isNotEmpty())
    }

    private fun calculateFare() {
        val origin = spinnerOrigin.selectedItem.toString()
        val destination = spinnerDestination.selectedItem.toString()

        val selectedTransportId = rgTransportMode.checkedRadioButtonId
        val transportMode: String = when (selectedTransportId) {
            R.id.rbBus -> "Bus"
            R.id.rbJeep -> "Jeep"
            R.id.rbModernJeep -> "Modern Jeep"
            else -> "Not selected"
        }

        val baseFare = fareMap[transportMode]?.get(Pair(origin, destination)) ?: 0.0

        val selectedDiscountId = rgDiscount.checkedRadioButtonId
        val discountType: String
        val discount: Double = when (selectedDiscountId) {
            R.id.rbStudent -> {
                discountType = "Student"
                0.20
            }
            R.id.rbPWD -> {
                discountType = "PWD"
                0.20
            }
            R.id.rbSenior -> {
                discountType = "Senior Citizen"
                0.20
            }
            else -> {
                discountType = "None"
                0.0
            }
        }

        val discountedFare = baseFare - (baseFare * discount)

        // Get the user-entered money
        val userMoneyStr = etUserMoney.text.toString()
        if (userMoneyStr.isNotEmpty()) {
            val userMoney = userMoneyStr.toDouble()

            // Calculate the change
            if (userMoney >= discountedFare) {
                val change = userMoney - discountedFare
                showReceiptDialog(origin, destination, baseFare, discountedFare, discountType, transportMode, change)
            } else {
                showReceiptDialog(origin, destination, baseFare, discountedFare, discountType, transportMode, null)
            }
        }
    }

    private fun showReceiptDialog(
        origin: String,
        destination: String,
        baseFare: Double,
        discountedFare: Double,
        discountType: String,
        transportMode: String,
        change: Double?
    ) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_receipt, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val tvReceiptOrigin = dialogView.findViewById<TextView>(R.id.tvReceiptOrigin)
        val tvReceiptDestination = dialogView.findViewById<TextView>(R.id.tvReceiptDestination)
        val tvReceiptBaseFare = dialogView.findViewById<TextView>(R.id.tvReceiptBaseFare)
        val tvReceiptDiscount = dialogView.findViewById<TextView>(R.id.tvReceiptDiscount)
        val tvReceiptTotalFare = dialogView.findViewById<TextView>(R.id.tvReceiptTotalFare)
        val tvReceiptTransportMode = dialogView.findViewById<TextView>(R.id.tvReceiptTransportMode)
        val tvReceiptChange = dialogView.findViewById<TextView>(R.id.tvReceiptChange)

        tvReceiptOrigin.text = "Origin: $origin"
        tvReceiptDestination.text = "Destination: $destination"
        tvReceiptBaseFare.text = "Base Fare: ₱${String.format("%.2f", baseFare)}"
        tvReceiptDiscount.text = "Discount: $discountType"
        tvReceiptTotalFare.text = "Total Fare: ₱${String.format("%.2f", discountedFare)}"
        tvReceiptTransportMode.text = "Transport Mode: $transportMode"

        if (change != null) {
            tvReceiptChange.text = "Change: ₱${String.format("%.2f", change)}"
        } else {
            tvReceiptChange.text = "Baba ka, noy. Kulang pletem"
        }

        val btnCloseReceipt = dialogView.findViewById<Button>(R.id.btnCloseReceipt)
        btnCloseReceipt.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}
