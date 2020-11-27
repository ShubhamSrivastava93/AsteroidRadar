package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.main.AsteroidsRecyclerViewAdapter.AsteroidClickListener
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.utils.Constants.DEFAULT_END_DATE_DAYS
import com.udacity.asteroidradar.utils.DateUtils.Companion.getCurrentDate
import com.udacity.asteroidradar.utils.DateUtils.Companion.getEndDate

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var asteroidsRecyclerViewAdapter: AsteroidsRecyclerViewAdapter
    private val asteroids = ArrayList<Asteroid>()

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        asteroidsRecyclerViewAdapter = AsteroidsRecyclerViewAdapter(requireActivity(), asteroids,
            object : AsteroidClickListener {
                override fun onAsteroidClick(asteroid: Asteroid) {
                    findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                }
            })

        binding.asteroidRecycler.adapter = asteroidsRecyclerViewAdapter
        binding.statusLoadingWheel.visibility = View.VISIBLE

        getDatabase(requireActivity()).asteroidDao.getAsteroidsFromDate(getCurrentDate())
            .observe(viewLifecycleOwner, Observer { asteroid: List<Asteroid> ->
                refreshAdapter(asteroid)
            })

        binding.statusLoadingWheel.visibility = View.VISIBLE
        viewModel.pictureOfDayLiveData.observe(
            requireActivity(), Observer { pictureOfDay: PictureOfDay ->
                binding.statusLoadingWheel.visibility = View.GONE
                binding.activityMainImageOfTheDay.contentDescription = String.format(
                    getString(R.string.nasa_picture_of_day_content_description_format),
                    pictureOfDay.title
                )
                if ("image".equals(pictureOfDay.mediaType)) {
                    Glide
                        .with(requireContext())

                        /* NASA image url is returning empty. It even doesn't work, if opened directly in the browser.
                         * However other images are fetched and displayed successfully
                         */
                        .load(pictureOfDay.url)
                        .into(binding.activityMainImageOfTheDay)
                }
            })

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun refreshAdapter(asteroids: List<Asteroid>) {
        binding.statusLoadingWheel.visibility = View.GONE
        this.asteroids.clear()
        this.asteroids.addAll(asteroids)
        asteroidsRecyclerViewAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_menu ->
                getDatabase(requireActivity()).asteroidDao.getAsteroidsBetweenDates(
                    getCurrentDate(),
                    getEndDate(DEFAULT_END_DATE_DAYS)
                ).observe(viewLifecycleOwner, Observer { asteroid: List<Asteroid> ->
                    refreshAdapter(asteroid)
                })
            R.id.show_today_menu ->
                getDatabase(requireActivity()).asteroidDao.getAsteroidsByDate(getCurrentDate())
                    .observe(viewLifecycleOwner, Observer { asteroid: List<Asteroid> ->
                        refreshAdapter(asteroid)
                    })
            R.id.show_saved_menu ->
                getDatabase(requireActivity()).asteroidDao.getAllAsteroids()
                    .observe(viewLifecycleOwner, Observer { asteroid: List<Asteroid> ->
                        refreshAdapter(asteroid)
                    })
        }
        return true
    }
}
