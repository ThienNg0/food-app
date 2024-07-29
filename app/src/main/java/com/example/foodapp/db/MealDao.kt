package com.example.foodapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodapp.pojo.Meal

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMeal(meal: Meal)
    @Delete
    suspend fun deleteMeal(meal: Meal)
    @Query("SELECT * FROM mealInformation")
    fun getAllMeals(): LiveData<List<Meal>>



}