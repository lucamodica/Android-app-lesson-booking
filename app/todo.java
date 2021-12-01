package com.example.esercitazione;


/*TODO ROBA PER IL TOAST

*  Context c= getApplicationContext();// ottengo dal so il contesto per esefgu
        CharSequence td="ciao";// testo
        int duration = Toast.LENGTH_SHORT;//durata
        Toast t=  Toast.makeText(c,td,duration);
        t.show();
* */


/*Todo convertire un text in stringa
* EditText text= new EditText(this);
        text.setText("ciaoaa");
*  String s = text.getText().toString();
 *
 *
 */


/* todo lanciare un altra activity
*
       Intent inte = new Intent(this, Activity2.class);
       result.putExtra(Intent.EXTRA_TEXT, testo.getText().toString());// aggiungo stringa in piu (es risultato)
setResult(Activity.RESULT_OK, result);
        startActivity(inte);
*
* */

/* TODO creare un timer
 new Handler(getMainLooper()).postDelayed(new Runnable() {
         @Override
         public void run() {
              setContentView(R.layout.activity_2);

         }
     },30000);

 */
/* todo creare il menù
* nel layout principale va inserito     <include layout="@layout/toolbar"/>
* nel file java :
* 1) implementare i  4 metodi 2 di default ed 1 ogni per ogni pulsante
*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_activity_action.xml items for use in the toolbar
        getMenuInflater().inflate(R.menu.main_activity_action,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("onOptionsItemSelected: ", item.getTitle().toString());
        switch (item.getItemId()) {
            case android.R.id.home:
// bottone del toolbar, quello del telefono chiama onBackPressed()
                finish();
                return true;
            case R.id.action_search:
                openSearch();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
*
*
    private void openSearch() {
        Log.d("Es1", "openSearch called");
        startActivity(new Intent(SearchManager.INTENT_ACTION_GLOBAL_SEARCH));
    }

    private void openSettings() {
        Log.d("Es1", "openSettings called");
        startActivity(new Intent(Settings.ACTION_SETTINGS));
    }
*todo(menu) nel main:
*
*     Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

* getSupportActionBar().setDisplayHomeAsUpEnabled(true); //opzionale
* */
/* todo usare risorse globali
 android:name="com.ium.unito.esempio1.MyGlobal"   ←   va inserito nel manifest dell app che lo vuole usareNel Manifest.xml

public class MyGlobal extends Application {
   private static String Variabile;
   public static String getVariabile() {
       return Variabile;
   }
   public static void setVariabile(String variabile) {
       MyGlobal.Variabile = variabile;
   }
} */
/*todo swipe laterali 12/10
1) Modificare filedi layout principale
 va messo come padre :
 <?xml version="1.0" encoding="utf-8"?>
<androidx.drawerlayout.widget.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">


  2)  e aggiunto un elemento:

    <com.google.android.material.navigation.NavigationView
        android:id="@+id/navview"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        app:menu="@menu/nav_items_laterale" />

</androidx.drawerlayout.widget.DrawerLayout>


3)creare un file xml menu con gli elementi da mostrare
4) per attivare  le navigazioni devi implementare q euste righe di codice
setSupportActionBar(toolbar);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationView navigationView = findViewById(R.id.navview);
        NavigationUI.setupWithNavController(navigationView, navController);
        */



/* todo adapter 12/10
1) il layout xml deve essere una list view (serve per fare una lista scrollabile partendo da un array generico)
* ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, arr);
        listView.setAdapter(adapter);
*
*
*
* */


/* todo per saper  la posizione id un oggetto cliccato*
listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String itemValue = (String) listView.getItemAtPosition(position);
                Toast.makeText(getActivity(),
                        "Position :" + position + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();
            }

/
 */

/* todo customizzare un adapter
* 1) creare una classe java ed estendere  ArrayAdapter<TIPO>
 2) EFFETTUARE LA SYNCRO
 *
 *
    LayoutInflater inflater;
    int layoutResourceId;
    MyObject data[];

    public CustomAdapter(Context context, int layoutResourceId, MyObject[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.inflater = ((Activity) context).getLayoutInflater();
        this.data = data;
    }


 * 3) scrivere la getView per vedere cosa esce a schermo
*  @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View row = convertView;
        MyObject ob = data[position];
        if (row == null) {
            // cerca i widget all'interno del singolo list item
            row = inflater.inflate(layoutResourceId, parent, false);
            ((TextView) (row.findViewById(R.id.editText1))).setText(ob.nome);
            ((TextView) (row.findViewById(R.id.editText2))).setText(ob.cognome);
            ((TextView) (row.findViewById(R.id.editText3))).setText(""+ob.eta);
            ((TextView) (row.findViewById(R.id.editText4))).setText(""+ob.aggiunto);
        }
        Button btn = row.findViewById(R.id.add_btn);
        btn.setOnClickListener(v -> Toast.makeText(parent.getContext(), "CLICK: " + position, Toast.LENGTH_LONG).show());
        return row;
    }
* */

/*todo dare i permessi ad un applicazione (contatti)
1) inserire nel main activity

*ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_CONTACTS},
                MY_PERMISSIONS_REQUEST_WRITE_CONTACTS);
*

 2) bisogna gestire una callback per verificare se i permessi sono stati ottenuti
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    Toast.makeText(this, "PERMESSO OTTENUTO", Toast.LENGTH_LONG).show();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
* */