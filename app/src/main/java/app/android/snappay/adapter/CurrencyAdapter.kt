package app.android.snappay.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.snappay.R
import app.android.snappay.constant.BundleConstant
import app.android.snappay.model.bean.CurrencyBean
import app.android.snappay.util.ImageUtils
import kotlinx.android.synthetic.main.item_country_currency.view.*

class CurrencyAdapter(private val context: Context, private var currencies: ArrayList<CurrencyBean>) : RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    companion object {
        val TAG = CurrencyAdapter::class.simpleName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_country_currency, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(position)
    }

    override fun getItemCount(): Int {
        return currencies.size
    }

    fun updateDataset(currencies: ArrayList<CurrencyBean>) {
        this.currencies = currencies
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(position: Int) {
            itemView.setOnClickListener {
                Intent().apply {
                    putExtra(BundleConstant.CURRENCY_BEAN, currencies[adapterPosition])
                    (context as Activity).setResult(Activity.RESULT_OK, this)
                    context.finish()
                }
            }
            itemView.tv_country_name.text = currencies[position].countryName
            itemView.tv_currency_iso_code.text = currencies[position].currencyCode
            ImageUtils.setImage(
                    imageView = itemView.iv_flag,
                    load = currencies[position].flagResId
            )
        }
    }
}


