package com.yarchike.work_3_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.yarchike.work_3_1.dto.AttachmentType
import com.yarchike.work_3_1.dto.PostModel
import kotlinx.android.synthetic.main.activity_one_post.*
import kotlinx.android.synthetic.main.item_post.view.*
import kotlinx.coroutines.launch
import splitties.toast.toast

class OnePostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_post)
        val id = intent.getStringExtra("id")
        lifecycleScope.launch {
            val resp = App.repository.getPostId(id.toLong())
            val post = resp.body()
            print(post)
            authorTv.text = post?.autor
            contentTv.text = post?.postResurse
            likesTv.text = post?.like.toString()
            repostsTv.text = post?.share.toString()
            when {
                post?.likeActionPerforming!! -> likeBtn.setImageResource(R.drawable.ic_favorite_pending_24dp)
                post?.isLike -> {
                    likeBtn.setImageResource(R.drawable.ic_like)
                    likesTv.setTextColor(
                        ContextCompat.getColor(
                            this@OnePostActivity,
                            R.color.colorRed
                        )
                    )
                }
                else -> {
                    likeBtn.setImageResource(R.drawable.ic_no_like)
                    likesTv.setTextColor(
                        ContextCompat.getColor(
                            this@OnePostActivity,
                            R.color.colorGrey
                        )
                    )
                }
            }
            when {
                post?.repostActionPerforming!! -> {
                    shareBtn.setImageResource(R.drawable.ic_share_pending_24dp)
                }
                post?.isShare!! -> {
                    shareBtn.setImageResource(R.drawable.ic_share)
                    repostsTv.setTextColor(
                        ContextCompat.getColor(
                            this@OnePostActivity,
                            R.color.colorGrey
                        )
                    )
                }
                else -> {
                    shareBtn.setImageResource(R.drawable.ic_no_share)
                    repostsTv.setTextColor(
                        ContextCompat.getColor(
                            this@OnePostActivity,
                            R.color.colorGrey
                        )
                    )
                }
            }
            when (post?.attachment?.mediaType) {
                AttachmentType.IMAGE -> loadImage(photoImg, post?.attachment.url)
            }
        }
    }
    private fun loadImage(photoImg: ImageView, imageUrl: String) {
        Glide.with(photoImg.context)
            .load(imageUrl)
            .into(photoImg)
    }


}