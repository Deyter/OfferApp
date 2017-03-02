package com.tigersoft.offerapp;


import java.util.ArrayList;

/**
 * Created by Deyter on 13.02.2017.
 */

public class JsonBind {
    public String getLink()
    {
        return query.link;
    }

    public String getText()
    {
        return query.text;
    }

    public int getCount()
    {
        return query.offers.size();
    }

    public String getOffer(int index)
    {
        return query.offers.get(index).text;
    }

    //structure of json
    Query query;

    public class Query {
        String link;
        String text;
        ArrayList<Offers> offers;

        public class Offers {
            String text;
        }
    }
}

