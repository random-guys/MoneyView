package com.random_guys.gomoneyviews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.random_guys.moneyview.MoneyEditText
import com.random_guys.moneyview.MoneyTextView
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var moneyEditText: MoneyEditText
    lateinit var moneyTextView: MoneyTextView
    lateinit var counter: TimerTask
    lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moneyEditText = findViewById(R.id.moneyEditText)
        moneyTextView = findViewById(R.id.moneyTextView)
    }

    override fun onResume() {
        super.onResume()
        counter = object : TimerTask() {
            override fun run() {
                moneyTextView.setMoneyText(String.format("%,.2f", ((System.currentTimeMillis() % 100000) / 3.0f)))
            }
        }
        timer = Timer()
        timer.scheduleAtFixedRate(counter, 1000L, 1000L)
    }

    override fun onPause() {
        super.onPause()
        counter.cancel()
        timer.cancel()
    }

    companion object {

        val TAG = MainActivity::class.java.simpleName
    }
}
