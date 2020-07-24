package spotifyAPI;

import model.Item;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import javax.swing.*;
import java.io.IOException;

public class WinSpotifyAPI extends SpotifyWebAPI{
    ChromeDriver driver;
    public WinSpotifyAPI()
    {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\navee\\Documents\\WinSpotify\\src\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        //  options.setBinary("C:\\\\Program Files (x86)\\\\BraveSoftware\\\\Brave-Browser\\\\Application\\\\brave.exe");
        options.addArguments("--app=https://accounts.spotify.com/en/login?continue=https:%2F%2Fopen.spotify.com%2F");
        // options.addArguments("--headless");
         driver = new ChromeDriver(options);
        driver.findElement(By.id("login-username")).sendKeys("naveen.box2@gmail.com", Keys.TAB,
                "Nothing234", Keys.ENTER);
    }

    @Override
    public void play() {
        WebElement e = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[3]/footer/div[1]/div[2]/div/div[1]/div[3]"));
        e = e.findElements(By.xpath(".//*")).get(0);
        if(e.getAttribute("title").startsWith("Pl"))
            e.click();
    }

    @Override
    public void pause() {
        WebElement e = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[3]/footer/div[1]/div[2]/div/div[1]/div[3]"));
        e = e.findElements(By.xpath(".//*")).get(0);
        if(e.getAttribute("title").startsWith("Pa"))
            e.click();
    }

    @Override
    public void setPlayBackPosition(long pos) {
        WebElement sliderSize = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[3]/footer/div/div[2]/div/div[2]/div[2]/div"));
        WebElement slider = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[3]/footer/div/div[2]/div/div[2]/div[2]/div/button"));
        int width = sliderSize.getSize().getWidth();
        System.out.println(width);
        Actions SliderAction = new Actions(driver);
        try {
            pos = pos - getPlayBackPosition();
            double percent = (double)pos/getDuration();
            SliderAction.clickAndHold(slider).moveByOffset((int)Math.ceil(width * percent), 0).release().perform();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean playTrack(String id) {
        driver.get("https://open.spotify.com/track/" + id.substring(id.lastIndexOf(':')+1));
        return true;
    }

    @Override
    public boolean playTrack(Item song) {
        playTrack(song.getUri());
        return false;
    }

    public static void main(String[] args) {
        WinSpotifyAPI api = new WinSpotifyAPI();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        api.setPlayBackPosition(1000);
    }
}
