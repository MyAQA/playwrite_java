package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import static utils.Constants.baseUrl;

public class PlaceholderMain extends BaseTest  {

    @Test
    public void checkPageTitle() {
        getPage().navigate(baseUrl);
        Assert.assertEquals(getPage().title(), "demosite");
    }

}
