package app.android.snappay.util

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan

object SpannableStringUtils {

    fun getSpannableString(unformattedString: String, capsCurrencyCode: String, amount: String): SpannableString {
        val availableBalanceSubLabelSpanStr = SpannableString(String.format(unformattedString, capsCurrencyCode, amount))
        availableBalanceSubLabelSpanStr.setSpan(
                StyleSpan(Typeface.BOLD),
                availableBalanceSubLabelSpanStr.indexOf(capsCurrencyCode),
                availableBalanceSubLabelSpanStr.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        availableBalanceSubLabelSpanStr.setSpan(
                RelativeSizeSpan(0.7f),
                availableBalanceSubLabelSpanStr.indexOf(capsCurrencyCode),
                availableBalanceSubLabelSpanStr.indexOf(capsCurrencyCode) + capsCurrencyCode.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )

        return availableBalanceSubLabelSpanStr
    }

    fun getSpannableString(unformattedString: String, currencyCode: String, amount: String, userName: String): SpannableString {
        val availableBalanceSubLabelSpanStr = SpannableString(String.format(unformattedString, currencyCode, amount, userName))
        availableBalanceSubLabelSpanStr.setSpan(
                StyleSpan(Typeface.BOLD),
                availableBalanceSubLabelSpanStr.indexOf(currencyCode),
                availableBalanceSubLabelSpanStr.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        availableBalanceSubLabelSpanStr.setSpan(
                RelativeSizeSpan(0.7f),
                availableBalanceSubLabelSpanStr.indexOf(currencyCode),
                availableBalanceSubLabelSpanStr.indexOf(currencyCode) + currencyCode.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )

        return availableBalanceSubLabelSpanStr
    }
}