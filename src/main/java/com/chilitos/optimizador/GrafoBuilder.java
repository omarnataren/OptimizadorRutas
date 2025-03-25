package com.chilitos.optimizador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GrafoBuilder {
    public void construirDesdeOverpass(double lat, double lon, int radioMetros) throws IOException {
        String ql = "[out:json];"
                + "(way(around:" + radioMetros + "," + lat + "," + lon + ")[\"highway\"];);"
                + "out body;>;out skel qt;";

        String url = "https://overpass-api.de/api/interpreter?data=" + URLEncoder.encode(ql, "UTF-8");
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();

            System.out.println("✅ JSON recibido desde Overpass:");
            System.out.println(response.toString());
        } else {
            System.out.println("❌ Error HTTP: " + responseCode);
        }
        con.disconnect();
    }
}
