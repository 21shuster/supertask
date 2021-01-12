package es.eoi.supertaskag.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.eoi.supertaskag.DataHolder
import es.eoi.supertaskag.R
import es.eoi.supertaskag.home.adapters.TaskAdapter
import es.eoi.supertaskag.models.Category
import es.eoi.supertaskag.models.Task
import kotlinx.android.synthetic.main.activity_category_list.*

class CategoryListActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    var uid: String = ""
    var category: Category? = null

    var allTask: ArrayList<Task> = arrayListOf()

    lateinit var mAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)

        category = DataHolder.currentCategory
        Log.v("miapp", "${category?.id}")
        supportActionBar?.title = category!!.name

        mAdapter = TaskAdapter(allTask) { task ->
            DataHolder.currentTask = task
            startActivity(Intent(this, DetailTaskActivity::class.java))
        }

        recylcerTareas.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recylcerTareas.adapter = mAdapter

        initFirebase()
    }

    fun initFirebase() {
        db = Firebase.firestore
        auth = Firebase.auth
        uid = auth.uid!!

        db.collection(DataHolder.dbUsers).document(uid).collection(DataHolder.dbTasks)
            .whereEqualTo("idCategory", category!!.id).addSnapshotListener { value, e ->

                allTask.clear()
                for (doc in value!!) {
                    val tarea = doc.toObject(Task::class.java)
                    tarea.id = doc.id
                    allTask.add(tarea)
                }

                //  Actualizar el recycler/adapter
                mAdapter.notifyDataSetChanged()
            }

    }
}


