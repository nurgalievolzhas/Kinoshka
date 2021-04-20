package com.snakes.kinoshka.ui.fragments.movies

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.snakes.kinoshka.viewmodels.MainViewModel
import com.snakes.kinoshka.R
import com.snakes.kinoshka.adapters.MovieAdapter
import com.snakes.kinoshka.databinding.FragmentMoviesBinding
import com.snakes.kinoshka.util.Constants.API_KEY
import com.snakes.kinoshka.util.NetworkResult
import com.snakes.kinoshka.util.observeOnce
import com.snakes.kinoshka.viewmodels.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movies.view.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment() {
    //Args
    private val args by navArgs<MoviesFragmentArgs>()

    //Views
    private lateinit var mView: View

    //Bindings
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    //Adapters
    private val mAdapter by lazy { MovieAdapter() }

    //ViewModels
    private lateinit var mainViewModel: MainViewModel
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ViewModel Init
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        movieViewModel = ViewModelProvider(requireActivity()).get(MovieViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //mView =  inflater.inflate(R.layout.fragment_movies, container, false)
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = mainViewModel
        //Menu init
        setHasOptionsMenu(true)

        setupRecyclerView()
        readDatabase()

        binding.movieFab.setOnClickListener {
            findNavController().navigate(R.id.action_moviesFragment_to_moviesBottomSheetFragment)
        }

        return binding.root
        //return mView
    }


    private fun setupRecyclerView() {
        binding.moviesRv.adapter = mAdapter
        binding.moviesRv.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.movie_menu, menu)
        val search = menu.findItem(R.id.movie_search)
        val searchView = search.actionView as? SearchView
        //searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!=null){
                    requestSearchApiData(query)
                }
                return true
            }
        })
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readMovie.observeOnce(viewLifecycleOwner, { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    mAdapter.setData(database[0].movieResponse)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            })
        }
    }

    private fun requestApiData() {
        mainViewModel.getPopularMovies(movieViewModel.applyQueries())
        mainViewModel.movieResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun requestSearchApiData(query:String){
        showShimmerEffect()
        mainViewModel.getSearchMovies(movieViewModel.applySearchQuery(query))
        mainViewModel.searchMovieResponse.observe(viewLifecycleOwner,{response->
            when(response){
                is NetworkResult.Success ->{
                    hideShimmerEffect()
                    val movieResponse = response.data
                    movieResponse?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error ->{
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading->{
                    showShimmerEffect()
                }
            }
        })
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readMovie.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].movieResponse)
                }
            })
        }
    }


    private fun showShimmerEffect() {
        binding.moviesRv.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.moviesRv.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}