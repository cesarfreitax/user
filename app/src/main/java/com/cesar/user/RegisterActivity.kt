package com.cesar.user

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isNotEmpty
import androidx.core.widget.addTextChangedListener
import com.cesar.user.databinding.RegisterBinding
import com.cesar.user.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val REQUEST_CODE_GALLERY = 1001
private const val REQUEST_CODE_CAMERA = 1002

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { RegisterBinding.inflate(layoutInflater) }

    private lateinit var bottomSheet: TextInputEditText
    private lateinit var photoExtra: File
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
    private lateinit var treatmentExtra: String

    private var cpfAux = ""
    private var phoneAux = ""
    private var maritalStateAux = ""
    private var maritalStateAuxIndice = 0
    private var treatmentAux = ""
    private var formatDate = SimpleDateFormat("dd/MM/y", Locale.US)

    private val genderList = arrayOf("Masculino", "Feminino", "Outros")
    private val maritalStateList = arrayOf("Estado civil", "Solteira", "Casada", "Divorciada", "Viúva")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setBottomSheetBinding()
        showGenderListDialog()
        showChooseImageMethod()
        setMasks()
        maritalStateAdapter()
        setDatePickerDialog()
        setTimePickerDialog()
        getStringExtra()
        setUpdate()
        setSaveBtn()
        setNameSuggestionsAdapter()
        setSetBtnValidation()
        setTreatmentBottomSheet()
    }

    // When the user clicks at the treatment input, open the bottom sheet with the options
    private fun setTreatmentBottomSheet() {
        bottomSheet.setOnClickListener {
            binding.registerTreatment.setText("")
            val bottomSheetDialog = setBottomSheetDialog()
            val bottomSheetView = setBottomSheetView()
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
            val radiogroup = bottomSheetView.findViewById<RadioGroup>(R.id.radiogroup)
            radiogroup.setOnCheckedChangeListener { _, _ ->
                chekedRadioButtonHandler(radiogroup, bottomSheetDialog)
            }

        }
    }

    private fun chekedRadioButtonHandler(radiogroup: RadioGroup, bottomSheetDialog: BottomSheetDialog) {
        treatmentAux = radiogroup.findViewById<RadioButton>(radiogroup.checkedRadioButtonId).text.toString()
        binding.registerTreatment.setText(treatmentAux)
        bottomSheetDialog.dismiss()
    }

    private fun setBottomSheetView() = LayoutInflater.from(applicationContext).inflate(
        R.layout.bottomsheet_treatment,
        findViewById<LinearLayout>(R.id.bottomsheet)
    )

    private fun setBottomSheetDialog() = BottomSheetDialog(this, com.google.android.material.R.style.Theme_Design_Light_BottomSheetDialog)

    // When the user cliks on save btn, save the inputs in shared preferences and navigate to home
    private fun setSaveBtn() {
        binding.registerSave.setOnClickListener {
            saveOnSharedPreferences()
            navigationToHome()
            finish()
        }
    }

    // Navigate to home
    private fun navigationToHome() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    // Set the name suggestions adapter to auto fill input with the list name
    private fun setNameSuggestionsAdapter() {
        val names = resources.getStringArray(R.array.Nomes)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, names)
        binding.registerName.setAdapter(adapter)
    }

    // Get the user values from shared preferences
    private fun getStringExtra() {
        photoExtra = File(filesDir, "foto.jpg")
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
        treatmentExtra = intent?.getStringExtra("treatment").toString()
        listExtra = listOf(
            nameExtra,
            emailExtra,
            passwordExtra,
            birthExtra,
            genderExtra,
            cpfExtra,
            phoneExtra,
            availableHourExtra,
            treatmentExtra
        )
    }

    // If the user clicked on update, fill the inputs with their values
    private fun setUpdate() {
        if (listExtra[0] != "null") {
            binding.registerImg.setImageDrawable(Drawable.createFromPath(photoExtra.toString()))
            binding.registerName.setText(nameExtra)
            binding.registerEmail.setText(emailExtra)
            binding.registerPassword.setText(passwordExtra)
            binding.registerBirth.setText(birthExtra)
            binding.registerGender.setText(genderExtra)
            binding.registerCpf.setText(cpfExtra)
            binding.registerPhone.setText(phoneExtra)
            binding.registerAvailableHour.setText(availableHourExtra)
            binding.registerMaritalState.setSelection(maritalStateIndiceExtra.toInt())
            binding.registerTreatment.setText(treatmentExtra)
        }
    }

    // Set TimePickerDialog to capture user available hour
    private fun setTimePickerDialog() {
        binding.registerAvailableHour.setOnClickListener {
            val getTime = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                { _: TimePicker, hour: Int, minute: Int ->
                    val minuteFormatted: String = oneDigitTimeHandler(minute)
                    val hourFormatted: String = oneDigitTimeHandler(hour)
                    binding.registerAvailableHour.setText("$hourFormatted:$minuteFormatted")
                },
                getTime.get(Calendar.HOUR),
                getTime.get(Calendar.MINUTE),
                true
            )
            timePicker.show()
        }
    }

    // Handle with one digit number for example 1:15 to 01:15 or 10:5 to 10:50
    private fun oneDigitTimeHandler(minute: Int) =
        if (minute < 10) "0${minute}" else minute.toString()

    // Set DatePickerDialog to capture user birth
    private fun setDatePickerDialog() {
        binding.registerBirth.setOnClickListener {
            val getDate = Calendar.getInstance()
            val datepicker = DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                { _, year, month, dayOfMonth ->

                    val selectDate = Calendar.getInstance()
                    selectDate.set(Calendar.YEAR, year)
                    selectDate.set(Calendar.MONTH, month)
                    selectDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    binding.registerBirth.setText(formatDate.format(selectDate.time))

                },
                getDate.get(Calendar.YEAR),
                getDate.get(Calendar.MONTH),
                getDate.get(Calendar.DAY_OF_MONTH)
            )
            datepicker.show()
        }
    }

    // Save the inputs in the phone shared preferences
    private fun saveOnSharedPreferences() {
        val sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE)
        sharedPref.edit().putString("name", binding.registerName.text.toString()).apply()
        sharedPref.edit().putString("email", binding.registerEmail.text.toString()).apply()
        sharedPref.edit().putString("password", binding.registerPassword.text.toString()).apply()
        sharedPref.edit().putString("birth", binding.registerBirth.text.toString()).apply()
        sharedPref.edit().putString("gender", binding.registerGender.text.toString()).apply()
        sharedPref.edit().putString("maritalState", maritalStateAux).apply()
        sharedPref.edit().putString("maritalStateIndice", maritalStateAuxIndice.toString()).apply()
        sharedPref.edit().putString("cpf", binding.registerCpf.text.toString()).apply()
        sharedPref.edit().putString("phone", binding.registerPhone.text.toString()).apply()
        sharedPref.edit().putString("availableHour", binding.registerAvailableHour.text.toString()).apply()
        sharedPref.edit().putString("treatment", treatmentAux).apply()
    }

    // Create adapter for spinner dropdown with an option list
    private fun maritalStateAdapter() {
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, maritalStateList)
        binding.registerMaritalState.adapter = arrayAdapter

        binding.registerMaritalState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val text: TextView = parent?.getChildAt(0) as TextView
                if (text.text != "Estado civil") {
                    text.setTextColor(text.resources.getColor(R.color.black))
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
    private fun setBottomSheetBinding() {
        bottomSheet = findViewById(R.id.register_treatment)
    }

    // Open the camera
    fun openCapturePhotoForImage() {
        binding.registerImg.setOnClickListener {
            openTheCamera()
        }

    }

    private fun openTheCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA)
    }

    // Open the gallery
    private fun openGalleryForImage() {
        binding.registerImg.setOnClickListener {
            openTheGallery()
        }

    }

    private fun openTheGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    // Check the requests and set the images
    @RequiresApi(Build.VERSION_CODES.P)
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_GALLERY){
            if (intent?.data != null) {
                binding.registerImg.setImageURI(intent.data) // handle chosen image

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
                binding.registerImg.setImageBitmap(intent.extras?.get("data") as Bitmap)

            }
        }
    }

    // Open a gender list option dialog
    private fun showGenderListDialog() {
        binding.registerGender.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Escolha seu gênero:")
            builder.setItems(genderList) { dialog, which ->
                Toast.makeText(applicationContext, genderList[which], Toast.LENGTH_SHORT).show()
                binding.registerGender.setText(genderList[which])
            }

            val dialog = builder.create()
            dialog.show()
        }
    }

    // Open an option dialog -> GALLERY or CAMERA
    private fun showChooseImageMethod() {
        binding.registerImg.setOnClickListener {
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
        binding.registerName.addTextChangedListener {
            it.toString().nameMask(binding.registerName)
            setSetBtnValidation()
        }

        binding.registerEmail.addTextChangedListener {
            it.toString().emailMask(binding.registerEmail)
            setSetBtnValidation()
        }

        binding.registerPassword.addTextChangedListener {
            it.toString().passwordMask(binding.registerPassword)
            setSetBtnValidation()
        }

        binding.registerBirth.addTextChangedListener {
            it.toString().notEmptyMask(binding.registerBirth)
            setSetBtnValidation()
        }

        binding.registerGender.addTextChangedListener {
            it.toString().notEmptyMask(binding.registerGender)
            setSetBtnValidation()
        }

        binding.registerCpf.addTextChangedListener {
            cpfAux = it.toString().cpfMask(cpfAux, binding.registerCpf)
            setSetBtnValidation()
        }

        binding.registerPhone.addTextChangedListener {
            phoneAux = it.toString().phoneMask(phoneAux, binding.registerPhone)
            setSetBtnValidation()
        }

        binding.registerAvailableHour.addTextChangedListener {
            it.toString().notEmptyMask(binding.registerAvailableHour)
            setSetBtnValidation()
        }

        binding.registerTreatment.addTextChangedListener {
            it.toString().notEmptyMask(binding.registerTreatment)
            setSetBtnValidation()
        }

    }

    // Change clickable and color state if the inputs are filled
    private fun setSetBtnValidation() {
        if (binding.registerName.text.length > 3 && binding.registerEmail.text.length > 5 && !binding.registerEmail.text.any(Char::isUpperCase) && binding.registerPassword.text.toString().checkRequirements && binding.registerBirth.text.toString().isNotEmpty() && binding.registerGender.text.toString().isNotEmpty() && binding.registerMaritalState.isNotEmpty() && binding.registerCpf.text.toString().length == 14 && binding.registerPhone.text.toString().length == 15 && binding.registerAvailableHour.text.toString().isNotEmpty() && binding.registerTreatment.text.toString().isNotEmpty()) {
            binding.registerSave.backgroundTintList = getColorStateList(R.color.black)
            binding.registerSave.setTextColor(binding.registerSave.resources.getColor(R.color.green_200))
            binding.registerSave.isClickable = true
        } else {
            binding.registerSave.backgroundTintList = getColorStateList(R.color.white)
            binding.registerSave.setTextColor(binding.registerSave.resources.getColor(R.color.black))
            binding.registerSave.isClickable = false
        }
    }
}