package com.example.dltb

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var selectBoundlistView: ListView
    private lateinit var selectRoutelistView: ListView
    private lateinit var dimBackground: View
    private lateinit var selectBoundOptionsbutton: Button
    private lateinit var selectRoutesOptionsbutton: Button
    private lateinit var northBoundOptions: Array<String>
    private lateinit var southBoundOptions: Array<String>
    private var selectedBound: String = "Select Bound"
    private var selectedRoute: String = "Select Route"

    private var isSelectedBound = false
    private var isSelectedRoute = false
    private var isDispatcherExists = false
    private var isDriverExists = false
    private var isConductorExists = false
    private var regularTripButtonClicked = false
    private var specialTripButtonClicked = false


    private lateinit var customButtons: CustomButtons

    private var nfcAdapter: NfcAdapter? = null
    private var dataInitialized = false
    private lateinit var dbHelper: DBHelper
    private lateinit var driverCardUID: String
    private lateinit var dispatcherCardUID: String
    private lateinit var conductorCardUID: String
    private var tagUID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        dbHelper = DBHelper(applicationContext)

        if (!dataInitialized) {
            val dbHelper = DBHelper(applicationContext)
            val dataInitializer = DataInitializer(dbHelper)
            dataInitializer.initializeData()
            dataInitialized = true
        }

        selectBoundOptionsbutton = findViewById(R.id.select_bound_btn)
        selectRoutesOptionsbutton = findViewById(R.id.select_route_btn)
        selectBoundlistView = findViewById(R.id.selectBoundListView)
        selectRoutelistView = findViewById(R.id.selectRouteListView)
        dimBackground = findViewById<View>(R.id.semiTransparentOverlay)

        customButtons = CustomButtons()

        northBoundOptions = arrayOf(
            "Sample 1 - Sample 2",
            "Sample 3 - Sample 4",
            "Sample 5 - Sample 6",
            "Sample 7 - Sample 7",
            "Sample 8 - Sample 9"
        )
        setOptionsForRouteButton("North Bound")

        southBoundOptions = arrayOf(
            "PITX - TAYABAS",
            "PITX - LUCBAN",
            "PITX - LUCENA",
            "LRT - DOLORES",
            "LRT - DALAHICAN",
            "LRT - TAYABAS",
            "LRT - LUCBAN",
            "LRT - LUCENA",
            "LRT - SM LUCENA",
            "CUBAO - DOLORES",
            "CUBAO - DALAHICAN"
        )

        selectBoundOptionsbutton.setOnClickListener {
            selectBound()
        }
        selectRoutesOptionsbutton.setOnClickListener {
            selectRoute()
        }
        boundAndRoute()

        // Date and Time
        val dateTextView = findViewById<TextView>(R.id.dateTextView)
        val timeTextView = findViewById<TextView>(R.id.timeTextView)
        val dateAndTime = DateAndTime(dateTextView, timeTextView)
        dateAndTime.start()

        // Initialize
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        tagUID = ""
        this.driverCardUID = ""
        this.dispatcherCardUID = ""
        this.conductorCardUID = ""

        isDispatcherExists = dbHelper.dispatcherExists(dispatcherCardUID)
        isDriverExists = dbHelper.driverExists(driverCardUID)
        isConductorExists = dbHelper.conductorExists(conductorCardUID)

        Log.d("Requirements", "$isDispatcherExists")
        Log.d("Requirements", "$isDriverExists")
        Log.d("Requirements", "$isConductorExists")
        Log.d("Requirements", "$isSelectedBound")
        Log.d("Requirements", "$isSelectedRoute")
        Log.d("Requirements", "$regularTripButtonClicked")
        Log.d("Requirements", "$specialTripButtonClicked")
        updateDispatchButtonState()

        // Toast.makeText(this, "$dispatcherCardUID, $tagUID", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        enableNFC()
    }

    override fun onPause() {
        disableNFC()
        super.onPause()
    }

    private fun setOptionsForRouteButton(direction: String) {
        // Set options for the second button based on the selected bound
        val options = if (direction == "North Bound") northBoundOptions else southBoundOptions
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, options)
        selectRoutelistView.adapter = adapter
    }

    private fun selectBound() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Bound")
        builder.setItems(arrayOf("North Bound", "South Bound")) { _, which ->
            val newSelectedBound = if (which == 0) "North Bound" else "South Bound"

            if (newSelectedBound != selectedBound) {
                isSelectedBound = true
                isSelectedRoute = false  // Reset
            }
            selectedBound = newSelectedBound
            selectBoundOptionsbutton.text = selectedBound

            // Clear the text for the route button when changing bound
            selectedRoute = "Select Route"
            selectRoutesOptionsbutton.text = selectedRoute
            setOptionsForRouteButton(selectedBound) // set options for bound
        }
        builder.show()
        Log.d("Select Bound req", "$isSelectedBound")
        Log.d("Select Bound req", "$isSelectedRoute")
    }

    private fun selectRoute() {

        if (selectRoutelistView.visibility == View.VISIBLE) {
            selectRoutelistView.visibility = View.GONE
            dimBackground.visibility = View.GONE
            Log.d("Visibility", "ListView and Overlay are now gone.")

            if (selectedRoute != "Select Route") {
                isSelectedRoute = true
            }
        } else {
            selectRoutelistView.visibility = View.VISIBLE
            dimBackground.visibility = View.VISIBLE
            Log.d("Visibility", "ListView and Overlay visible.")
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == NfcAdapter.ACTION_TAG_DISCOVERED) {

            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            val uidBytes = tag?.id

            if (uidBytes != null) {
                val tagUID = byteArrayToHexString(uidBytes)
                Toast.makeText(this, "UID: $tagUID", Toast.LENGTH_SHORT).show()

                // Check if the UID exists
                val driverExists = dbHelper.driverExists(tagUID)
                val dispatcherExists = dbHelper.dispatcherExists(tagUID)
                val conductorExists = dbHelper.conductorExists(tagUID)

                val drivers = dbHelper.getDriverData()
                val dispatchers = dbHelper.getDispatcherData()
                val conductors = dbHelper.getConductorData()

                val dispatcherText = findViewById<TextView>(R.id.dispatcher_text)
                val driverText = findViewById<TextView>(R.id.driver_text)
                val conductorText = findViewById<TextView>(R.id.conductor_text)

                val dispatcherName = findViewById<TextView>(R.id.dispatcher_name)
                val driverName = findViewById<TextView>(R.id.driver_name)
                val conductorName = findViewById<TextView>(R.id.conductor_name)

                if (driverExists && !isDriverExists) {
                    tapInDriver()
                    isDriverExists = true

                    for (driver in drivers) {
                        val driverUid = driver.first
                        val driverNameDB = driver.second
                        if (driverUid == tagUID) {
                            driverText.visibility = View.VISIBLE
                            driverName.visibility = View.VISIBLE
                            driverName.text = driverNameDB
                            Log.d("DriverInfo", "UID: $driverUid, Name: $driverNameDB")
                            break
                        } else {
                            Toast.makeText(this, "Does not exist", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                if (dispatcherExists && !isDispatcherExists) {
                    tapInDispatcher()
                    isDispatcherExists = true

                    for (dispatcher in dispatchers) {
                        val dispatcherUid = dispatcher.first
                        val dispatcherNameDB = dispatcher.second
                        if (dispatcherUid == tagUID) {
                            dispatcherText.visibility = View.VISIBLE
                            dispatcherName.visibility = View.VISIBLE
                            dispatcherName.text = dispatcherNameDB
                            Log.d("Dispatcher Info", "UID: $dispatcherUid, Name: $dispatcherNameDB")
                            break
                        }
                    }
                }

                if (conductorExists && !isConductorExists) {
                    tapInConductor()
                    isConductorExists = true

                    for (conductor in conductors) {
                        val conductorUid = conductor.first
                        val conductorNameDB = conductor.second
                        if (conductorUid == tagUID) {
                            conductorText.visibility = View.VISIBLE
                            conductorName.visibility = View.VISIBLE
                            conductorName.text = conductorNameDB
                            Log.d("Conductor Info", "UID: $conductorUid, Name: $conductorNameDB")
                            break
                        }
                    }
                }

                Log.d("Requirements", "$isDispatcherExists")
                Log.d("Requirements", "$isDriverExists")
                Log.d("Requirements", "$isConductorExists")
                Log.d("Requirements", "$isSelectedBound")
                Log.d("Requirements", "$isSelectedRoute")
                Log.d("Requirements", "$regularTripButtonClicked")
                Log.d("Requirements", "$specialTripButtonClicked")

                updateDispatchButtonState()

            }
        }
    }

    private fun byteArrayToHexString(byteArray: ByteArray): String {
        val stringBuilder = StringBuilder(byteArray.size * 2)
        for (byte in byteArray) {
            val hex = Integer.toHexString(0xFF and byte.toInt())
            if (hex.length == 1) {
                stringBuilder.append('0')
            }
            stringBuilder.append(hex)
        }
        return stringBuilder.toString().toUpperCase()
    }

    private fun enableNFC() {
        val nfcIntent = Intent(this, javaClass).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
        val nfcPendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0)
        val intentFiltersArray = arrayOf(IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED))

        nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, intentFiltersArray, null)
    }

    private fun disableNFC() {
        nfcAdapter?.disableForegroundDispatch(this)
    }

    private fun tapInDriver(): String {

        if (tagUID == driverCardUID) {

            Log.d("DriverCard", "Tapped in")
            val button = findViewById<Button>(R.id.driver_btn,)
            button.setTextColor(Color.WHITE)
            button.setBackgroundResource(R.drawable.green_btn)
            button.text = ""
            isDriverExists = true
        } else {
            Log.d("Database Result", "UID Card Does not exist")
        }
        return driverCardUID
    }

    private fun tapInDispatcher(): String {

        if (tagUID == dispatcherCardUID) {

            Log.d("DispatcherCard", "Tapped in")
            val button = findViewById<Button>(R.id.dispatcher_btn,)
            button.setTextColor(Color.WHITE)
            button.setBackgroundResource(R.drawable.green_btn)
            button.text = ""
            isDispatcherExists = true
        } else {
            Log.d("Database Result", "UID Card Does not exist")
        }
        return dispatcherCardUID
    }

    private fun tapInConductor(): String {

        if (tagUID == conductorCardUID) {

            Log.d("ConductorCard", "Tapped in")
            val button = findViewById<Button>(R.id.conductor_btn,)
            button.setTextColor(Color.WHITE)
            button.setBackgroundResource(R.drawable.green_btn)
            button.text = ""
            isConductorExists = true
        } else {
            Log.d("Database Result", "UID Card Does not exist")
        }
        return conductorCardUID
    }

    private fun boundAndRoute(){
        val regularTripButton = findViewById<Button>(R.id.regulartrip_btn)
        val specialTripButton = findViewById<Button>(R.id.specialtrip_btn)
        regularTripButton.setOnClickListener {
            if (!regularTripButtonClicked) {
                regularTripButtonClicked = true
                specialTripButtonClicked = false
                customButtons.handleTripButtonClick(regularTripButton)
                Log.d("Regular Button", "$regularTripButtonClicked")
                Log.d("Special Button", "$specialTripButtonClicked")
                updateDispatchButtonState()
            }
        }
        regularTripButton.performClick()

        specialTripButton.setOnClickListener {
            if (!specialTripButtonClicked) {
                specialTripButtonClicked = true
                regularTripButtonClicked = false
                customButtons.handleTripButtonClick(specialTripButton)
                Log.d("Special Button", "$specialTripButtonClicked")
                Log.d("Regular Button", "$regularTripButtonClicked")
                updateDispatchButtonState()
            }
        }

        selectRoutelistView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = selectRoutelistView.adapter.getItem(position) as String
            selectedRoute = selectedItem
            selectRoutesOptionsbutton.text = selectedRoute
            selectRoutelistView.visibility = View.GONE
            dimBackground.visibility = View.GONE
            isSelectedRoute = true
            updateDispatchButtonState()
        }
    }

    private fun updateDispatchButtonState() {
        val dispatchButton = findViewById<Button>(R.id.dispatch_btn)
        val allConditionsMet =
                isSelectedBound &&
                isSelectedRoute &&
                isDispatcherExists &&
                isDriverExists &&
                isConductorExists &&
                ((regularTripButtonClicked && !specialTripButtonClicked) ||
                  (!regularTripButtonClicked && specialTripButtonClicked))

        if (allConditionsMet) {
            dispatchButton.isClickable = true
            dispatchButton.setTextColor(Color.WHITE)
            dispatchButton.setBackgroundResource(R.drawable.light_blue_button)
            Log.d("Requirements", "The requirements are met")
            dispatchButton.setOnClickListener {
                val intent = Intent(this, DispatcherPage::class.java)
                startActivity(intent)
            }
        } else {
            dispatchButton.isClickable = false
            dispatchButton.setTextColor(Color.LTGRAY)
            dispatchButton.setBackgroundResource(R.drawable.disabled_button)
            Log.d("Requirements", "The requirements are not met")
        }
    }
}


