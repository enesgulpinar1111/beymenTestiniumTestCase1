package pages;

import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import java.util.List;
import static utils.BaseWD.bekle;
import static utils.BaseWD.waitUntilElementVisible;


public class SepetSayfasi {
    private WebDriver driver;
    @FindBy(css = ".m-orderSummary__item.-grandTotal .m-orderSummary__value")
    private WebElement toplamFiyat;

    @FindBy(id = "quantitySelect0-key-0")
    private WebElement miktarMenu;

    @FindBy(id = "removeCartItemBtn0-key-0")
    private WebElement bosSepetElement;

    @FindBy(id = "emtyCart")
    private WebElement sepetBosBilgiKutusu;

    public SepetSayfasi(WebDriver driver) {
            this.driver = driver;

            PageFactory.initElements(driver, this);
        }



    public String fiyatAyrisimi() {
        return toplamFiyat.getText().split(" ")[0];
    }


    public boolean miktarArtirma(int miktar) {
        Select select = new Select(miktarMenu);
        List<WebElement> options = select.getOptions();


        if (stokMevcutMu(miktar, options)) {
            select.selectByValue(String.valueOf(miktar));
            bekle(2);
            return true;
        } else {
            throw new NotFoundException();
        }

    }

    private boolean stokMevcutMu(int miktar, List<WebElement> options) {
        for (WebElement option : options) {
            if (String.valueOf(miktar).equals(option.getAttribute("value"))) {
                return true;
            }
        }
        return false;
    }

    public void sepetBos() {

        waitUntilElementVisible(driver, bosSepetElement).click();
        bekle(2);

    }

    public boolean isDisplayedSepet() {
        return waitUntilElementVisible(driver, sepetBosBilgiKutusu).isDisplayed();
    }

    public String secilenMiktariAl() {
        Select select = new Select(bosSepetElement);
        bekle(2);
        return select.getFirstSelectedOption().getAttribute("value");
    }
}