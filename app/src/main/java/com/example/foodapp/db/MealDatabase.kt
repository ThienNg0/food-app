package com.example.foodapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodapp.pojo.Meal

@Database(entities = [Meal::class], version = 1)
@TypeConverters(MealTypeConvertor::class)
abstract class MealDatabase :RoomDatabase() {
    abstract fun mealDao():MealDao
    companion object{
        @Volatile
        var INSTANCE: MealDatabase? = null
        @Synchronized
        fun getInstance(context: Context):MealDatabase
        {
            if(INSTANCE == null)
            {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        MealDatabase::class.java,
                        "meal.db"
                    ).fallbackToDestructiveMigration()
                        .build()
            }
            return INSTANCE as MealDatabase
        }
    }


}