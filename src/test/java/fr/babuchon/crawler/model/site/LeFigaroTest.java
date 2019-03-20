package fr.babuchon.crawler.model.site;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeFigaroTest {

    private LeFigaro figaro;

    @BeforeEach
    void setUp() {
        figaro = new LeFigaro();
    }

    @Test
    void TrickImageUrl() {
        assertEquals("http://i.f1g.fr/media/ext/5000x/api-tvmag.lefigaro.fr/img/000/251/25113974.jpg", figaro.trickImageUrl("http://i.f1g.fr/media/ext/1500x/api-tvmag.lefigaro.fr/img/000/251/25113974.jpg"));
    }

}