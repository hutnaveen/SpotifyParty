package spotifyAPI;

import com.google.gson.Gson;
import model.OAuthTokenData;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;

public class WebRequest<T>{
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";

    public static <T> T sendRequest(String type, String requestURL, OAuthTokenData authorization, Class<T> t)
    {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(requestURL)
                .method(type, null)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + authorization.getAccess_token())
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String data = response.body().string();
            if(data.startsWith("{\n" +
                    "  \"error\":"))
            {
                System.out.println("REDFLAGS");
                System.out.println(data);
                System.exit(100);
            }
            if(t != null) {
                Gson son = new Gson();
                return son.fromJson(data, t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void sendRequest(String type, String requestURL, OAuthTokenData authorization)
    {
        sendRequest(type, requestURL, authorization, null);
    }

}
