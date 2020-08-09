package com.yarchike.work_3_1.adapter


import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yarchike.work_3_1.R
import com.yarchike.work_3_1.dto.PostModel
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.create_post.*
import kotlinx.android.synthetic.main.item_post.view.*
import kotlinx.android.synthetic.main.item_post.view.authorTv
import kotlinx.android.synthetic.main.item_post.view.contentTv
import kotlinx.android.synthetic.main.item_post.view.likeBtn
import kotlinx.android.synthetic.main.item_post.view.likesTv
import kotlinx.android.synthetic.main.item_post.view.repostsTv
import kotlinx.android.synthetic.main.item_post.view.shareBtn
import kotlinx.android.synthetic.main.item_repost.view.*
import splitties.toast.toast

class PostAdapter(val list: List<PostModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var likeBtnClickListener: OnLikeBtnClickListener? = null
    var repostsBtnClickListener: OnRepostsBtnClickListener? = null
    private val ITEM_TYPE_POST = 1
    private val ITEM_TYPE_REPOST = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_POST) {
            val postView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
            PostViewHolder(this, postView)
        } else {
            val repostView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_repost, parent, false)
            RepostViewHolder(this, repostView)
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PostViewHolder -> holder.bind(list[position])
            is RepostViewHolder -> holder.bind(list[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].repostResurs == null) {
            ITEM_TYPE_POST
        } else ITEM_TYPE_REPOST
    }


    interface OnLikeBtnClickListener {
        fun onLikeBtnClicked(item: PostModel, position: Int)
    }

    interface OnRepostsBtnClickListener {
        fun onRepostsBtnClicked(item: PostModel, position: Int)
    }


}

class RepostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            likeBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition]
                    if (item.likeActionPerforming) {
                        context.toast(context.getString(R.string.like_in_progress))
                    } else {
                        adapter.likeBtnClickListener?.onLikeBtnClicked(item, currentPosition)
                    }
                }
            }
        }
    }

    fun bind(post: PostModel) {
        with(itemView) {
            authorTv.text = post.repostResurs?.autor
            contentTv.text = post.repostResurs?.postResurse
            likesTv.text = post.like.toString()
            repostsTv.text = post.share.toString()
            autorRP.text = post.autor

            when {
                post.likeActionPerforming -> likeBtn.setImageResource(R.drawable.ic_favorite_pending_24dp)
                post.isLike -> {
                    likeBtn.setImageResource(R.drawable.ic_like)
                    likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }
                else -> {
                    likeBtn.setImageResource(R.drawable.ic_no_like)
                    likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
                }
            }
            when {
                post.repostActionPerforming -> {
                    shareBtn.setImageResource(R.drawable.ic_share_pending_24dp)
                }
                post.isShare -> {
                    shareBtn.setImageResource(R.drawable.ic_share)
                    repostsTv.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
                }
                else -> {
                    shareBtn.setImageResource(R.drawable.ic_no_share)
                    repostsTv.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
                }
            }
        }
    }

}

class PostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            likeBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition]
                    if (item.likeActionPerforming) {
                        context.toast(context.getString(R.string.like_in_progress))
                    } else {
                        adapter.likeBtnClickListener?.onLikeBtnClicked(item, currentPosition)
                    }
                }
            }
            shareBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
                    if (item.isShare) {
                        context.toast("Can't repost repost)")
                    } else {
                        showDialog(context) {
                            adapter.repostsBtnClickListener?.onRepostsBtnClicked(
                                item,
                                currentPosition
                            )
                        }
                    }
                }
            }
        }
    }

    fun bind(post: PostModel) {
        with(itemView) {
            authorTv.text = post.autor
            contentTv.text = post.postResurse
            likesTv.text = post.like.toString()
            repostsTv.text = post.share.toString()

            when {
                post.likeActionPerforming -> likeBtn.setImageResource(R.drawable.ic_favorite_pending_24dp)
                post.isLike -> {
                    likeBtn.setImageResource(R.drawable.ic_like)
                    likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }
                else -> {
                    likeBtn.setImageResource(R.drawable.ic_no_like)
                    likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
                }
            }
            when {
                post.repostActionPerforming -> {
                    shareBtn.setImageResource(R.drawable.ic_share_pending_24dp)
                }
                post.isShare -> {
                    shareBtn.setImageResource(R.drawable.ic_share)
                    repostsTv.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
                }
                else -> {
                    shareBtn.setImageResource(R.drawable.ic_no_share)
                    repostsTv.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
                }
            }
        }
    }
    fun showDialog(context: Context, createBtnClicked: (content: String) -> Unit) {
        val dialog = AlertDialog.Builder(context)
            .setView(R.layout.create_post)
            .show()
        dialog.createPostBtn.setOnClickListener {
            createBtnClicked(dialog.contentEdtRepost.text.toString())
            dialog.dismiss()
        }
    }


}

