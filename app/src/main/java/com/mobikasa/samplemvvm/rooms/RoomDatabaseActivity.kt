package com.mobikasa.samplemvvm.rooms

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.mobikasa.samplemvvm.base.BaseActivity
import com.mobikasa.samplemvvm.R
import com.mobikasa.samplemvvm.databinding.ActivityRoomDatabaseBinding
import kotlinx.coroutines.launch
import java.util.*

class RoomDatabaseActivity :
    BaseActivity<ActivityRoomDatabaseBinding>(R.layout.activity_room_database) {

    private lateinit var mDatabase: ContactDatabase

    override fun onActivityCreated(dataBinder: ActivityRoomDatabaseBinding) {
        mDatabase = ContactDatabase.getContactDatabase(this)
        lifecycleScope.launch {
            mDatabase.contactDao().insertContact(Contact(0, "Kuldeep", "9839822362", Date(),1))
        }

        lifecycleScope.launch {
            mDatabase.contactDao().fetchContacts().observe(this@RoomDatabaseActivity) {
                Log.d("TAG", "$it")
            }
        }
    }

    override fun onRetryClick(apiName: String) {
    }
}