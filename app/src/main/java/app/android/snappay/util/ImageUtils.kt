package app.android.snappay.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import app.android.snappay.GlideApp
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.io.ByteArrayOutputStream
import java.io.IOException

@Suppress("unused")
object ImageUtils {
    val TAG = ImageUtils::class.simpleName

    @Throws(IOException::class)
    fun modifyOrientation(bitmap: Bitmap, image_absolute_path: String): Bitmap {
        val ei = ExifInterface(image_absolute_path)
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap, 270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(bitmap, true, false)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(bitmap, false, true)
            else -> bitmap
        }
    }

    private fun rotate(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap {
        val matrix = Matrix()
        matrix.preScale((if (horizontal) -1 else 1).toFloat(), (if (vertical) -1 else 1).toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun setImage(
            imageView: ImageView,
            isCenterCrop: Boolean = false,
            isCenterInside: Boolean = false,
            isRoundCorners: Boolean = false,
            isCircleCrop: Boolean = false,
            roundingRadius: Int = 5.toPx,
            load: Any?,
            placeholderResId: Any? = null,
            errorResId: Any? = null
    ) {
        if (!isValidContextForGlide(imageView.context)) return
        GlideApp.with(imageView.context).load(load).apply {
            if (isCenterCrop) centerCrop()
            if (isCenterInside) centerInside()
            if (isRoundCorners) RoundedCorners(roundingRadius)
            if (isCircleCrop) circleCrop()

            placeholderResId?.let {
                when (it) {
                    is Drawable -> placeholder(it)
                    is Int -> placeholder(it)
                    else -> {
                    }
                }
            }

            errorResId?.let {
                when (it) {
                    is Drawable -> error(it)
                    is Int -> error(it)
                    else -> {
                    }
                }

            }
            into(imageView)
        }
    }

    /*https://github.com/bumptech/glide/issues/1484*/
    // You cannot start a load for a destroyed activity
    private fun isValidContextForGlide(context: Context?): Boolean {
        if (context == null) return false

        if (context is Activity) {
            if (context.isDestroyed || context.isFinishing) {
                return false
            }
        }
        return true
    }

    fun fixOrientation(bitmap: Bitmap): Bitmap {
        if (bitmap.width > bitmap.height) {
            val matrix = Matrix()
            matrix.postRotate(90f)
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
        return bitmap
    }

    fun getImageUri(context: Context, inImage: Bitmap): Uri {
        @Suppress("UNUSED_VARIABLE")
        val bytes = ByteArrayOutputStream()
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "Title", null)
        Log.d(TAG, "getImageUri() returned: " + path)
        return Uri.parse(path)
    }

    fun resize(originalBitmap: Bitmap, size: Int): Bitmap {
        val w = originalBitmap.width
        val h = originalBitmap.height
        if (Math.max(w, h) > size) {
            val scalledW: Int
            val scalledH: Int
            if (w <= h) {
                scalledW = (w / (h.toDouble() / size)).toInt()
                scalledH = size
            } else {
                scalledW = size
                scalledH = (h / (w.toDouble() / size)).toInt()
            }
            val result = Bitmap.createScaledBitmap(originalBitmap, scalledW, scalledH, true)
            if (!originalBitmap.isRecycled) {
                originalBitmap.recycle()
            }
            return result
        } else {
            return originalBitmap
        }
    }
}
