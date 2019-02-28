package fr.babuchon.crawler.model.site;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Tele7Test {

    private Tele7 t7;

    @BeforeEach
    void setUp() {
        t7 = new Tele7();
    }

    @Test
    void trickImageUrlTest() {
        
        assertEquals("https://resize.programme-television.ladmedia.fr/img/var/imports/agtv/0/7/9/2139362970_43.jpg" , t7.trickImageUrl("https://resize.programme-television.ladmedia.fr/r/193,149,forcex,center-middle/img/var/imports/agtv/0/7/9/2139362970_43.jpg"));

    }
}