package com.github.hadywalied.airpurifier.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.github.hadywalied.airpurifier.R
import com.github.hadywalied.airpurifier.domain.*
import kotlinx.android.synthetic.main.config_ap_wifi_layout.*
import kotlinx.android.synthetic.main.config_oil_layout.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private val responseObserver: Observer<ResponseMessage> = Observer { t -> showError(t) }
    private val errorObserver: Observer<String> = Observer { t -> showError(t) }
    private val oilObserver: Observer<OilData> = Observer { t -> handleOilData(t) }
    private val ipObserver: Observer<IpConfiguration> = Observer { t -> BASE_URL = t.ip }

    private fun handleOilData(t: OilData?) {
        if (t != null) {
            oilPercentageTextView.text = "Oil Precentage: ${t.percentage}%"
            oilPercentage.progress = t.percentage
            oilTimeTextView.text = "Remaining Time (s): ${t.lifetime}"
        }
    }

    private fun showError(t: ResponseMessage?) {
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

        //setting observers
        viewModel.responselivedata.observe(viewLifecycleOwner, responseObserver)
        viewModel.errorLivedata.observe(viewLifecycleOwner, errorObserver)
        viewModel.oillivedata.observe(viewLifecycleOwner, oilObserver)
        viewModel.iplivedata.observe(viewLifecycleOwner, ipObserver)

        setApBtn.setOnClickListener {
            val ssidName = apSSIDEdit.text.toString()
            val passwd = apPasswordEdit.text.toString()
            if (ssidName.isNotBlank() and ssidName.isNotEmpty() and passwd.isNotEmpty() and passwd.isNotBlank()) {
                var wifiConfig = WifiConfig(ssidName, passwd)
                viewModel.sendWifiConfig(wifiConfig)
            } else {
                Toast.makeText(
                    context,
                    "Please Check the SSID and Password Values",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

}