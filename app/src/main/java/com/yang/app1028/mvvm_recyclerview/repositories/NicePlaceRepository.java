package com.yang.app1028.mvvm_recyclerview.repositories;

import androidx.lifecycle.MutableLiveData;

import com.yang.app1028.mvvm_recyclerview.models.NicePlace;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton Pattern
 */

public class NicePlaceRepository {
    private static NicePlaceRepository instance;
    private ArrayList<NicePlace> dataset = new ArrayList<>();

    public static NicePlaceRepository getInstance() {
        if (instance == null) {
            instance = new NicePlaceRepository();
        }
        return instance;
    }

    //Pretend to get data from web service
    public MutableLiveData<List<NicePlace>> getNicePlaces() {
        setNicePlaces();
        MutableLiveData<List<NicePlace>> data = new MutableLiveData<>();
        data.setValue(dataset);
        return data;
    }

    private void setNicePlaces() {
        dataset.add(new NicePlace("bbbbb", null));
        dataset.add(new NicePlace("ccccc", null));
        dataset.add(new NicePlace("ddddd", null));
        dataset.add(new NicePlace("eeeee", null));

        dataset.add(new NicePlace("aaaaa", null));
        dataset.add(new NicePlace("bbbbb", null));
        dataset.add(new NicePlace("ccccc", null));
        dataset.add(new NicePlace("ddddd", null));
        dataset.add(new NicePlace("eeeee", null));

        dataset.add(new NicePlace("aaaaa", null));
        dataset.add(new NicePlace("bbbbb", null));
        dataset.add(new NicePlace("ccccc", null));
        dataset.add(new NicePlace("ddddd", null));
        dataset.add(new NicePlace("eeeee", null));
    }
}
