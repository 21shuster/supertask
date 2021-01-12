package es.eoi.supertaskag.home

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.eoi.supertaskag.DataHolder
import es.eoi.supertaskag.R
import es.eoi.supertaskag.models.Category
import es.eoi.supertaskag.models.Task
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_task.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TaskActivity : AppCompatActivity() {

    val TAG = "miapp"
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    val allCategories: ArrayList<Category> = arrayListOf()
    var idCategorySelected = ""
    var priority : Int = 0
    var dateStart: Date? = null
    var dateEnd: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        auth = Firebase.auth
        db = Firebase.firestore

        loadAllCategories()

        btn_newTask.setOnClickListener {
            createCategoryAlert()
        }

        input_categoriaTarea.setOnClickListener {
            showAllCategories()
        }

        selectPriority()

        btn_crearTarea.setOnClickListener{
            createNewTask()
        }

        input_inicioTarea.setOnClickListener {
            selectHour(true)
        }

        input_finTarea.setOnClickListener {
            selectHour()
        }

        input_fechaTarea.setOnClickListener {
            selectDate()
        }
    }

    fun selectPriority(){
        btnLow.setOnClickListener{
            btnLow.alpha = 1F
            btnMiddle.alpha = 0.5F
            btnHigh.alpha = 0.5F
            priority = 1
        }
        btnMiddle.setOnClickListener{
            btnLow.alpha = 0.5F
            btnMiddle.alpha = 1F
            btnHigh.alpha = 0.5F
            priority = 2
        }
        btnHigh.setOnClickListener{
            btnLow.alpha = 0.5F
            btnMiddle.alpha = 0.5F
            btnHigh.alpha = 1F
            priority = 3
        }
    }
    fun createCategoryInFirestore(categoryName: String) {
        val uid = auth.currentUser!!.uid

        val newCategory = Category()
        newCategory.name = categoryName

        db.collection(DataHolder.dbUsers).document(uid).collection(DataHolder.dbCategories)
            .add(newCategory).addOnSuccessListener { document ->
                Log.v(TAG, "createCategory:TODOFENOMENAL")
            }.addOnFailureListener { e ->
                Log.v(TAG, "createCategory:failure", e)
            }
    }

    fun createCategoryAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Nueva categoría")

        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.ed_txt_alert, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editText)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar") { dialogInterface, i ->

            val filter = allCategories.filter { it ->
                it.name?.toLowerCase().equals(editText.text.toString().toLowerCase())
            }
            if (filter.isNotEmpty()) {
                Toast.makeText(this, "Ya existe esa categoría", Toast.LENGTH_SHORT).show()
            } else {
                createCategoryInFirestore(editText.text.toString())
            }

        }
        builder.show()
    }

    fun loadAllCategories() {
        val uid = auth.currentUser!!.uid

        db.collection(DataHolder.dbUsers).document(uid).collection(DataHolder.dbCategories)
            .addSnapshotListener { value, e ->
                allCategories.clear()
                for (doc in value!!) {
                    val category = doc.toObject(Category::class.java)
                    category.id = doc.id
                    allCategories.add(category)
                }
            }
    }

    fun selectDate() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, { datePicker, year, month, day ->
            Log.i("miapp", "Año $year, mes $month y dia $day")

            val pickedDateTime = Calendar.getInstance()
            val startHour = pickedDateTime.get(Calendar.HOUR_OF_DAY)
            val startMinute = pickedDateTime.get(Calendar.MINUTE)

            pickedDateTime.set(year, month, day, startHour, startMinute)

            val fechaFormateada = formatDate(pickedDateTime.time, "EEEE, d 'de' MMMM 'de' YYYY")
            input_fechaTarea.setText(fechaFormateada)

            val horaInicio = formatDate(pickedDateTime.time, "HH:mm")
            input_inicioTarea.setText(horaInicio)
            dateStart = pickedDateTime.time

            val pickedDateTimeEnd = Calendar.getInstance()
            pickedDateTimeEnd.set(year, month, day, startHour + 1, startMinute)
            val horaFin = formatDate(pickedDateTimeEnd.time, "HH:mm")
            input_finTarea.setText(horaFin)
            dateEnd = pickedDateTime.time

        }, startYear, startMonth, startDay).show()
    }

    fun selectHour(isStart: Boolean = false) {

        if (dateStart != null) {

            val curTime = Calendar.getInstance()
            curTime.time = dateStart
            val startHour = curTime.get(Calendar.HOUR_OF_DAY)
            val startMinute = curTime.get(Calendar.MINUTE)
            val startYear = curTime.get(Calendar.YEAR)
            val startMonth = curTime.get(Calendar.MONTH)
            val startDay = curTime.get(Calendar.DAY_OF_MONTH)

            TimePickerDialog(this, { _, hour, minute ->

                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(startYear, startMonth, startDay, hour, minute)
                dateStart = pickedDateTime.time

                val horaFormateada = formatDate(pickedDateTime.time, "HH:mm")

                if (isStart) {
                    dateStart = pickedDateTime.time
                    input_inicioTarea.setText(horaFormateada)
                } else {
                    dateEnd = pickedDateTime.time
                    input_finTarea.setText(horaFormateada)
                }


            }, startHour, startMinute, true).show()

        } else {
            selectDate()
        }
    }

    fun formatDate(date: Date, formatTarget: String): String? {
        val formatter = SimpleDateFormat(formatTarget, Locale.getDefault())
        return formatter.format(date)
    }

    fun showAllCategories() {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Selecciona una categoria")

        var categories: Array<String> = arrayOf()
        if (allCategories.size > 0) {
            categories = allCategories.map { it.name!! }.toTypedArray()
        }
        var checkedItem = 0

        builder.setSingleChoiceItems(categories, checkedItem) { dialog, idSeleccionado ->
            Log.v("miapp", "Ha seleccionado $idSeleccionado")
            checkedItem = idSeleccionado
        }
        builder.setPositiveButton("OK") { dialog, which ->
            Log.v("miapp", "Ha elegido finalmente ${categories[checkedItem]}")
            input_categoriaTarea.setText(categories[checkedItem])
            idCategorySelected = allCategories[checkedItem].id.toString()
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    fun createNewTask() {
        if(input_tituloTarea.text != null && input_categoriaTarea.text != null && input_fechaTarea != null){
            createTaskInFirestore(Task(
                auth.currentUser!!.uid,
                input_tituloTarea.text.toString(),
                input_descripcionTarea.text.toString(),
                idCategorySelected,
                dateStart!!,
                dateEnd!!,
                priority,
            0)
            )
        } else Toast.makeText(this, "Por favor rellena los campos", Toast.LENGTH_LONG).show()
    }

    fun createTaskInFirestore(task: Task) {
        val uid = auth.currentUser!!.uid
        db.collection(DataHolder.dbUsers).document(uid).collection(DataHolder.dbTasks)
            .add(task).addOnSuccessListener { document ->
                Log.v(TAG, "createCategory:TODOFENOMENAL")
                finish()
            }.addOnFailureListener { e ->
                Log.v(TAG, "createCategory:failure", e)
            }
    }
}