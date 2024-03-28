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


class WordsAdapter(
    val context: Context,
    private val onItemClickListener: OnItemClickListener
) : FilterableListAdapter<Word, WordsAdapter.ViewHolder>(DiffCallback()) {


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
        fun bind(word: Word) {

            word.apply {

                //price

                binding.tvTitle.text = word_uz

                //post click listener
                binding.cvMain.setOnClickListener {
                    if (id != null) {
                        onItemClickListener.onItemClickListener(
                            id, word
                        )
                    }
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Word, newItem: Word) =
            oldItem == newItem

    }

    interface OnItemClickListener {
        fun onItemClickListener(id: String, word: Word)
    }

    override fun onFilter(list: List<Word>, constraint: String): List<Word> {
        val tokens =
            constraint.lowercase().trim().split("\\s+".toRegex()).filter { it.isNotBlank() }

        return if (constraint.isBlank()) {
            list.toList()
        } else {
            list.filter { word ->
                tokens.all { token ->
                    word.word_de.lowercase().trim().contains(token, ignoreCase = true) ||
                            word.word_uz.lowercase().trim().contains(token, ignoreCase = true)
                }
            }
        }
    }


}


