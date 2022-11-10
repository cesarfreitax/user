package com.cesar.user.utils

import android.content.res.Resources
import android.widget.AutoCompleteTextView
import android.widget.EditText
import com.cesar.user.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

fun String.cpfMask(cpfAux: String, editText: TextInputEditText) : String {

    var cpfMasked = this

    if (this.length == 3 && cpfAux.length <= this.length) {
        cpfMasked = "${this}."
        editText.setText(cpfMasked)
    }
    if (this.length == 7 && cpfAux.length <= this.length) {
        cpfMasked = "${this}."
        editText.setText(cpfMasked)
    }
    if (this.length == 11 && cpfAux.length <= this.length) {
        cpfMasked = "${this}-"
        editText.setText(cpfMasked)
    }
    if (this.length > 14 && cpfAux.length <= this.length) {
        cpfMasked = cpfMasked.substring(0, 14)
        editText.setText(cpfMasked)
    }
    if (this.length < 14) {
        editText.setTextColor(editText.resources.getColor(R.color.red))
    } else {
        editText.setTextColor(editText.resources.getColor(R.color.green))
    }

    editText.setSelection(editText.length())

    return cpfMasked
}

fun String.phoneMask(phoneAux: String, editText: TextInputEditText) : String {
    var phoneMasked = this

    if (this.length == 2 && phoneAux.length < this.length && this.first().toString() != "(") {
        phoneMasked = "($this) "
        editText.setText(phoneMasked)
    }
    if (this.length == 3 && phoneAux.length < this.length) {
        phoneMasked = "$this) "
        editText.setText(phoneMasked)
    }
    if (this.length == 4 && phoneAux.length < this.length) {
        phoneMasked = "${this.substring(0, 2)}) ${this.substring(2, 3)}"
    }
    if (this.length == 10 && phoneAux.length <= this.length) {
        phoneMasked = "$this-"
        editText.setText(phoneMasked)
    }
    if (this.length == 11 && phoneAux.length < this.length && this.last().toString() != "-") {
        phoneMasked = "${this.substring(0, 10)}-${this.substring(10, 11)}"
        editText.setText(phoneMasked)
    }
    if (this.length > 15 && phoneAux.length <= this.length) {
        phoneMasked = phoneMasked.substring(0, 15)
        editText.setText(phoneMasked)
    }
    if (this.length < 15) {
        editText.setTextColor(editText.resources.getColor(R.color.red))
    } else {
        editText.setTextColor(editText.resources.getColor(R.color.green))
    }

    editText.setSelection(editText.length())

    return phoneMasked
}

fun String.nameMask (editText: AutoCompleteTextView) {
    if (this.length < 3) {
        editText.setTextColor(editText.resources.getColor(R.color.red))
    } else {
        editText.setTextColor(editText.resources.getColor(R.color.green))
    }
}

fun String.emailMask (editText: EditText) {
    if (this.length > 5 && !this.hasUpperCase()) {
        editText.setTextColor(editText.resources.getColor(R.color.green))
    } else {
        editText.setTextColor(editText.resources.getColor(R.color.red))
    }
}

fun String.isLongEnough() = length >= 8
fun String.hasEnoughDigits() = count(Char::isDigit) >= 3
fun String.hasUpperCase() = any(Char::isUpperCase)
fun String.hasSpecialChar() = any { it in "@#$%ˆˆ*()" }

val requirements = listOf(String::isLongEnough, String::hasEnoughDigits, String::hasUpperCase, String::hasSpecialChar)
val String.checkRequirements get() = requirements.all { check -> check(this) }

fun String.passwordMask (editText: TextInputEditText) {
    if (this.checkRequirements) {
        editText.setTextColor(editText.resources.getColor(R.color.green))
    } else {
        editText.setTextColor(editText.resources.getColor(R.color.red))
    }
}

fun String.notEmptyMask (editText: TextInputEditText) {
    if (this.isNotEmpty()) {
        editText.setTextColor(editText.resources.getColor(R.color.green))
    }

}

