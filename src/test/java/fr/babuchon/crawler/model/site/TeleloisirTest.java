package fr.babuchon.crawler.model.site;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeleloisirTest {

    private Teleloisir teleLoisir;

    @BeforeEach
    void setUp() {
        teleLoisir = new Teleloisir();
    }

    @Test
    void trickImageUrlTest() {
        assertEquals("http://images.one.prismamedia.com/program/b/b/e/7/e/9/c/4/5/2/5/f/b/0/e/e.jpg", teleLoisir.trickImageUrl("https://tel.img.pmdstatic.net/fit/http.3A.2F.2Fimages.2Eone.2Eprismamedia.2Ecom.2Fprogram.2Fb.2Fb.2Fe.2F7.2Fe.2F9.2Fc.2F4.2F5.2F2.2F5.2Ff.2Fb.2F0.2Fe.2Fe.2Ejpg/1160x500/crop-from/top/esprits-criminels.jpg"));
    }
}