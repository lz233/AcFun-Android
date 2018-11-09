package com.hubertyoung.common.baserx;


import android.text.TextUtils;

import java.util.concurrent.ConcurrentHashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import static com.hubertyoung.common.utils.TUtil.checkNotNull;


/**
 * 事件总线
 * <p>
 * <p>
 * LiveBus.getDefault().postEvent("LiveData","hi LiveData");
 * <p>
 * <p>
 * LiveBus.getDefault().subscribe("LiveData").observe(this, new Observer<Object>() {
 *
 * @Override public void onChanged(@Nullable Object o) {
 * Log.e("onChanged",((String)o));
 * }
 * });
 */
public class LiveBus {

    private static volatile LiveBus instance;

    private final ConcurrentHashMap<Object, LiveBusData<Object>> mLiveBus;

    private LiveBus() {
        mLiveBus = new ConcurrentHashMap<>();
    }

    public static LiveBus getDefault() {
        if (instance == null) {
            synchronized (LiveBus.class) {
                if (instance == null) {
                    instance = new LiveBus();
                }
            }
        }
        return instance;
    }


    public <T> MutableLiveData<T> subscribe( Object eventKey) {
        checkNotNull(eventKey);
        return subscribe(eventKey, "");
    }

    public <T> MutableLiveData<T> subscribe( Object eventKey, String tag) {
        checkNotNull(eventKey);
        return (MutableLiveData<T>) subscribe(eventKey, tag, Object.class);
    }

    public <T> MutableLiveData<T> subscribe( Object eventKey, Class<T> tClass) {
        return subscribe(eventKey, null, tClass);
    }

    public <T> MutableLiveData<T> subscribe( Object eventKey, String tag, Class<T> tClass) {
        checkNotNull(eventKey);
        checkNotNull(tClass);
        String key;
        if (!TextUtils.isEmpty(tag)) {
            key = eventKey + tag;
        } else {
            key = (String ) eventKey;
        }

        if (!mLiveBus.containsKey(key)) {
            mLiveBus.put(key, new LiveBusData<>(true));
        } else {
            LiveBusData liveBusData = mLiveBus.get(key);
            liveBusData.isFirstSubscribe = false;
        }

        return (MutableLiveData<T>) mLiveBus.get(key);
    }

    public <T> MutableLiveData<T> postEvent( Object eventKey, T value) {
        checkNotNull(eventKey);
        return postEvent(eventKey, null, value);
    }

    public <T> MutableLiveData<T> postEvent( Object eventKey, String tag, T value) {
        checkNotNull(eventKey);
        String key;
        if (!TextUtils.isEmpty(tag)) {
            key = eventKey + tag;
        } else {
            key = (String ) eventKey;
        }
        MutableLiveData<T> mutableLiveData = subscribe(key);
        mutableLiveData.postValue(value);
        return mutableLiveData;
    }


    public static class LiveBusData<T> extends MutableLiveData<T> {

        private boolean isFirstSubscribe;

        public LiveBusData(boolean isFirstSubscribe) {
            this.isFirstSubscribe = isFirstSubscribe;
        }

        @Override
        public void observe( @NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
            super.observe(owner, new ObserverWrapper<>(observer, isFirstSubscribe));
        }
    }

    private static class ObserverWrapper<T> implements Observer<T> {

        private Observer<T> observer;

        private boolean isChanged;

        private ObserverWrapper(Observer<T> observer, boolean isFirstSubscribe) {
            this.observer = observer;
            isChanged = isFirstSubscribe;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (isChanged) {
                if (observer != null) {
                    observer.onChanged(t);
                }
            } else {
                isChanged = true;
            }
        }

    }


    public void clear(Object eventKey) {
        clear(eventKey, null);
    }

    public void clear( Object eventKey, String tag) {
        if (mLiveBus != null && mLiveBus.size() > 0) {
            String clearkey;
            if (!TextUtils.isEmpty(tag)) {
                clearkey = eventKey + tag;
            } else {
                clearkey = (String ) eventKey;
            }
            mLiveBus.remove(clearkey);
        }

    }

}
