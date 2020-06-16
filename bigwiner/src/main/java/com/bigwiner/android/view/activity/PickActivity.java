package com.bigwiner.android.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.R;
import com.bigwiner.android.view.BigwinerApplication;

import java.util.ArrayList;
import java.util.List;

import intersky.appbase.BaseActivity;
import intersky.mywidget.SearchViewLayout;
import intersky.mywidget.conturypick.Country;
import intersky.mywidget.conturypick.LetterHolder;
import intersky.mywidget.conturypick.PyAdapter;
import intersky.mywidget.conturypick.PyEntity;
import intersky.mywidget.conturypick.SideBar;
import intersky.mywidget.conturypick.VH;
import xpx.com.toolbar.utils.ToolBarHelper;

public class PickActivity extends BaseActivity {

    private ArrayList<Country> selectedCountries = new ArrayList<>();
    private ArrayList<Country> allCountries = new ArrayList<>();
    public TextView title;
    public ImageView back;
    public SearchViewLayout etSearch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolBarHelper.setSutColor(this, Color.argb(0, 255, 255, 255));
        setContentView(R.layout.activity_pick);
        this.mToolBarHelper.hidToolbar(this, (RelativeLayout) this.findViewById(com.bigwiner.R.id.buttomaciton));
        this.measureStatubar(this, (RelativeLayout) this.findViewById(com.bigwiner.R.id.stutebar));
        this.title = this.findViewById(R.id.title);
        this.back = this.findViewById(R.id.back);
        this.back.setOnClickListener(this.backListener);

        RecyclerView rvPick = (RecyclerView) findViewById(R.id.rv_pick);
        SideBar side = (SideBar) findViewById(R.id.side);
        etSearch = findViewById(R.id.et_search);
        TextView tvLetter = (TextView) findViewById(R.id.tv_letter);
        allCountries.clear();
        allCountries.addAll(Country.getAll(this));
        selectedCountries.clear();
        selectedCountries.addAll(allCountries);
        final CAdapter adapter = new CAdapter(selectedCountries);
        rvPick.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvPick.setLayoutManager(manager);
        rvPick.setAdapter(adapter);
        etSearch.setDotextChange(new SearchViewLayout.DoTextChange() {
            @Override
            public void doTextChange(boolean visiable) {
                String string = etSearch.getText().toString();
                selectedCountries.clear();
                for (Country country : allCountries) {
                    if(country.name.toLowerCase().contains(string.toLowerCase()))
                        selectedCountries.add(country);
                }
                adapter.update(selectedCountries);
            }
        });

        side.addIndex("#", side.indexes.size());
        side.setOnLetterChangeListener(new SideBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                tvLetter.setVisibility(View.VISIBLE);
                tvLetter.setText(letter);
                int  position = adapter.getLetterPosition(letter);
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }
            }

            @Override
            public void onReset() {
                tvLetter.setVisibility(View.GONE);
            }
        });
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    class CAdapter extends PyAdapter<RecyclerView.ViewHolder> {

        public CAdapter(List<? extends PyEntity> entities) {
            super(entities);
        }

        @Override
        public RecyclerView.ViewHolder onCreateLetterHolder(ViewGroup parent, int viewType) {
            return new LetterHolder(getLayoutInflater().inflate(R.layout.item_letter, parent, false));
        }

        @Override
        public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater().inflate(R.layout.item_country_large_padding, parent, false));
        }

        @Override
        public void onBindHolder(RecyclerView.ViewHolder holder, PyEntity entity, int position) {
            VH vh = (VH)holder;
            final Country country = (Country)entity;
            vh.tvName.setText(country.name);
            vh.tvCode.setText("+" + country.code);
            vh.line.setVisibility(View.VISIBLE);
            if(position != getItemCount()-1)
            {
                if(getItemViewType(position+1) == 0)
                {
                    vh.line.setVisibility(View.GONE);
                }

            }

            holder.itemView.setOnClickListener(v -> {
                Intent data = new Intent(getIntent().getAction());
                data.putExtra("code", "+"+country.code);
                data.putExtra("name", country.name);
                data.setPackage(BigwinerApplication.mApp.getPackageName());
                sendBroadcast(data);
                finish();
            });
        }

        @Override
        public void onBindLetterHolder(RecyclerView.ViewHolder holder, LetterEntity entity, int position) {
            ((LetterHolder)holder).textView.setText(entity.letter.toUpperCase());
        }
    }
}
