package com.github.hadywalied.airpurifier.ui.main

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import com.github.antonpopoff.colorwheel.gradientseekbar.setTransparentToColor
import com.github.hadywalied.airpurifier.R
import com.github.hadywalied.airpurifier.domain.*
import com.google.android.material.radiobutton.MaterialRadioButton
import kotlinx.android.synthetic.main.config_ap_wifi_layout.*
import kotlinx.android.synthetic.main.config_intervals_layout.*
import kotlinx.android.synthetic.main.config_light_layout.*
import kotlinx.android.synthetic.main.config_motion_layout.*
import kotlinx.android.synthetic.main.config_oil_layout.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private val responseObserver: Observer<ResponseMessage> = Observer { t -> responseHandler(t) }
    private val errorObserver: Observer<String> = Observer { t -> showError(t) }
    private val oilObserver: Observer<OilData> = Observer { t -> handleOilData(t) }
    private val ipObserver: Observer<IpConfiguration> = Observer { t -> handleIpConfiguration(t) }

    private fun handleIpConfiguration(t: IpConfiguration?) {
        if (t != null) {
            BASE_URL = t.ip
            if (BASE_URL != "http://192.168.4.1:80")
                viewModel.getOilData()
            else
                Toast.makeText(
                    context,
                    "Please set the Wifi Configurations in order to be able to use the app.",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    private fun handleOilData(t: OilData?) {
        if (t != null) {
            oilPercentageTextView.text = "Oil Precentage: ${t.percentage}%"
            oilPercentage.progress = t.percentage
            oilTimeTextView.text = "Remaining Time (s): ${t.lifetime}"
        }
    }

    private fun responseHandler(t: ResponseMessage?) {
        Toast.makeText(context, t?.message, Toast.LENGTH_SHORT).show()
    }

    private fun showError(t: String) {
        Toast.makeText(context, t, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setObservers()

        setArrowsCallback()

        //region AP
        setApBtn.setOnClickListener {
            val ssidName = apSSIDEdit.text.toString()
            val passwd = apPasswordEdit.text.toString()
            if (ssidName.isNotBlank() and ssidName.isNotEmpty() and passwd.isNotEmpty() and passwd.isNotBlank()) {
                var wifiConfig = WifiConfig(ssidName, passwd)
                viewModel.sendWifiConfig(wifiConfig)
            } else {
                Toast.makeText(
                    context,
                    "Please, Check the SSID and Password Values",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        //endregion

        // region light
        modeRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            val materialRadioButton = radioGroup.findViewById<MaterialRadioButton>(i)
            when (materialRadioButton.text) {
                "off", "fan" -> run {
                    colorWheelLayout.visibility = View.GONE
                }
                "breathing", "static" -> {
                    colorWheelLayout.visibility = View.VISIBLE
                }
            }
        }
        colorWheel.colorChangeListener = {i -> gradient.setTransparentToColor(i)}
        setColorBtn.setOnClickListener {
            val rgb = colorWheel.rgb
            val materialRadioButton =
                modeRadioGroup.findViewById<MaterialRadioButton>(modeRadioGroup.checkedRadioButtonId)
            val lightConfig =
                LightConfig(
                    Color.alpha(gradient.argb),
                    Color.red(rgb),
                    Color.green(rgb),
                    Color.blue(rgb),
                    materialRadioButton.text.toString()
                )
            if (BASE_URL != "http://192.168.4.1:80")
                viewModel.sendLightConfigurations(lightConfig)
            else
                Toast.makeText(
                    context,
                    "Please set the Wifi Configurations in order to be able to use the app.",
                    Toast.LENGTH_SHORT
                ).show()
        }
        // endregion

        //region intervals
        intervalsRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<MaterialRadioButton>(i)
            when (radioButton.text) {
                "low", "mid", "high" -> run {
                    custom_panel.visibility = View.GONE
                }
                "custom" -> {
                    custom_panel.visibility = View.VISIBLE
                }
            }
        }
        setIntervalsBtn.setOnClickListener {
            val radioButton =
                intervalsRadioGroup.findViewById<MaterialRadioButton>(intervalsRadioGroup.checkedRadioButtonId)
            if (radioButton.text.toString() == "custom") {
                if (onTimeEdit.text.toString().isNotEmpty()
                    and offTimeEdit.text.toString().isNotEmpty()
                    and periodEditText.text.toString().isNotEmpty()
                ) {
                    val intervalsConfig =
                        IntervalsConfig(
                            mode = radioButton.text.toString(),
                            Integer.valueOf(onTimeEdit.text.toString()),
                            Integer.valueOf(offTimeEdit.text.toString()),
                            Integer.valueOf(periodEditText.text.toString()),
                            Integer.valueOf(powerSlider.values[1].toString())
                        )
                    if (BASE_URL != "http://192.168.4.1:80")
                        viewModel.sendIntervalsConfigurations(intervalsConfig)
                    else
                        Toast.makeText(
                            context,
                            "Please set the Wifi Configurations in order to be able to use the app.",
                            Toast.LENGTH_SHORT
                        ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Please, Check the Values",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val intervalsConfig =
                    IntervalsConfig(
                        mode = radioButton.text.toString(),
                        0,
                        0,
                        0,
                        0
                    )
                if (BASE_URL != "http://192.168.4.1:80")
                    viewModel.sendIntervalsConfigurations(intervalsConfig)
                else
                    Toast.makeText(
                        context,
                        "Please set the Wifi Configurations in order to be able to use the app.",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
        //endregion

        //region motion
        setMotionBtn.setOnClickListener {
            val motionSensorConfig = MotionSensorConfig(toggleMotionBtn.isChecked)
            if (BASE_URL != "http://192.168.4.1:80")
                viewModel.sendMotionConfigurations(motionSensorConfig)
            else
                Toast.makeText(
                    context,
                    "Please set the Wifi Configurations in order to be able to use the app.",
                    Toast.LENGTH_SHORT
                ).show()
        }
        //endregion

    }

    private fun setObservers() {
        //setting observers
        viewModel.responselivedata.observe(viewLifecycleOwner, responseObserver)
        viewModel.errorLivedata.observe(viewLifecycleOwner, errorObserver)
        viewModel.oillivedata.observe(viewLifecycleOwner, oilObserver)
        viewModel.iplivedata.observe(viewLifecycleOwner, ipObserver)
    }

    private fun setArrowsCallback() {
        ap_show.setOnClickListener {
            //            TransitionManager.beginDelayedTransition(apCardView, AutoTransition())

            if (ap_card_group.visibility == View.VISIBLE) {
                ap_card_group.visibility = View.GONE
                ap_show.setImageResource(android.R.drawable.arrow_down_float)
            } else {
                ap_card_group.visibility = View.VISIBLE
                ap_show.setImageResource(android.R.drawable.arrow_up_float)
            }
        }

        oil_show.setOnClickListener {
            //            TransitionManager.beginDelayedTransition(apCardView, AutoTransition())

            if (oil_card_group.visibility == View.VISIBLE) {
                oil_card_group.visibility = View.GONE
                oil_show.setImageResource(android.R.drawable.arrow_down_float)
            } else {
                oil_card_group.visibility = View.VISIBLE
                oil_show.setImageResource(android.R.drawable.arrow_up_float)
                if (BASE_URL != "http://192.168.4.1:80")
                    viewModel.getOilData()
                else
                    Toast.makeText(
                        context,
                        "Please set the Wifi Configurations in order to be able to use the app.",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

        intervals_show.setOnClickListener {
            //            TransitionManager.beginDelayedTransition(apCardView, AutoTransition())

            if (intervals_card_group.visibility == View.VISIBLE) {
                intervals_card_group.visibility = View.GONE
                intervals_show.setImageResource(android.R.drawable.arrow_down_float)
            } else {
                intervals_card_group.visibility = View.VISIBLE
                intervals_show.setImageResource(android.R.drawable.arrow_up_float)
            }
        }

        light_show.setOnClickListener {
            //            TransitionManager.beginDelayedTransition(apCardView, AutoTransition())

            if (light_card_group.visibility == View.VISIBLE) {
                light_card_group.visibility = View.GONE
                light_show.setImageResource(android.R.drawable.arrow_down_float)
            } else {
                light_card_group.visibility = View.VISIBLE
                light_show.setImageResource(android.R.drawable.arrow_up_float)
            }
        }

        motion_show.setOnClickListener {
            //            TransitionManager.beginDelayedTransition(apCardView, AutoTransition())

            if (motion_card_group.visibility == View.VISIBLE) {
                motion_card_group.visibility = View.GONE
                motion_show.setImageResource(android.R.drawable.arrow_down_float)
            } else {
                motion_card_group.visibility = View.VISIBLE
                motion_show.setImageResource(android.R.drawable.arrow_up_float)
            }
        }
    }

}