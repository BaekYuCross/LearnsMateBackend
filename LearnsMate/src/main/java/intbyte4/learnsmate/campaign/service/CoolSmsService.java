package intbyte4.learnsmate.campaign.service;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoolSmsService {
    private final DefaultMessageService messageService;

    public void sendSms(String to, String text) {
        Message message = new Message();
        message.setFrom("01045901841");
        message.setTo(to);
        message.setText(text
                +"\n더 자세한 사항은 LearnsBuddy 홈페이지를 방문해 주세요.\n\n"
                + "감사합니다.\n\n"
                + "LearnsBuddy 드림");

        this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}
