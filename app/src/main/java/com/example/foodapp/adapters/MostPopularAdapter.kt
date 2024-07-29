package com.example.foodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.PopularItemsBinding
import com.example.foodapp.pojo.MealsByCategory

// Adapter để hiển thị danh sách món ăn phổ biến
class MostPopularAdapter() : RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {
    private var mealsList = ArrayList<MealsByCategory>()
     var onLongClick: ((MealsByCategory) -> Unit)?=null
    lateinit var onItemClick: ((MealsByCategory) -> Unit)
    // Cài đặt danh sách món ăn
    fun setMeals(mealsList: ArrayList<MealsByCategory>) {
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    // Tạo ViewHolder cho mỗi mục dữ liệu
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(
            PopularItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    // Gắn dữ liệu vào ViewHolder
    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        // Sử dụng Glide để tải ảnh từ URL và hiển thị vào ImageView
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)
        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealsList[position])
        }
        holder.itemView.setOnLongClickListener {
            onLongClick?.invoke(mealsList[position])
            true
        }
    }

    // Trả về số lượng mục trong danh sách
    override fun getItemCount(): Int {
        return mealsList.size
    }

    // ViewHolder cho mục dữ liệu của danh sách
    class PopularMealViewHolder(var binding: PopularItemsBinding) : RecyclerView.ViewHolder(binding.root)
}
