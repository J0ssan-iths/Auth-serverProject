package se.iths.josefine.authserverproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class AuthServerProjectApplication {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        SpringApplication.run(AuthServerProjectApplication.class, args);
//
//        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
//        generator.initialize(2048);
//        KeyPair keyPair = generator.generateKeyPair();
//        String privateKey = Base64.getEncoder()
//                .encodeToString(keyPair.getPrivate().getEncoded());
//        String publicKey = Base64.getEncoder()
//                .encodeToString(keyPair.getPublic().getEncoded());
//        System.out.println("Private key att kopiera:");
//        System.out.println(privateKey);
//        System.out.println("Public key att kopiera:");
//        System.out.println(publicKey);
    }

}
