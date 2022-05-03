package tw.com.showsky.demo

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tw.com.showsky.demo.databinding.ActivityMainBinding

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
