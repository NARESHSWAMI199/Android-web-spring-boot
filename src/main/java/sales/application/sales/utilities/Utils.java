package sales.application.sales.utilities;

import sales.application.sales.exceptions.MyException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

        public static Long getCurrentMillis(){
            long millis = new java.util.Date().getTime();
            return millis;
        }

        public static String mobileRegex = "^(?=(?:[8-9]){1})(?=[0-9]{8}).*";

        public static String getMillisToDate(Long millis){
            DateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
            Date date = new Date(millis);
            return format.format(date);
        }


        public static boolean isEmpty(String string){
            return (string ==null || string.trim().equals(""));
        }


        public static String toTitleCase(String value){
            if(Utils.isEmpty(value)) return value;
            return value.substring(0,1).toUpperCase() + value.substring(1,value.length());
        }


        public static String aesEncrypt(String Data, String secretKey) {
            try {
                Key key = new SecretKeySpec(secretKey.getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES"); // Default uses ECB PKCS5Padding
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] encVal = cipher.doFinal(Data.getBytes());
                String encryptedValue = java.util.Base64.getEncoder().encodeToString(encVal);
                return encryptedValue;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error while encrypting: " + e.getMessage());
            }
            return null;
        }

        public static String aesDecrypt(String strToDecrypt, String secretKey) {
            try {
                Key key = new SecretKeySpec(secretKey.getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, key);
                return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error while encrypting: " + e.getMessage());
            }
            return null;
        }

        public static boolean isValidImage(String image){
            String IMAGE_PATTERN =
                    "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
            Pattern pattern =  Pattern.compile(IMAGE_PATTERN);
            Matcher matcher = pattern.matcher(image);
            return matcher.matches();
        }


        public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        public static boolean isValidEmail(String emailStr) {
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
            return matcher.matches();
        }

        public static void mobileAndEmailValidation(String email ,String contact,String errorMessage) throws MyException {
            if (Utils.isEmpty(contact) || !contact.matches(Utils.mobileRegex)) throw new MyException(errorMessage.replaceAll("_","mobile number") +  " ["+contact+"]") ;
            if (Utils.isEmpty(email) || !isValidEmail(email)) throw new MyException(errorMessage.replaceAll("_","email address") + " ["+email+"]") ;
        }





    public static String isValidName(final String name) throws MyException {
        String NAME_PATTERN = "^[a-zA-Z](?=.{1,100}$)[A-Za-z_& ]*(?:\\h+[A-Z][A-Za-z]*)*$";
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(name);
        System.out.println(matcher.matches());
        if(!matcher.matches()){
            String message ="";
            String neededSyntax = "First latter should be in upper case (optional), " +
                    "and not a number and special symbols like : ^*$+?[]()| are not allowed.";
                message = "Not a valid username";
                System.out.println(message);
                throw  new MyException(message + " "+neededSyntax  );
            }
        return name;
    }

}




