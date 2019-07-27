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
 * Add com.gomoney.global.moneyview.MoneyTextView into your XML layouts and you are done!
 * For more information, check http://github.com/wajahatkarim3/EasyMoney-Widgets
 *
 * @author Wajahat Karim (http://wajahatkarim.com)
 * @version 1.0.0 01/20/2017
 */
class MoneyTextView : AppCompatTextView {

    private var _currencySymbol: String? = null
    private var _showCurrency: Boolean = false
    private var _showCommas: Boolean = false

    /**
     * Get the value of the text without any commas and currency.
     * For example, if the edit text value is $ 1,34,000.60 then this method will return 134000.60
     *
     * @return A string of the raw value in the text field
     */
    val valueString: String
        get() {

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

    /**
     * Whether currency is shown in the text or not. (Default is true)
     *
     * @return true if the currency is shown otherwise false.
     */
    var isShowCurrency: Boolean
        get() = _showCurrency
        set(value) {
            _showCurrency = value
            setValue(text.toString())
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
        _showCurrency = true
        _showCommas = true

        // Check for the attributes
        if (attrs != null) {
            // Attribute initialization
            val attrArray = context.obtainStyledAttributes(attrs, R.styleable.MoneyTextView, 0, 0)
            try {
                var currnecy = attrArray.getString(R.styleable.MoneyTextView_currency_symbol)
                if (currnecy == null)
                    currnecy = Currency.getInstance(Locale.getDefault()).symbol
                setCurrency(currnecy)

                _showCurrency = attrArray.getBoolean(R.styleable.MoneyTextView_show_currency, true)
                _showCommas = attrArray.getBoolean(R.styleable.MoneyTextView_show_commas, true)
            } finally {
                attrArray.recycle()
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setValue(text.toString())
    }

    private fun getDecoratedStringFromNumber(number: Long): String {
        val numberPattern = "#,###,###,###"
        val formatter = DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat
        formatter.applyPattern("$_currencySymbol $numberPattern")
        return formatter.format(number)
    }

    private fun setValue(valueStr: String) {
        try {
            val originalString = valueString
            val longVal = java.lang.Long.parseLong(originalString)
            val formattedString = getDecoratedStringFromNumber(longVal)

            //setting text after format to EditText
            text = formattedString

        } catch (nfe: NumberFormatException) {
            nfe.printStackTrace()
            text = valueStr

            val valStr = valueString

            if (valStr == "") {
                val `val`: Long = 0
                text = getDecoratedStringFromNumber(`val`)
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
                        text = "$front."
                    } else {
                        val nums = valueString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val front = getDecoratedStringFromNumber(java.lang.Long.parseLong(nums[0]))
                        val finalText = front + "." + nums[1]
                        val spannableString = SpannableString(finalText)
                        spannableString.setSpan(
                            RelativeSizeSpan(0.7f),
                            front.length,
                            finalText.length,
                            Spannable.SPAN_PRIORITY
                        )
                        text = spannableString
                    }
                }
            }
        }

    }

    /**
     * Set the currency symbol for the edit text. (Default is US Dollar $).
     *
     * @param newSymbol the new symbol of currency in string
     */
    fun setCurrency(newSymbol: String?) {
        _currencySymbol = newSymbol
        setValue(text.toString())
    }

    /**
     * Set the currency symbol for the edit text. (Default is US Dollar $).
     *
     * @param locale the locale of new symbol. (Defaul is Locale.US)
     */
    fun setCurrency(locale: Locale) {
        setCurrency(Currency.getInstance(locale).symbol)
    }

    /**
     * Set the currency symbol for the edit text. (Default is US Dollar $).
     *
     * @param currency the currency object of new symbol. (Defaul is Locale.US)
     */
    fun setCurrency(currency: Currency) {
        setCurrency(currency.symbol)
    }

    /**
     * Shows the currency in the text. (Default is shown).
     */
    fun showCurrencySymbol() {
        isShowCurrency = true
    }

    /**
     * Hides the currency in the text. (Default is shown).
     */
    fun hideCurrencySymbol() {
        isShowCurrency = false
    }

    /**
     * Shows the commas in the text. (Default is shown).
     */
    fun showCommas() {
        _showCommas = true
        setValue(text.toString())
    }

    /**
     * Hides the commas in the text. (Default is shown).
     */
    fun hideCommas() {
        _showCommas = false
        setValue(text.toString())
    }
}
