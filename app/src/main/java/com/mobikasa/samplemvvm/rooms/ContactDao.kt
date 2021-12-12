package com.mobikasa.samplemvvm.rooms

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDao {
    @Insert
    suspend fun insertContact(data: Contact)

    @Update
    suspend fun updateContact(data: Contact)

    @Delete
    suspend fun deleteContact(data: Contact)

    @Query("Select *from contact")
    fun fetchContacts(): LiveData<List<Contact>>
}