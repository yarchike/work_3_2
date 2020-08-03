package com.yarchike.work_3_1.Post


import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yarchike.work_3_1.R
import kotlinx.android.synthetic.main.postv_view.view.*


class PostAdapter(
    private val onLikeClicked: (Post, View) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: ArrayList<Post> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(
                R.layout.postv_view, parent, false
            )
        )
    }

    fun getIteanList(): List<Post> {
        return items
    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PostViewHolder -> (
                    holder.bind(items[position])
                    )
        }
    }

    fun submiDataList(blockList: ArrayList<Post>) {
        items = blockList
    }

    inner class PostViewHolder
    constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val autorText = itemView.autorText
        val postText = itemView.postText
        val likeText = itemView.likeText
        val commentText = itemView.commentText
        val shareText = itemView.shareText
        val datePost = itemView.datePost
        val likeImage = itemView.likeImage
        val imageLocal = itemView.imageLocal
        val postImage = itemView.postImage
        val repostImgAutor = itemView.repostImgAutor
        val repostDateText = itemView.repostDateText
        val repostAutorText = itemView.repostAutorText
        val typePost = itemView.typePost
        val imageHide = itemView.imageHide
        val quantityView = itemView.quantityView

        fun bind(post: Post) {
            if (post.hidePost) {
                items.remove(post)
            }


            val requesoption = RequestOptions().placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
            when (post.type) {
                PostTypes.YoutubeVideo -> {
                    postText.visibility = View.GONE
                    postImage.visibility = View.VISIBLE
                    postImage.setImageResource(R.drawable.ic_play)
                    postImage.setOnClickListener {
                        val intent = Intent().apply {
                            action = Intent.ACTION_VIEW
                            setData(Uri.parse(post.postResurse))
                        }
                        itemView.context.startActivity(intent)
                    }
                    typePost.setText(R.string.Youtube_video)
                }
                PostTypes.SponsoredPosts -> {
                    postText.visibility = View.GONE
                    postImage.visibility = View.VISIBLE
                    Glide.with(itemView.context)
                        .applyDefaultRequestOptions(requesoption)
                        .load(post.postResurse)
                        .into(postImage)
                    postImage.setOnClickListener {
                        val intent = Intent().apply {
                            action = Intent.ACTION_VIEW
                            setData(Uri.parse(post.url))
                        }
                        itemView.context.startActivity(intent)
                    }
                    typePost.setText(R.string.Sponsored_posts)
                }
                PostTypes.Reposts -> {
                    repostImgAutor.visibility = View.VISIBLE
                    repostDateText.visibility = View.VISIBLE
                    repostAutorText.visibility = View.VISIBLE
                    postText.setText(post.postResurse)
                    repostDateText.setText(post.dateRepost?.toString())


                    repostAutorText.setText(post.autorRepost)

                    repostDateText.setText(dateToString(post))
                    typePost.setText(R.string.Reposts)
                }


                PostTypes.Events -> {
                    postText.setText(post.postResurse)
                    typePost.setText(R.string.Events)
                }
            }



            autorText.setText(post.autor)


            if (post.like > 0) {
                likeText.setText(post.like.toString())
            } else {
                likeText.setText("")
            }
            if (post.comments > 0) {
                commentText.setText(post.comments.toString())
            } else {
                commentText.setText("")
            }
            if (post.share > 0) {
                shareText.setText(post.share.toString())
            } else {
                shareText.setText("")
            }


            datePost.setText(dateToString(post))
            quantityView.setText(post.viewsPost.toString())




            likeImage.setOnClickListener {

                (onLikeClicked(post, itemView))


            }





            imageLocal.setOnClickListener {
                try {
                    val (lat, lng) = post.coordinates
                    val geoUri = Uri.parse("geo:$lat,$lng")
                    val intent = Intent().apply {
                        action = Intent.ACTION_VIEW
                        setData(geoUri)
                    }
                    itemView.context.startActivity(intent)
                } catch (e: NullPointerException) {
                    Toast.makeText(
                        itemView.context,
                        "Данных геопозиции не заданы",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
            imageHide.setOnClickListener {
                post.hidePost = true
                items.remove(post)
                notifyItemRemoved(adapterPosition)

            }


        }

        private fun dateToString(post: Post): String {
            val publishedAgo = (System.currentTimeMillis() - post.date) / 1000
            val toMin = if (publishedAgo > 3599) {
                publishedAgo / 3600
            } else {
                publishedAgo / 60
            }
            val dateString = when (publishedAgo) {
                in 0..59 -> "менее минуты назад"
                in 60..179 -> "минуту назад"
                in 180..299 -> "$toMin минуты назад"
                in 300..3599 -> "$toMin минут назад"
                in 3600..17999 -> "$toMin часа назад"
                else -> "$toMin часов назад "
            }
            return dateString
        }

    }


}