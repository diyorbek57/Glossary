package uz.maxtac.glossary.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uz.maxtac.glossary.R
import uz.maxtac.glossary.databinding.ItemWordBinding
import uz.maxtac.glossary.manager.MainPreferences
import uz.maxtac.glossary.model.Word


class ThemesAdapter(
    val context: Context,
    private val onItemClickListener: OnItemClickListener
) : FilterableListAdapter<String, ThemesAdapter.ViewHolder>(DiffCallback()) {


    val TAG: String = "ProductsAdapter"
    private lateinit var binding: ItemWordBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)


    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    inner class ViewHolder(val binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(theme:String ) {



                //price

                binding.tvTitle.text = theme

                //post click listener
                binding.cvMain.setOnClickListener {

                        onItemClickListener.onItemClickListener(
                            theme
                        )

                }
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

    }

    interface OnItemClickListener {
        fun onItemClickListener(theme: String)
    }

    override fun onFilter(list: List<String>, constraint: String): List<String> {
        val tokens = constraint.lowercase().trim().split("\\s+".toRegex()).filter { it.isNotBlank() }

        return if (constraint.isBlank()) {
            list.toList()
        } else {
            list.filter { theme ->
                tokens.all { token ->
                   theme.lowercase().trim().contains(token, ignoreCase = true)
                }
            }
        }
    }


}


