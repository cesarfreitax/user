package com.cesar.user

import android.app.Activity
import android.app.AlertDialog.THEME_HOLO_DARK
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.cesar.user.utils.OnDateSetListenerWithDateTreatmentImpl
import com.cesar.user.utils.cpfMask
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Calendar

private const val REQUEST_CODE_GALLERY = 1001
private const val REQUEST_CODE_CAMERA = 1002

class RegisterActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    lateinit var img: ImageView
    lateinit var emailContainer: TextInputLayout
    lateinit var birth: TextInputEditText
    lateinit var gender: TextInputEditText
    lateinit var maritalState: Spinner
    lateinit var cpf: TextInputEditText

    var cpfAux = ""

    private val genderList = arrayOf("Masculino", "Feminino", "Outros")
    private val maritalStateList = arrayOf("Estado civil", "Solteira", "Casada", "Divorciada", "Viúva")

    var teste = ""

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        img = findViewById(R.id.register_img)
        birth = findViewById(R.id.register_birth)
        gender = findViewById(R.id.register_gender)
        maritalState = findViewById(R.id.register_marital_status)
        emailContainer = findViewById(R.id.register_email_container)
        cpf = findViewById(R.id.register_cpf)
//        emailContainer.suffixText = "@gmail.com"

        pickDate()
        showGenderListDialog()
//        openGalleryForImage()
//        openCapturePhotoForImage()
        showChooseImageMethod()

        cpf.addTextChangedListener {
            cpfAux = it.toString().cpfMask(cpfAux, cpf)
        }

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, maritalStateList)
        maritalState.adapter = arrayAdapter
        maritalState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(applicationContext, "Estado civil selecionado: ${maritalStateList[position]}", Toast.LENGTH_SHORT).show()
                teste = maritalStateList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    fun selectYearDate(datePickerListener: OnDateSetListenerWithDateTreatmentImpl, context: Context) {
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(context, R.style.Theme_User, datePickerListener, year, month, day).show()
    }


    fun openCapturePhotoForImage() {
        img.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA)
        }

    }

    private fun openGalleryForImage() {
        img.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_GALLERY)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_GALLERY){
            if (intent?.data != null) {
                img.setImageURI(intent.data) // handle chosen image
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CAMERA && intent != null){
            if (intent.data != null) {
                img.setImageBitmap(intent.extras?.get("data") as Bitmap)

            }
        }
    }

    private fun showGenderListDialog() {
        gender.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Escolha seu gênero:")
            builder.setItems(genderList) { dialog, which ->
                Toast.makeText(applicationContext, genderList[which], Toast.LENGTH_SHORT).show()
                gender.setText(genderList[which])
            }

            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun showChooseImageMethod() {
        img.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Foto de perfil")
            builder.setMessage("De onde você gostaria de buscar a foto?")
                .setPositiveButton("Galeria") { _, _ ->
                    openGalleryForImage()
                }
                .setNegativeButton("Câmera") { _, _ ->
                    openCapturePhotoForImage()
                }
            val dialog = builder.create()
            dialog.show()
        }
    }


    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    private fun pickDate() {
        birth.setOnClickListener {
            selectYearDate(OnDateSetListenerWithDateTreatmentImpl { date ->
                birth.setText(date)
            }, this)
//            getDateTimeCalendar()
//
//            DatePickerDialog(this, this, year, month, day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month + 1
        savedYear = year

        getDateTimeCalendar()
        birth.setText("$savedDay-$savedMonth-$savedYear")

    }

    //APLICAR NO OLHINHO
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        return super.onTouchEvent(event)
//
//        if (onKeyDown())
//    }

}