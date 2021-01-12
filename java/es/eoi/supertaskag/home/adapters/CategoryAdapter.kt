package es.eoi.supertaskag.home.adapters

import es.eoi.supertaskag.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import es.eoi.supertaskag.DataHolder
import es.eoi.supertaskag.models.Category
import es.eoi.supertaskag.models.Task

class CategoryAdapter(options: FirestoreRecyclerOptions<Category>, val onClick: (Category) -> Unit) :
    FirestoreRecyclerAdapter<Category, CategoryAdapter.ViewHolder>(options) {

    var user = FirebaseAuth.getInstance().currentUser
    var db = FirebaseFirestore.getInstance()

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Category) {
        holder.nombre.setText(model.name)
        holder.cantidad.setText("${model.cantidad.toString()}")
        holder.itemView.setOnClickListener {
            onClick(model)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).id
                .equals(user!!.uid)
        ) MSG_TYPE_RIGHT else MSG_TYPE_LEFT
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nombre: TextView
        var cantidad: TextView

        init {
            nombre = itemView.findViewById(R.id.tvNombre)
            cantidad = itemView.findViewById(R.id.tvCantidad)
        }
    }

    /*fun getCantidad(idCategory : String) : Int {
        val uid = Firebase.auth.currentUser!!.uid
        var cantidad = 0

        db.collection(DataHolder.dbUsers).document(uid).collection(DataHolder.dbTasks)
            .addSnapshotListener { value, e ->
                for (doc in value!!) {
                    val task = doc.toObject(Task::class.java)
                    if(task.idCategory == idCategory)
                        cantidad += 1
                }
            }
        return cantidad
    }
*/
    companion object {
        const val MSG_TYPE_LEFT = 0
        const val MSG_TYPE_RIGHT = 1
    }
}