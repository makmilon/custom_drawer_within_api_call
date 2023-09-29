  package com.example.customdrawer


  import android.os.Bundle
  import android.widget.Toast
  import androidx.appcompat.app.ActionBarDrawerToggle
  import androidx.appcompat.app.AppCompatActivity
  import androidx.appcompat.widget.Toolbar
  import androidx.drawerlayout.widget.DrawerLayout
  import androidx.recyclerview.widget.LinearLayoutManager
  import androidx.recyclerview.widget.RecyclerView
  import com.example.customdrawer.adapter.MenuAdapter
  import com.example.customdrawer.dataclass.MenuItemItem
  import com.example.customdrawer.retrofit.ApiService
  import com.example.customdrawer.retrofit.RetrofitClient
  import retrofit2.Call
  import retrofit2.Callback
  import retrofit2.Response

  class MainActivity : AppCompatActivity() {

      private lateinit var drawerLayout: DrawerLayout
      private lateinit var toolbar: Toolbar
      private lateinit var recyclerView: RecyclerView

      private lateinit var apiService: ApiService
      private val retrofit = RetrofitClient.getClient()

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_main)

          apiService = retrofit.create(ApiService::class.java)

          // Initialize views
          drawerLayout = findViewById(R.id.drawerLayout)
          toolbar = findViewById(R.id.toolbar)
          recyclerView = findViewById(R.id.drawerRecyclerView) // Replace with your RecyclerView ID

          // Set up the toolbar
          setSupportActionBar(toolbar)

          // Set a custom hamburger icon as the navigation icon
          toolbar.setNavigationIcon(R.drawable.ham)

          // Set up the drawer toggle
          val toggle = ActionBarDrawerToggle(
              this,
              drawerLayout,
              toolbar,
              R.string.navigation_drawer_open,
              R.string.navigation_drawer_close
          )

          drawerLayout.addDrawerListener(toggle)
          toggle.syncState()

          // Create an empty list to hold the menu items
          val menuItems = mutableListOf<MenuItemItem>()

          // Create and set the adapter for the RecyclerView
          val adapter = MenuAdapter(menuItems)
          recyclerView.layoutManager = LinearLayoutManager(this)
          recyclerView.adapter = adapter

          // Make the API call to fetch data and update the list
          apiService.getUsers().enqueue(object : Callback<List<MenuItemItem>> {
              override fun onResponse(call: Call<List<MenuItemItem>>, response: Response<List<MenuItemItem>>) {
                  if (response.isSuccessful) {
                      val items = response.body()
                      if (items != null) {
                          menuItems.addAll(items)
                          adapter.notifyDataSetChanged()
                      }
                  }
              }

              override fun onFailure(call: Call<List<MenuItemItem>>, t: Throwable) {
                  // Handle API call failure
                  Toast.makeText(this@MainActivity, "API call failed", Toast.LENGTH_SHORT).show()
              }
          })
      }
  }
