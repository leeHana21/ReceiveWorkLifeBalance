package com.github.leehana21.receiveworklifebalance

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "work_life_balance"
        const val CHANNEL_ID = "SMS_CHANNEL_1"

        fun showToast(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private val mainViewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initObserver()
        getLastData()
        getRealTimeData()
        createNotificationChannel()
    }
    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun initObserver() {
        mainViewModel.fireStoreGetData.observe(this, {
            when (it) {
                null -> {
                    findViewById<TextView>(R.id.tv_data).text = "없음"
                }
                else -> {
                    findViewById<TextView>(R.id.tv_data).text = it
                }
            }
        })

        mainViewModel.fireStoreSetData.observe(this, {
            if(it != null){
             mainViewModel.showNotice(this,it)
            }
        })

    }

    private fun initView() {
        findViewById<TextView>(R.id.tv_data).text = ""

    }

    private fun initTestData() {
        mainViewModel.setData()
    }

    private fun getLastData(){
        mainViewModel.getLastData()
    }

    private fun getRealTimeData(){
        mainViewModel.getRealTimeData()
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}