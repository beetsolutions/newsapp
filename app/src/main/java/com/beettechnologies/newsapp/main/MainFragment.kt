package com.beettechnologies.newsapp.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beettechnologies.newsapp.R
import com.beettechnologies.newsapp.databinding.FragmentMainBinding
import com.beettechnologies.newsapp.di.Injectable
import com.beettechnologies.newsapp.util.autoCleared

import javax.inject.Inject

class MainFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoCleared<FragmentMainBinding>()

    lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentMainBinding>(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )

        binding = dataBinding
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        mainViewModel.setSource("reddit-r-all")
        mainViewModel.newsResourceLiveData.observe(this, Observer { newsResource ->
            binding.newsResource = newsResource
        })
    }
}