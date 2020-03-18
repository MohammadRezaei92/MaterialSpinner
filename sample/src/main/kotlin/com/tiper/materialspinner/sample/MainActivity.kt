package com.tiper.materialspinner.sample

import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tiper.MaterialSpinner
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val listener by lazy {
        object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                Log.v("MaterialSpinner", "onItemSelected parent=${parent.id}, position=$position")
                parent.focusSearch(View.FOCUS_UP)?.requestFocus()
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
                Log.v("MaterialSpinner", "onNothingSelected parent=${parent.id}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        SearchAdapter(resources.getStringArray(R.array.planets_array).asList()).let {
            material_spinner_1.apply {
                adapter = it
                onItemSelectedListener = listener
                onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                    Log.v("MaterialSpinner", "onFocusChange hasFocus=$hasFocus")
                }
                prompt = "Search"
            }
            material_spinner_2.apply {
                adapter = it
                onItemSelectedListener = listener
            }
            material_spinner_3.apply {
                adapter = it
                onItemSelectedListener = listener
                selection = 3
                setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_downward, theme))
            }
            spinner.adapter = it
            appCompatSpinner.adapter = it
        }
        material_spinner_1.let {

        }
        b1_clear.setOnClickListener {
            material_spinner_1.selection = ListView.INVALID_POSITION
        }
        b1_error.setOnClickListener {
            material_spinner_1.onClick()
        }
        b2_clear.setOnClickListener {
            material_spinner_2.selection = ListView.INVALID_POSITION
        }
        b2_error.setOnClickListener {
            material_spinner_2.onClick()
        }
        b3_clear.setOnClickListener {
            material_spinner_3.selection = ListView.INVALID_POSITION
        }
        b3_error.setOnClickListener {
            material_spinner_3.onClick()
        }
    }

    private fun MaterialSpinner.onClick() {
        error = if (error.isNullOrEmpty()) resources.getText(R.string.error) else null
    }

    /**
     * Adapter with filter ability use for filter items in search mode
     */
    private inner class SearchAdapter(private val searchableItems: List<String>) : BaseAdapter(),
        Filterable {

        private var newItemList = searchableItems

        override fun getCount(): Int {
            return newItemList.size
        }

        override fun getItem(position: Int): Any? {
            return newItemList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            return getDropDownView(position, convertView, parent)
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
            val view = LayoutInflater.from(this@MainActivity)
                .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false) as TextView
            view.text = newItemList[position]
            return view
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(filter: CharSequence?): FilterResults {
                    return FilterResults().apply {
                        if (filter.isNullOrEmpty()) {
                            count = searchableItems.size
                            values = searchableItems
                        } else {
                            searchableItems.filter { it.contains(filter.toString(), true) }
                                .let {
                                    count = it.size
                                    values = it
                                }
                        }
                    }
                }

                override fun publishResults(p0: CharSequence?, result: FilterResults?) {
                    newItemList = result?.values as List<String>
                    notifyDataSetChanged()
                }
            }
        }
    }
}
