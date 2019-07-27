package com.gomoney.gomoneyviews

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.gomoney.global.moneyview.MoneyEditText
import com.gomoney.global.moneyview.MoneyTextView

class MainActivity : AppCompatActivity() {

    lateinit var moneyEditText: MoneyEditText
    lateinit var moneyTextView: MoneyTextView
    lateinit var spinnerCurrency: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moneyEditText = findViewById(R.id.moneyEditText)
        moneyTextView = findViewById(R.id.moneyTextView)
        spinnerCurrency = findViewById(R.id.spinnerCurrency)

        val checkCommas = findViewById<CheckBox>(R.id.checkCommas)
        checkCommas.setOnCheckedChangeListener { _, b ->
            if (b) {
                moneyEditText.showCommas()
                moneyTextView.showCommas()
            } else {
                moneyEditText.hideCommas()
                moneyTextView.hideCommas()
            }

            Log.w(TAG, "onCreate: Value: " + moneyTextView.valueString)
            Log.w(TAG, "onCreate: Formatted Value: " + moneyTextView.formattedString)

            Log.e(TAG, "onCreate: Value: " + moneyEditText.valueString)
            Log.e(TAG, "onCreate: Formatted Value: " + moneyEditText.formattedString)
        }

        val checkCurrency = findViewById<CheckBox>(R.id.checkCurrency)
        checkCurrency.setOnCheckedChangeListener { _, b ->
            if (b) {
                moneyEditText.showCurrencySymbol()
                moneyTextView.showCurrencySymbol()
            } else {
                moneyEditText.hideCurrencySymbol()
                moneyTextView.hideCurrencySymbol()
            }

            Log.w(TAG, "onCreate: Value: " + moneyTextView.valueString)
            Log.w(TAG, "onCreate: Formatted Value: " + moneyTextView.formattedString)

            Log.e(TAG, "onCreate: Value: " + moneyEditText.valueString)
            Log.e(TAG, "onCreate: Formatted Value: " + moneyEditText.formattedString)
        }

        spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                val itemName = spinnerCurrency.selectedItem.toString()
                val symbol = itemName.substring(itemName.indexOf("(") + 1, itemName.indexOf(")"))
                moneyEditText.setCurrency(symbol)
                moneyTextView.setCurrency(symbol)

                Log.w(TAG, "onCreate: Value: " + moneyTextView.valueString)
                Log.w(TAG, "onCreate: Formatted Value: " + moneyTextView.formattedString)

                Log.e(TAG, "onCreate: Value: " + moneyEditText.valueString)
                Log.e(TAG, "onCreate: Formatted Value: " + moneyEditText.formattedString)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        moneyTextView.setText(R.string.hint)

        Log.w(TAG, "onCreate: Value: " + moneyTextView.valueString)
        Log.w(TAG, "onCreate: Formatted Value: " + moneyTextView.formattedString)

        Log.e(TAG, "onCreate: Value: " + moneyEditText.valueString)
        Log.e(TAG, "onCreate: Formatted Value: " + moneyEditText.formattedString)
    }

    companion object {

        val TAG = MainActivity::class.java.simpleName
    }
}
