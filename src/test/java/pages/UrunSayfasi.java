package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

import static utils.BaseWD.bekle;

public class UrunSayfasi {
    private WebDriver driver;

    @FindBy(className = "o-productDetail__description")
    public WebElement urunAdiElement;

    @FindBy(className = "m-price__new")
    public WebElement fullFiyat;

    @FindBy(className = "m-price__lastPrice")
    private WebElement indirimliFiyat;

    @FindBy(className = "m-variation")
    private WebElement urunSize;

    @FindBy(xpath = "//*[@id='addBasket']")
    private WebElement sepeteEkleButonu;

    public UrunSayfasi(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public static String parsePrice(String fiyat) {
        fiyat = fiyat.split(" ")[0];
        if (!fiyat.contains(",")) {
            return fiyat + ",00";
        }

        return fiyat;
    }

    private WebElement ilkMevcutSize() {
        List<WebElement> mevcutSize = urunSize.findElements(By.cssSelector("span:not(.m-variation__item.-disabled)"));
        return mevcutSize.get(0);
    }


    public void mevcutUruneTiklama()  {
        WebElement size = ilkMevcutSize();
        if (size != null) size.click();
        bekle(2);
    }

    public void sepeteEkleButonunaTiklama() {
        sepeteEkleButonu.click();
        bekle(2);
    }

    public void sepetimeGit() {
        driver.get("https://www.beymen.com/cart");
        bekle(2);
    }

    public String sonFiyat() {
        try {
            return mevcutDegilseVirgulEkle(indirimliFiyat.getText().split("\n")[1].trim());
        } catch (Exception e) {
            return mevcutDegilseVirgulEkle(fullFiyat.getText());
        }

    }

    private static String mevcutDegilseVirgulEkle(String a) {

        a = a.split(" ")[0];
        if (!a.contains(",")) {
            return a + ",00";
        }

        return a;
    }
}