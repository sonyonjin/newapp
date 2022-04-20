package com.skyautonet.seda_aiv.ui.videolist

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
import com.skyautonet.seda_aiv.model.VideoItem
import java.text.SimpleDateFormat
import java.util.*

class VideoListAdapter(
    val context: Context,
    var videoList: MutableList<VideoItem>,
    rvList: RecyclerView,
    var videoItemClick: VideoItemClick
    ) : RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {

    init {
        rvList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            ResourcesCompat.getDrawable(context.resources, R.drawable.list_divider, null)?.let {
                setDrawable(it)
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val videoItem = videoList[position]

        setEnabledItem(holder, videoItem.isEnable)
        holder.clDownload.tag = position

        if (!TextUtils.isEmpty(videoItem.date_time)) {
            val sDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
            val date = sDateFormat.parse(videoItem.date_time)

            holder.tvDate.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)

            val sb = StringBuilder()
            val timeFormat = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
            sb.append(timeFormat.format(date)).append(" ~ ")
            date.time += 30000
            sb.append(timeFormat.format(date))

            holder.tvTime.text = sb.toString()
        } else {
            holder.tvDate.text = ""
            holder.tvTime.text = ""
        }

        if (!TextUtils.isEmpty(videoItem.file_name)) {
            val parseStr = videoItem.file_name.split("_")
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

        if (!TextUtils.isEmpty(videoItem.size)) {
            "${videoItem.size}M".also {
                holder.tvFileSize.text = it
                holder.clDownload.setOnClickListener {
                    holder.itemView.tag = videoItem
                    videoItemClick.onDownload(holder)
                }
            }
        } else {
            holder.tvFileSize.text = ""
            holder.clDownload.isEnabled = false
        }
    }

    private fun setEnabledItem(viewHolder: ViewHolder, isEnabled: Boolean) {
        viewHolder.tvDate.isEnabled = isEnabled
        viewHolder.tvTime.isEnabled = isEnabled
        viewHolder.tvEvent.isEnabled = isEnabled
        viewHolder.tvFileSize.isEnabled = isEnabled
        viewHolder.clDownload.isEnabled = isEnabled
        viewHolder.ivDownload.isEnabled = isEnabled
    }

    fun setDisableItem(viewHolder: ViewHolder) {
        if (viewHolder.itemView.tag is VideoItem) {
            val videoItem = viewHolder.itemView.tag as VideoItem
            videoItem.isEnable = false

            if (viewHolder.clDownload.tag is Int) {
                val position = viewHolder.clDownload.tag as Int
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate = itemView.findViewById<TextView>(R.id.tv_date)
        val tvTime = itemView.findViewById<TextView>(R.id.tv_time)
        val tvEvent = itemView.findViewById<TextView>(R.id.tv_event)
        val tvFileSize = itemView.findViewById<TextView>(R.id.tv_file_size)
        val clDownload = itemView.findViewById<ConstraintLayout>(R.id.cl_play)
        val ivDownload = itemView.findViewById<ImageView>(R.id.iv_download)
    }

    fun setList(videoList: MutableList<VideoItem>) {
        this.videoList = videoList
    }

    interface VideoItemClick {
        fun onDownload(viewHolder: VideoListAdapter.ViewHolder)
    }
}