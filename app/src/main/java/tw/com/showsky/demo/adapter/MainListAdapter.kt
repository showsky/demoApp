package tw.com.showsky.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tw.com.showsky.demo.data.AssetsData
import tw.com.showsky.demo.databinding.ViewItemBinding

/**
 * Created by showsky on 2022/5/3
 */
class MainListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mData: ArrayList<AssetsData> = ArrayList()
    private var mItemClickListener: ItemClickListener? = null

    interface ItemClickListener {
        fun onClick(view: View, assetsData: AssetsData)
    }

    inner class ItemViewHolder(private val mBind: ViewItemBinding) : RecyclerView.ViewHolder(mBind.root) {

        lateinit var mAssetsData: AssetsData

        init {
            mBind.root.setOnClickListener { view ->
                mItemClickListener?.onClick(view, mAssetsData)
            }
        }

        public fun setData(assetsData: AssetsData) {
            mAssetsData = assetsData
            Glide.with(mBind.imageViewImage)
                .load(mAssetsData.imageUrl)
                .into(mBind.imageViewImage)
            mBind.textViewName.text = mAssetsData.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(
            ViewItemBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: ItemViewHolder = holder as ItemViewHolder
        viewHolder.setData(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    public fun setItemOnClickListener(itemClickListener: ItemClickListener) {
        mItemClickListener = itemClickListener
    }

    public fun setData(data: List<AssetsData> ) {
        mData.addAll(data)
        notifyDataSetChanged()
    }
}
