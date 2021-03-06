package fr.babuchon.crawler.model.site;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CeSoirTvTest {

    private CeSoirTv ceSoir;

    @BeforeEach
    void setUp() {
        ceSoir = new CeSoirTv();
    }

    @Test
    void trickImageUrlTest() {
        System.out.println(ceSoir.trickImageUrl("https://tel.img.pmdstatic.net/fit/http.3A.2F.2Fimages.2Eone.2Eprismamedia.2Ecom.2Fprogram.2Fe.2F9.2Fc.2Fa.2F8.2F3.2F3.2F9.2Fa.2F1.2F4.2F4.2F3.2Fc.2F4.2Fc.2Ejpg/300x200/background-color/FFFFFF/the-voice-la-plus-belle-voix.jpg"));
        assertEquals("http://images.one.prismamedia.com/program/0/8/2/b/d/7/8/4/e/e/5/7/b/4/4/8.jpg", ceSoir.trickImageUrl("https://tel.img.pmdstatic.net/fit/http.3A.2F.2Fimages.2Eone.2Eprismamedia.2Ecom.2Fprogram.2F0.2F8.2F2.2Fb.2Fd.2F7.2F8.2F4.2Fe.2Fe.2F5.2F7.2Fb.2F4.2F4.2F8.2Ejpg/1160x500/crop-from/top/la-carte-aux-tresors.jpg"));
    }
}