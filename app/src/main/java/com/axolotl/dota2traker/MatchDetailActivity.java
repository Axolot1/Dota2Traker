package com.axolotl.dota2traker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by axolotl on 16/7/8.
 */
public class MatchDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MATCH_ID = "extra_match_id";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);
        ButterKnife.bind(this);
    }

    public static void startDetailActivity(Context context, String matchId) {
        Intent i = new Intent(context, MatchDetailActivity.class);
        i.putExtra(EXTRA_MATCH_ID, matchId);
        context.startActivity(i);
    }

}
