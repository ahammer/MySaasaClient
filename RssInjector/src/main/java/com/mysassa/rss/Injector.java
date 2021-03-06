package com.mysaasa.rss;

import com.mysaasa.api.responses.LoginUserResponse;
import com.mysaasa.api.MySaasaClient;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by adam on 2014-09-28.
 */


public class Injector {
    static final String[] newsUrl= new String[]{"http://www.reddit.com/.rss","http://www.theverge.com/google/rss/index.xml","http://rss.slashdot.org/Slashdot/slashdot"};

    public static void main(String[] args) throws Exception {
        int port = 8080   ;
        updateNews("theme1.simpletest.ca", port,"http");
    }

    private static void updateNews(String site, int port, String scheme) throws IOException, FeedException {
        MySaasaClient mySaasaClient = new MySaasaClient(site,port,scheme);

        mySaasaClient.getAuthenticationManager()
                .login("adam", "adam")
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<LoginUserResponse>() {
            @Override
            public void call(LoginUserResponse loginUserResponse) {
                System.out.println("Response: "+loginUserResponse);
            }
        });

        for (String urlString:newsUrl) {
            URL url = new URL(urlString);
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(httpcon));
            List entries = feed.getEntries();
            Iterator itEntries = entries.iterator();

            while (itEntries.hasNext()) {
                SyndEntry entry = (SyndEntry) itEntries.next();
                System.out.println();

            }
        }
    }
}
