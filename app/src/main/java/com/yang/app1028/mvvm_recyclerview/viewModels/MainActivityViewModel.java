package com.yang.app1028.mvvm_recyclerview.viewModels;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yang.app1028.mvvm_recyclerview.models.NicePlace;
import com.yang.app1028.mvvm_recyclerview.repositories.NicePlaceRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<NicePlace>> mNicePlaces;
    private NicePlaceRepository mRepo;
    private MutableLiveData<Boolean> mIsUpadating = new MutableLiveData<>();

    public void init() {
        if (mNicePlaces != null) {
            return;
        }
        mRepo = NicePlaceRepository.getInstance();
        mNicePlaces = mRepo.getNicePlaces();
    }

    public void addValue(final NicePlace nicePlace) {
        mIsUpadating.setValue(true);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                List<NicePlace> dataset = mNicePlaces.getValue();
                dataset.add(nicePlace);
                mNicePlaces.postValue(dataset);
                mIsUpadating.postValue(false);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }

    public LiveData<List<NicePlace>> getNicePlaces() {
        return mNicePlaces;
    }

    public LiveData<Boolean> getIsUpdating() {
        return mIsUpadating;
    }
}
