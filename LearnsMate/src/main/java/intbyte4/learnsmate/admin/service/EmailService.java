package intbyte4.learnsmate.admin.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class EmailService {

    private JavaMailSender mailSender;
    private StringRedisTemplate stringRedisTemplate;  // StringRedisTemplate 사용

    @Autowired
    public EmailService(StringRedisTemplate stringRedisTemplate, JavaMailSender mailSender) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.mailSender = mailSender;
    }

    private final long VERIFICATION_CODE_TTL = 5; // 5분

    public void sendVerificationEmail(String email) {
        log.info("start sending verification email");
        String verificationCode = generateVerificationCode();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[LearnsMate] 이메일 인증 코드 안내");
        message.setText("안녕하세요,\n\n"
                + "LearnsMate를 이용해 주셔서 진심으로 감사드립니다.\n"
                + "아래의 인증 코드를 입력하여 이메일 인증을 완료해 주시기 바랍니다.\n\n"
                + "인증 코드: " + verificationCode + "\n\n"
                + "※ 인증 코드는 발송 후 5분간만 유효합니다.\n\n"
                + "본 이메일은 LearnsMate 서비스 이용과 관련하여 발송되었습니다. "
                + "궁금하신 사항이 있으시면 LearnsMate 고객 지원팀으로 언제든 문의해 주시기 바랍니다.\n\n"
                + "감사합니다.\n\n"
                + "LearnsMate 드림");

        mailSender.send(message);

        saveVerificationCode(email, verificationCode);
    }

    //필기. 해당 이메일의 코드가 일치하는지 확인하는 코드
    public boolean verifyCode(String email, String code) {
        String savedCode = stringRedisTemplate.opsForValue().get(email);  //필기. Redis에서 코드 가져오기

        // 필기. 인증 코드가 일치하면 Redis에서 해당 키값(email)을 True로 설정
        if (savedCode != null && savedCode.equals(code)) {
            // 인증 성공 시 Redis에 True 저장하고 TTL을 1시간으로 설정
            stringRedisTemplate.opsForValue().set(email, "True", 1, TimeUnit.HOURS);
            return true;
        } else {
            return false;
        }
    }

    //필기. Redis에 코드 저장
    private void saveVerificationCode(String email, String code) {
        stringRedisTemplate.opsForValue().set(email, code, VERIFICATION_CODE_TTL, TimeUnit.MINUTES);
    }

    //필기. 6자리 난수 발생 (0~999999)
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(999999);
        return String.format("%06d", code);
    }
}