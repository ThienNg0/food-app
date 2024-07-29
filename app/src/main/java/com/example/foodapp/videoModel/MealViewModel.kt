package com.example.foodapp.videoModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.pojo.Meal
import com.example.foodapp.pojo.MealList
import com.example.foodapp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(
    val mealDatabase: MealDatabase
):ViewModel() {
    private var mealDetailsLiveData = MutableLiveData<Meal>()

    fun getMealDetails(id:String)
    {
        RetrofitInstance.api.getMealDetails(id).enqueue(object :Callback<MealList>
        {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body()!=null)
                {
                    mealDetailsLiveData.value=response.body()!!.meals[0]
                }
                return
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
               Log.d("MealActivity",t.message.toString())
            }
        })

    }
    fun observerMealDetailsLiveData(): LiveData<Meal>
    {
        return mealDetailsLiveData
    }
    fun insertMeal(meal: Meal)
    {
        viewModelScope.launch {
            mealDatabase.mealDao().upsertMeal(meal)
        }
    }






}