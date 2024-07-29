package com.example.foodapp.framents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.activites.MainActivity
import com.example.foodapp.adapters.MealsApdapter
import com.example.foodapp.databinding.FragmentSearchBinding
import com.example.foodapp.videoModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
 private lateinit var binding: FragmentSearchBinding
 private lateinit var viewModel: HomeViewModel
 private lateinit var searchRecyclerAdapter: MealsApdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            prepareRecyclerView()
        binding.imgSearchButton.setOnClickListener {
            searchMeals()
        }
        observeSearchedMealsLiveData()
        var searchJob: Job? = null
        binding.edSearchBox.addTextChangedListener {searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                    viewModel.searchMeals(searchQuery.toString())

            }
        }

    }

    private fun observeSearchedMealsLiveData() {
        viewModel.observeSearchMealsLiveData().observe(viewLifecycleOwner, Observer { mealsList ->
                       searchRecyclerAdapter.differ.submitList(mealsList)

        })

    }


    private fun searchMeals() {
        val searchQuery = binding.edSearchBox.text.toString()
        if(searchQuery.isNotEmpty())
        {
            viewModel.searchMeals(searchQuery)

        }

    }

    private fun prepareRecyclerView() {
        searchRecyclerAdapter = MealsApdapter()
        binding.rvSearchMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = searchRecyclerAdapter


        }
    }
}