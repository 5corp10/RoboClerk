package com.example.roboclerk

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu

//import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowId
import android.widget.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {

    //heart rate
    var hRates = arrayOf("6+ Adult 60-100", "6+ Adult >100", "6+ Adult <60",
        "0+ Child 110-150", "0+ Child >150", "0+ Child <110",
        "2+ Child 85-125", "2+ Child >125", "2+ Child <85",
        "4+ Child 75-115", "4+ Child >115", "4+ Child <75")

    var qrsDurations = arrayOf("<2.5", "2.5-3", ">3")

    //irregularly irregular is atrial fibrilation
    var hRhythms = arrayOf("regular", "irregularly regular", "irregularly irregular")


    //3-5 normal, >5 is first degree heart block, <3 is pre-excitation
    var prSegments = arrayOf("3-5", ">5", "<3")

    var spinner1:Spinner? = null
    var spinner2:Spinner? = null
    var spinner3:Spinner? = null
    var spinner4:Spinner? = null
    var spinner5:Spinner? = null
    var spinner6:Spinner? = null
    var spinner7:Spinner? = null
    var spinner8:Spinner? = null
    var spinner9:Spinner? = null
    var diagnosis:TextView? = null

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        spinner1 = this.spinner_hrate
        spinner1!!.setOnItemSelectedListener(this)
        val aa1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, hRates)
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1!!.setAdapter(aa1)

        spinner2 = this.spinner_hrhythm
        spinner2!!.setOnItemSelectedListener(this)
        val aa2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, hRhythms)
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2!!.setAdapter(aa2)

        spinner3 = this.spinner_qrsduration
        spinner3!!.setOnItemSelectedListener(this)
        val aa3 = ArrayAdapter(this, android.R.layout.simple_spinner_item, qrsDurations)
        aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner3!!.setAdapter(aa3)

        spinner4 = this.spinner_printerval
        spinner4!!.setOnItemSelectedListener(this)
        val aa4 = ArrayAdapter(this, android.R.layout.simple_spinner_item, prSegments)
        aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner4!!.setAdapter(aa4)

        diagnosis = this.dx

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        var dxs = ""

        //heart rate
        when (hRates[position]) {
            "6+ Adult >100" -> dxs += "tachycardia\n"
            "0+ Child >150" -> dxs += "tachycardia\n"
            "2+ Child >125" -> dxs += "tachycardia\n"
            "4+ Child >115" -> dxs += "tachycardia\n"
            "6+ Adult <60"  -> dxs += "bradycardia\n"
            "0+ Child <110" -> dxs += "bradycardia\n"
            "2+ Child <85"  -> dxs += "bradycardia\n"
            "4+ Child <75"  -> dxs += "bradycardia\n"
            else -> {}

        }
        diagnosis!!.text = "Dx:\n" + dxs
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }
}
