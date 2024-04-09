package com.zaza.cleanerexceptionsbot.commands.impl;

import com.zaza.cleanerexceptionsbot.commands.Command;
import com.zaza.cleanerexceptionsbot.sender.TelegramSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;

import java.util.List;
import java.util.Random;

@Component
@Slf4j
@RequiredArgsConstructor
public class PayCommand implements Command<Long> {

    private final TelegramSender telegramSender;

    @Value("${payment.token}")
    private String PAYMENT_TOKEN;

    @Override
    public void execute(Long chatId) {
        sendInvoice(chatId);
        log.info("Sent message with payment to: " + chatId);
    }

    private void sendInvoice(Long chatId) {
        SendInvoice sendInvoice = new SendInvoice();
        LabeledPrice product = new LabeledPrice("Месячная подписка", 60000);
        sendInvoice.setChatId(chatId);
        sendInvoice.setCurrency("RUB");
        sendInvoice.setPrices(List.of(product));
        sendInvoice.setTitle("Подписка на сервис");
        sendInvoice.setDescription("Месячная подписка на сервис очистки дисков");
        Random random = new Random();
        int number = random.nextInt(900000) + 100000;
        sendInvoice.setPayload(String.valueOf(number));
        sendInvoice.setProviderToken(PAYMENT_TOKEN);

        telegramSender.sendInvoice(sendInvoice);
    }
}
