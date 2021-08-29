package com.assignment.githubrepoapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assignment.githubrepoapp.R
import com.assignment.githubrepoapp.data.model.RepoListModel
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.item_repo_list.view.*


class RepoListAdapter(repoListModelArrayList: ArrayList<RepoListModel>, context: Context)
    : RecyclerView.Adapter<RepoListAdapter.ViewHolder>() {

    private val repoListModelArrayList: ArrayList<RepoListModel>
    private val context: Context

    init {
        this.repoListModelArrayList = repoListModelArrayList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_repo_list, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val repoModal: RepoListModel = repoListModelArrayList[position]

        holder.itemAuthor.text = repoModal.author
        holder.itemName.text = repoModal.name
        holder.itemDescription.text = repoModal.description
        holder.itemLanguage.text = repoModal.language
        holder.itemStar.text = repoModal.stars.toString()
        holder.itemFork.text = repoModal.forks.toString()
        holder.itemAvatarImg.setImageURI(Uri.parse(repoModal.avatar))
    }

    override fun getItemCount(): Int {
        return repoListModelArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemAuthor: TextView = itemView.item_author
        val itemName: TextView = itemView.item_name
        val itemDescription: TextView = itemView.item_description
        val itemAvatarImg: ShapeableImageView = itemView.item_avatar_img
        val itemLanguage: TextView = itemView.item_language
        val itemStar: TextView = itemView.item_star
        val itemFork: TextView = itemView.item_fork
    }
}

