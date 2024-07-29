package com.example.foodapp.framents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

import com.example.foodapp.activites.MainActivity
import com.example.foodapp.adapters.MealsApdapter
import com.example.foodapp.databinding.FragmentFavoritesBinding
import com.example.foodapp.videoModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class FavoritesFragment : Fragment() {
           private lateinit var binding: FragmentFavoritesBinding
           private lateinit var viewModel: HomeViewModel
           private lateinit var favoritesAdapter: MealsApdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                viewModel = (activity as MainActivity).viewModel
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       super.onViewCreated(view, savedInstanceState)

       prepareRecyclerView()
       oberserFavorites()
       val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
           ItemTouchHelper.UP or ItemTouchHelper.DOWN,
           ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
       )
       {
           override fun onMove(
               recyclerView: RecyclerView,
               viewHolder: RecyclerView.ViewHolder,
               target: RecyclerView.ViewHolder
           ) = true

           override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               val position = viewHolder.adapterPosition
               val deletedMeal = favoritesAdapter.differ.currentList[position]
               viewModel.deleteMeal(deletedMeal)

               Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_LONG).setAction(
                   "Undo"
               ) {
                   viewModel.insertMeal(deletedMeal)
               }.show()
           }
       }

       ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)

   }

    private fun prepareRecyclerView() {
        favoritesAdapter = MealsApdapter()
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoritesAdapter
        }
    }

    private fun oberserFavorites() {
        viewModel.observeFavoritesMealsLiveData().observe(requireActivity(), Observer { meals ->
                favoritesAdapter.differ.submitList(meals)
        })
    }

}