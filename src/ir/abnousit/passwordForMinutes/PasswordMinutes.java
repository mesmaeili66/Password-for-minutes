package ir.abnousit.passwordForMinutes;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Mahdi Esmaeili
 */
public class PasswordMinutes {

    private Integer minutesPassswordBeAvailable = 5;
    private HashMap<Integer, List> lengthPattern = new HashMap<Integer, List>();

    private String generatePasswordMinutes(Timestamp timeStamp) throws CloneNotSupportedException, NoSuchAlgorithmException {
        String minuteFormatted = new SimpleDateFormat("yyyyMMddHHmm").format(timeStamp);
        String encryptedPassword = encryptPassword(minuteFormatted);
        return encryptedPassword;
    }

    private String encryptPassword(String password) throws NoSuchAlgorithmException, CloneNotSupportedException {
        /*Do more to encrypt*/
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String enPassword = (new HexBinaryAdapter()).marshal(md5.digest(password.getBytes()));
//        System.out.println(password + " *** " + enPassword);
        return enPassword;
    }

    public String generatePassword() throws NoSuchAlgorithmException, CloneNotSupportedException {
        return generatePasswordMinutes(new Timestamp(new Date().getTime()));
    }

    public String generateMiddlePassword() throws NoSuchAlgorithmException, CloneNotSupportedException {
        String rawPass = generatePasswordMinutes(new Timestamp(new Date().getTime()));
        List<Integer> pattern = generatePattern(8);
        String password = generatePasswordByPattern(rawPass, pattern);
        return password;
    }

    public String generateShortPassword() throws NoSuchAlgorithmException, CloneNotSupportedException {
        String rawPass = generatePasswordMinutes(new Timestamp(new Date().getTime()));
        List<Integer> pattern = generatePattern(5);
        String password = generatePasswordByPattern(rawPass, pattern);
        return password;
    }

    private String generatePasswordByLength(Integer length) throws NoSuchAlgorithmException, CloneNotSupportedException {
        if (length > 12) length = 12;
        String rawPass = generatePasswordMinutes(new Timestamp(new Date().getTime()));
        List<Integer> pattern = generatePattern(length);
        String password = generatePasswordByPattern(rawPass, pattern);
        return password;
    }

    public String generatePassword(String timeStamp) throws NoSuchAlgorithmException, CloneNotSupportedException {
        return encryptPassword(timeStamp);
    }

    public Boolean validatePassword(String password) throws NoSuchAlgorithmException, CloneNotSupportedException {
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < minutesPassswordBeAvailable; i++) {
            if (i != 0) calendar.add(Calendar.MINUTE, -1);
            String loopPassword = password.length() > 12 ?
                    generatePasswordMinutes(new Timestamp(calendar.getTime().getTime())) :
                    generatePasswordByPattern(generatePasswordMinutes(new Timestamp(calendar.getTime().getTime())), lengthPattern.get(password.length()));
            if (loopPassword.equals(password)) return true;
        }
        return false;
    }

    private List<Integer> generatePattern(Integer length) {
        if (!lengthPattern.containsKey(length)) {
            List<Integer> patternArray = new ArrayList<Integer>();
            while (patternArray.size() < length) {
                Long randomIndex = Math.round(Math.random() * 32);
                patternArray.add(Integer.parseInt(randomIndex.toString()));
            }
            lengthPattern.put(length, patternArray);
            return patternArray;
        }
        return lengthPattern.get(length);
    }

    private String generatePasswordByPattern(String password, List<Integer> arrayPattern) {
        StringBuilder passwordInner = new StringBuilder();
        for (int i : arrayPattern) {
            passwordInner.append(password.substring(i, i + 1));
        }
        return passwordInner.toString();
    }

    public Integer getMinutesPassswordBeAvailable() {
        return minutesPassswordBeAvailable;
    }

    public void setMinutesPassswordBeAvailable(Integer minutesPassswordBeAvailable) {
        this.minutesPassswordBeAvailable = minutesPassswordBeAvailable;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, CloneNotSupportedException {
        PasswordMinutes pm = new PasswordMinutes();
//        System.out.println(pm.validatePassword(pm.generatePassword("201307221337")));
        String pass = pm.generatePasswordByLength(20);
        System.out.println(pass);
        System.out.println(pm.validatePassword(pass));

    }
}
