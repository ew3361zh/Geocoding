package com.example.geocoding

import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.IOException

// defined before class, needs to be 24 or fewer characters
private const val TAG = "GEOCODE_PLACE_ACTIVITY"

class MainActivity : AppCompatActivity() {

    private lateinit var placeNameInput: EditText
    private lateinit var mapButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        placeNameInput = findViewById(R.id.place_name_input)
        mapButton = findViewById(R.id.map_button)

        mapButton.setOnClickListener {
            val placeName = placeNameInput.text.toString()
            if (placeName.isBlank()) {
                Toast.makeText(this, getString(R.string.no_place_entered_error), Toast.LENGTH_LONG).show()
            } else {
                Log.d(TAG, "About to geocode $placeName")
                showMapForPlace(placeName)
            }
        }
    }

    private fun showMapForPlace(placeName: String) {
        // geocode place name to get list of locations
        val geocoder = Geocoder(this)
        // get first and most likely match only
        try {
            val addresses = geocoder.getFromLocationName(placeName, 1)
            // if user enters nonsense into the search bar, addresses list will be empty
            // use and intent to launch map app, for first location, if a location is found
            if (addresses.isNotEmpty()) {
                // display place
                val address = addresses.first()
                Log.d(TAG, "First address is $address")
                // makes a string in the form "geo:45, -90" (mpls approximate result)
                val geoUriString = "geo:${address.latitude},${address.longitude}"
                Log.d(TAG, "Using geo uri $geoUriString")
                val geoUri = Uri.parse(geoUriString)
                val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
                Log.d(TAG, "Launching map activity")
                // this will send a message to the android system to say this activity wants to launch another activity
                startActivity(mapIntent)
            } else {
                Log.d(TAG, "No places found for string $placeName")
                Toast.makeText(this, getString(R.string.no_places_found_error), Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: IOException) {
            Log.e(TAG, "Unable to geocode place $placeName", e)
            Toast.makeText(this, "Sorry, unable to geocode place. Are you online?", Toast.LENGTH_LONG).show()
        }

    }

}