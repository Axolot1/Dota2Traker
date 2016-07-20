package com.axolotl.dota2traker.dagger;

import com.axolotl.dota2traker.MatchDetailActivity;
import com.axolotl.dota2traker.fragment.MatchDetailFragment;
import com.axolotl.dota2traker.fragment.MatchHistoryFragment;
import com.axolotl.dota2traker.fragment.PlayerCardFragment;
import com.axolotl.dota2traker.service.DotaIntentService;
import com.axolotl.dota2traker.service.DotaTaskService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by axolotl on 16/6/29.
 */
@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface MainComponent {
    void inject(PlayerCardFragment fragment);
    void inject(DotaIntentService service);
    void inject(MatchHistoryFragment fragment);
    void inject(MatchDetailActivity activity);
    void inject(MatchDetailFragment fragment);
    void inject(DotaTaskService service);
}
