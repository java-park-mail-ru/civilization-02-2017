package sample.auth.utils;


import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicLong;

public class AuthorizationUtils {
    private static Logger logger = LoggerFactory.getLogger(AuthorizationUtils.class);
    private static final String SALT = "BB081A278A0A6B452531752559C208C8B0E868DC2FD6EA3149C4036D3C9DCB8BD1D877824BBA287EF16F885FF357FD32AF7580F8641D460F3251C758711E5656";
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    @SuppressWarnings("all")
    @Nullable
    public static String generateSHA512(String passwordToHash) {
        String generatedPassword = null;
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(SALT.getBytes("UTF-8"));
            final byte[] bytes = md.digest(passwordToHash.getBytes("UTF-8"));
            final StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("No such algorithm!", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("Failed to encode UTF-8 while creating HASH", e);
        }
        return generatedPassword;
    }

    //Временное решение, в дальнейшем будем генерить ID в БД
    public static long generateUID() {
        return ID_GENERATOR.getAndIncrement();
    }
}
