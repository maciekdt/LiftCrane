package com.example.liftcrane.ui.liftslist

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.liftcrane.R
import com.example.liftcrane.databinding.ActivityLiftsListBinding
import com.example.liftcrane.endpoints.FirestoreService
import com.example.liftcrane.model.Lift
import com.example.liftcrane.ui.LIFT_INTENT_FLAG
import com.example.liftcrane.ui.account.AccountActivity
import com.example.liftcrane.ui.lift.LiftActivity
import com.example.liftcrane.ui.qrscanner.QRScannerActivity
import com.example.liftcrane.ui.reviewslist.ReviewsListActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.collection.LLRBNode
import kotlinx.coroutines.launch

class LiftsListActivity : AppCompatActivity() {

    private val fireStore = FirestoreService()

    private lateinit var binding: ActivityLiftsListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiftsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomBar()
        initListView(this)
        setTopBar()
    }

    override fun onResume() {
        super.onResume()
        setBottomBar()
    }


    private fun initListView(activity: AppCompatActivity){
        binding.root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            val lifts = fireStore.getAllLifts()
            binding.list.layoutManager = LinearLayoutManager(activity)
            val adapter = LiftRecyclerAdapter(lifts)
            adapter.onItemClick = { lift ->
                startReviewActivity(lift)
            }
            binding.list.adapter = adapter
        }
    }

    private fun setBottomBar(){
        binding.bottomNavigation.selectedItemId = R.id.lifts

        binding.bottomNavigation.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.account -> {
                        startActivity(Intent(applicationContext, AccountActivity::class.java))
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.scanner -> {
                        startActivity(Intent(applicationContext, QRScannerActivity::class.java))
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.reviews -> {
                        startActivity(Intent(applicationContext, ReviewsListActivity::class.java))
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.lifts -> return@OnNavigationItemSelectedListener true
                }
                false
            })
    }

    private fun setTopBar(){
        val menu = binding.topAppBar.menu
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView

        val editText: EditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        editText.setTextColor(Color.WHITE)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.isIconified = false
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val adapter = binding.list.adapter as LiftRecyclerAdapter
                adapter.filterByQuery(newText!!)
                return false
            }
        })
    }

    private fun startReviewActivity(lift:Lift){
        val intent = Intent(this, LiftActivity::class.java)
        intent.putExtra(LIFT_INTENT_FLAG, lift)
        startActivity(intent)
    }
}