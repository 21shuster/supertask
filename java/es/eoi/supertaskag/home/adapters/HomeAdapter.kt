package es.eoi.supertaskag.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialCalendar
import es.eoi.supertaskag.R
import es.eoi.supertaskag.models.Category
import es.eoi.supertaskag.models.CategoryHome
import kotlinx.android.synthetic.main.item_home.view.*

class HomeAdapter(private val mDataSet: List<CategoryHome>, val onClick: (Category) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return MainViewHolder(v)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val data = mDataSet[position]
        data.let {
            holder.assignData(it)
        }
    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val name = v.findViewById<TextView>(R.id.tvNombre)
        private val count = v.findViewById<TextView>(R.id.tvCantidad)
        private val card = v.findViewById<MaterialCardView>(R.id.card)

        fun assignData(data: CategoryHome) {
            name.text = data.name
            count.text = data.count.toString()
            card.setOnClickListener {
                val category = Category()
                category.id = data.id
                category.name = data.name
                onClick(category)
            }
        }
    }
}
