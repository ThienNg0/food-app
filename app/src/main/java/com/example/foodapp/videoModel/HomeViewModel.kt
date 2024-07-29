package com.example.foodapp.videoModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.pojo.Category
import com.example.foodapp.pojo.CategoryList
import com.example.foodapp.pojo.MealsByCategoryList
import com.example.foodapp.pojo.MealsByCategory
import com.example.foodapp.pojo.Meal
import com.example.foodapp.pojo.MealList
import com.example.foodapp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
) : ViewModel(
) {

//    init{
//        getRandomMeal()
//
//    }



    // LiveData để giữ dữ liệu của bữa ăn ngẫu nhiên
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoritesLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private val searchMealsLiveData = MutableLiveData<List<Meal>>()
    private var saveSateRandomMeal: Meal ?= null
    // Phương thức để gọi API và lấy dữ liệu bữa ăn ngẫu nhiên
    fun getRandomMeal() {
        saveSateRandomMeal?.let {meal->
            randomMealLiveData.postValue(meal)
            return

        }
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.isSuccessful) {
                    // Xử lý khi API gọi thành công và nhận được dữ liệu hợp lệ
                    val randomMeal: Meal = response.body()?.meals?.get(0) ?: return
                    randomMealLiveData.value = randomMeal
                    saveSateRandomMeal = randomMeal
                } else {
                    // Xử lý khi API gọi thành công nhưng có lỗi từ phía server
                    Log.e("HomeFragment", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                // Xử lý khi API gọi thất bại
                Log.e("HomeFragment", "Error: ${t.message}", t)
            }
        })
    }
    fun getPopularItems()
    {
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object : Callback<MealsByCategoryList> {
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if (response.isSuccessful) {
                    popularItemsLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
               Log.d("HomeFragment",t.message.toString())
            }
        })
    }
    fun getCategories()
    {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
               response.body()?.let {categoryList ->
                   categoriesLiveData.postValue(categoryList.categories)
               }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("HomeFragment",t.message.toString())
            }
        })
    }
    fun getMealId(id:String)
    {
        RetrofitInstance.api.getMealDetails(id).enqueue(object :Callback<MealList>
        {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let {meal->
                    bottomSheetMealLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel",t.message.toString())
            }
        })
    }
    fun deleteMeal(meal: Meal)
    {
        viewModelScope.launch {
            mealDatabase.mealDao().deleteMeal(meal)
        }
    }
    fun insertMeal(meal: Meal)
    {
        viewModelScope.launch {
            mealDatabase.mealDao().upsertMeal(meal)
        }
    }
    fun searchMeals(searchQuery: String) = RetrofitInstance.api.searchMeals(searchQuery).enqueue(
        object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                    val mealList = response.body()?.meals
                            mealList.let {
                        searchMealsLiveData.postValue(it)
                    }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                    Log.e("HomeViewModel",t.message.toString())
            }
        }
    )
    fun observeSearchMealsLiveData(): LiveData<List<Meal>> = searchMealsLiveData



    // Phương thức để quan sát LiveData của bữa ăn ngẫu nhiên
    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }
    fun observePopularItemsLiveData(): LiveData<List<MealsByCategory>> {
        return popularItemsLiveData
    }
    fun observeCategoriesLiveData(): LiveData<List<Category>> {
        return categoriesLiveData
    }
    fun observeFavoritesMealsLiveData(): LiveData<List<Meal>> {
        return favoritesLiveData
    }
    fun observeBottomSheetMeal():LiveData<Meal> = bottomSheetMealLiveData

}
