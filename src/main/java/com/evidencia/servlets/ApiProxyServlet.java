package com.evidencia.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

@WebServlet("/apiProxy")
public class ApiProxyServlet extends HttpServlet {

    private static final String API_URL_PUBLIC  = "https://escueladecapacitacionpetrolera.edu.co/sena/api.php";
    private static final String API_URL_PRIVATE = "https://escueladecapacitacionpetrolera.edu.co/sena/api2.php";
    private static final String API_KEY  = "abcd-1234-efgh-5678";
    private static final String USER_ID  = "1";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String mode = req.getParameter("mode");   // "public" o "auth"
        String id   = req.getParameter("id");     // opcional

        boolean isAuth = "auth".equals(mode);
        String targetUrl = isAuth ? API_URL_PRIVATE : API_URL_PUBLIC;

        if (id != null && !id.isBlank()) {
            if (isAuth) {
                targetUrl += "/" + URLEncoder.encode(id.trim(), StandardCharsets.UTF_8);
            } else {
                targetUrl += "?id=" + URLEncoder.encode(id.trim(), StandardCharsets.UTF_8);
            }
        }

        HttpURLConnection conn = (HttpURLConnection) new URL(targetUrl).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        if (isAuth) {
            conn.setRequestProperty("X-API-KEY", API_KEY);
            conn.setRequestProperty("X-USER-ID", USER_ID);
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 400) ? conn.getErrorStream() : conn.getInputStream();
        String body = is == null ? "{\"ok\":false,\"error\":\"Sin respuesta\"}"
                                 : new String(is.readAllBytes(), StandardCharsets.UTF_8);

        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.getWriter().write(body);
    }
}
