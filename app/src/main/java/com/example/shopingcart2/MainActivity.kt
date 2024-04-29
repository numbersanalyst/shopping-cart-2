package com.example.shopingcart2


import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener

data class Product(val name: String, val image: Int, var amount: Int)

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private var products = mutableListOf<Product>()
    private lateinit var adapter: ShoppingCartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.simple_listview)

        val fruitsData = hashMapOf(
            "Cherry" to R.drawable.cherry,
            "Garnet" to R.drawable.garnet,
            "Grapes" to R.drawable.grapes,
            "Pear" to R.drawable.pear,
            "Raspberry" to R.drawable.raspberry,
            "Banana" to R.drawable.banana,
            "Kivy" to R.drawable.kivy,
            "Lemon" to R.drawable.lemon,
            "Orange" to R.drawable.orange
        )

        products = fruitsData.toSortedMap().map { Product(it.key, it.value, 0) }.toMutableList()

        adapter = ShoppingCartAdapter(this, products)
        listView.adapter = adapter

        val clearButton = findViewById<Button>(R.id.clear)
        clearButton.setOnClickListener {
            adapter.clearAmount()
            Toast.makeText(this, "The list is clear", Toast.LENGTH_SHORT).show()
        }
    }

    inner class ShoppingCartAdapter(private val context: MainActivity, private val productList: List<Product>) : BaseAdapter() {

        override fun getCount(): Int {
            return productList.size
        }

        override fun getItem(position: Int): Product {
            return productList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        fun clearAmount() {
            productList.forEach{it.amount = 0}
            notifyDataSetChanged()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val product = getItem(position)
            val view: View = convertView ?: layoutInflater.inflate(R.layout.list_view_items, parent, false)

            val productName = view.findViewById<TextView>(R.id.textView)
            val productImg = view.findViewById<ImageView>(R.id.imageView)
            val productAmount = view.findViewById<EditText>(R.id.editTextNumber)
            val addButton = view.findViewById<Button>(R.id.add)
            val removeButton = view.findViewById<Button>(R.id.delete)

            productName.text = product.name
            productImg.setImageResource(product.image)
            productAmount.setText(product.amount.toString())

            addButton.setOnClickListener {
                val currAmount = productAmount.text.toString().toIntOrNull() ?: 0
                if (currAmount == 99) {
                    productAmount.setText("0")
                } else {
                    productAmount.setText((currAmount + 1).toString())
                }
            }

            removeButton.setOnClickListener {
                val currAmount = productAmount.text.toString().toIntOrNull() ?: 0
                if (currAmount > 0) {
                    productAmount.setText((currAmount - 1).toString())
                }
            }

            productAmount.addTextChangedListener {
                product.amount = it.toString().toIntOrNull() ?: 0
            }

            return view
        }
    }
}