package com.example.luigi.spesapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corsista on 16/04/2018.
 */

public class ListDetailActivity extends AppCompatActivity implements DetailInterface {
    private RecyclerView myRecyclerView;
    private ItemRecyclerAdapter itemRecyclerAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    public static ListDetailActivity.LayoutManagerType mCurrentLayoutManagerType;
    public Articolo articolo;
    public View filtro;
    boolean nascondi = false;
    private EditText name;
    private EditText value;
    LinearLayout alertLayout;
    FloatingActionButton fab;

    @Override
    public int getListId() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        return id;
    }

    public enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_detail_layout);
        this.filtro = findViewById(R.id.filtro);


        Intent fromAdapter = getIntent();
        String nameList = fromAdapter.getStringExtra("nome");

        myRecyclerView = (RecyclerView) findViewById(R.id.recyclerDetail);
        itemRecyclerAdapter = new ItemRecyclerAdapter(ListDetailActivity.this);
        myLayoutManager = new LinearLayoutManager(this);
        mCurrentLayoutManagerType = ListDetailActivity.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setAdapter(itemRecyclerAdapter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(nameList);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListDetailActivity.super.onBackPressed();
            }
        });

        filtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nascondi == false) {
                    findViewById(R.id.checkFiltro).setVisibility(View.VISIBLE);
                    nascondi = true;
                }
                else {
                    findViewById(R.id.checkFiltro).setVisibility(View.INVISIBLE);
                    nascondi = false;
                }
                itemRecyclerAdapter.notifyDataSetChanged();
                itemRecyclerAdapter.nascondi = nascondi;
                itemRecyclerAdapter.updateList(ListDetailActivity.this);
                itemRecyclerAdapter.notifyDataSetChanged();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context thisContext = ListDetailActivity.this;
                ListDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ListDetailActivity.this);
                        name = new EditText(ListDetailActivity.this);
                        value = new EditText(ListDetailActivity.this);
                        name.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setTitle("Aggiungi Articolo");

                        alertLayout = new LinearLayout(getApplicationContext());
                        alertLayout.setMinimumWidth(1000);
                        alertLayout.addView(name);
                        alertLayout.addView(value);
                        alertLayout.setOrientation(alertLayout.VERTICAL);
                        name.setHint("Nome");
                        value.setHint("Quantità");
                        name.setWidth(500);
                        value.setWidth(500);
                        builder.setView(alertLayout);
                        builder.setPositiveButton("Aggiungi", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int id_list = getIntent().getIntExtra("id", 0);

                                ItemDatabaseManager itemDatabaseManager = new ItemDatabaseManager(ListDetailActivity.this);
                                itemDatabaseManager.open();
                                Long cursor = itemDatabaseManager.createItem(String.valueOf(name.getText()), id_list, String.valueOf(value.getText()));
                                Log.d("cursor", cursor.toString());
                                itemRecyclerAdapter.updateList(ListDetailActivity.this);

                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton("ANNULLA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                });
            }
        });
    }

}

