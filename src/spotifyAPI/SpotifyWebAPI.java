package spotifyAPI;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import exception.SpotifyException;
import interfaces.SpotifyPlayerAPI;
import model.Artist;
import model.Item;
import model.OAuthTokenData;
import model.PlayerData;

import model.SearchItem;
import model.UserData;
import oAuth.OAuthHTTPServer;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

public class SpotifyWebAPI implements SpotifyPlayerAPI {
    OAuthTokenData oAuthToken;
    URI redirect = null;
    final String ogRedirect = "http%3A%2F%2Flocalhost%3A8081%2Fhello";
    final String clientID = "c1ca134a13a74a1b95046d69c8ef11d1";
    String randomString;
    String code;
    Properties props;
    public SpotifyWebAPI() {
        props = new Properties();
        InputStream stream = getClass().getResourceAsStream("/spotifyAPI/token.properties");
        if(stream == null)
        {
            try {
                new File(getClass().getResource("/spotifyAPI/").getPath()+"token.properties").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                props.load(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (props.getProperty("refreshToken") != null) {
                oAuthToken = new OAuthTokenData();
                oAuthToken.setRefresh_token(props.getProperty("refreshToken"));
                oAuthToken = reFreshToken();
            }
        }
        if(oAuthToken == null){
            try {
                props.load(getClass().getResourceAsStream("/spotifyAPI/token.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            final StringKeyGenerator secureKeyGenerator =
                    new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);
            randomString = secureKeyGenerator.generateKey();
            String urlEncoded = createHash(randomString);


            //   System.out.println("Random String="+ randomString);
            //  System.out.println("urlEncoded String="+ urlEncoded);
            //redirect = new URI("https://accounts.spotify.com:443/authorize?client_id=c1ca134a13a74a1b95046d69c8ef11d1&response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A8081%2Fhello&scope=user-read-playback-state%2Cuser-read-email%2C");
            try {
                redirect = new URI("https://accounts.spotify.com:443/authorize?client_id=" + clientID + "&response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A8081%2Fhello&scope=user-read-playback-state%2Cuser-read-email%2C%2Cuser-read-private%2C&code_challenge=" + urlEncoded + "&code_challenge_method=S256");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            OAuthHTTPServer server = new OAuthHTTPServer(8081);
            try {
                Desktop.getDesktop().browse(redirect);
                code = (server.getCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
            oAuthToken = getToken();
            System.out.println(oAuthToken.getAccess_token());
            System.out.println(oAuthToken.getRefresh_token());
        }
        new Thread(() -> {
            try {
                Thread.sleep(3480000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            oAuthToken = reFreshToken();
        }).start();
    }

    private static String createHash(String value) {
       try {
           MessageDigest md = MessageDigest.getInstance("SHA-256");
           byte[] digest = md.digest(value.getBytes(StandardCharsets.US_ASCII));
           return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
       }catch (Exception e){
           e.printStackTrace();
       }
        return null;
    }

    private OAuthTokenData getToken()
    {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8");

        RequestBody body = RequestBody.create(mediaType, "client_id="+clientID+"&grant_type=authorization_code&code="+code+"&redirect_uri=http%3A%2F%2Flocalhost%3A8081%2Fhello&code_verifier="+randomString);
        Request request = new Request.Builder()
                .url("https://accounts.spotify.com/api/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .build();
        try {
            Response response = client.newCall(request).execute();
            Gson son = new Gson();
            String bod = response.body().string();
          //  System.out.println(bod);
            OAuthTokenData sat = son.fromJson(bod, OAuthTokenData.class);
            props.setProperty("refreshToken", sat.getRefresh_token());
            try {
                props.store(new FileOutputStream(getClass().getResource("/spotifyAPI/token.properties").getFile()), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sat;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private OAuthTokenData reFreshToken()
    {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=refresh_token&refresh_token="+oAuthToken.getRefresh_token()+"&client_id="+clientID);
        Request request = new Request.Builder()
                .url("https://accounts.spotify.com/api/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            Gson son = new Gson();
            OAuthTokenData sat = son.fromJson(response.body().string(), OAuthTokenData.class);
            props.setProperty("refreshToken", sat.getRefresh_token());
            try {
                props.store(new FileOutputStream(getClass().getResource("/spotifyAPI/token.properties").getFile()), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sat;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public boolean isSingle() {
        return false;
    }


    @Override
    public void setVolume(int am) {

    }

    @Override
    public void playPause() throws IOException {
            if(isPlaying())
                pause();
            else
                play();
    }

    @Override
    public void play() {
    }

    @Override
    public void pause() {
    }

    @Override
    public long getPlayBackPosition() throws IOException {
        return getPlayerData().getProgress_ms();
    }

    @Override
    public void setPlayBackPosition(long pos) {

    }

    @Override
    public long getDuration() throws IOException {
        return getPlayingTrack().getDuration_ms();
    }

    @Override
    public int getVolume() throws IOException {
        return getPlayerData().getDevice().getVolume_percent();
    }

    @Override
    public boolean isPlaying() throws IOException {
        return getPlayerData().is_playing();
    }

    @Override
    public void nextTrack() {

    }

    @Override
    public void previousTrack() {
    }

    @Override
    public List<Artist> getTrackArtists() throws IOException {
        return getPlayingTrack().getArtists();
    }

    @Override
    public String getTrackAlbum() throws IOException {
        return getPlayingTrack().getAlbum().getName();
    }

    @Override
    public String getTrackUri() throws IOException {
        Item item = getPlayingTrack();
        return item.getUri();
    }

    @Override
    public String getTrackName() throws IOException {
        return getPlayingTrack().getName();
    }

    @Override
    public Item getPlayingTrack() throws IOException {
        return getPlayerData().getItem();
    }

    // TODO: 4/15/20
    @Override
    public boolean playTrack(String id) {
        return false;
    }

    @Override
    public boolean playTrack(Item song) {
        return false;
    }
    public void saveTrack(String trackId) {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/tracks?ids=" + trackId.substring(trackId.lastIndexOf(':')+1))
                .method("PUT", body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + oAuthToken.getAccess_token())
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public PlayerData getPlayerData() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/player")
                .method("GET", null)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + oAuthToken.getAccess_token())
                .build();
            Response response = client.newCall(request).execute();
            Gson son = new Gson();
            String data = response.body().string();
            if(data.startsWith("{\n" +
                    "  \"error\":"))
            {
                System.out.println("REDFLAGS");
                System.out.println(data);
                System.exit(100);
            }
            return son.fromJson(data, PlayerData.class);
    }
    public Item getTrackInfo(String id)
    {
        if(id.contains("track:"))
        {
            id = id.substring(id.lastIndexOf(':') + 1);
        }
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/tracks/" + id)
                .method("GET", null)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + oAuthToken.getAccess_token())
                .build();
        try {
            Gson gson = new Gson();
            return gson.fromJson(client.newCall(request).execute().body().string(), Item.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public SearchItem search(String q)
    {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/search?q="+q+"&type=track")
                .method("GET", null)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + oAuthToken.getAccess_token())
                .build();
        Gson son = new Gson();
        try {
            return son.fromJson(client.newCall(request).execute().body().string(), SearchItem.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public UserData getUserData()
    {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .method("GET", null)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + oAuthToken.getAccess_token())
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson son = new Gson();
        try {
            return son.fromJson(response.body().string(), UserData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
