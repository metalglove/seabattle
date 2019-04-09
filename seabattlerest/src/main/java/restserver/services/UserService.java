package restserver.services;

import domain.User;
import dtos.requests.LoginRequestDto;
import dtos.requests.RegisterRequestDto;
import dtos.responses.LoginResponseDto;
import dtos.responses.RegisterResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import restserver.interfaces.IUserService;
import restserver.repositories.UserRepository;

import javax.crypto.SecretKeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import domain.cryptography.PBKDF2;

@Service
@Qualifier("userService")
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResultDto = new LoginResponseDto(null,null, false);
        User user = userRepository.findUserByName(loginRequestDto.getUsername());
        if (user != null) {
            SecretKeyFactory skf;
            try {
                skf = SecretKeyFactory.getInstance(PBKDF2.ALGORITHM);
                boolean correct = PBKDF2.verifyPassword(skf, hexStringToByteArray(user.getSecret()), loginRequestDto.getSecret(), hexStringToByteArray(user.getSalt()));
                if (correct)
                    return new LoginResponseDto(user.getId(), user.getName(), true);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
                return loginResultDto;
            }
        }
        return loginResultDto;
    }

    @Override
    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) {
        if (userRepository.findUserByName(registerRequestDto.getUsername()) != null) {
            return new RegisterResponseDto(-1, null, false, "The user already exists.");
        }
        User user = new User();
        user.setName(registerRequestDto.getUsername());
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2.ALGORITHM);
            byte[] salt = PBKDF2.generateSalt();
            byte[] hash = PBKDF2.calculateHash(skf, registerRequestDto.getSecret(), salt);
            user.setSecret(bytesToHex(hash));
            user.setSalt(bytesToHex(salt));
            User storedUser = userRepository.save(user);
            if (storedUser != null) {
                return new RegisterResponseDto(user.getId().intValue(), user.getName(), true, "Successfully registered!");
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return new RegisterResponseDto(-1, null, false, "Something happened, Try again later.");
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
