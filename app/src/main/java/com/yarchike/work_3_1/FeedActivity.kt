package com.yarchike.work_3_1


import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.yarchike.work_3_1.adapter.PostAdapter
import com.yarchike.work_3_1.dto.PostModel
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.item_load_more.*
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast
import java.util.concurrent.TimeUnit

class FeedActivity : AppCompatActivity(),
    PostAdapter.OnLikeBtnClickListener, PostAdapter.OnRepostsBtnClickListener,
    PostAdapter.OnLoadMoreBtnClickListener {
    private var dialog: ProgressDialog? = null
    var adapter = PostAdapter(ArrayList<PostModel>())
    var items = ArrayList<PostModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        fab.setOnClickListener {
            start<CreatePostActivity>()
        }
        swipeContainer.setOnRefreshListener {
            refreshData()
        }

    }

    private fun refreshData() {
        lifecycleScope.launch {
            val newData = App.repository.getPosts()
            swipeContainer.isRefreshing = false
            if (newData.isSuccessful) {
                adapter?.newRecentPosts(newData.body()!!)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            dialog = ProgressDialog(this@FeedActivity).apply {
                setMessage(this@FeedActivity.getString(R.string.please_wait))
                setTitle(R.string.downloading_posts)
                setCancelable(false)
                setProgressBarIndeterminate(true)
                show()
            }
            val result = App.repository.getPosts()
            println(result.toString())
            dialog?.dismiss()
            if (result.isSuccessful) {
                with(container) {
                    items = result.body() as ArrayList<PostModel>
                    layoutManager = LinearLayoutManager(this@FeedActivity)
                    adapter = PostAdapter(items as MutableList<PostModel>).apply {
                        likeBtnClickListener = this@FeedActivity
                        repostsBtnClickListener = this@FeedActivity
                        loadMoreBtnClickListener = this@FeedActivity
                    }
                }
            } else {
                toast(R.string.error_occured)
            }
        }
    }


    override fun onLikeBtnClicked(item: PostModel, position: Int) {
        lifecycleScope.launch {
            item.likeActionPerforming = true
            with(container) {
                adapter?.notifyItemChanged(position)
                val response = if (item.isLike) {
                    App.repository.cancelMyLike(item.id.toLong())
                } else {
                    App.repository.likedByMe(item.id.toLong())
                }
                item.likeActionPerforming = false
                if (response.isSuccessful) {
                    item.updatePost(response.body()!!)
                }
                adapter?.notifyItemChanged(position)
            }
        }
    }

    override fun onRepostsBtnClicked(item: PostModel, position: Int, content: String) {

        lifecycleScope.launch {
            item.repostActionPerforming = true
            with(container) {
                adapter?.notifyItemChanged(position)
                val response =
                    App.repository.createRepost(content, item)
                item.repostActionPerforming = false
            }
        }
    }

    override fun onLoadMoreBtnClickListener(last: Long, size: Int) {
        lifecycleScope.launch {


            val response =
                App.repository.getPostsOld(last)
            progressbar.visibility = View.INVISIBLE
            loadMoreBtn.isEnabled = true
            if (response.isSuccessful) {
                // Если все успешно, то новые элементы добавляем в начало
                // нашего списка.
                val newItems = response.body()!!

                items.addAll(newItems)
                adapter.newRecentPosts(items)
                // Оповещаем адаптер о новых элементах
                with(container) {
                    adapter?.notifyItemRangeInserted(size + newItems.size, newItems.size)
                }

            } else {
                toast("Error occured")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFirstTime(this)) {
            NotifictionHelper.comeBackNotification(this)
            setLastVisitTime(this, System.currentTimeMillis())
        } else {
            setLastVisitTime(this, System.currentTimeMillis())
        }

    }

    private fun scheduleJob() {
        val checkWork =
            PeriodicWorkRequestBuilder<UserNotHereWorker>(
                SHOW_NOTIFICATION_AFTER_UNVISITED_MS,
                TimeUnit.MILLISECONDS
            )
                .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "user_present_work",
                ExistingPeriodicWorkPolicy.KEEP, checkWork
            )
    }
}





