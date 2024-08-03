package Utils;

public class PlaywrightWrapper extends BaseTest {


    public static String  emailId;

    public static String getRandomEmail(){
        emailId = "testAPIAutomation"+ System.currentTimeMillis() +"@gmail.com";
        return emailId;
    }
}
