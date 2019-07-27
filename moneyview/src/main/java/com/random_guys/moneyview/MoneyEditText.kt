package com.random_guys.moneyview

import android.content.Context
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.RelativeSizeSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import java.text.DecimalFormat
import java.util.*

/**
 * The EditText widget for support of money requirements like currency, number formatting, comma formatting etc.
 *
 *
 * Add com.random_guys.moneyview.MoneyEditText into your XML layouts and you are done!
 * For more information, check https://github.com/random-guys/MoneyView
 *
 * @author Wajahat Karim (http://wajahatkarim.com)
 * @author Raymond Tukpe
 * @version 1.0.0 01/20/2017
 * @version 1.1.0 27/07/2019
 */
class MoneyEditText : AppCompatEditText {

    companion object {
        @JvmStatic
        val TAG = MoneyEditText::class.java.simpleName
    }

    private var _currencySymbol: String? = null

    /**
     * Get the value of the text without any commas and currency.
     * For example, if the edit text value is $ 1,34,000.60 then this method will return 134000.60
     *
     * @return A string of the raw value in the text field
     */
    val valueString: String
        get() {

            var string = text!!.toString()

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
        get() = text!!.toString()

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        updateValue(text!!.toString())
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        // Setting Default Parameters
        _currencySymbol = Currency.getInstance(Locale.getDefault()).symbol

        // Check for the attributes
        if (attrs != null) {
            // Attribute initialization
            val attrArray = context.obtainStyledAttributes(attrs, R.styleable.MoneyEditText, 0, 0)
            try {
                var currency = attrArray.getString(R.styleable.MoneyEditText_currency_symbol)
                if (currency == null)
                    currency = Currency.getInstance(Locale.getDefault()).symbol
                setCurrency(currency)
            } finally {
                attrArray.recycle()
            }
        }

        // Add Text Watcher for Decimal formatting
        initTextWatchers()
    }

    private fun initTextWatchers() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                this@MoneyEditText.removeTextChangedListener(this)
                val backupString = charSequence.toString()

                try {
                    val originalString = valueString
                    val longVal = java.lang.Long.parseLong(originalString)
                    val formattedString = getDecoratedStringFromNumber(longVal)

                    //setting text after format to EditText
                    setText(formattedString)
                    setSelection(text!!.length)

                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                    setText(backupString)

                    val valStr = valueString

                    if (valStr == "") {
                        val valueNumber: Long = 0
                        setText(getDecoratedStringFromNumber(valueNumber))
                    } else {
                        // Some decimal number
                        if (valStr.contains(".")) {
                            if (valStr.indexOf(".") == valStr.length - 1) {
                                // decimal has been currently put
                                val front = getDecoratedStringFromNumber(
                                    java.lang.Long.parseLong(
                                        valStr.substring(
                                            0,
                                            valStr.length - 1
                                        )
                                    )
                                )
                                setText("$front.")
                            } else {
                                val nums: Array<String> =
                                    valueString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                // the decimal part of the string can only have two digits
                                // move the extra string to the whole number part
                                if (nums[1].length > 2) {
                                    nums[0] = nums[0].plus(nums[1][0])
                                    nums[1] = nums[1].substring(1)
                                }

                                val front = getDecoratedStringFromNumber(java.lang.Long.parseLong(nums[0]))
                                val finalText = front + "." + nums[1]
                                val spannableString = SpannableString(finalText)
                                spannableString.setSpan(
                                    RelativeSizeSpan(0.7f),
                                    front.length,
                                    finalText.length,
                                    Spannable.SPAN_PRIORITY
                                )
                                setText(spannableString)
                            }
                        }
                    }
                    setSelection(text?.length!!)
                }

                this@MoneyEditText.addTextChangedListener(this)

            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    private fun updateValue(text: String) {
        setText(text)
    }

    private fun getDecoratedStringFromNumber(number: Long): String {
        val numberPattern = "#,###,###,###"
        val formatter = DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat
        formatter.applyPattern("$_currencySymbol $numberPattern")
        return formatter.format(number)
    }

    /**
     * Set the currency symbol for the edit text. (Default is US Dollar $).
     *
     * @param newSymbol the new symbol of currency in string
     */
    fun setCurrency(newSymbol: String?) {
        _currencySymbol = newSymbol
        updateValue(text!!.toString())
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
