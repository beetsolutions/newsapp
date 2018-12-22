package com.beettechnologies.newsapp.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beettechnologies.newsapp.R
import com.beettechnologies.newsapp.common.data.model.Status
import com.beettechnologies.newsapp.databinding.FragmentMainBinding
import com.beettechnologies.newsapp.di.Injectable
import com.beettechnologies.newsapp.util.autoCleared
import javax.inject.Inject
import android.support.v7.widget.RecyclerView


private const val QUERY: String = "reddit-r-all"

class MainFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoCleared<FragmentMainBinding>()

    private lateinit var mainViewModel: MainViewModel

    private var adapter : MainAdapter? = null

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

        adapter = MainAdapter()

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        dataBinding.list.layoutManager = layoutManager
        dataBinding.list.setHasFixedSize(true)
        dataBinding.list.adapter = adapter
        dataBinding.list.addItemDecoration(SpacesItemDecoration(18))
        binding = dataBinding
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        mainViewModel.setSource(QUERY)
        mainViewModel.newsResourceLiveData.observe(this, Observer { newsResource ->
            binding.newsResource = newsResource
            if(newsResource?.status == Status.SUCCESS) {
                adapter?.setItems(newsResource.data!!)
            }
        })
    }

    inner class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = space
            outRect.right = space
            outRect.bottom = space

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space
            }
        }
    }
}