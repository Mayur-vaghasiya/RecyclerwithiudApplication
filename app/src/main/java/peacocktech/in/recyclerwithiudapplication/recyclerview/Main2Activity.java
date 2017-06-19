package peacocktech.in.recyclerwithiudapplication.recyclerview;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import peacocktech.in.recyclerwithiudapplication.R;
import peacocktech.in.recyclerwithiudapplication.recyclerview.Datahelper.DataBaseHepler;

public class Main2Activity extends AppCompatActivity {
    // EditText edittext;
    int textlength = 0;
    ArrayAdapter<Friend> adapter1;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private ArrayList<Friend> friendArrayList = new ArrayList<Friend>();
    private FloatingActionButton fab;
    private int gender;
    private DataBaseHepler dhelper;
    private CleanableEditText search;
    private ArrayList<Friend> listfriOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main2);

       /* View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }*/

        this.listfriOrigin = new ArrayList<Friend>();
        this.listfriOrigin.addAll(friendArrayList);
        search = (CleanableEditText) findViewById(R.id.search);
        /*this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        search.requestFocus();*/
        dhelper = new DataBaseHepler(this);
        friendArrayList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyle_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        setRecyclerViewData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Main2Activity.this);
                dialog.setContentView(R.layout.dialog_add); //layout for dialog
                dialog.setTitle("Add a new friend");
                dialog.setCancelable(false); //none-dismiss when touching outside Dialog

                // set the custom dialog components - texts and image
                final EditText name = (EditText) dialog.findViewById(R.id.name);
                final EditText job = (EditText) dialog.findViewById(R.id.job);
                Spinner spnGender = (Spinner) dialog.findViewById(R.id.gender);
                View btnAdd = dialog.findViewById(R.id.btn_ok);
                View btnCancel = dialog.findViewById(R.id.btn_cancel);


                //set spinner adapter
                final ArrayList<String> gendersList = new ArrayList<>();
                gendersList.add("Male");
                gendersList.add("Female");
                ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(Main2Activity.this, android.R.layout.simple_dropdown_item_1line, gendersList);
                spnGender.setAdapter(spnAdapter);
                //set handling event for 2 buttons and spinner
                spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            gender = 0;
                        } else {
                            gender = 1;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Friend friend = new Friend(name.getText().toString().trim(), gender, job.getText().toString().trim());
                        //adding new object to arraylist
                        dhelper.adduser(friend);
                        if (friend != null) {

                            Toast.makeText(Main2Activity.this, "insert", Toast.LENGTH_SHORT).show();
                        }
                        friendArrayList.add(friend);
                        //notify data set changed in RecyclerView adapter
                        adapter.notifyDataSetChanged();
                        //close dialog after all
                        dialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                setRecyclerViewData();
                String query1 = query.toString();
                adapter.filter(query1);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setRecyclerViewData() {
        friendArrayList = dhelper.getalluserinfo();
        //adding data to array list
        adapter = new RecyclerAdapter(this, friendArrayList);
        recyclerView.setAdapter(adapter);
    }
}
