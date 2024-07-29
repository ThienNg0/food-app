package com.example.foodapp.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.MealItemBinding
import com.example.foodapp.pojo.MealsByCategory

class CategoryMealsAdapter: RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewModel>() {
    private var mealsList = ArrayList<MealsByCategory>()
        fun setMealList(mealsList: List<MealsByCategory>) {
            this.mealsList = mealsList as ArrayList<MealsByCategory>
            notifyDataSetChanged()
        }

    inner class CategoryMealsViewModel(val binding: MealItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewModel {
       return CategoryMealsViewModel(MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CategoryMealsViewModel, position: Int) {
       Glide.with(holder.itemView).load(mealsList[position].strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = mealsList[position].strMeal
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }
}