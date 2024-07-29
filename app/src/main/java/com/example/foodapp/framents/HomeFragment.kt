package com.example.foodapp.framents

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.activites.CategoryMealsActivity
import com.example.foodapp.activites.MainActivity
import com.example.foodapp.activites.MealActivity
import com.example.foodapp.adapters.CategoriesAdapter
import com.example.foodapp.adapters.MostPopularAdapter
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.framents.bottomsheet.MealBottomSheetFragment
import com.example.foodapp.pojo.MealsByCategory
import com.example.foodapp.pojo.Meal
import com.example.foodapp.videoModel.HomeViewModel


class HomeFragment : Fragment() {

    // Biến binding để tương tác với các thành phần trong layout
    private lateinit var binding: FragmentHomeBinding

    // ViewModel để quản lý dữ liệu
    private lateinit var viewModel: HomeViewModel

    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example.foodapp.fragments.idMeal"
        const val MEAL_NAME = "com.example.foodapp.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.foodapp.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.foodapp.fragments.categoryName"


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            viewModel = (activity as MainActivity).viewModel
        // Khởi tạo ViewModel
//        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        popularItemsAdapter = MostPopularAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout cho fragment và khởi tạo biến binding
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()
        // Gọi ViewModel để lấy một bữa ăn ngẫu nhiên
       viewModel.getRandomMeal()

        // Quan sát sự thay đổi của dữ liệu bữa ăn ngẫu nhiên
        observeRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()

        onCategoryClick()
        onPopularItemLongClick()

        onSearchIconClick()


    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener {
           findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onItemClick = {meal->
            val mealBottomSheetFrament = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFrament.show(childFragmentManager, "Meal Info")


        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
            categories.forEach { category ->
                categoriesAdapter.setCategoryList(categories)
            }

        })
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick ={meal -> val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner,{
                mealList ->
            popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)
        })

    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        // Quan sát LiveData từ ViewModel để cập nhật bữa ăn ngẫu nhiên
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner, Observer
        { meal ->
            // Cập nhật giao diện với bữa ăn ngẫu nhiên được lấy về, sử dụng Glide để tải ảnh
            Glide.with(this@HomeFragment)
                .load(meal.strMealThumb)
                .into(binding.imageRandomMeal)

            this.randomMeal = meal
        })
    }
}