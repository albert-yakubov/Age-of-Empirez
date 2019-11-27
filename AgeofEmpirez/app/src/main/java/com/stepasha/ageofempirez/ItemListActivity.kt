package com.stepasha.ageofempirez

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.stepasha.ageofempirez.models.API
import com.stepasha.ageofempirez.models.Civilization
import com.stepasha.ageofempirez.models.Structure
import com.stepasha.ageofempirez.models.Technology
import com.stepasha.ageofempirez.retro.AgeOfEmpiresAPI
import com.stepasha.ageofempirez.viewmodel.ItemListViewModel
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import kotlinx.android.synthetic.main.item_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    private var viewAdapter: SimpleItemRecyclerViewAdapter? = null

    lateinit var ageOfEmpiresAPI: AgeOfEmpiresAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }
        ageOfEmpiresAPI = AgeOfEmpiresAPI.Factory.create()

        setupRecyclerView(item_list as RecyclerView)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this@ItemListActivity, ItemListViewModel.ageOfEmpiresApiObjects, twoPane)
        if (isNetworkConnected()) {
            getData()
        } else {
            Toast.makeText(this@ItemListActivity, "No Network", Toast.LENGTH_LONG).show()
        }

    }
    private fun getData() {

        // Add civilizations
        val civilizationIds = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        civilizationIds.forEach {
            getCivilizations(it)
        }

        // Add structures
        val structuresIds = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        structuresIds.forEach {
            getStructures(it)
        }

        // Add technologies
        val technologiesIds = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        technologiesIds.forEach {
            getTechnologies(it)
        }
        ItemListViewModel.ageOfEmpiresApiObjects.shuffle()
    }
    fun getCivilizations(id: Int) {

        ageOfEmpiresAPI.getCivilization(id).enqueue(object : Callback<Civilization> {
            override fun onFailure(call: Call<Civilization>, t: Throwable) {
            }

            override fun onResponse(call: Call<Civilization>, response: Response<Civilization>) {

                if (response.isSuccessful) {
                    val civilization = response.body()
                    civilization?.let {
                        it.id = id
                        it.category = "Civilization"
                        val listSize = ItemListViewModel.ageOfEmpiresApiObjects.size
                        ItemListViewModel.ageOfEmpiresApiObjects.add(civilization)
                        ItemListViewModel.ageOfEmpiresApiObjectsHash.put(listSize, civilization.id)
                        viewAdapter?.notifyItemInserted(listSize)
                    }
                }
            }
        })
    }
    fun getStructures(id: Int) {

        ageOfEmpiresAPI.getStructure(id).enqueue(object : Callback<Structure> {
            override fun onFailure(call: Call<Structure>, t: Throwable) {
            }

            override fun onResponse(call: Call<Structure>, response: Response<Structure>) {
                if (response.isSuccessful) {
                    val structure = response.body()
                    structure?.let {
                        it.id = id
                        it.category = "Structure"
                        ItemListViewModel.ageOfEmpiresApiObjects.add(structure)
                        viewAdapter?.notifyItemInserted(ItemListViewModel.ageOfEmpiresApiObjects.size - 1)
                    }
                }
            }
        })
    }
    fun getTechnologies(id: Int) {

        ageOfEmpiresAPI.getTechnologies(id).enqueue(object : Callback<Technology> {
            override fun onFailure(call: Call<Technology>, t: Throwable) {
            }

            override fun onResponse(call: Call<Technology>, response: Response<Technology>) {
                if (response.isSuccessful) {
                    val technologies = response.body()
                    technologies?.let {
                        it.id = id
                        it.category = "Technology"
                        ItemListViewModel.ageOfEmpiresApiObjects.add(technologies)
                        viewAdapter?.notifyItemInserted(ItemListViewModel.ageOfEmpiresApiObjects.size - 1)
                    }
                }
            }
        })
    }


    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: ItemListActivity,
        private val values: MutableList<API>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {
        lateinit var context: Context
        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as API
                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                                putSerializable(ItemDetailFragment.ARG_ITEM_ID, item)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            context = parent.context
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.nameText.text = item.name
            holder.isFav.text = item.description()

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        fun textFavorite(boolean: Boolean): Boolean {
            return boolean
        }
        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameText: TextView = view.nameText
            val isFav: TextView = view.isFavorite
        }
    }
    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

}
