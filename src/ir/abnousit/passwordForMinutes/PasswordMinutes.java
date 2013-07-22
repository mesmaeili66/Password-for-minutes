package ir.abnousit.passwordForMinutes;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Mahdi Esmaeili
 */
public class PasswordMinutes {

    private Integer minutesPassswordBeAvailable = 5;

    private String generatePasswordMinutes(Timestamp timeStamp) throws CloneNotSupportedException, NoSuchAlgorithmException {
        String minuteFormatted = new SimpleDateFormat("yyyyMMddHHmm").format(timeStamp);
        String encryptedPassword = encryptPassword(minuteFormatted);
        return encryptedPassword;
    }

    private String encryptPassword(String password) throws NoSuchAlgorithmException, CloneNotSupportedException {
        /*Do more to encrypt*/
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String enPassword = (new HexBinaryAdapter()).marshal(md5.digest(password.getBytes()));
        System.out.println(password + " *** " + enPassword);
        return enPassword;
    }

    public String generatePassword() throws NoSuchAlgorithmException, CloneNotSupportedException {
        return generatePasswordMinutes(new Timestamp(new Date().getTime()));
    }

    public String generatePassword(String timeStamp) throws NoSuchAlgorithmException, CloneNotSupportedException {
        return encryptPassword(timeStamp);
    }

    public Boolean validatePassword(String password) throws NoSuchAlgorithmException, CloneNotSupportedException {
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < minutesPassswordBeAvailable; i++) {
            if (i != 0) calendar.add(Calendar.MINUTE, -1);
            String loopPassword = generatePasswordMinutes(new Timestamp(calendar.getTime().getTime()));
            if (loopPassword.equals(password)) return true;
        }
        return false;
    }

    public Integer getMinutesPassswordBeAvailable() {
        return minutesPassswordBeAvailable;
    }

    public void setMinutesPassswordBeAvailable(Integer minutesPassswordBeAvailable) {
        this.minutesPassswordBeAvailable = minutesPassswordBeAvailable;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, CloneNotSupportedException {
        PasswordMinutes pm = new PasswordMinutes();
        System.out.println(pm.validatePassword(pm.generatePassword("201307221337")));
    }
}
