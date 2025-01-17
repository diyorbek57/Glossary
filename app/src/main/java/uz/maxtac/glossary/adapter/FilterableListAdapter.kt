package uz.maxtac.glossary.adapter


import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


abstract class FilterableListAdapter<T, VH : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(diffCallback), Filterable {
    private var originalList: List<T> = currentList.toList()
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                return FilterResults().apply {
                    values = if (constraint.isNullOrEmpty())
                        originalList
                    else
                        onFilter(originalList, constraint.toString())
                }
            }
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as? List<T>, true)
            }
        }
    }
    override fun submitList(list: List<T>?) {
        submitList(list, false)
    }
    abstract fun onFilter(list: List<T>, constraint: String): List<T>
    /**
     * This function is responsible for maintaining the
     * actual contents for the list for filtering
     * The submitList for parent class delegates false
     * so that a new contents can be set
     * While a filter pass true which make sure original list
     * is maintained
     *
     * @param filtered True if the list was updated using filter interface
     * */
    private fun submitList(list: List<T>?, filtered: Boolean) {
        if (!filtered)
            originalList = list ?: listOf()
        super.submitList(list)
    }
}