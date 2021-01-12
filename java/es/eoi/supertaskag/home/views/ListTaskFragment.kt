package es.eoi.supertaskag.home.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import es.eoi.supertaskag.DataHolder
import es.eoi.supertaskag.R
import es.eoi.supertaskag.home.adapters.CategoryAdapter
import es.eoi.supertaskag.home.adapters.TaskAdapter
import es.eoi.supertaskag.models.Category
import es.eoi.supertaskag.models.Task
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_list_task.*
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.fragment_tasks.recyclerView

class ListTaskFragment : Fragment() {

    val TAG = "miapp"
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    var category: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        category = Paper.book().read(DataHolder.idCategory)
        val uid = auth.currentUser!!.uid
        val query = FirebaseFirestore.getInstance()
            .collection(DataHolder.dbUsers).document(uid).collection(DataHolder.dbTasks)
            .whereEqualTo("idCategory", category)
            .limit(50)
        Log.v(TAG, "category : $category")
        val options: FirestoreRecyclerOptions<Task> =
            FirestoreRecyclerOptions.Builder<Task>().setQuery(
                query,
                Task::class.java
            ).build()
       // var adaptador = TaskAdapter(options){
       //    startActivity(Intent())
        //}
//        recyclerViewTask.apply {
//            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//            adapter = adaptador
//        }
//        recyclerViewTask.adapter = adaptador
        //adaptador!!.startListening()
    }
}