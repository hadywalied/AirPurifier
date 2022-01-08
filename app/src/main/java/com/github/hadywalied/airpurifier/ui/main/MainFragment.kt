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
import com.github.hadywalied.airpurifier.domain.BASE_URL
import com.github.hadywalied.airpurifier.domain.IpConfiguration
import com.github.hadywalied.airpurifier.domain.OilData
import com.github.hadywalied.airpurifier.domain.ResponseMessage

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
        TODO("Not yet implemented")
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        //setting observers
        viewModel.responselivedata.observe(viewLifecycleOwner, responseObserver)
        viewModel.errorLivedata.observe(viewLifecycleOwner, errorObserver)
        viewModel.oillivedata.observe(viewLifecycleOwner, oilObserver)
        viewModel.iplivedata.observe(viewLifecycleOwner, ipObserver)

    }

}