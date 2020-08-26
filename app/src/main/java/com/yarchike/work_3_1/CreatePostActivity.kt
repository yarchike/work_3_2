package com.yarchike.work_3_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.yarchike.work_3_1.dto.AttachmentModel
import com.yarchike.work_3_1.dto.AttachmentType
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.create_repost.*

import kotlinx.coroutines.launch
import splitties.toast.toast
import java.io.IOException

class CreatePostActivity : AppCompatActivity() {

    private var dialog: ProgressDialog? = null
    val REQUEST_IMAGE_CAPTURE = 1
    private var attachmentModel: AttachmentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

       attachPhotoImg.setOnClickListener {
           dispatchTakePictureIntent()
       }


        createPostBtn.setOnClickListener {
            lifecycleScope.launch {
                // Показываем крутилку
                dialog = ProgressDialog(this@CreatePostActivity).apply {
                    setMessage(this@CreatePostActivity.getString(R.string.please_wait))
                    setTitle(R.string.create_new_post)
                    setCancelable(false)
                    setProgressBarIndeterminate(true)
                    show()
                }
                // Обворачиваем в try catch, потому что возможны ошибки при соединении с сетью
                try {
                    val result = App.repository.createPost(contentEdt.text.toString(), attachmentModel)
                    if (result.isSuccessful) {
                        // обрабатываем успешное создание поста
                        handleSuccessfullResult()
                    } else {
                        // обрабоатываем ошибку
                        handleFailedResult()
                    }
                } catch (e: IOException) {
                    // обрабатываем ошибку
                    handleFailedResult()
                } finally {
                    // закрываем диалог
                    dialog?.dismiss()
                }

            }
        }

    }


    private fun handleSuccessfullResult() {
        toast(R.string.post_created_successfully)
        finish()
    }

    private fun handleFailedResult() {
        toast(R.string.error_occured)
    }
    private fun transparetAllIcons() {
        attachPhotoImg.setImageResource(R.drawable.ic_image_inactive)
    }
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
    private fun imageUploaded() {
        transparetAllIcons()
        attachPhotoDoneImg.visibility = View.VISIBLE
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            imageBitmap?.let {
                lifecycleScope.launch {
                    dialog = ProgressDialog(this@CreatePostActivity).apply {
                        setMessage(this@CreatePostActivity.getString(R.string.please_wait))
                        setTitle(R.string.create_new_post)
                        setCancelable(false)
                        setProgressBarIndeterminate(true)
                        show()
                    }
                    val imageUploadResult =  App.repository.upload(it)
                    NotifictionHelper.mediaUploaded(AttachmentType.IMAGE, this@CreatePostActivity)
                    dialog?.dismiss()
                    if (imageUploadResult.isSuccessful) {
                        imageUploaded()
                        attachmentModel = imageUploadResult.body()
                    } else {
                        toast("Can't upload image")
                    }
                }
            }
        }
    }
}
