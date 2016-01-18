package com.metalrain.simple.api.messages;

import com.metalrain.simple.api.enums.NetworkState;

/**
 * Created by Adam on 1/13/2015.
 */
public class NetworkStateChange {
    public final NetworkState state;

    public NetworkStateChange(NetworkState state) {
        this.state = state;
    }
}
