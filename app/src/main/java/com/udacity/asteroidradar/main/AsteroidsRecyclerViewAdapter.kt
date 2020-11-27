package com.udacity.asteroidradar.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidListChildBinding
import com.udacity.asteroidradar.model.Asteroid

class AsteroidsRecyclerViewAdapter(
    context: Context,
    asteroids: List<Asteroid>,
    asteroidClickListener: AsteroidClickListener
) : RecyclerView.Adapter<AsteroidsRecyclerViewAdapter.AsteroidsViewHolder>() {
    private var context: Context
    private var asteroids = ArrayList<Asteroid>()
    var asteroidClickListener: AsteroidClickListener

    init {
        this.context = context
        this.asteroids = asteroids as ArrayList<Asteroid>
        this.asteroidClickListener = asteroidClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsViewHolder {
        val asteroidListChildBinding = AsteroidListChildBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AsteroidsViewHolder(asteroidListChildBinding)
    }


    override fun getItemCount() = asteroids.size


    override fun onBindViewHolder(holder: AsteroidsViewHolder, position: Int) {
        holder.onBind(position)
    }

    inner class AsteroidsViewHolder(asteroidListChildBinding: AsteroidListChildBinding) :
        RecyclerView.ViewHolder(asteroidListChildBinding.root) {
        private var asteroidListChildBinding: AsteroidListChildBinding

        init {
            this.asteroidListChildBinding = asteroidListChildBinding
        }

        fun onBind(position: Int) {
            val asteroid = asteroids.get(position)
            asteroidListChildBinding.asteroid = asteroid
            this.asteroidListChildBinding.root.setOnClickListener {
                asteroidClickListener.onAsteroidClick(asteroids.get(position))
            }
        }

    }

    interface AsteroidClickListener {
        fun onAsteroidClick(asteroid: Asteroid)
    }
}