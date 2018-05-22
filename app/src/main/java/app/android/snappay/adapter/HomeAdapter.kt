package app.android.snappay.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.snappay.R
import app.android.snappay.activity.SnapPayActivity
import app.android.snappay.util.ImageUtils
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.item_home.view.*

class HomeAdapter(private val context: Context) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    companion object {
        val TAG = HomeAdapter::class.simpleName
        val resIds = arrayOf(R.drawable.ic_snap_pay, R.drawable.ic_utilities, R.drawable.ic_loyalty_cards, R.drawable.ic_offers)
        val textRes = arrayOf(R.string.snap_n_pay, R.string.utilities, R.string.loyalty_cards, R.string.offers)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(position)
    }

    override fun getItemCount(): Int {
        return 4
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(position: Int) {
            ImageUtils.setImage(
                    imageView = itemView.iv_image,
                    load = resIds[position],
                    placeholderResId = resIds[position],
                    errorResId = resIds[position]
            )
            itemView.tv_label.text = context.getString(textRes[position])
            itemView.setOnClickListener {
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                when (adapterPosition) {
                    0 -> {
                        IntentIntegrator(context as Activity).apply {
                            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                            setPrompt("")
                            captureActivity = SnapPayActivity::class.java
                            initiateScan()
                        }
                    }
                    1 -> {
                    }
                    2 -> {
                    }
                    3 -> {
                    }
                }

            }
        }
    }
}
