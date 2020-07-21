package spotifyAPI;

import com.google.gson.Gson;
import exception.SpotifyException;
import interfaces.SpotifyPlayerAPI;
import model.Artist;
import model.Item;
import model.PlayerData;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.IOException;
import java.util.List;

public class SpotWebSkimmer implements SpotifyPlayerAPI {
    private WebDriver driver;
    private FirefoxOptions options;
    private String oAuthToken;
    OkHttpClient client;
    Request request;
    public SpotWebSkimmer(String user, String password)
    {
        if(System.getProperty("os.name").startsWith("Mac"))
            System.setProperty("webdriver.gecko.driver", getClass().getResource("/spotifyAPI/geckodriver").getPath());
        else if(System.getProperty("os.name").startsWith("Windows"))
            System.setProperty("webdriver.gecko.driver", "C:\\Users\\navee\\Google Drive\\Idea\\Flow\\src\\windows\\geckodriver.exe");
        else
            System.setProperty("webdriver.gecko.driver", getClass().getResource("/linux/geckodriver").getPath());
        options = new FirefoxOptions();
        //options.setBinary(getClass().getResource("/Users/naveen/Documents/Flow/Firefox.app/Contents/MacOS/firefox").getPath());
        options.setHeadless(true);
       // options.setBinary(getClass().getResource("/Firefox.app/Contents/MacOS/firefox").getPath());

        oAuthToken = getOauthToken(user, password).trim();
        client = new OkHttpClient().newBuilder().build();
        request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/player")
                .method("GET", null)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + oAuthToken)
                .build();
        // 3480000 = 58 minutes
        //The token expires every 60 minutes so its refreshed every 58 minutes
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3480000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                oAuthToken = getOauthToken(user, password).trim();
                client = new OkHttpClient().newBuilder().build();
                request = new Request.Builder()
                        .url("https://api.spotify.com/v1/me/player")
                        .method("GET", null)
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer " + oAuthToken)
                        .build();
            }
        }).start();
        //
        //
        System.out.println(oAuthToken);
    }
    private String getOauthToken(String user, String pass)
    {
        driver = new FirefoxDriver(options);
        driver.get("https://developer.spotify.com/console/get-user-player/?market=&additional_types=");
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/main/article/div[3]/div/div/form/div[3]/div/span/button")).click();
        /* try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
         driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/main/article/div[3]/div/div/div[1]/div/div/div[2]/form/div[2]/div/div[13]/div/label/span")).click();

        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/main/article/div[3]/div/div/div[1]/div/div/div[2]/form/div[2]/div/div[5]/div/label/span")).click();
         /*

        for(WebElement e: driver.findElements(By.className("form-scope")))
            e.click();*/
        getElement(By.xpath("/html/body/div[1]/div[2]/div/main/article/div[3]/div/div/div[1]/div/div/div[2]/form/div[1]/div/div/div/div/label/span")).click();
        driver.findElement(By.id("oauthRequestToken")).click();
        driver.findElement(By.id("login-username")).sendKeys(user);
        driver.findElement(By.id("login-password")).sendKeys(pass, Keys.ENTER);//syGo#1An22
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (isElement(By.id("auth-accept")))
            driver.findElement(By.id("auth-accept")).click();
        String ret =  getElement(By.id("oauth-input")).getAttribute("value");
        driver.quit();
        return ret;
    }
    private WebElement getElement(By by)
    {
        while (!isElement(by))
        {}
        return driver.findElement(by);
    }
    private boolean isElement(By by)
    {
        try
        {
            driver.findElement(by);
            return true;
        }catch (NoSuchElementException e)
        {
            return false;
        }
    }

    @Override
    public boolean isSingle() {
        return false;
    }


    @Override
    public void setVolume(int am) {

    }

    @Override
    public void playPause() {
        try {
            if(isPlaying())
                pause();
            else
                play();
        }catch (SpotifyException e)
        {

        }
    }

    @Override
    public void play() {
    }

    @Override
    public void pause() {
    }

    @Override
    public long getPlayBackPosition() throws SpotifyException {
        return getPlayerData().getProgress_ms();
    }

    @Override
    public void setPlayBackPosition(long pos) {

    }

    @Override
    public long getDuration() throws SpotifyException{
       return getPlayingTrack().getDuration_ms();
    }

    @Override
    public int getVolume() throws SpotifyException{
       return getPlayerData().getDevice().getVolume_percent();
    }

    @Override
    public boolean isPlaying() throws SpotifyException{
        return getPlayerData().is_playing();
    }

    @Override
    public void nextTrack() {

    }

    @Override
    public void previousTrack() {
    }

    @Override
    public List<Artist> getTrackArtists() {
        return getPlayingTrack().getArtists();
    }

    @Override
    public String getTrackAlbum() {
        return getPlayingTrack().getAlbum().getName();
    }

    @Override
    public String getTrackUri() {
        return getPlayingTrack().getUri();
    }

    @Override
    public String getTrackName() {
        return getPlayingTrack().getName();
    }

    @Override
    public Item getPlayingTrack() {
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
                .addHeader("Authorization", "Bearer " + oAuthToken)
                .addHeader("Cookie", "sp_t=e780e37cec7d9c68eb85bda1a53c77a1")
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public PlayerData getPlayerData()
    {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/player")
                .method("GET", null)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + oAuthToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Gson son = new Gson();
            return son.fromJson(response.body().string(), PlayerData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
