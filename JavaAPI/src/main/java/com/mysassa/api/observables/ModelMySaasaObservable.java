package com.mysassa.api.observables;

import com.mysassa.api.MySaasaClient;
import com.mysassa.api.responses.SimpleResponse;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * This is used to create observables that emit a Model item, but is 2 way generic
 * you start by defining the type you want to emit, and the type of the network response
 * you implement a function that returns the network call, and another function that
 * parses the response and emits items to the subscriber.
 */
public abstract class ModelMySaasaObservable <T, V extends SimpleResponse> implements Observable.OnSubscribe<T> {
    private final MySaasaClient mySaasa;
    private V response;


    public ModelMySaasaObservable(MySaasaClient mySaasa) {
        this.mySaasa = mySaasa;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        if (!subscriber.isUnsubscribed()) {

            if (response != null) {
                handleResponse(subscriber);
                return;
            }

            Call<V> call = getNetworkCall();

            try {
                mySaasa.startNetwork();
                response = call.execute().body();
                handleResponse(subscriber);
            } catch (Exception e) {
                onError(e);
                subscriber.onError(e);
            } finally {
                mySaasa.stopNetwork();
            }
        }
    }

    protected void onError( Exception e) {
        //Reserved for over-ride
    }

    private void handleResponse(Subscriber<? super T> subscriber) {
        if (response.isSuccess()) {
            processItems(response, subscriber);
        } else {
            subscriber.onError(new RuntimeException(response.getMessage()));
        }
    }

    public abstract void processItems(V response, Subscriber<? super T> subscriber);



    protected abstract Call<V> getNetworkCall();

    public MySaasaClient getMySaasa() {
        return mySaasa;
    }

    public V getResponse() {
        return response;
    }
}
