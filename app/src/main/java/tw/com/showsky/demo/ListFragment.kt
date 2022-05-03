package tw.com.showsky.demo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tw.com.showsky.demo.adapter.MainListAdapter
import tw.com.showsky.demo.data.AssetsData
import tw.com.showsky.demo.databinding.FragmentListBinding
import tw.com.showsky.demo.model.DataStatus
import tw.com.showsky.demo.model.MainViewModel
import tw.com.showsky.demo.repository.OpenseaNetworkImpl

/**
 * Created by showsky on 2022/5/3
 */
class ListFragment : Fragment() {

    lateinit var mAdapter: MainListAdapter
    lateinit var mBind: FragmentListBinding
    lateinit var mViewModel: MainViewModel

    var mOffset = 0
    var mIsLoading = false
    var mIsDataEnd = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.app_name)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(false)
        }
        mBind = FragmentListBinding.inflate(inflater, container, false)
        mViewModel = ViewModelProvider(
            requireActivity(),
            MainViewModel.MainViewModelFactory(OpenseaNetworkImpl())
        ).get(MainViewModel::class.java)

        return mBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                owner.lifecycle.removeObserver(this)
            }
        })
    }

    private fun initView() {
        if (!this::mAdapter.isInitialized) {
            mAdapter = MainListAdapter()
        }
        mAdapter.setItemOnClickListener(object : MainListAdapter.ItemClickListener {
            override fun onClick(view: View, assetsData: AssetsData) {
                val detailFragment = DetailFragment.newInstance(
                    assetsData.collectionName!!,
                    assetsData.contractAddress!!,
                    assetsData.tokenId!!
                )
                parentFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, detailFragment)
                    .addToBackStack("detail")
                    .commit()
            }
        })
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL

        mBind.recyclerViewList.layoutManager = gridLayoutManager
        mBind.recyclerViewList.adapter = mAdapter
        mBind.recyclerViewList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        if (mIsLoading) {
                            return
                        }
                        val count = mAdapter.itemCount
                        val lastPosition = (mBind.recyclerViewList.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                        if (lastPosition == (count - 1)) {
                            if (mIsDataEnd) {
                                Toast.makeText(context, "Data End", Toast.LENGTH_SHORT).show()
                                return
                            }
                            mOffset += MainViewModel.SIZE
                            mViewModel.loadAssets(mOffset)
                        }
                    }
                }
            }
        })

        mViewModel.assetsDataLiveData.observe(viewLifecycleOwner) { dataStatus ->

            when (dataStatus.status()) {

                DataStatus.LOADING -> {
                    mIsLoading = true
                    mBind.progressView.visibility = View.VISIBLE
                }

                DataStatus.SUCCESS -> {
                    mIsLoading = false
                    mBind.progressView.visibility = View.GONE
                    dataStatus.data()?.let {
                        mAdapter.setData(it)
                    }
                }

                DataStatus.ERROR -> {
                    mIsLoading = false
                    mBind.progressView.visibility = View.GONE
                    Toast.makeText(
                        context,
                        dataStatus.error().toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }

                DataStatus.EMPTY -> {
                    mIsLoading = false
                    mBind.progressView.visibility = View.GONE
                    mIsDataEnd = true
                }

            }

        }

        if (!mIsDataEnd && !mIsLoading) {
            mViewModel.loadAssets(mOffset)
        }
    }
}