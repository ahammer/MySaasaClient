package com.mysassa.api.observables;

import com.mysassa.api.MySaasaClient;
import com.mysassa.api.responses.SimpleResponse;

import java.io.IOException;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Adam on 3/26/2016.
 */
public abstract class  StandardMySaasaObservable<T extends SimpleResponse> implements Observable.OnSubscribe<T>{
    private final MySaasaClient mySaasa;
    private T response;


    protected StandardMySaasaObservable(MySaasaClient mySaasa) {
        this.mySaasa = mySaasa;
    }



    @Override
    public void call(Subscriber<? super T> subscriber) {
        if (!subscriber.isUnsubscribed()) {

            if (response != null) {
                handleResponse(subscriber);
                return;
            }

            Call<T> call = getNetworkCall();

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
            subscriber.onNext(response);
            onSuccess(response);
            subscriber.onCompleted();
        } else {
            subscriber.onError(new RuntimeException(response.getMessage()));
        }
    }

    protected void onSuccess(T response) {

    }

    protected abstract Call<T> getNetworkCall();

    public MySaasaClient getMySaasa() {
        return mySaasa;
    }

    public T getResponse() {
        return response;
    }
}
