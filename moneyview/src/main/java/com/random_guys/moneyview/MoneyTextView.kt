package com.random_guys.moneyview

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import java.text.DecimalFormat
import java.util.*

/**
 * The TextView widget for support of money requirements like currency, number formatting, comma formatting etc.
 *
 *
 * Add com.random_guys.moneyview.MoneyTextView into your XML layouts and you are done!
 * For more information, check https://github.com/random-guys/MoneyView
 *
 * @author Wajahat Karim (http://wajahatkarim.com)
 * @author Raymond Tukpe
 * @version 1.0.0 01/20/2017
 * @version 1.1.0 27/07/2019
 */
class MoneyTextView : AppCompatTextView {

    private var _currencySymbol: String? = null

    fun setMoneyText(valueStr: String) {
        val (_text, _spannable) = setValue(valueStr)
        text = if (_spannable.isEmpty()) _text else _spannable
    }

    /**
     * Get the value of the text without any commas and currency.
     * For example, if the edit text value is $ 1,034,000.60 then this method will return 134000.60
     *
     * @return A string of the raw value in the text field
     */
    fun valueString(): String {
        var string = text.toString()

        if (string.contains(",")) {
            string = string.replace(",", "")
        }
        if (string.contains(" ")) {
            string = string.substring(string.indexOf(" ") + 1, string.length)
        }
        return string
    }

    /**
     * Get the value of the text without any commas and currency.
     * For example, if the edit text value is $ 1,034,000.60 then this method will return 134000.60
     *
     * @param {@link String} text
     * @return A string of the raw value in the text field
     */
    private fun valueString(text: String): String {
        var string = text

        if (string.contains(",")) {
            string = string.replace(",", "")
        }
        if (string.contains(" ")) {
            string = string.substring(string.indexOf(" ") + 1, string.length)
        }
        return string
    }

    /**
     * Get the value of the text with formatted commas and currency.
     * For example, if the edit text value is $ 1,34,000.60 then this method will return exactly $ 1,34,000.60
     *
     * @return A string of the text value in the text field
     */
    val formattedString: String
        get() {
            setValue(text.toString())
            return text.toString()
        }

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        // Setting Default Parameters
        _currencySymbol = Currency.getInstance(Locale.getDefault()).symbol

        // Check for the attributes
        if (attrs != null) {
            // Attribute initialization
            val attrArray = context.obtainStyledAttributes(attrs, R.styleable.MoneyTextView, 0, 0)
            try {
                var currency = attrArray.getString(R.styleable.MoneyTextView_currency_symbol)
                if (currency == null) currency = Currency.getInstance(Locale.getDefault()).symbol
                setCurrency(currency)
            } finally {
                attrArray.recycle()
            }
        }
    }

    private fun getDecoratedStringFromNumber(number: Long): String {
        val numberPattern = "#,###,###,###"
        val formatter = DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat
        formatter.applyPattern("$_currencySymbol $numberPattern")
        return formatter.format(number)
    }

    private fun setValue(valueStr: String): Pair<String, SpannableString> {
        var _text = ""
        var _spannableString = SpannableString("")
        try {
            val originalString = valueString(valueStr)
            val longVal = java.lang.Long.parseLong(originalString)
            val formattedString = getDecoratedStringFromNumber(longVal)

            //setting text after format to EditText
            _text = formattedString
        } catch (nfe: Throwable) {
            nfe.printStackTrace()

            if (valueStr == "") {
                _text = getDecoratedStringFromNumber(0L)
            } else {
                // Some decimal number
                if (valueStr.contains(".")) {
                    if (valueStr.indexOf(".") == valueStr.length - 1) {
                        // decimal has been currently put
                        val front = getDecoratedStringFromNumber(
                            java.lang.Long.parseLong(
                                valueString(valueStr).substring(
                                    0,
                                    valueString(valueStr).length - 1
                                )
                            )
                        )
                        _text = "$front."
                    } else {
                        val nums =
                            valueString(valueStr).split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val front = getDecoratedStringFromNumber(java.lang.Long.parseLong(nums[0]))
                        val finalText = front + "." + nums[1]
                        val spannableString = SpannableString(finalText)
                        spannableString.setSpan(
                            RelativeSizeSpan(0.7f),
                            front.length,
                            finalText.length,
                            Spannable.SPAN_PRIORITY
                        )
                        _spannableString = spannableString
                    }
                }
            }
        }
        return Pair(_text, _spannableString)
    }

    /**
     * Set the currency symbol for the edit text. (Default is US Dollar $).
     *
     * @param newSymbol the new symbol of currency in string
     */
    fun setCurrency(newSymbol: String?) {
        _currencySymbol = newSymbol
    }

    /**
     * Set the currency symbol for the edit text. (Default is US Dollar $).
     *
     * @param locale the locale of new symbol. (Default is Locale.US)
     */
    fun setCurrency(locale: Locale) {
        setCurrency(Currency.getInstance(locale).symbol)
    }

    /**
     * Set the currency symbol for the edit text. (Default is US Dollar $).
     *
     * @param currency the currency object of new symbol. (Default is Locale.US)
     */
    fun setCurrency(currency: Currency) {
        setCurrency(currency.symbol)
    }
}
