package com.github.arekolek.phone;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ContactUtils {

    private Context context;

    public ContactUtils(Context context){
        this.context = context;
    }

    public List<Contact> getAllContacts(){

        Uri uri = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            uri = ContactsContract.CommonDataKinds.Contactables.CONTENT_URI;
        } else {
            uri = ContactsContract.Data.CONTENT_URI;

        }

        List<Contact> list = new ArrayList<>();
        ContentResolver provider = context.getContentResolver();
        Cursor cursor = provider.query(uri, null, null, null, null);
        if(cursor.getCount() > 0){
            try {
                while (cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    list.add(new Contact(name, phone, ""));
                }
            } catch (Exception ex){

            } finally {
                cursor.close();
            }

        }
        return list;
    }

    public HashMap<String, Contact> fetchAllToMap(){
        HashMap<String, Contact> map = new HashMap<>();
        List<Contact> list = getAllContacts();
        for(Contact item : list) map.put(item.getPhone().trim().toLowerCase(), item);
        return map;
    }

}