package com.cesar.user

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isNotEmpty
import androidx.core.widget.addTextChangedListener
import com.cesar.user.utils.*
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val REQUEST_CODE_GALLERY = 1001
private const val REQUEST_CODE_CAMERA = 1002

class RegisterActivity : AppCompatActivity() {

    lateinit var img: ImageView
    lateinit var email: EditText
    lateinit var password: TextInputEditText
    lateinit var birth: TextInputEditText
    lateinit var gender: TextInputEditText
    lateinit var maritalState: Spinner
    lateinit var cpf: TextInputEditText
    lateinit var phone: TextInputEditText
    lateinit var name: AutoCompleteTextView
    lateinit var availableHour: TextInputEditText
    lateinit var saveBtn: Button

    private lateinit var nameExtra: String
    private lateinit var emailExtra: String
    private lateinit var passwordExtra: String
    private lateinit var birthExtra: String
    private lateinit var genderExtra: String
    private lateinit var maritalStateExtra: String
    private lateinit var maritalStateIndiceExtra: String
    private lateinit var cpfExtra: String
    private lateinit var phoneExtra: String
    private lateinit var availableHourExtra: String
    private lateinit var listExtra: List<String>

    private var cpfAux = ""
    private var phoneAux = ""
    private var maritalStateAux = ""
    private var maritalStateAuxIndice = 0
    private var finished = 0
    private var formatDate = SimpleDateFormat("dd/MM/y", Locale.US)
    private var formatTime = SimpleDateFormat("HH:mm", Locale.US)

    private val genderList = arrayOf("Masculino", "Feminino", "Outros")
    private val maritalStateList = arrayOf("Estado civil", "Solteira", "Casada", "Divorciada", "Viúva")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        setComponentBinding()
        showGenderListDialog()
        showChooseImageMethod()
        setMasks()
        maritalStateAdapter()
        setDatePickerDialog()
        setTimePickerDialog()
        setGetStringExtra()
        setUpdate()
        setSaveBtn()
        setNameSuggestionsAdapter()
    }

    private fun setSaveBtn() {
        saveBtn.setOnClickListener {
            sharedPreferences()
            val intent = Intent(this, MenuHamburguerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setNameSuggestionsAdapter() {
        val names = resources.getStringArray(R.array.Nomes)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, names)
        name.setAdapter(adapter)
    }

    private fun setGetStringExtra() {
        nameExtra = intent?.getStringExtra("name").toString()
        emailExtra = intent?.getStringExtra("email").toString()
        passwordExtra = intent?.getStringExtra("password").toString()
        birthExtra = intent?.getStringExtra("birth").toString()
        genderExtra = intent?.getStringExtra("gender").toString()
        maritalStateExtra = intent?.getStringExtra("maritalState").toString()
        maritalStateIndiceExtra = intent?.getStringExtra("maritalStateIndice").toString()
        cpfExtra = intent?.getStringExtra("cpf").toString()
        phoneExtra = intent?.getStringExtra("phone").toString()
        availableHourExtra = intent?.getStringExtra("availableHour").toString()
        listExtra = listOf(
            nameExtra,
            emailExtra,
            passwordExtra,
            birthExtra,
            genderExtra,
            cpfExtra,
            phoneExtra,
            availableHourExtra
        )
    }

    private fun setUpdate() {
        if (listExtra[0] != "null") {
            name.setText(nameExtra)
            email.setText(emailExtra)
            password.setText(passwordExtra)
            birth.setText(birthExtra)
            gender.setText(genderExtra)
            // MARITAL STATE
            cpf.setText(cpfExtra)
            phone.setText(phoneExtra)
            availableHour.setText(availableHourExtra)
            maritalStateList[0] = maritalStateExtra
            maritalStateList[maritalStateIndiceExtra.toInt()] = "Estado civil"
        }
    }

    private fun setTimePickerDialog() {
        availableHour.setOnClickListener {
            val getTime = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                { _: TimePicker, hour: Int, minute: Int ->

                    val selectTime = Calendar.getInstance()
                    selectTime.set(Calendar.HOUR, hour-12)
                    selectTime.set(Calendar.MINUTE, minute)

                    availableHour.setText(formatTime.format(selectTime.time))

                },
                getTime.get(Calendar.HOUR),
                getTime.get(Calendar.MINUTE),
                true
            )
            timePicker.show()
        }
    }

    // Set DatePickerDialog to capture user birth
    private fun setDatePickerDialog() {
        birth.setOnClickListener {
            val getDate = Calendar.getInstance()
            val datepicker = DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                { _, year, month, dayOfMonth ->

                    val selectDate = Calendar.getInstance()
                    selectDate.set(Calendar.YEAR, year)
                    selectDate.set(Calendar.MONTH, month)
                    selectDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    birth.setText(formatDate.format(selectDate.time))

                },
                getDate.get(Calendar.YEAR),
                getDate.get(Calendar.MONTH),
                getDate.get(Calendar.DAY_OF_MONTH)
            )
            datepicker.show()
        }
    }

    private fun sharedPreferences() {
        val sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE)
        sharedPref.edit().putString("name", name.text.toString()).apply()
        sharedPref.edit().putString("email", email.text.toString()).apply()
        sharedPref.edit().putString("password", password.text.toString()).apply()
        sharedPref.edit().putString("birth", birth.text.toString()).apply()
        sharedPref.edit().putString("gender", gender.text.toString()).apply()
        sharedPref.edit().putString("maritalState", maritalStateAux).apply()
        sharedPref.edit().putString("maritalStateIndice", maritalStateAuxIndice.toString()).apply()
        sharedPref.edit().putString("cpf", cpf.text.toString()).apply()
        sharedPref.edit().putString("phone", phone.text.toString()).apply()
        sharedPref.edit().putString("availableHour", availableHour.text.toString()).apply()
    }

    // Create adapter for spinner dropdown with an option list
    private fun maritalStateAdapter() {
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, maritalStateList)
        maritalState.adapter = arrayAdapter

        maritalState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val text: TextView = parent?.getChildAt(0) as TextView
                if (text.text != "Estado civil") {
                    text.setTextColor(text.resources.getColor(R.color.green))
                }
                maritalStateAux = maritalStateList[position]
                maritalStateAuxIndice = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    // Set binding according with the view
    private fun setComponentBinding() {
        img = findViewById(R.id.register_img)
        birth = findViewById(R.id.register_birth)
        gender = findViewById(R.id.register_gender)
        maritalState = findViewById(R.id.register_marital_status)
        email = findViewById(R.id.register_email)
        password = findViewById(R.id.register_password)
        cpf = findViewById(R.id.register_cpf)
        phone = findViewById(R.id.register_phone)
        name = findViewById(R.id.register_name)
        availableHour = findViewById(R.id.register_available_hour)
        saveBtn = findViewById(R.id.register_save)
    }

    // Open the camera
    fun openCapturePhotoForImage() {
        img.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA)
        }

    }

    // Open the gallery
    private fun openGalleryForImage() {
        img.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_GALLERY)
        }

    }

    // Check the requests and set the images
    @RequiresApi(Build.VERSION_CODES.P)
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_GALLERY){
            if (intent?.data != null) {
                img.setImageURI(intent.data) // handle chosen image

                val imageUri: Uri = intent.data!!
                val source = ImageDecoder.createSource(contentResolver, imageUri)
                val bitmap = ImageDecoder.decodeBitmap(source)
                val file = File(filesDir, "foto.jpg")
                file.outputStream().use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CAMERA && intent != null){
            if (intent.data != null) {
                img.setImageBitmap(intent.extras?.get("data") as Bitmap)

            }
        }
    }

    // Open a gender list option dialog
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

    // Open an option dialog -> GALLERY or CAMERA
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

    // Set the masks for validations, formatting and visual
    private fun setMasks() {
        name.addTextChangedListener {
            it.toString().nameMask(name)
            setSetBtnValidation()
        }

        email.addTextChangedListener {
            it.toString().emailMask(email)
            setSetBtnValidation()
        }

        password.addTextChangedListener {
            it.toString().passwordMask(password)
            setSetBtnValidation()
        }

        birth.addTextChangedListener {
            it.toString().notEmptyMask(birth)
            setSetBtnValidation()
        }

        gender.addTextChangedListener {
            it.toString().notEmptyMask(gender)
            setSetBtnValidation()
        }

        cpf.addTextChangedListener {
            cpfAux = it.toString().cpfMask(cpfAux, cpf)
            setSetBtnValidation()
        }

        phone.addTextChangedListener {
            phoneAux = it.toString().phoneMask(phoneAux, phone)
            setSetBtnValidation()
        }

        availableHour.addTextChangedListener {
            it.toString().notEmptyMask(availableHour)
            setSetBtnValidation()
        }

    }

    private fun setSetBtnValidation() {
        if (name.text.length > 3 && email.text.length > 5 && password.text.toString().checkRequirements && birth.text.toString().isNotEmpty() && gender.text.toString().isNotEmpty() && maritalState.isNotEmpty() && cpf.text.toString().length == 14 && phone.text.toString().length == 15 && availableHour.text.toString().isNotEmpty()) {
            saveBtn.backgroundTintList = getColorStateList(R.color.purple_500)
            saveBtn.setTextColor(saveBtn.resources.getColor(R.color.white))
            saveBtn.isClickable = true
        } else {
            saveBtn.backgroundTintList = getColorStateList(R.color.white)
            saveBtn.setTextColor(saveBtn.resources.getColor(R.color.black))
            saveBtn.isClickable = false
        }
    }

    //APLICAR NO OLHINHO
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        return super.onTouchEvent(event)
//
//        if (onKeyDown())
//    }

}