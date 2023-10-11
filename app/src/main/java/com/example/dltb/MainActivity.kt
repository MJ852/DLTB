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
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import org.w3c.dom.Text
import com.example.dltb.VehicleOptions


class MainActivity : AppCompatActivity() {

    private lateinit var selectBoundlistView: ListView
    private lateinit var selectRoutelistView: ListView
    private lateinit var selectVehicleNo: ListView
    private lateinit var dimBackground: View
    private lateinit var selectBoundOptionsbutton: Button
    private lateinit var selectRoutesOptionsbutton: Button
    private lateinit var selectVehicleOptionsbutton: Button
    private lateinit var passengerCountText: TextView
    private lateinit var northBoundOptions: Array<String>
    private lateinit var southBoundOptions: Array<String>
    private lateinit var selectVehicleOptions: Array<String>
    private var selectedBound: String = "Select Bound"
    private var selectedRoute: String = "Select Route"
    private var driverName: String = ""
    private var conductorName: String = ""
    private var passengerCount: Int = 0
    private var employeeType: String = ""

    private var isSelectedBound = false
    private var isSelectedRoute = false
    private var isDispatcherExists = false
    private var isDriverExists = false
    private var isEmployeeExists = false
    private var isConductorExists = false
    private var regularTripButtonClicked = false
    private var specialTripButtonClicked = false

    private lateinit var customButtons: CustomButtons

    private var nfcAdapter: NfcAdapter? = null
    private var dataInitialized = false
    private lateinit var dbHelper: DBHelper
    private lateinit var employeeCardUID: String
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
        selectVehicleOptionsbutton = findViewById(R.id.vehicle_number_btn)
        selectBoundlistView = findViewById(R.id.selectBoundListView)
        selectRoutelistView = findViewById(R.id.selectRouteListView)
        selectVehicleNo = findViewById(R.id.selectVehicleListView)
        dimBackground = findViewById<View>(R.id.semiTransparentOverlay)
        passengerCountText = findViewById(R.id.passenger_count)

        customButtons = CustomButtons()


        passengerCount = 0
        passengerCountText.text = "${String.format("%02d", passengerCount)}"

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

        selectVehicleOptionsbutton.setOnClickListener {
            selectVehicleNo()
        }

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
        this.employeeCardUID = ""
//        this.dispatcherCardUID = ""
//        this.conductorCardUID = ""



        // dispatcherName = findViewById<TextView>(R.id.dispatcher_name)
        driverName = findViewById<TextView>(R.id.driver_name).toString()
        conductorName = findViewById<TextView>(R.id.conductor_name).toString()

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

    private fun selectVehicleNo() {
        val vehicleOptionsList = VehicleOptions.selectVehicleOptions
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, vehicleOptionsList)
        selectVehicleNo.adapter = adapter

        selectVehicleOptionsbutton.setOnClickListener {
            if (selectVehicleNo.visibility == View.VISIBLE) {
                selectVehicleNo.visibility = View.GONE
            } else {
                selectVehicleNo.visibility = View.VISIBLE
            }
        }

        selectVehicleNo.setOnItemClickListener { _, _, position, _ ->
            val selectedNumber = vehicleOptionsList[position]
            selectVehicleOptionsbutton.text =
                selectedNumber // Set the button text to the selected item
            selectVehicleNo.visibility = View.GONE
        }
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
            updateDispatchButtonState()
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

//        val vehicleNumberBtn = findViewById<Button>(R.id.vehicle_number_btn)

        if (selectRoutelistView.visibility == View.VISIBLE) {
            selectRoutelistView.visibility = View.GONE
            //vehicleNumberBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.dim_gray))
            // dimBackground.visibility = View.GONE
            Log.d("Visibility", "ListView and Overlay are now gone.")

            if (selectedRoute != "Select Route") {
                isSelectedRoute = true
                updateDispatchButtonState()
            } else {
                isSelectedRoute = false
                updateDispatchButtonState()
            }
        } else {
            selectRoutelistView.visibility = View.VISIBLE
            // dimBackground.visibility = View.VISIBLE
            // vehicleNumberBtn.setBackgroundResource(R.color.gray)
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
                val employeeDataExists = dbHelper.employeeExists(tagUID)

                // Get the data of the UID
                val employeeData = dbHelper.getEmployeeData()
                val matchingEmployeeData = employeeData.find { it.UID == tagUID }

                // UI text
                val dispatcherText = findViewById<TextView>(R.id.dispatcher_text)
                val driverText = findViewById<TextView>(R.id.driver_text)
                val conductorText = findViewById<TextView>(R.id.conductor_text)

                // Display the names
                val dispatcherNameDisplay = findViewById<TextView>(R.id.dispatcher_name)
                val driverNameDisplay = findViewById<TextView>(R.id.driver_name)
                val conductorNameDisplay = findViewById<TextView>(R.id.conductor_name)

                if (matchingEmployeeData != null) {
                    Log.d("Card", "Tapped in")

                    val employeeName = matchingEmployeeData.Name
                    val employeeType = matchingEmployeeData.EmployeeType


                    if (employeeType == "driver" ) {

                        Log.d("DriverCard", "Tapped in")

                        //change button color
                        val driverButton = findViewById<Button>(R.id.driver_btn)
                        driverButton.setTextColor(Color.WHITE)
                        driverButton.setBackgroundResource(R.drawable.green_btn)
                        driverButton.text = ""
                        isDriverExists = true

                        for (employee in employeeData) {
                            val uid = employee.UID
                           // val name = employee.Name
                            if (uid == tagUID) {
                                driverText.visibility = View.VISIBLE
                                driverNameDisplay.visibility = View.VISIBLE
                                driverNameDisplay.text = employeeName
                                driverName = employeeName
                              //  Log.d("EmployeeInfo", "UID: $uid, Name: $name")
                                break
                            }
                        }
                    } else if (employeeType == "dispatcher") {

                        Log.d("DispatcherCard", "Tapped in")

                        //change button color
                        val dispatcherButton = findViewById<Button>(R.id.dispatcher_btn)
                        dispatcherButton.setTextColor(Color.WHITE)
                        dispatcherButton.setBackgroundResource(R.drawable.green_btn)
                        dispatcherButton.text = ""
                        isDispatcherExists = true

                        for (employee in employeeData) {
                            val uid = employee.UID
                           // val name = employee.Name
                            if (uid == tagUID) {
                                dispatcherText.visibility = View.VISIBLE
                                dispatcherNameDisplay.visibility = View.VISIBLE
                                dispatcherNameDisplay.text = employeeName
                               // Log.d("EmployeeInfo", "UID: $uid, Name: $name")
                                break
                            }
                        }
                    } else if (employeeType == "conductor") {

                        Log.d("ConductorCard", "Tapped in")

                        val conductorButton = findViewById<Button>(R.id.conductor_btn)
                        conductorButton.setTextColor(Color.WHITE)
                        conductorButton.setBackgroundResource(R.drawable.green_btn)
                        conductorButton.text = ""
                        isConductorExists = true

                        for (employee in employeeData) {
                            val uid = employee.UID
                           // val name = employee.Name
                            if (uid == tagUID) {
                                conductorText.visibility = View.VISIBLE
                                conductorNameDisplay.visibility = View.VISIBLE
                                conductorNameDisplay.text = employeeName
                                conductorName = employeeName
                                //Log.d("EmployeeInfo", "UID: $uid, Name: $name")
                                break
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "UID does not exist", Toast.LENGTH_SHORT).show()
                }

                Log.d("Requirements", "$isDispatcherExists")
                Log.d("Requirements", "$isDriverExists")
                Log.d("Requirements", "$isConductorExists")
                Log.d("Requirements", "$isSelectedBound")
                Log.d("Requirements", "$isSelectedRoute")
                Log.d("Requirements", "$regularTripButtonClicked")
                Log.d("Requirements", "$specialTripButtonClicked")

                updateDispatchButtonState()

            } else {
                Toast.makeText(this, "Card cannot be found", Toast.LENGTH_SHORT).show()
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

    private fun boundAndRoute() {
        val regularTripButton = findViewById<Button>(R.id.regulartrip_btn)
        val specialTripButton = findViewById<Button>(R.id.specialtrip_btn)
        regularTripButton.setOnClickListener {
            if (!regularTripButtonClicked) {
                regularTripButtonClicked = true
                specialTripButtonClicked = false
                customButtons.handleTripButtonClick(regularTripButton)

                updateDispatchButtonState()
            }
        }
        regularTripButton.performClick()

        specialTripButton.setOnClickListener {
            if (!specialTripButtonClicked) {
                specialTripButtonClicked = true
                regularTripButtonClicked = false
                customButtons.handleTripButtonClick(specialTripButton)

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
            dispatchButton.isEnabled = true
            dispatchButton.setTextColor(Color.WHITE)
            dispatchButton.setBackgroundResource(R.drawable.light_blue_button)
        } else {
            dispatchButton.setTextColor(Color.LTGRAY)
            dispatchButton.setBackgroundResource(R.drawable.disabled_button)
        }

        dispatchButton.setOnClickListener(){
            launchDispatcherPage()
        }
    }
    private fun dialogBoxAlert() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Details Incomplete")
        alertDialog.setMessage("Please complete the details to proceed")

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { dialog, which ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    private fun launchDispatcherPage() {
        if (isSelectedBound && isSelectedRoute && isDispatcherExists && isDriverExists && isConductorExists &&
            ((regularTripButtonClicked && !specialTripButtonClicked) || (!regularTripButtonClicked && specialTripButtonClicked))) {

            val intent = Intent(this, DispatcherPage::class.java)
            intent.putExtra("CONDUCTOR_Name", conductorName ?: "DefaultConductorName")
            intent.putExtra("DRIVER_Name", driverName ?: "DefaultDriverName")
            startActivity(intent)
        } else {
            dialogBoxAlert()
        }
    }


    private fun passengerCountNumber() {
        passengerCount = 0
    }
}




