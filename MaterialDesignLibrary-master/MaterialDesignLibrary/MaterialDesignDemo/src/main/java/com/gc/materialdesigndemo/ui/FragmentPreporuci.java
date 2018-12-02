package com.gc.materialdesigndemo.ui;

import android.Manifest;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gc.materialdesigndemo.R;

import java.util.ArrayList;


public class FragmentPreporuci extends Fragment {

    ArrayList<Knjiga> knjige = new ArrayList<>();
    ArrayList<MyContacts> contacts  = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        knjige = getArguments().getParcelableArrayList("knjige");
        container.removeAllViews();
        container.clearDisappearingChildren();
        View rootView = inflater.inflate(R.layout.preporuci, container, false);

        return rootView;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        ListView lista = (ListView)getView().findViewById(R.id.listaFragment);
        Button dPosalji = (Button)getView().findViewById(R.id.dPosalji);
        Spinner sKategorije = (Spinner)getView().findViewById(R.id.sKontakti);

        OpisKnjigeAdapter adapter = new OpisKnjigeAdapter(getActivity(),  knjige);
        lista.setAdapter(adapter);
        showContacts();

        dPosalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

    }

    protected void sendEmail() {
        Log.i("Send email", "");
        Spinner sKontakti = (Spinner)getView().findViewById(R.id.sKontakti);
        final String email = sKontakti.getSelectedItem().toString();
        int pos = sKontakti.getSelectedItemPosition();
        String ime = contacts.get(pos).getName();
        String tekst = "Zdravo " + ime + ", \nProcitaj knjigu "+knjige.get(0).getNaziv()+" od "+ knjige.get(0).getAutori()+"!";
//        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//        emailIntent.setData(Uri.parse("mailto:"));
//        emailIntent.setType("text/plain");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MY FIRST EMAIL");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, tekst);

        String uriText =
                "mailto:"+email+
                        "?subject=" + Uri.encode("MY FISRT MAIL") +
                        "&body=" + Uri.encode(tekst);

        Uri uri = Uri.parse(uriText);

        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(uri);
        try {
            startActivity(sendIntent);
            getActivity().finish();
            Log.i("Finished sending email", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showContacts() {

        Spinner sKategorije = (Spinner)getView().findViewById(R.id.sKontakti);

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && getActivity().checkCallingOrSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            contacts = getContactNames();
            ArrayList<String> emails = new ArrayList<>();
            for(int i = 0; i < contacts.size(); i++)
                emails.add(contacts.get(i).getEmail());

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, emails); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sKategorije.setAdapter(spinnerArrayAdapter);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Read the name of all the contacts.
     *
     * @return a list of names.
     */
    private ArrayList<MyContacts> getContactNames() {
        ArrayList<MyContacts> contacts = new ArrayList<>();
        // Get the ContentResolver
        ContentResolver cr = getActivity().getContentResolver();
        // Get the Cursor of all the contacts
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        // Move the cursor to first. Also check whether the cursor is empty or not.
        if (cursor.moveToFirst()) {
            // Iterate through the cursor
            do {
                // Get the contacts name
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                Cursor cur1 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);

                while (cur1.moveToNext()) {
                    //to get the contact names
                    String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    MyContacts cont = new MyContacts(email, name);
                    if(email!=null){
                        contacts.add(cont);
                    }
                }
                cur1.close();

            } while (cursor.moveToNext());
        }

        // Close the curosor
        cursor.close();

        return contacts;
    }
}
