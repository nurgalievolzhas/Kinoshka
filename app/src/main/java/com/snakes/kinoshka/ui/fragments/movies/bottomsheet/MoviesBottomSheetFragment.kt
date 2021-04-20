package com.snakes.kinoshka.ui.fragments.movies.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.snakes.kinoshka.R
import com.snakes.kinoshka.util.Constants.DEFAULT_PAGE
import com.snakes.kinoshka.viewmodels.MovieViewModel
import kotlinx.android.synthetic.main.fragment_movies_bottom_sheet.view.*
import java.lang.Exception
import java.util.*


class MoviesBottomSheetFragment : BottomSheetDialogFragment() {
    //Views
    private lateinit var mView: View
    //ViewModels
    private lateinit var movieViewModel:MovieViewModel
    //DataStore
    private var pageChip = DEFAULT_PAGE
    private var pageChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieViewModel = ViewModelProvider(requireActivity()).get(MovieViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_movies_bottom_sheet, container, false)

        movieViewModel.readPage.asLiveData().observe(viewLifecycleOwner,{value->
            pageChip = value.selectedPage
            updateChip(value.selectedPageId,mView.page_cg)
        })


        mView.page_cg.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedPage = chip.text.toString().toLowerCase(Locale.ROOT)
            pageChip = selectedPage
            pageChipId = checkedId
        }

        mView.aplly_btn.setOnClickListener {
            movieViewModel.savePage(
                pageChip,
                pageChipId
            )
            val action = MoviesBottomSheetFragmentDirections.actionMoviesBottomSheetFragmentToMoviesFragment(true)
            findNavController().navigate(action)
        }

        return mView
    }

    @SuppressLint("LongLogTag")
    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0){
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            }catch (e:Exception){
                Log.e("MoviesBottomSheetFragment",e.toString())
            }
        }
    }

}