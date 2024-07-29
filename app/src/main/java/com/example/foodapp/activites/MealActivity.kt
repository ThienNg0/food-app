package com.example.foodapp.activites

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityMealBinding
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.framents.HomeFragment
import com.example.foodapp.pojo.Meal
import com.example.foodapp.videoModel.MealViewModel
import com.example.foodapp.videoModel.MealViewModelFactory


class MealActivity : AppCompatActivity() {
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var binding: ActivityMealBinding
    private lateinit var youtubeLink: String
    private lateinit var mealMvvm:MealViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this,viewModelFactory).get(MealViewModel::class.java)
//        mealMvvm = ViewModelProvider(this).get(MealViewModel::class.java)
        getInformationFromIntent()
        setinformationToViews()
        loadingCase()

       mealMvvm.getMealDetails(mealId)
        observerMealDetailsLiveData()
        onYouTubeClick()
        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.addToFavorites.setOnClickListener{
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Meal Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYouTubeClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            // Verify that the intent can be resolved to an activity
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // If no app can handle the intent, open the YouTube website
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
                startActivity(webIntent)
            }
        }
    }

    private var mealToSave:Meal?=null
    private fun observerMealDetailsLiveData() {
       mealMvvm.observerMealDetailsLiveData().observe(this,object : Observer<Meal>{
           override fun onChanged(t: Meal) {
               onResponseCase()
                   val meal = t
               mealToSave = meal
               binding.tvCategory.text = "Category : ${meal.strCategory}"
               binding.tvArea.text = "Area : ${meal.strArea}"
               binding.tvInstructionContent.text = meal.strInstructions

               youtubeLink = meal.strYoutube.toString()

           }
       })
    }


    private fun setinformationToViews() {
       Glide.with(applicationContext)
           .load(mealThumb)
           .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.black))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))

    }

    private fun getInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase()
    {
        binding.progressBar.visibility = View.VISIBLE
        binding.addToFavorites.visibility = View.INVISIBLE
        binding.tvInstruction.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE

    }
    private fun onResponseCase()
    {
        binding.progressBar.visibility = View.INVISIBLE
        binding.addToFavorites.visibility = View.VISIBLE
        binding.tvInstruction.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
}