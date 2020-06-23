package com.huawei.codelabs.hihealth.happysport.adapters

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.huawei.codelabs.hihealth.happysport.RecordActivity
import com.huawei.codelabs.hihealth.happysport.data.RunningRepository
import com.huawei.codelabs.hihealth.happysport.databinding.ListItemSportBinding
import com.huawei.codelabs.hihealth.happysport.viewmodels.HiHealthKitAdapter.ISportListener

class RunningSportAdapter (private val context: Context):
    ListAdapter<RunningRepository.RunningSport, RunningSportAdapter.ViewHolder>(RunningSportDiffCallback()) {
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
        fun bind(listener: View.OnClickListener, item: RunningRepository.RunningSport) {
            binding.apply {
                runningSport = item
                clickListener = listener
                executePendingBindings()
            }
        }
    }
}


private class RunningSportDiffCallback : DiffUtil.ItemCallback<RunningRepository.RunningSport>() {

    override fun areItemsTheSame(
        oldItem: RunningRepository.RunningSport,
        newItem: RunningRepository.RunningSport
    ): Boolean {
        return oldItem.sport.id == newItem.sport.id
    }

    override fun areContentsTheSame(
        oldItem: RunningRepository.RunningSport,
        newItem: RunningRepository.RunningSport
    ): Boolean {
        return oldItem == newItem
    }
}