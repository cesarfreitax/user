package com.cesar.user.utils

import com.cesar.user.R
import com.google.android.material.textfield.TextInputEditText

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
