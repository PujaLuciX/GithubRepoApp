package com.assignment.githubrepoapp.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.assignment.githubrepoapp.R
import com.assignment.githubrepoapp.data.model.RepoListModel
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.item_repo_list.view.*

class RepoListAdapter(repoListModelArrayList: MutableList<RepoListModel>, context: Context)
    : RecyclerView.Adapter<RepoListAdapter.ViewHolder>() {

    private val repoListModelArrayList: MutableList<RepoListModel>
    private val context: Context
    private var mExpandedPosition : Int = -1
    private var recyclerView : RecyclerView? = null

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

        val repoModal: RepoListModel = repoListModelArrayList.get(position)
        Log.d("Puja: ", "The language color is "+repoModal.languageSymbolColor)
        holder.itemAuthor.text = repoModal.author
        holder.itemName.text = repoModal.name
        holder.itemDescription.text = repoModal.description
        holder.itemLanguage.text = repoModal.language
        holder.itemStar.text = repoModal.stars.toString()
        holder.itemFork.text = repoModal.forks.toString()
        val mDrawable: LayerDrawable = ContextCompat.getDrawable(context,
                R.drawable.solid_circle) as LayerDrawable
        val  shape =   mDrawable.findDrawableByLayerId(R.id.solidCircle) as (GradientDrawable)
        shape.setColor(Color.parseColor(repoModal.languageSymbolColor))
        holder.itemLanguageSymbolColor.setImageDrawable(mDrawable)

        Log.d("Puja: ", "Image url is " + Uri.parse(repoModal.avatar))
        holder.itemAvatarImg.setImageURI(null);
        holder.itemAvatarImg.setImageURI(Uri.parse(repoModal.avatar))
        Log.d("Puja: ", "Value set in Holder.....onBindViewHolder(): called" + position.toString())

        val isExpanded = position == mExpandedPosition
        holder.group.setVisibility(if (isExpanded) View.VISIBLE else View.GONE)
        holder.itemView.isActivated = isExpanded
        holder.itemView.setOnClickListener {
            mExpandedPosition = if (isExpanded) -1 else position
            TransitionManager.beginDelayedTransition(recyclerView)
            notifyItemChanged(position)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun getItemCount(): Int {
        return repoListModelArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemAuthor: TextView = itemView.item_author
        val itemName: TextView = itemView.item_name
        val itemDescription: TextView = itemView.item_description
        val itemAvatarImg: ShapeableImageView = itemView.item_avatar_img
        val itemLanguageSymbolColor: ImageView = itemView.language_symbol
        val itemLanguage: TextView = itemView.item_language
        val itemStar: TextView = itemView.item_star
        val itemFork: TextView = itemView.item_fork
        val group: Group = itemView.group
    }
}
