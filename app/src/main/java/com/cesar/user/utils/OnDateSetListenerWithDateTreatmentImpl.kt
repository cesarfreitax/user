package com.cesar.user.utils
import android.app.DatePickerDialog
import android.widget.DatePicker

    class OnDateSetListenerWithDateTreatmentImpl(private val callback: (date: String) -> Unit): DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            val dayString = String.format("%02d", dayOfMonth)
            val monthString = String.format("%02d", monthOfYear.plus(1))
            val date = "$dayString/$monthString/$year"

            callback(date)
        }
    }

