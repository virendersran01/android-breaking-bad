package com.example.breakingbad.presentation.home.ui.activity

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.breakingbad.R
import com.example.breakingbad.databinding.ActivityHomeBinding
import com.example.breakingbad.framework.base.BaseActivity
import com.example.breakingbad.presentation.actordetail.ui.ActorDetailActivity.Companion.startCatDetailActivity
import com.example.breakingbad.domain.Actor
import com.example.breakingbad.presentation.home.ui.adapter.ActorAdapter
import com.example.breakingbad.presentation.home.ui.viewholder.ActorViewHolder
import com.example.breakingbad.presentation.home.viewmodel.HomeViewModel

class HomeActivity : BaseActivity(), ActorViewHolder.ActorClickListener,
    android.widget.SearchView.OnQueryTextListener {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: ActorAdapter
    private var uiModels: MutableList<Actor> = mutableListOf()

    override fun initialise() {
        super.initialise()
        setupSwipeToRefresh()
        viewModel.bind()
    }

    override fun initialiseViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)[HomeViewModel::class.java]
    }

    override fun initialiseViewBinding() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun initialiseView() {
        super.initialiseView()
        initialiseSearchView()
        initialiseRecycler()
    }

    override fun getRootView(): View {
        return binding.root
    }

    override fun onActorClick(item: Actor, position: Int) {
        binding.searchView.clearFocus()
        startCatDetailActivity(item.char_id)
    }

    override fun getToolbar(): Toolbar {
        return binding.homeToolbar
    }

    override fun observeLiveData() {
        viewModel.models.observe(this, {
            populate(it)
        })
    }

    private fun populate(models: List<Actor>) {
        binding.homeSwipeRefresh.isRefreshing = false
        binding.loadingProgressBar.visibility = View.GONE
        binding.homeList.visibility = View.VISIBLE
        uiModels.clear()
        uiModels.addAll(models)
        adapter.notifyDataSetChanged()
    }

    private fun initialiseRecycler() {
        adapter = ActorAdapter(uiModels, this)
        adapter.setHasStableIds(true)
        binding.homeList.layoutManager = LinearLayoutManager(this)
        binding.homeList.adapter = adapter
    }

    private fun setupSwipeToRefresh() {
        binding.homeSwipeRefresh.setOnRefreshListener {
            binding.homeSwipeRefresh.isRefreshing = true
            viewModel.refresh { canRefresh ->
                if (! canRefresh) {
                    binding.homeSwipeRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun initialiseSearchView() {
        binding.searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.searchQuery = query
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}