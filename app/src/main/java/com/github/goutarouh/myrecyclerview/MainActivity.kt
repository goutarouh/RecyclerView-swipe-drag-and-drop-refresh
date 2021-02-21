package com.github.goutarouh.myrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var displayList = mutableListOf<String>()
    private var countryList = mutableListOf<String>().apply {
        add("北海道")
        add("青森")
        add("岩手")
        add("秋田")
        add("山形")
        add("宮城")
        add("福島")
        add("新潟")
        add("東京")
        add("群馬")
        add("栃木")
        add("茨城")
        add("千葉")
        add("埼玉")
        add("神奈川")
        add("長野")
        add("静岡")
        add("岐阜")
        add("石川")
        add("福井")
        add("山梨")
        add("愛知")
        add("京都")
        add("滋賀")
        add("和歌山")
        add("奈良")
        add("三重")
        add("富山")
        add("大阪")
        add("兵庫")
        add("岡山")
        add("鳥取")
        add("島根")
        add("広島")
        add("山口")
        add("愛媛")
        add("高知")
        add("徳島")
        add("香川")
        add("福岡")
        add("大分")
        add("長崎")
        add("佐賀")
        add("宮崎")
        add("熊本")
        add("鹿児島")
        add("沖縄")
    }

    private lateinit var deletedCountry: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayList.addAll(countryList)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerAdapter = RecyclerAdapter(displayList)
        recyclerView.adapter = recyclerAdapter

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            displayList.clear()
            displayList.addAll(countryList)
            recyclerView.adapter!!.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val startPosition = viewHolder.adapterPosition
            val endPosition = target.adapterPosition

            Collections.swap(displayList, startPosition, endPosition)
            recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var position = viewHolder.adapterPosition

            when (direction) {
                ItemTouchHelper.LEFT -> {
                    deletedCountry = displayList.get(position)
                    displayList.removeAt(position)
                    recyclerAdapter.notifyItemRemoved(position)

                    Snackbar.make(recyclerView, "$deletedCountry is deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            displayList.add(position, deletedCountry)
                            recyclerAdapter.notifyItemInserted(position)
                        }.show()
                }
                ItemTouchHelper.RIGHT -> {
                    val editText = EditText(this@MainActivity)
                    editText.setText(displayList[position])

                    AlertDialog.Builder(this@MainActivity).apply {
                        setTitle("Update an Item")
                        setCancelable(true)
                        setView(editText)
                        setNegativeButton("cancel") { _, _ ->
                            displayList.clear()
                            displayList.addAll(countryList)
                            recyclerView.adapter!!.notifyDataSetChanged()
                        }
                        setPositiveButton("update") { _, _ ->
                            displayList.set(position, editText.text.toString())
                            recyclerView.adapter!!.notifyDataSetChanged()
                        }
                    }.show()
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val item = menu!!.findItem(R.id.action_search)
        if (item != null) {
            val searchView = item.actionView as SearchView
            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.isEmpty()) {
                        displayList.clear()
                        displayList.addAll(countryList)
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query!!.isNotEmpty()) {
                        displayList.clear()
                        for (country in countryList) {
                            if (country.contains(query)) {
                                displayList.add(country)
                            }
                        }
                        recyclerView.adapter!!.notifyDataSetChanged()
                    } else {
                        displayList.clear()
                        displayList.addAll(countryList)
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }
}