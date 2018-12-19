package fr.babuchon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigourous Test :-)
     */
	@Test
    public void testApp()
    {

        assertEquals("https://resize.programme-television.ladmedia.fr/img/var/imports/agtv/0/7/9/2139362970_43.jpg" , trickImageUrl("https://resize.programme-television.ladmedia.fr/r/193,149,forcex,center-middle/img/var/imports/agtv/0/7/9/2139362970_43.jpg"));
    }

    private String trickImageUrl(String imageUrl) {
        int start = imageUrl.lastIndexOf("/r");
        int end = imageUrl.indexOf("/img");
        if(start != -1 && end != -1) {
            String temp = "" + imageUrl;
            imageUrl = imageUrl.substring(0, start) + temp.substring(end, temp.length());
        }
        return imageUrl;
    }
}
