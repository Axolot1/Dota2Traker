package com.axolotl.dota2traker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.axolotl.dota2traker.fragment.MatchHistoryFragment;
import com.axolotl.dota2traker.utils.LogUtils;

/**
 * Created by axolotl on 16/7/2.
 */
public class MatchHistoryActivity extends AppCompatActivity {

    public static final String EXTRA_ID64 = "extra_steamId64";
    public static final String EXTRA_IMGURL = "extra_imgurl";
    public static final String EXTRA_NAME = "extra_name";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_history_layout);
        if(savedInstanceState == null){
            Intent i = getIntent();
            String name = i.getStringExtra(EXTRA_NAME);
            MatchHistoryFragment detailFragment = new MatchHistoryFragment();
            Bundle bundle = new Bundle();
            bundle.putString(MatchHistoryFragment.ARG_ID64, i.getStringExtra(EXTRA_ID64));
            bundle.putString(MatchHistoryFragment.ARG_IMG, i.getStringExtra(EXTRA_IMGURL));
            bundle.putString(MatchHistoryFragment.ARG_USER_NAME, name);
            detailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.match_history_container, detailFragment).commit();
        }
    }
}
