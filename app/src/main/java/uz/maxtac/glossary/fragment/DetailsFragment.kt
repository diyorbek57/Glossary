package uz.maxtac.glossary.fragment

import android.R.attr
import android.R.attr.label
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import uz.maxtac.glossary.R
import uz.maxtac.glossary.databinding.FragmentDetailsBinding
import uz.maxtac.glossary.model.Word
import uz.maxtac.glossary.utils.Extensions.toast
import uz.maxtac.glossary.utils.Resource
import uz.maxtac.glossary.utils.Utils
import uz.maxtac.glossary.viewmodel.MainViewModel


@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    val TAG: String = "HomeFragment"
    private val viewModel by viewModels<MainViewModel>()
    private val args: DetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailsBinding.bind(view)

        inits()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSingleWord(args.wordId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun inits() {

        initUI()
        // observer()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initUI() {
        showDetails(args.word)

        args.word.apply {

            binding.apply {

                cvCopy.setOnClickListener {
                    copyText("word", tvWord.text.toString())
                }

                ivCopyDefinition.setOnClickListener {
                    copyText("definition", tvTranslation.text.toString())
                }

            }
        }

    }

//    private fun observer() {
//        viewModel.word.observe(viewLifecycleOwner) {
//            when (it.status) {
//                Resource.Status.SUCCESS -> {
//                    it.data?.let { it1 -> showDetails(it1) }
//                    Utils.hideLoading(binding.progressBar)
//                    binding.llMain.visibility = View.VISIBLE
//                }
//
//                Resource.Status.ERROR -> {
//                    it.message?.let { it1 -> Log.e(TAG, it1) }
//                    Utils.hideLoading(binding.progressBar)
//                    binding.llMain.visibility = View.GONE
//                }
//
//                Resource.Status.LOADING -> {
//                    Utils.showLoading(binding.progressBar)
//                    binding.llMain.visibility = View.GONE
//                }
//            }
//        }
//
//    }

    private fun showDetails(word: Word) {
        binding.apply {
            word.apply {
                tvWord.text = word_uz
                tvTranslation.text = word_de
            }


        }
    }

    private fun copyText(label: String, text: String) {
        val clipboard: ClipboardManager? =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText(label, text)
        clipboard?.setPrimaryClip(clip)
        toast("Text Copied!")
    }

}