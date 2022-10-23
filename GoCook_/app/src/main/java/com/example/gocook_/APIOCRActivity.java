package com.example.gocook_;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class APIOCRActivity {

        // main(String[] args) -> main(String args)로 바꿈
        public static String main(String imageFilePath) {
            List<String> resultList = new ArrayList<String>();
            String totalText = "";
            String apiURL = "https://av4b7uzpdp.apigw.ntruss.com/custom/v1/18681/3ecc1e5217aae38b26dda784fcd23f08dbdd720b6588f1783c3dec5145b47fd3/general";
            String secretKey = "aXJDR25sV1ZyUlFVcU5IT0xGWm1nVGRtVHpCalJzc2I=";
            String imageFile = imageFilePath;

            try {
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setUseCaches(false);
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setReadTimeout(30000);
                con.setRequestMethod("POST");
                String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
                con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                con.setRequestProperty("X-OCR-SECRET", secretKey);

                JSONObject json = new JSONObject();
                json.put("version", "V2");
                json.put("requestId", UUID.randomUUID().toString());
                json.put("timestamp", System.currentTimeMillis());
                JSONObject image = new JSONObject();
                image.put("format", "jpg");
                image.put("name", "demo");
                JSONArray images = new JSONArray();
                images.put(image);
                json.put("images", images);
                String postParams = json.toString();

                con.connect();
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                long start = System.currentTimeMillis();
                File file = new File(imageFile);
                writeMultiPart(wr, postParams, file, boundary);
                wr.close();

                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                System.out.println(response);
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray mImages = jsonObject.getJSONArray("images");
                JSONObject objImages = mImages.getJSONObject(0);
                JSONArray mFields = objImages.getJSONArray("fields");
                for(int i = 0; i<mFields.length(); i++){
                    JSONObject objFields = mFields.getJSONObject(i);
                    String result = objFields.getString("inferText");
                    resultList.add(result);
                }
                totalText = String.join(" ",resultList);
            } catch (Exception e) {
                System.out.println(e);
            }
            return totalText;
        }

        private static void writeMultiPart(OutputStream out, String jsonMessage, File file, String boundary) throws
                IOException {
            StringBuilder sb = new StringBuilder();
            sb.append("--").append(boundary).append("\r\n");
            sb.append("Content-Disposition:form-data; name=\"message\"\r\n\r\n");
            sb.append(jsonMessage);
            sb.append("\r\n");

            out.write(sb.toString().getBytes("UTF-8"));
            out.flush();

            if (file != null && file.isFile()) {
                out.write(("--" + boundary + "\r\n").getBytes("UTF-8"));
                StringBuilder fileString = new StringBuilder();
                fileString
                        .append("Content-Disposition:form-data; name=\"file\"; filename=");
                fileString.append("\"" + file.getName() + "\"\r\n");
                fileString.append("Content-Type: application/octet-stream\r\n\r\n");
                out.write(fileString.toString().getBytes("UTF-8"));
                out.flush();

                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[8192];
                    int count;
                    while ((count = fis.read(buffer)) != -1) {
                        out.write(buffer, 0, count);
                    }
                    out.write("\r\n".getBytes());
                }

                out.write(("--" + boundary + "--\r\n").getBytes("UTF-8"));
            }
            out.flush();
        }
    }


