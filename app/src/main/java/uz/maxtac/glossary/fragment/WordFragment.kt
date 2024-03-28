package uz.maxtac.glossary.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uz.maxtac.glossary.R
import uz.maxtac.glossary.adapter.WordsAdapter
import uz.maxtac.glossary.databinding.FragmentHomeBinding
import uz.maxtac.glossary.databinding.FragmentWordBinding
import uz.maxtac.glossary.model.Word
import uz.maxtac.glossary.utils.Extensions
import uz.maxtac.glossary.utils.Extensions.hideSoftKeyboard
import uz.maxtac.glossary.utils.Resource
import uz.maxtac.glossary.utils.Utils
import uz.maxtac.glossary.viewmodel.MainViewModel

@AndroidEntryPoint
class WordFragment : Fragment(R.layout.fragment_word),WordsAdapter.OnItemClickListener {

    private var _binding: FragmentWordBinding? = null
    private val binding get() = _binding!!
    val TAG: String = "HomeFragment"
    private val viewModel by viewModels<MainViewModel>()
    private val args: WordFragmentArgs by navArgs()
    //adapter
    var wordsAdapter: WordsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWordBinding.bind(view)

        inits()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllWords()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun inits() {
        wordsAdapter = WordsAdapter(requireContext(), this)

        initUI()
        observer()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initUI() {
        binding.apply {

            rvWords.layoutManager = GridLayoutManager(
                requireContext(), 1,
                GridLayoutManager.VERTICAL,
                false
            )
            rvWords.adapter = wordsAdapter


            edtSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    Log.d(TAG, "Text changed: $charSequence")
                    wordsAdapter?.filter?.filter(charSequence?.toString()?.lowercase())

//                    // Check if the filter is empty and submit the original list
//                    if (charSequence.isNullOrBlank()) {
//                        wordsAdapter?.submitList(viewModel.words.value?.data)
//                    }
                }

                override fun afterTextChanged(editable: Editable?) {}
            })

            edtSearch.setOnFocusChangeListener { view, b ->
                when (b) {
                    true -> ivSearch.visibility = View.GONE
                    false -> ivSearch.visibility = View.VISIBLE
                }
            }




            root.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    // Check if the touch event is outside the EditText
                    hideSoftKeyboard()
                    if (!Extensions.isTouchInsideView(event, edtSearch)) {
                        // Clear focus from the EditText
                        edtSearch.clearFocus()
                    }
                }
                false
            }
        }


    }

    private fun observer() {
        viewModel.words.observe(viewLifecycleOwner) { it ->
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    val filteredAndSortedWords: List<Word>? = it.data?.filter { it.theme == args.theme }?.sortedBy { it.word_uz }
                    wordsAdapter?.submitList(filteredAndSortedWords)

                    Utils.hideLoading(binding.progressBar)
                }

                Resource.Status.ERROR -> {
                    it.message?.let { it1 -> Log.e(TAG, it1) }
                    Utils.hideLoading(binding.progressBar)
                }

                Resource.Status.LOADING -> {
                    Utils.showLoading(binding.progressBar)
                }
            }
        }

    }

    override fun onItemClickListener(id: String, word: Word) {
        findNavController().navigate(
            WordFragmentDirections.actionWordFragmentToDetailsFragment(
                id,
                word
            )
        )
    }

}