package app.auth.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class OTPService {

    private static final Integer EXPIRE_MINS = 15;
    private final LoadingCache<String,String> otpCache  = CacheBuilder.newBuilder().
            expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return null;
                }
            });

    public String generateOTP(String key){
        Random random = new Random();
        String otp = String.valueOf(1000 + random.nextInt(9000));
        otpCache.put(key, otp);
        return otp;
    }

    public String getOtp(String key){
        try{
            return otpCache.get(key);
        }catch (Exception e){
            log.info(e.getMessage());
            return null;
        }
    }

    public void clearOTP(String key){
        otpCache.invalidate(key);
    }
}

