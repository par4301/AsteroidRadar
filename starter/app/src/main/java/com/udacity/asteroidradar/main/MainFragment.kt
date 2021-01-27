package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.AsteroidRadarApplication
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

//        val application = requireNotNull(this.activity).application
//        val dataSource = AsteroidDatabase.getInstance(application).asteroidDatabaseDao

        val viewModelFactory = MainViewModelFactory(AsteroidRadarApplication().repository)

        val asteroidViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(MainViewModel::class.java)

        binding.mainViewModel = asteroidViewModel

        val adapter = AsteroidAdapter(AsteroidAdapter.AsteroidListener {
            asteroid ->  asteroidViewModel.onAsteroidClicked(asteroid)
        })

        binding.asteroidRecycler.adapter = adapter

        asteroidViewModel.asteroids.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        asteroidViewModel.navigateToAsteroidDetails.observe(viewLifecycleOwner, Observer { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                asteroidViewModel.onAsteroidClickedNavigated()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.show_all_menu -> viewModel.setAsteroidFilter(MainViewModel.AsteroidFilter.WEEKLY)
            R.id.show_rent_menu -> viewModel.setAsteroidFilter(MainViewModel.AsteroidFilter.TODAY)
            else -> viewModel.setAsteroidFilter(MainViewModel.AsteroidFilter.ALL)
        }
        return true
    }
}
