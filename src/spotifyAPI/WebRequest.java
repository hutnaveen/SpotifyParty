package spotifyAPI;

import com.google.gson.Gson;
import model.OAuthTokenData;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.concurrent.TimeUnit;

public class WebRequest<T>{
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";

    public static <T> T sendRequest(String type, String requestURL, OAuthTokenData authorization, Class<T> t)
    {
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(1000, TimeUnit.MILLISECONDS).readTimeout(1000, TimeUnit.MILLISECONDS).writeTimeout(1000, TimeUnit.MILLISECONDS)
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
            int code = response.code();
            if(code == 429){
                String wait =  response.header("Retry-After");
                response.close();
                System.out.println("Waiting: " + wait + " secconds");
               Thread.sleep(Long.parseLong(wait)*1000);
            }
            else if(code == 503) {
                return null;
            }
            else if(data.startsWith("{\n" +
                    "  \"error\":"))
            {
                System.out.println("REDFLAGS");
                System.out.println(data);
                System.out.println(System.currentTimeMillis());
                System.exit(100);
            }
            else if(t != null) {
                Gson son = new Gson();
                response.close();
            }
            if(t != null) {
                Gson son = new Gson();
                return son.fromJson(data, t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.close();
        }
        return null;
    }
    public static void sendRequest(String type, String requestURL, OAuthTokenData authorization)
    {
        sendRequest(type, requestURL, authorization, null);
    }
    

}
