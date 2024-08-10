package tests;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import utils.BaseWD;
import utils.ExcelReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.SepetSayfasi;
import pages.Anasayfa;
import pages.UrunSayfasi;


public class SenaryoTest extends BaseWD {

    Anasayfa anasayfa;
    UrunSayfasi urunSayfasi;

    SepetSayfasi sepetSayfasi;
    WebDriver driver;

    private static String filePath = "C:/Users/enesg/IdeaProjects/beymendeneme000001/src/test/java/Resources/excelTablo01.xlsx";
    private static String finalPrice;
    private static String selectedProduct;
    private static int retryCount = 1;

    boolean success;
    WebDriverWait wait;


    @Before
    public void driverSetup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        anasayfa = new Anasayfa(driver);
        sepetSayfasi = new SepetSayfasi(driver);
        urunSayfasi = new UrunSayfasi(driver);

    }

    @Test
    public void fullTest() {
        bekle(2);
        anasayfa.cerezlerVeAcilirPencerelereTiklama();
        Assert.assertTrue(anasayfa.sayfaAcildiMi());
        urunleriArama();

        while (!success) {
            try {
                urunHareketleri();
                sepetHareketleri();
                success = true;
            } catch (Exception e) {
                if (retryCount < 3) {

                    sepetBosTekrarla();
                } else {
                    System.out.println("Error: Sepet işlemleri " + retryCount + " tekrar deneniyor.");
                    success = true;
                }
            }
        }
    }


    public void sepetBosTekrarla() {
        System.out.println(selectedProduct + " yeni ürün deneniyor.");
        retryCount++;

        bekle(2);

        sepetSayfasi.sepetBos();

        bekle(3);

        driver.navigate().back();
        driver.navigate().back();
    }


    private void urunHareketleri() {

        anasayfa.RastgeleUruneTiklama();
        UrunBilgisiniDosyayaYaz();
        sepeteUrunEkleme();
        urunSayfasi.sepetimeGit();
        bekle(3);
        fiyatlarEsitMi();
    }


    private void sepetHareketleri() {
        int hedefMiktar = 2;
        sepetSayfasi.miktarArtirma(hedefMiktar);

        String miktarHataMesaji = "Error: Seçilen miktar hedef miktarla uyuşmuyor.";
        Assert.assertEquals(miktarHataMesaji, String.valueOf(hedefMiktar), sepetSayfasi.secilenMiktariAl());

        String sepetBosErrorMessage = "Error: Sepet boşaltılamadı";
        sepetSayfasi.sepetBos();
        Assert.assertEquals(sepetBosErrorMessage, true, sepetSayfasi.isDisplayedSepet());
    }


    private void fiyatlarEsitMi() {
        Assert.assertEquals(finalPrice, sepetSayfasi.fiyatAyrisimi());
    }

    private void sepeteUrunEkleme(){
        selectedProduct = urunSayfasi.urunAdiElement.getText();
        urunSayfasi.mevcutUruneTiklama();
        urunSayfasi.sepeteEkleButonunaTiklama();
        finalPrice = urunSayfasi.sonFiyat();
    }


    public void urunleriArama() {
        ExcelReader excelReader = new ExcelReader();

        String arama01 = excelReader.getExcelValue(filePath, 0, 0, 0);
        String arama02 = excelReader.getExcelValue(filePath, 0, 0, 1);
        anasayfa.aramaKutusunuEtkinlestir();
        anasayfa.urunAdiArama(arama01);
        bekle(2);

        WebElement noThanksButton = driver.findElement(By.xpath("//button[text()='No Thanks']"));
        noThanksButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(noThanksButton));
        bekle(2);


        anasayfa.clearSearchBox();
        anasayfa.urunAdiArama(arama02);
        anasayfa.oneriKutusu.sendKeys(Keys.ENTER);
        bekle(2);

    }


    public void UrunBilgisiniDosyayaYaz() {
        String urunBilgisi = urunSayfasi.urunAdiElement.getText();
        String urunFiyati = urunSayfasi.sonFiyat();

        try (FileWriter writer = new FileWriter("product.txt")) {
            writer.write("Ürün Adı: " + urunBilgisi + "\n");
            writer.write("Fiyatı: " + urunFiyati + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @After
    public void quitDriver() {
        bekle(2);
        driver.quit();
    }
}

