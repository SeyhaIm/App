package kh.com.kredit.kauthentications.Models.OtpModels;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class Totp {

    private final String secret;
    private final Clock clock;
    private static final int DELAY_WINDOW = 1;


    public Totp(String secret) {
        this.secret = secret;
        clock = new Clock();
    }

    public Totp(String secret, Clock clock) {
        this.secret = secret;
        this.clock = clock;
    }

    public String now() {
        return leftPadding(hash(secret, clock.getCurrentInterval()));
    }


    public boolean verify(String otp) {

        long code = Long.parseLong(otp);
        long currentInterval = clock.getCurrentInterval();

        int pastResponse = Math.max(DELAY_WINDOW, 0);

        for (int i = pastResponse; i >= 0; --i) {
            int candidate = generate(this.secret, currentInterval - i);
            if (candidate == code) {
                return true;
            }
        }
        return false;
    }

    private int generate(String secret, long interval) {
        return hash(secret, interval);
    }

    private int hash(String secret, long interval) {
        byte[] hash = new byte[0];
        try {
            //Base32 encoding is just a requirement for google authenticator. We can remove it on the next releases.
            hash = new Hmac(Hash.SHA1, Base32.decode(secret), interval).digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (Base32.DecodingException e) {
            e.printStackTrace();
        }
        return bytesToInt(hash);
    }

    private int bytesToInt(byte[] hash) {
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) |
                ((hash[offset + 1] & 0xff) << 16) |
                ((hash[offset + 2] & 0xff) << 8) |
                (hash[offset + 3] & 0xff);

        return binary % Digits.SIX.getValue();
    }


    private String leftPadding(int otp) {
        return String.format("%06d", otp);
    }

    public int getRemainingSeconds() {
        Calendar c = Calendar.getInstance();
        if (c.get(Calendar.SECOND) < 30) {
            int in = 30 - c.get(Calendar.SECOND);
            return in;
        } else {
            int se = c.get(Calendar.SECOND) - 29;
            int in = 30 - se;
            return in;
        }
    }
}
