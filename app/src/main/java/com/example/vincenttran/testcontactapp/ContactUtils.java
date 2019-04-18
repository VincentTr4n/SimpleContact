package com.example.vincenttran.testcontactapp;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class ContactUtils {

    private Context context;

    public ContactUtils(Context context){
        this.context = context;
    }

    public List<ExampleItem> getAllContacts(){

        Uri uri = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            uri = ContactsContract.CommonDataKinds.Contactables.CONTENT_URI;
        } else {
            uri = ContactsContract.Data.CONTENT_URI;

        }

        List<ExampleItem> list = new ArrayList<>();
        ContentResolver provider = context.getContentResolver();
        Cursor cursor = provider.query(uri, null, null, null, null);
        if(cursor.getCount() > 0){
            try {
                while (cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    list.add(new ExampleItem(name, phone));
                }
            } catch (Exception ex){

            } finally {
                cursor.close();
            }

        }
        return list;
    }

}
