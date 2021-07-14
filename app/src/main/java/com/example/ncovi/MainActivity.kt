package com.example.ncovi

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.demomotion.data.database.AppDatabase
import com.example.ncovi.base.BaseActivity
import com.example.ncovi.data.datasource.local.LocalCoviDataSourceImpl
import com.example.ncovi.data.datasource.remote.RemoteCoviDataSourceImpl
import com.example.ncovi.data.datasource.remote.api.CoroutineState
import com.example.ncovi.data.datasource.remote.api.RetrofitBuilder
import com.example.ncovi.data.model.Recent
import com.example.ncovi.data.repo.CoviRepositoryImpl
import com.example.ncovi.databinding.ActivityMainBinding
import com.example.ncovi.ui.main.MainViewModel
import com.example.ncovi.utils.extensions.formatDate
import com.example.ncovi.utils.extensions.hide
import com.example.ncovi.utils.extensions.show
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    private var isFirstTime = true

    override fun inflateViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun initViews() {
    }

    override fun setupData() {
        viewModel =
            MainViewModel(
                CoviRepositoryImpl(
                    RemoteCoviDataSourceImpl(RetrofitBuilder.apiService),
                    LocalCoviDataSourceImpl(AppDatabase.invoke(this).coviDao())
                )
            )
        viewModel.getRecent()
    }

    override fun observeData() {
        lifecycleScope.launch {
            viewModel.recentCovi.collect {
                when (it.status) {
                    CoroutineState.LOADING -> {
                        displayRecentLoadingState()
                    }
                    CoroutineState.SUCCESS -> {
                        displayRecentSuccessState(it.data ?: Recent())
                    }
                    CoroutineState.ERROR -> {
                        displayRecentErrorState(it.message.toString())
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.historyCovi.collect {
                when (it.status) {
                    CoroutineState.LOADING -> {
                        displayHistoryLoadingState()
                    }
                    CoroutineState.SUCCESS -> {
                        displayHistorySuccessState(it.data ?: emptyList())
                    }
                    CoroutineState.ERROR -> {
                        displayHistoryErrorState(it.message.toString())
                    }
                }
            }
        }
        viewModel.localData.observe(this, Observer {
            if (it.isEmpty()) {
                viewModel.getHistory(400, 0)
            } else {
                displayHistorySuccessState(it.asReversed())
            }
        })
    }

    override fun handleEvents() = with(viewBinding) {
        swipeLayout.setOnRefreshListener {
            viewModel.getRecent()
            swipeLayout.isRefreshing = false
        }
    }

    override fun onActivityReady(savedInstanceState: Bundle?) {
    }

    override fun onActivityReady() {

    }

    override fun onResume() {
        super.onResume()
        viewModel.getRecent()
    }

    private fun displayRecentLoadingState() {
        viewBinding.progressBar.show()
    }

    private fun displayRecentErrorState(message: String) {
        viewBinding.progressBar.hide()
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun displayHistoryLoadingState() {
        viewBinding.progressBarChart.show()
    }

    private fun displayHistoryErrorState(message: String) {
        viewBinding.progressBarChart.hide()
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun displayRecentSuccessState(data: Recent) = with(viewBinding) {
        viewBinding.progressBar.hide()
        layoutGeneral.apply {
            textNumInfected.text = data.infected.toString()
            textNumTreated.text = data.treated.toString()
            textNumRecovered.text = data.recovered.toString()
            textNumDeceased.text = data.deceased.toString()
            textUpdateDate.text = getString(
                R.string.title_update_date_formatted,
                data.lastUpdatedAtApify.formatDate()
            )
        }
        viewModel.localData.value?.let {
            if (it.isNotEmpty()) {
                if (it.last().lastUpdatedAtApify == data.lastUpdatedAtApify) {
                    viewModel.delete(it.last().id)
                    viewModel.insert(data)
                }
            }
        }
    }

    private fun displayHistorySuccessState(data: List<Recent>) {
        viewBinding.progressBarChart.hide()
        val filteredList =
            data.map { it.copy(lastUpdatedAtApify = it.lastUpdatedAtApify.split("T")[0]) }
                .distinctBy { it.lastUpdatedAtApify }.asReversed()
        val infectedList = filteredList.map { item -> item.infected.toFloat() }
        val treatedList = filteredList.map { item -> item.treated.toFloat() }
        val recoveredList = filteredList.map { item -> item.recovered.toFloat() }
        val deceasedList = filteredList.map { item -> item.deceased.toFloat() }
        val maxNum = infectedList.last().toInt()
        viewBinding.chartViewInfected.displayChart(
            infectedList,
            ContextCompat.getColor(this, R.color.color_infected),
            CHART_MAX_HEIGHT,
            maxNum
        )
        viewBinding.chartViewTreated.displayChart(
            treatedList,
            ContextCompat.getColor(this, R.color.color_treated),
            (CHART_MAX_HEIGHT * treatedList[0] / maxNum).toInt(),
            maxNum
        )
        viewBinding.chartViewRecovered.displayChart(
            recoveredList,
            ContextCompat.getColor(this, R.color.color_recovered),
            (CHART_MAX_HEIGHT * recoveredList[0] / maxNum).toInt(),
            maxNum
        )
        viewBinding.chartViewDeceased.displayChart(
            deceasedList,
            ContextCompat.getColor(this, R.color.color_deceased),
            (CHART_MAX_HEIGHT * deceasedList[0] / maxNum).toInt(),
            maxNum
        )
        viewBinding.textMaxY.text = maxNum.toString()
        viewModel.localData.value?.let {
            if (it.isEmpty()) {
                viewModel.insertAll(data.asReversed())
            }
        }
        val dayList = filteredList.map { it.lastUpdatedAtApify }.asReversed()
        val textList = mutableListOf<TextView>()
        val params = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        )
        for (i in dayList.indices step 7) {
            val textDay = TextView(this)
            textDay.text = dayList[i].split("-")[2] + "/" + dayList[i].split("-")[1]
            textDay.layoutParams = params
            textDay.gravity = Gravity.CENTER
            textDay.textSize = 12f
            textDay.setTextColor(ContextCompat.getColor(this, R.color.color_chart_desc))
            textList.add(textDay)
        }
        viewBinding.layoutDays.removeAllViews()
        textList.asReversed().forEach {
            viewBinding.layoutDays.addView(it)
        }
        viewBinding.layoutDays.invalidate()
    }

    companion object {
        private const val CHART_MAX_HEIGHT = 600
    }
}
