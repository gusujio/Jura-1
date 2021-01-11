package com.example.jura20.fragments.HomeFragment
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jura20.R
import com.example.jura20.fragments.HomeFragment.data.Item

class JurAdapter(context: Context?, var clickListener: OnItemClickListener): ListAdapter<Item, JurAdapter.ViewHolder>(
    Callback()
) {

    private val inflater = LayoutInflater.from(context)
    private class Callback: DiffUtil.ItemCallback<Item>(){
        override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: Item, newItem: Item)= oldItem == newItem

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private var image: ImageView = itemView.findViewById(R.id.image_item)
        private var title: TextView = itemView.findViewById(R.id.title_item)

        fun bind(version: Item, action: OnItemClickListener){
            image.setImageResource(version.image)
            title.text = version.level

            itemView.setOnClickListener{
                action.click(version, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.list_item_rv, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

}
interface OnItemClickListener{
    fun  click(item: Item, position: Int)
}