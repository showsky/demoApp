package tw.com.showsky.demo

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tw.com.showsky.demo.adapter.MainListAdapter
import tw.com.showsky.demo.data.AssetsData
import tw.com.showsky.demo.databinding.ActivityMainBinding
import tw.com.showsky.demo.model.DataStatus
import tw.com.showsky.demo.model.MainViewModel
import tw.com.showsky.demo.repository.OpenseaNetworkImpl

class MainActivity : AppCompatActivity() {

    lateinit var mContext: Context
    lateinit var mBind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mBind = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(mBind.toolbar)
        setContentView(mBind.root)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
