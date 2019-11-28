package com.example.roboclerk

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Double.parseDouble
import kotlin.math.PI
import kotlin.math.sin


class MainActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {

    //heart rate
    var hRates = arrayOf(
        "6+ Adult 60-100", "6+ Adult >100", "6+ Adult <60",
        "0+ Child 110-150", "0+ Child >150", "0+ Child <110",
        "2+ Child 85-125", "2+ Child >125", "2+ Child <85",
        "4+ Child 75-115", "4+ Child >115", "4+ Child <75"
    )

    var qrsDurations = arrayOf("<2.5", "2.5-3", ">3")

    //irregularly irregular is atrial fibrilation
    var hRhythms = arrayOf("regular", "irregularly regular", "irregularly irregular")


    //3-5 normal, >5 is first degree heart block, <3 is pre-excitation
    var prSegments = arrayOf("3-5", ">5", "<3")

    var spinner1: Spinner? = null
    var spinner2: Spinner? = null
    var spinner3: Spinner? = null
    var spinner4: Spinner? = null
    var spinner5: Spinner? = null
    var spinner6: Spinner? = null
    var spinner7: Spinner? = null
    var spinner8: Spinner? = null
    var spinner9: Spinner? = null

    var input1: EditText? = null
    var input2: EditText? = null

    var diagnosis: TextView? = null

    private var axisFound: Boolean = false


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

        input1 = this.i_net_defl
        input1!!.setOnFocusChangeListener(editTextFocus)

        input2 = this.avf_net_defl
        input2!!.setOnFocusChangeListener(editTextFocus)

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
        //diagnosis!!.text = "Dx:\n"

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

    override fun onItemSelected(parent: AdapterView<*>, v: View, position: Int, id: Long) {
        var dxs = ""
        //diagnosis!!.text = v.id.toString() + "\n" + R.id.spinner_hrate.toString() + "...\n"
        when (parent.id) {
            R.id.spinner_hrate -> {
                when (hRates[position]) {
                    "6+ Adult >100" -> dxs += "rate: tachycardia\n"
                    "0+ Child >150" -> dxs += "rate: tachycardia\n"
                    "2+ Child >125" -> dxs += "rate: tachycardia\n"
                    "4+ Child >115" -> dxs += "rate: tachycardia\n"
                    "6+ Adult <60" -> dxs += "rate: bradycardia\n"
                    "0+ Child <110" -> dxs += "rate: bradycardia\n"
                    "2+ Child <85" -> dxs += "rate: bradycardia\n"
                    "4+ Child <75" -> dxs += "rate: bradycardia\n"
                    else -> {
                    }
                }
                diagnosis!!.text = diagnosis!!.text.toString() + dxs
            }
            else -> {
            }

        }


    }

    override fun onNothingSelected(arg0: AdapterView<*>) {
    }

    public val editTextFocus =
        OnFocusChangeListener { v, gainFocus ->

            //axis
            if (!axisFound) {
                if (!gainFocus) {
                    var iNetDefl = input1!!.text.toString()
                    var avfNetDefl = input2!!.text.toString()
                    if (isNumeric(iNetDefl) && isNumeric(avfNetDefl)) {
                        var dxs = ""
                        var i = iNetDefl.toDouble()
                        var avf = avfNetDefl.toDouble()

                        if (i < 0) {
                            if (avf < 0)
                                dxs += "%.0f".format(-90 - (kotlin.math.sin(avf / i) * 180 / PI)).toString() + "\n"
                            else
                                dxs += "%.0f".format(90 - (kotlin.math.sin(avf / i) * 180 / PI)).toString() + "\n"
                        } else
                            dxs += "%.0f".format((sin(avf / i) * 180 / PI)).toString() + "\n"

                        diagnosis!!.text = diagnosis!!.text.toString() + "axis: " + dxs
                        axisFound = true
                        hideSoftKeyboard(v)
                    }
                }
            }
        }

    fun isNumeric(string: String): Boolean {
        var numeric = true

        try {
            parseDouble(string)
        } catch (e: NumberFormatException) {
            numeric = false
        }
        return numeric
    }

    public fun hideSoftKeyboard(v: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v!!.windowToken, 0)
        //imm.hideSoftInputFromWindow(v!!.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
}
