package tw.com.showsky.demo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import tw.com.showsky.demo.databinding.FragmentDetailBinding
import tw.com.showsky.demo.model.DataStatus
import tw.com.showsky.demo.model.MainViewModel
import tw.com.showsky.demo.repository.OpenseaNetworkImpl

/**
 * Created by showsky on 2022/5/2
 */
class DetailFragment : Fragment() {

    companion object {
        const val KEY_COLLECTION_NAME = "collection_name"
        const val KEY_CONTRACT_ADDRESS = "contract_address"
        const val KEY_TOKEN_ID = "token_id"

        fun newInstance(collectionName: String, contractAddress: String, tokenId: String): DetailFragment {
            val args = Bundle(3).apply {
                putString(KEY_COLLECTION_NAME, collectionName)
                putString(KEY_CONTRACT_ADDRESS, contractAddress)
                putString(KEY_TOKEN_ID, tokenId)
            }
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var mBind: FragmentDetailBinding
    private lateinit var mViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            arguments?.let {
                title = it.getString(KEY_COLLECTION_NAME)
            }
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        mBind = FragmentDetailBinding.inflate(layoutInflater, container, false)
        mViewModel = ViewModelProvider(
            requireActivity(),
            MainViewModel.MainViewModelFactory(OpenseaNetworkImpl())
        ).get(MainViewModel::class.java)
        return mBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                parentFragmentManager.popBackStack()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
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
        mViewModel.assetDataLiveData.observe(viewLifecycleOwner) { dataStatus ->

            when (dataStatus.status()) {

                DataStatus.LOADING -> {
                    mBind.progressView.visibility = View.VISIBLE
                }

                DataStatus.SUCCESS -> {
                    mBind.progressView.visibility = View.GONE
                    dataStatus.data()?.let { assetData ->
                        Glide.with(mBind.imageViewImage)
                            .load(assetData.imageUrl)
                            .into(mBind.imageViewImage)
                        mBind.textViewName.text = assetData.name
                        mBind.textViewDesc.text = assetData.description
                        mBind.textViewLink.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(assetData.permalink))
                            startActivity(intent)
                        }
                    }
                }

                DataStatus.EMPTY -> {
                    mBind.progressView.visibility = View.GONE
                    Toast.makeText(
                        context,
                        dataStatus.error().toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }

                DataStatus.ERROR -> {
                    mBind.progressView.visibility = View.GONE
                }
            }

        }

        val contractAddress = arguments?.getString(KEY_CONTRACT_ADDRESS) ?: ""
        val tokenId = arguments?.getString(KEY_TOKEN_ID) ?: ""
        mViewModel.loadAsset(contractAddress, tokenId)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
