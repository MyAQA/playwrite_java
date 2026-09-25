package tests;

import base.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import static utils.Constants.baseUrl;

@Slf4j
public class PlaceholderMain extends BaseTest  {

    @Test
    public void checkPageTitle() {
        log.info("Starting test");
        getPage().navigate(baseUrl);
        Assert.assertEquals(getPage().title(), "demosite");
    }

}
