package com.huawei.codelabs.hihealth.happysport.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.huawei.codelabs.hihealth.happysport.RecordActivity
import com.huawei.codelabs.hihealth.happysport.data.WalkingRepository
import com.huawei.codelabs.hihealth.happysport.databinding.ListItemSportBinding

class WalkingSportAdapter (private val context: Context):
    ListAdapter<WalkingRepository.WalkingSport, WalkingSportAdapter.ViewHolder>(WalkingSportDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val runningSport =getItem(position)
        holder.apply {
            bind(createOnClickListener(runningSport.sport.id), runningSport)
            itemView.tag =runningSport
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemSportBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    private fun createOnClickListener(sportId: Long): View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(context, RecordActivity::class.java).apply {
                putExtra("SPORT_ID", sportId)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(context, intent, null)
        }
    }

    class ViewHolder(private val binding: ListItemSportBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, item: WalkingRepository.WalkingSport) {
            binding.apply {
                walkingSport = item
                clickListener = listener
                executePendingBindings()
            }
        }
    }
}


private class WalkingSportDiffCallback : DiffUtil.ItemCallback<WalkingRepository.WalkingSport>() {

    override fun areItemsTheSame(
        oldItem: WalkingRepository.WalkingSport,
        newItem: WalkingRepository.WalkingSport
    ): Boolean {
        return oldItem.sport.id == newItem.sport.id
    }

    override fun areContentsTheSame(
        oldItem: WalkingRepository.WalkingSport,
        newItem: WalkingRepository.WalkingSport
    ): Boolean {
        return oldItem == newItem
    }
}