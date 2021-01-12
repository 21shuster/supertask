package es.eoi.supertaskag.home.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.eoi.supertaskag.DataHolder
import es.eoi.supertaskag.R
import es.eoi.supertaskag.home.CategoryListActivity
import es.eoi.supertaskag.home.TaskActivity
import es.eoi.supertaskag.home.adapters.HomeAdapter
import es.eoi.supertaskag.models.Category
import es.eoi.supertaskag.models.CategoryHome
import es.eoi.supertaskag.models.Task
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlin.collections.ArrayList

class TasksFragment : Fragment() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    var uid: String? = null
    var allCategories: ArrayList<Category> = arrayListOf()
    var allTasks: ArrayList<Task> = arrayListOf()
    var allData: ArrayList<CategoryHome> = arrayListOf()
    lateinit var mAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFirebase()
        floating_action_button.setOnClickListener {
            startActivity(Intent(activity, TaskActivity::class.java))
        }

        mAdapter = HomeAdapter(allData) { category ->
            DataHolder.currentCategory = category
            startActivity(Intent(context, CategoryListActivity::class.java))
        }
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = mAdapter
    }

    fun initFirebase() {
        db = Firebase.firestore
        auth = Firebase.auth
        uid = auth.uid

        loadAllCategories()
    }

    fun loadAllCategories() {
        db.collection(DataHolder.dbUsers).document(uid!!).collection(DataHolder.dbCategories).get()
            .addOnSuccessListener { result ->

                allCategories.clear()
                for (document in result) {
                    val cat = document.toObject(Category::class.java)
                    cat.id = document.id
                    allCategories.add(cat)
                }

                loadAllTasks()
            }
            .addOnFailureListener { e ->
                Log.v("miapp", "loadAllCategories:failure", e)
            }

    }

    fun loadAllTasks() {
        db.collection(DataHolder.dbUsers).document(uid!!).collection(DataHolder.dbTasks).get()//.whereEqualTo("completed", false).get()
            .addOnSuccessListener { result ->

                allTasks.clear()
                for (document in result) {
                    val task = document.toObject(Task::class.java)
                    Log.v("miapp", "{${task.idCategory} -> ${task.description}}")
                    allTasks.add(task)
                }

                mappingData()
            }
            .addOnFailureListener { e ->
                Log.v("miapp", "loadAllCategories:failure", e)
            }
    }

    fun mappingData() {

        allData.clear()
        allCategories.forEach { category ->
            val currData = CategoryHome()
            currData.id = category.id
            currData.name = category.name

            val dataFiltered = allTasks.filter { task -> task.idCategory == category.id }
            allTasks.forEach{task -> Log.v("miapp", "${task.idCategory} == ${category.id }")}
            currData.count = dataFiltered.size

            allData.add(currData)
        }

        mAdapter.notifyDataSetChanged()

    }

    override fun onResume() {
        super.onResume()
        loadAllCategories()
    }

}