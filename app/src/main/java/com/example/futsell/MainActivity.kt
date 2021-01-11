package com.example.futsell

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.futsell.ui.main.MainViewModel
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.playerLiveData.observe(this, Observer { player ->
            startBid.text = player.startingBid
            currentBid.text = player.currentBid
            buyNow.text = player.buyNowPrice
            expires.text = player.expires.toString()
            name.text = player.name
            rating.text = player.rating
            position.text = player.preferredPosition
        })

        viewModel.showProgress.observe(this, Observer {
            progress_bar.isVisible = it
        })

        viewModel.dialogFragment.observe(this, Observer {
            it.show(supportFragmentManager, it::class.java.name)
        })

    }

    override fun onStart() {
        super.onStart()
        viewModel.onActivityStart()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onActivityStop()
    }

}