package com.github.arekolek.phone

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.core.view.isVisible
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_call.*
import java.util.concurrent.TimeUnit

class CallActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    private lateinit var number: String

    var contactMap : HashMap<String, Contact> = HashMap();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)
        number = intent.data.schemeSpecificPart
    }

    override fun onStart() {
        super.onStart()

        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PermissionChecker.PERMISSION_GRANTED) {
            var contactUtils : ContactUtils = ContactUtils(this);
            contactMap = contactUtils.fetchAllToMap();
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), CallActivity.REQUEST_PERMISSION_CONTACT)
        }

        answer.setOnClickListener {
            OngoingCall.answer()
        }

        hangup.setOnClickListener {
            OngoingCall.hangup()
        }

        OngoingCall.state
            .subscribe(::updateUi)
            .addTo(disposables)

        OngoingCall.state
            .filter { it == Call.STATE_DISCONNECTED }
            .delay(1, TimeUnit.SECONDS)
            .firstElement()
            .subscribe { finish() }
            .addTo(disposables)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (requestCode == CallActivity.REQUEST_PERMISSION_CONTACT && PermissionChecker.PERMISSION_GRANTED in grantResults) {
            var contactUtils : ContactUtils = ContactUtils(this);
            contactMap = contactUtils.fetchAllToMap();
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(state: Int) {
        var result = number;
        if(contactMap.containsKey(number)) result = contactMap.get(number)!!.name;
        callInfo.text = "${state.asString().toLowerCase().capitalize()}\n$result"

        answer.isVisible = state == Call.STATE_RINGING
        hangup.isVisible = state in listOf(
            Call.STATE_DIALING,
            Call.STATE_RINGING,
            Call.STATE_ACTIVE
        )
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    companion object {
        fun start(context: Context, call: Call) {
            Intent(context, CallActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(call.details.handle)
                .let(context::startActivity)
        }
        const val REQUEST_PERMISSION_CONTACT = 0
    }
}
