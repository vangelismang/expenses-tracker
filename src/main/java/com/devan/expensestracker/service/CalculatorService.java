package com.devan.expensestracker.service;

import com.devan.expensestracker.model.ExpensesRecord;
import com.devan.expensestracker.model.SalaryRecord;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;

@Service
@Getter
@Slf4j
public class CalculatorService {

    @Value(value = "${email}")
    private String email_id;

    @Value(value = "${password}")
    private String password;

    @Value(value = "${sender:}")
    private String senderAddress;

    private final ServiceBase<ExpensesRecord, Long> expensesService;

    private final ServiceBase<SalaryRecord, Long> salaryService;

    public CalculatorService(@Qualifier("expenses") ServiceBase<ExpensesRecord, Long> expensesService,
                             @Qualifier("salary") ServiceBase<SalaryRecord, Long> salaryService) {
        this.expensesService = expensesService;
        this.salaryService = salaryService;
    }

    public void calculate() {

        //set properties
        Properties properties = new Properties();
        //You can use imap or imaps , *s -Secured
        properties.put("mail.store.protocol", "imaps");
        //Host Address of Your Mail
        properties.put("mail.imaps.host", "imap.gmail.com");
        //Port number of your Mail Host
        properties.put("mail.imaps.port", "993");

        try {


            //create a session
            Session session = Session.getDefaultInstance(properties, null);
            //SET the store for IMAPS
            Store store = session.getStore("imaps");
            System.out.println("Connection initiated......");
            //Trying to connect IMAP server
            store.connect(email_id, password);
            System.out.println("Connected to " + email_id);
            //Get inbox folder
            Folder inbox = store.getFolder("inbox");
            //SET readonly format (*You can set read and write)
            inbox.open(Folder.READ_ONLY);

            Message[] salaryMessages = setCriteria(salaryService.findLastUpdateDate(), inbox);

            for (Message message : salaryMessages) {
                if (message.getSubject().charAt(0) == 'Μ' && message.getSubject().charAt(2) == 'Σ') {
                    String content = message.getContent().toString();

                    int index = content.lastIndexOf("(ΠΙ)");
                    content = content.substring(index - 15, index - 4).replaceAll("[^\\d.]", "");

                    SalaryRecord salaryRecord = new SalaryRecord();
                    salaryRecord.setAmount(BigDecimal.valueOf(Double.parseDouble(content)));
                    salaryRecord.setDate(message.getSentDate().toInstant());
                    salaryService.create(salaryRecord);
                }
            }

            Message[] expensesMessages = setCriteria(expensesService.findLastUpdateDate(), inbox);

            for (Message message : expensesMessages) {
                if (message.getSubject().charAt(0) == 'Α' && message.getSubject().charAt(1) == 'Γ') {
                    ExpensesRecord expensesRecord = new ExpensesRecord();
                    expensesRecord.setAmount(BigDecimal.valueOf(
                            Double.parseDouble(message.getSubject().replaceAll("[^\\d.]", ""))));
                    expensesRecord.setDate(message.getSentDate().toInstant());
                    expensesService.create(expensesRecord);
                }

            }

            inbox.close(true);
            store.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Message[] setCriteria(Instant start, Folder folder) throws MessagingException {
        SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yy");
        Date startDate = Date.from(start);
        Date finDate = Date.from(Instant.now());
        SearchTerm sender = new FromTerm(new InternetAddress(senderAddress));
        SearchTerm fromTerm = new ReceivedDateTerm(ComparisonTerm.GT, startDate);
        SearchTerm toTerm = new ReceivedDateTerm(ComparisonTerm.LT, finDate);
        AndTerm term = new AndTerm(fromTerm, toTerm);
        AndTerm finalTerm = new AndTerm(term, sender);

        return folder.search(finalTerm);
    }
}
