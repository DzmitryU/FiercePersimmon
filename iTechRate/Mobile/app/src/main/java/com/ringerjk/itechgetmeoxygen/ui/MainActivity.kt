package com.ringerjk.itechgetmeoxygen.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.ringerjk.itechgetmeoxygen.R
import com.ringerjk.itechgetmeoxygen.app.App
import com.ringerjk.itechgetmeoxygen.manager.NetworkManager
import com.ringerjk.itechgetmeoxygen.manager.SharedPreferenceManager
import com.ringerjk.itechgetmeoxygen.model.ResponceTemplate
import com.ringerjk.itechgetmeoxygen.model.RoomState
import com.ringerjk.itechgetmeoxygen.model.RoomStates
import com.ringerjk.itechgetmeoxygen.model.State
import com.ringerjk.itechgetmeoxygen.util.CarbonLevel
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject




class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val TAG = MainActivity::class.java.simpleName

    @Inject
    lateinit var networkManager: NetworkManager
    @Inject
    lateinit var spManager: SharedPreferenceManager

    //    private var chart: BarChart? = null
    private var chart: CombinedChart? = null
    private var currentTemperature: TextView? = null
    private var currentCo2: TextView? = null
    private var toolbar: Toolbar? = null
    private var durationSpinner: AppCompatSpinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.Companion.applicationComponent.plus(this)
        initView()
        initChartData()
    }

    private fun initView() {
        chart = findViewById(R.id.chart_activity_main) as CombinedChart?
        currentTemperature = findViewById(R.id.current_temperature_tv) as TextView?
        currentCo2 = findViewById(R.id.current_co2_tv) as TextView?
        toolbar = findViewById(R.id.toolbar) as Toolbar?
        setSupportActionBar(toolbar)
        durationSpinner = findViewById(R.id.duration_spinner_activity_main) as AppCompatSpinner?
        durationSpinner?.adapter = ArrayAdapter.createFromResource(this,
                R.array.duration_array,
                R.layout.support_simple_spinner_dropdown_item)
        durationSpinner?.onItemSelectedListener = this

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.getItemAtPosition(position) as String?) {
            getString(R.string.hours_24) -> updateRoomStatesPeriod(20)
            getString(R.string.hours_48) -> updateRoomStatesPeriod(30)
            getString(R.string.hours_72) -> updateRoomStatesPeriod(60)
            getString(R.string.week) -> updateRoomStatesPeriod(120)
            getString(R.string.month) -> updateRoomStatesPeriod(240)
        }
    }

    override fun onStart() {
        super.onStart()
        updateCurrentTempAndCo2()
    }

    private fun initChartData() {
        chart?.description?.isEnabled = false
        chart?.setBackgroundColor(Color.WHITE)
        chart?.setDrawGridBackground(false)
        chart?.setDrawBarShadow(false)
        chart?.isHighlightFullBarEnabled = false
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.log_out_menu_item -> {
                logOut()
                return true
            }
            R.id.refresh_menu_item -> {
                updateCurrentTempAndCo2()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateCurrentTempAndCo2() {
        networkManager.getStateOfMyRoom()
                .map { t: ResponceTemplate<RoomState>? ->
                    return@map t?.body
                }.observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: RoomState? ->
                    currentTemperature?.text = t?.state?.temp.toString() + " C"
                    currentCo2?.text = t?.state?.carbon.toString() + " PPM"
                    title = t?.room
                    currentCo2?.setBackgroundColor(CarbonLevel.Companion.levelColor(t?.state?.quality))
                }, { t: Throwable? ->
                    Log.e(TAG, t?.message, t)
                })
    }

    private fun updateRoomStatesPeriod(number: Int?) {
        networkManager.getStateOfMyRoomPeriod(number)
                .map { t: ResponceTemplate<RoomStates>? ->
                    return@map t?.body
                }.map { t: RoomStates? -> return@map t?.states }
                .map { t: List<State>? ->
                    val entries = ArrayList<Entry>()
                    val barEntries = ArrayList<BarEntry>()
                    t?.sortedBy { state -> state.id }
                    t?.forEachIndexed { index, state ->
                        if (state.temp != null && state.carbon != null) {
                            entries.add(Entry(index.toFloat(), state.temp.toFloat()))
                            barEntries.add(BarEntry(index.toFloat(), state.carbon.toFloat()))
                        }
                    }
                    val set = LineDataSet(entries, "Temperature")
                    set.color = Color.rgb(240, 238, 70)
                    set.lineWidth = 2.5f
                    set.setCircleColor(Color.rgb(240, 238, 70))
                    set.circleRadius = 5f
                    set.fillColor = Color.rgb(240, 238, 70)
                    set.mode = LineDataSet.Mode.CUBIC_BEZIER
                    set.setDrawValues(true)
                    set.valueTextSize = 10f
                    set.valueTextColor = Color.rgb(240, 238, 70)

                    val lineData = LineData(set)

                    val set2 = BarDataSet(barEntries, "Carbon")
                    set2.setColors(Color.rgb(61, 165, 255))
                    set2.valueTextColor = Color.rgb(61, 165, 255)
                    set2.valueTextSize = 10f
                    set2.axisDependency = YAxis.AxisDependency.LEFT
                    val barData = BarData(set2)

                    val combineData = CombinedData()
                    combineData.setData(barData)
                    combineData.setData(lineData)
                    return@map combineData
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: CombinedData? ->
                    chart?.data = t
                    chart?.invalidate()
                    chart?.setScaleMinima(5f, 1f)
                }, { t: Throwable? ->
                    Log.e(TAG, t?.message, t)
                })
    }

    private fun logOut() {
        spManager.removeServerToken()
        startActivity(Intent(this, LogInActivity::class.java))
        finish()
    }
}
