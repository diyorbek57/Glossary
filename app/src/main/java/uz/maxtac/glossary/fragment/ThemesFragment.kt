package uz.maxtac.glossary.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uz.maxtac.glossary.R
import uz.maxtac.glossary.adapter.ThemesAdapter
import uz.maxtac.glossary.adapter.WordsAdapter
import uz.maxtac.glossary.databinding.FragmentHomeBinding
import uz.maxtac.glossary.manager.MainPreferences
import uz.maxtac.glossary.model.Word
import uz.maxtac.glossary.utils.Extensions
import uz.maxtac.glossary.utils.Extensions.hideSoftKeyboard
import uz.maxtac.glossary.utils.Resource
import uz.maxtac.glossary.utils.Utils
import uz.maxtac.glossary.viewmodel.MainViewModel

@AndroidEntryPoint
class ThemesFragment : Fragment(R.layout.fragment_themes), ThemesAdapter.OnItemClickListener {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val TAG: String = "SavesFragment"
    private val viewModel by viewModels<MainViewModel>()

    //adapter
    var themesAdapter: ThemesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
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
        themesAdapter = ThemesAdapter(requireContext(), this)

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
            rvWords.adapter = themesAdapter


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

                    themesAdapter?.filter?.filter(charSequence?.toString()?.lowercase())

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
        viewModel.words.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Resource.Status.SUCCESS -> {
                    val uniqueThemes: List<String> = result.data!!.distinctBy { it.theme }.map { it.theme }
                    themesAdapter?.submitList(uniqueThemes)
                    Utils.hideLoading(binding.progressBar)
                }

                Resource.Status.ERROR -> {
                    result.message?.let { it1 -> Log.e(TAG, it1) }
                    Utils.hideLoading(binding.progressBar)
                }

                Resource.Status.LOADING -> {
                    Utils.showLoading(binding.progressBar)
                }
            }
        }

    }


    override fun onItemClickListener(theme: String,) {
        findNavController().navigate(
            ThemesFragmentDirections.actionNavSavesToWordFragment(
                theme
            )
        )
    }


}