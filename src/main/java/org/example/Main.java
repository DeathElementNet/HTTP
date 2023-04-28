package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        var gson = new Gson();
        Cat[] cats;

        var allCats = retrieveCatsData();

        cats = gson.fromJson(allCats, Cat[].class);

        List<Cat> catsWithVotes = Arrays.stream(cats)
                .filter(cat -> cat.getUpvotes() > 0)
                .toList();

        var gsonPP = new GsonBuilder().setPrettyPrinting().create();

        String s = gsonPP.toJson(catsWithVotes);

        System.out.println(s);
    }

    private static String retrieveCatsData() throws IOException {

        CloseableHttpClient httpClient = HttpClients.custom()

                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // время ожидания получения данных
                        .setRedirectsEnabled(false) //
                        .build())
                .build();

        HttpGet request = new HttpGet("https://raw.githubusercontent.com/" +
                "netology-code/jd-homeworks/master/http/task1/cats");

        CloseableHttpResponse response = httpClient.execute(request);

        return (new String(response.getEntity().getContent().readAllBytes()));

    }
}