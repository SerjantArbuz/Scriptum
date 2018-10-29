package sgtmelon.scriptum.app.vm.fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.RankRepo;

public final class RankViewModel extends AndroidViewModel {

    private RankRepo rankRepo;

    public RankViewModel(@NonNull Application application) {
        super(application);
    }

    public RankRepo getRankRepo() {
        return rankRepo;
    }

    public void setRankRepo(RankRepo rankRepo) {
        this.rankRepo = rankRepo;
    }

    public RankRepo loadData() {
        RoomDb db = RoomDb.provideDb(getApplication().getApplicationContext());
        rankRepo = db.daoRank().get();
        db.close();

        return rankRepo;
    }

}