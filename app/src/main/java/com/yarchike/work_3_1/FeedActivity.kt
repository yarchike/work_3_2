package com.yarchike.work_3_1

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.yarchike.work_3_1.Post.Post
import com.yarchike.work_3_1.Post.PostAdapter
import com.yarchike.work_3_1.Post.PostData
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.postv_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException

class FeedActivity : AppCompatActivity() {
    private val postAdapter: PostAdapter = PostAdapter() { post, view ->
        if (post.isLike) {
            post.isLike = false
            post.like--
            view.likeImage.setImageResource(R.drawable.ic_no_like)
            view.likeText.setTextColor(Color.BLACK)
            view.likeText.text = post.like.toString()
        } else {
            post.isLike = true
            post.like++
            view.likeImage.setImageResource(R.drawable.ic_like)
            view.likeText.setTextColor(Color.RED)
            view.likeText.text = post.like.toString()

        }
        lifecycleScope.launch {
            try {
                //val postTemp = PostData.postPosts(post)

            } catch (e: ConnectException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@FeedActivity,
                        "Ошибка с соединенеие с сервером",
                        Toast.LENGTH_LONG
                    ).show()
                }

                if (post.isLike) {
                    post.isLike = false
                    post.like--
                    view.likeImage.setImageResource(R.drawable.ic_no_like)
                    view.likeText.setTextColor(Color.BLACK)
                    view.likeText.text = post.like.toString()
                } else {
                    post.isLike = true
                    post.like++
                    view.likeImage.setImageResource(R.drawable.ic_like)
                    view.likeText.setTextColor(Color.RED)
                    view.likeText.text = post.like.toString()
                }


            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        progressBar.visibility = ProgressBar.VISIBLE
        initRecycleView()
        addData()
    }

    private fun initRecycleView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FeedActivity)
            adapter = postAdapter
        }
    }


    private fun addData() {

        lifecycleScope.launch {
            try {
                val dataPost = Repository.getPosts()
                    .filter {
                        !it.hidePost
                    }
                val dataSponser = PostData.getPostSponsored()
                val data = ArrayList<Post>()


                var temp = 0
                var tempPost = 0
                for (element in dataPost) {
                    data.add(element)
                    temp++
                    if (temp == 3 && tempPost < dataSponser.size) {
                        temp = 0
                        data.add(dataSponser[tempPost])
                        tempPost++
                    }
                }


                Log.d("MY", data.toString())
                postAdapter.submiDataList(data as ArrayList<Post>)
                postAdapter.notifyItemRangeInserted(0, data.size)
                withContext(Dispatchers.Main) {
                    progressBar.visibility = ProgressBar.GONE
                }

            } catch (e: ConnectException) {
                Toast.makeText(
                    this@FeedActivity,
                    "Ошибка с соединенеие с сервером",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

}