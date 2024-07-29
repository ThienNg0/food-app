    package com.example.foodapp.activites

    import android.os.Bundle
    import androidx.appcompat.app.AppCompatActivity
    import androidx.lifecycle.Observer
    import androidx.lifecycle.ViewModelProvider
    import androidx.recyclerview.widget.GridLayoutManager
    import com.example.foodapp.adapters.CategoryMealsAdapter
    import com.example.foodapp.databinding.ActivityCategoryMealsBinding
    import com.example.foodapp.framents.HomeFragment
    import com.example.foodapp.videoModel.CategoryMealsViewModel

    class CategoryMealsActivity : AppCompatActivity() {
        lateinit var binding: ActivityCategoryMealsBinding
        lateinit var categoryMealsViewModel: CategoryMealsViewModel
        lateinit var categoryMealsAdapter: CategoryMealsAdapter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
            setContentView(binding.root)

            prepareRecyclerView()

            categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]

            categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

            categoryMealsViewModel.observeMealsLiveData().observe(this,Observer { mealsList ->
                        categoryMealsAdapter.setMealList(mealsList)
            })
        }

        private fun prepareRecyclerView() {
           categoryMealsAdapter = CategoryMealsAdapter()
            binding.rvMeals.apply {
                layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
                adapter = categoryMealsAdapter
            }
        }
    }
