package com.skyautonet.seda_aiv.ui.videostorage

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.data.source.local.file.VideoFile
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class VideoStorageAdapter(
    val context: Context,
    var videoList: MutableList<VideoFile>,
    rvList: RecyclerView,
    var videoItemClick: VideoItemClick
    ) : RecyclerView.Adapter<VideoStorageAdapter.ViewHolder>() {

    init {
        rvList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            ResourcesCompat.getDrawable(context.resources, R.drawable.list_divider, null)?.let {
                setDrawable(it)
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video_storage, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val videoItem: VideoFile = videoList[position]

        holder.ivDelete.isSelected = videoItem.isDeleteSelected
        holder.ivDelete.tag = position

        if (!TextUtils.isEmpty(videoItem.fileName)) {
            val parseStr = videoItem.fileName.split("_")
            holder.tvEvent.text = when (parseStr[0].lowercase(Locale.getDefault())) {
                "driving" -> "Driving"
                "parking" -> "Parking"
                "event" -> "Event"
                else -> ""
            }

            if (!TextUtils.isEmpty(parseStr[1]) && parseStr[1].length >= 20) {
                val startDateStr = parseStr[1].substring(0, 14)
                val sDateFormat = SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault())
                val date = sDateFormat.parse(startDateStr)

                holder.tvDate.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)

                val sb = StringBuilder()
                val timeFormat = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
                sb.append(timeFormat.format(date)).append(" ~ ")

                val endDateStr = parseStr[1].substring(0, 8) + parseStr[1].substring(14, 20)
                val endDate = sDateFormat.parse(endDateStr)
                sb.append(timeFormat.format(endDate))

                holder.tvTime.text = sb.toString()
            }
        } else {
            holder.tvEvent.text = ""
        }

        val decimalFormat = DecimalFormat("0.00")
        "${decimalFormat.format(videoItem.size.toDouble() / 1000000)}M".also { holder.tvFileSize.text = it }
        holder.clPlay.setOnClickListener {
            holder.itemView.tag = videoItem
            videoItemClick.onPlay(holder)
        }

        holder.ivDelete.setOnClickListener {
            holder.itemView.tag = videoItem
            videoItemClick.onDeleteCheckbox(holder)
        }
    }

    fun allSelectDeleteItem() {
        var isAllSelected = true
        for (videoItem in videoList) {
            if (!videoItem.isDeleteSelected) {
                isAllSelected = false
                break
            }
        }

        for (videoItem in videoList) {
            videoItem.isDeleteSelected = !isAllSelected
        }

        notifyDataSetChanged()
    }

    fun getDeleteItemList(): MutableList<VideoFile> {
        val deleteItemList = ArrayList<VideoFile>()
        for (videoItem in videoList) {
            if (videoItem.isDeleteSelected) {
                deleteItemList.add(videoItem)
            }
        }
        return deleteItemList
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate = itemView.findViewById<TextView>(R.id.tv_date)
        val tvTime = itemView.findViewById<TextView>(R.id.tv_time)
        val tvEvent = itemView.findViewById<TextView>(R.id.tv_event)
        val tvFileSize = itemView.findViewById<TextView>(R.id.tv_file_size)
        val clPlay = itemView.findViewById<ConstraintLayout>(R.id.cl_play)
        val ivDelete = itemView.findViewById<ImageView>(R.id.iv_delete)
    }

    fun setList(videoList: MutableList<VideoFile>) {
        this.videoList = videoList
    }

    interface VideoItemClick {
        fun onPlay(viewHolder: VideoStorageAdapter.ViewHolder)
        fun onDeleteCheckbox(viewHolder: VideoStorageAdapter.ViewHolder)
    }
}