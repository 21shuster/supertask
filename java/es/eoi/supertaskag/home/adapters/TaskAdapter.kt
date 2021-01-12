package es.eoi.supertaskag.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.eoi.supertaskag.R
import es.eoi.supertaskag.models.Task
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(private val mDataSet: List<Task>, val onClick: (Task) -> Unit) :
    RecyclerView.Adapter<TaskAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return MainViewHolder(v)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val data = mDataSet[position]
        data.let { task ->
            holder.assignData(task)
        }
    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val name = v.findViewById<TextView>(R.id.tvTitle)
        private val date = v.findViewById<TextView>(R.id.tvDate)
        private val time = v.findViewById<TextView>(R.id.tvDuration)
        private val priority = v.findViewById<MaterialButton>(R.id.priority)
        private val card = v.findViewById<MaterialCardView>(R.id.cardTarea)
        val context = v.context

        fun assignData(data: Task) {

            card.setOnClickListener {
                onClick(data)
            }

            when (data.priority) {
                0 -> {
                    priority.text = "BAJA"
                    priority.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            android.R.color.holo_blue_light
                        )
                    )
                }
                1 -> {
                    priority.text = "MEDIA"
                    priority.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            android.R.color.holo_orange_light
                        )
                    )
                }
                2 -> {
                    priority.text = "ALTA"
                    priority.setBackgroundColor(ContextCompat.getColor(context, R.color.superred))
                }
            }

            name.text = data.title

            val fechaFormateada = formatDate(data.startDate!!, "dd/MM/YYYY")
            date.text = fechaFormateada

            val inicio = formatDate(data.startDate!!, "HH:mm")
            val fin = formatDate(data.endDate!!, "HH:mm")
            time.text = "$inicio - $fin"

        }

        fun formatDate(date: Date, formatTarget: String): String? {
            val formatter = SimpleDateFormat(formatTarget, Locale.getDefault())
            return formatter.format(date)
        }
    }
}