package pages;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import java.util.Random;

import static utils.BaseWD.bekle;

public class Anasayfa {
    private WebDriver driver;

    @FindBy(className = "o-header__search--input")
    public WebElement aramaKutusuElement;

    @FindBy(id = "o-searchSuggestion__input")
    public WebElement oneriKutusu;

    @FindBy(className = "o-header__search--close")
    private WebElement clearButton;

    @FindBy(className = "m-productImageList")
    private List<WebElement> urunlerElement;

    @FindBy(xpath = "//*[@id='onetrust-accept-btn-handler']")
    private WebElement acceptCookiesButton;

    @FindBy(className = "o-modal__closeButton")
    private WebElement cinsiyetSecimiKapama;

    @FindBy(className = "dn-slide-deny-btn")
    private WebElement bildirimiKapaButonu;

    WebDriverWait wait;

    public Anasayfa(WebDriver driver) {
        this.driver = driver;
        driver.get("https://www.beymen.com");
        driver.manage().window().maximize();
        PageFactory.initElements(driver, this);

    }


    public void cerezlerVeAcilirPencerelereTiklama() {
        acceptCookiesButton.click();
        bekle(2);
        cinsiyetSecimiKapama.click();
        bekle(2);

        try {

        } catch (TimeoutException e) {
            System.out.println("Görünür olması beklenirken timeout oluştu.");
        }
    }

    public void RastgeleUruneTiklama() {
        Random random = new Random();
        int randomIndex = random.nextInt(urunlerElement.size());
        urunlerElement.get(randomIndex).click();
        bekle(2);
    }

    public boolean sayfaAcildiMi() {
        String beklenenTitle = "Beymen.com – Türkiye’nin Tek Dijital Lüks Platformu";
        return driver.getTitle().contains(beklenenTitle);
    }

    public void aramaKutusunuEtkinlestir() {
        aramaKutusuElement.click();
    }

    public void urunAdiArama(String urunAdiElement) {
        oneriKutusu.sendKeys(urunAdiElement);
    }

    public void clearSearchBox() {
        clearButton.click();
    }
}