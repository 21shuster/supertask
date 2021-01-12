package es.eoi.supertaskag.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.eoi.supertaskag.DataHolder
import es.eoi.supertaskag.R
import es.eoi.supertaskag.models.Category
import es.eoi.supertaskag.models.Task
import kotlinx.android.synthetic.main.activity_detail_task.*
import java.text.SimpleDateFormat
import java.util.*

class DetailTaskActivity : AppCompatActivity() {

    var task: Task = Task()
    var category: Category? = null

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_task)

        auth = Firebase.auth
        db = Firebase.firestore

        task = DataHolder.currentTask!!
        category = DataHolder.currentCategory

        initData()

        btnCompleteTask.setOnClickListener {
            completeTask()
        }
    }

    fun completeTask() {
        db.collection(DataHolder.dbUsers)
            .document(auth.currentUser!!.uid)
            .collection(DataHolder.dbTasks)
            .document(task.id!!)
            .update("completed", true)
            .addOnSuccessListener {
                finish()
            }
            .addOnFailureListener { e -> Log.w("miapp", "Error updating document", e) }

    }

    fun initData() {

        val dCategory = findViewById<MaterialButton>(R.id.dCategory)
        dCategory.text = category!!.name

        val dPriority = findViewById<MaterialButton>(R.id.dPriority)
        setPriority(dPriority)

        val dName = findViewById<TextView>(R.id.dName)
        dName.text = task.title

        val dDescription = findViewById<TextView>(R.id.dDescription)
        dDescription.text = task.description

        val dDate = findViewById<TextView>(R.id.dDate)
        val fechaFormateada = formatDate(task.startDate!!, "dd/MM/YYYY")
        dDate.text = fechaFormateada

        val inicio = formatDate(task.startDate!!, "HH:mm")
        val fin = formatDate(task.endDate!!, "HH:mm")

        val dTime = findViewById<TextView>(R.id.dTime)
        dTime.text = "$inicio - $fin"

    }

    fun formatDate(date: Date, formatTarget: String): String? {
        val formatter = SimpleDateFormat(formatTarget, Locale.getDefault())
        return formatter.format(date)
    }

    fun setPriority(dPriority: MaterialButton) {
        when (task.priority) {
            0 -> {
                dPriority.text = "Baja"
                dPriority.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.holo_blue_light
                    )
                )
            }
            1 -> {
                dPriority.text = "Media"
                dPriority.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.holo_orange_light
                    )
                )
            }
            2 -> {
                dPriority.text = "Alta"
                dPriority.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.superred
                    )
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.edit -> {
                val editIntent = Intent(this, TaskActivity::class.java)
                editIntent.putExtra("edit", true)
                startActivity(editIntent)
                finish()
            }
            R.id.delete -> {
                deleteTask()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun deleteTask() {
        db.collection(DataHolder.dbUsers)
            .document(auth.currentUser!!.uid)
            .collection(DataHolder.dbTasks)
            .document(task.id!!)
            .delete()
            .addOnSuccessListener {
                finish()
            }
            .addOnFailureListener { e -> Log.w("miapp", "Error updating document", e) }
    }
}
