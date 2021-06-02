package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.dto.Report;
import com.gmail.yuliakazachok.corebanking.entities.Credit;
import com.gmail.yuliakazachok.corebanking.entities.Payment;
import com.gmail.yuliakazachok.corebanking.entities.states.CreditStates;
import com.gmail.yuliakazachok.corebanking.repositories.CreditRepository;
import com.gmail.yuliakazachok.corebanking.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final PaymentRepository paymentRepository;
    private final CreditRepository creditRepository;

    public List<Report> makeReport() {
        float volumeIssue = 0.0f;
        float percent = 0;
        int countCreditExpired = 0;
        float totalProfit;
        float paymentCredit = 0.0f;
        int indexCredit = 0;
        int indexPayment = 0;
        Credit credit;
        Payment payment;
        Date date;

        List<Report> listReport = new ArrayList<>();
        List<Credit> creditList = new ArrayList<>(creditRepository.findAllOrderByDateOpen());
        List<Payment> paymentList = new ArrayList<>(paymentRepository.findAllOrderByDate());
        long timeEndDate = new Date(System.currentTimeMillis()).getTime();
        Credit firstCredit = creditList.stream().findFirst().get();
        Date firstDate = new Date(firstCredit.getDateOpen().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDate);
        long calendarTime = calendar.getTimeInMillis();

        credit = firstCredit;
        indexCredit++;
        payment = paymentList.stream().findFirst().get();
        indexPayment++;

        while (calendarTime < timeEndDate) {
            while (credit.getDateOpen().getTime() <= calendarTime) {
                volumeIssue += credit.getSum();
                if (credit.getState() == CreditStates.STATE_EXPIRED) {
                    countCreditExpired += 1;
                }
                percent = ((float) countCreditExpired / (indexCredit + 1)) * 100;

                credit = creditList.get(indexCredit);
                indexCredit++;
            }

            while (payment.getDate().getTime() <= calendarTime) {
                paymentCredit += payment.getSum();

                payment = paymentList.get(indexPayment);
                indexPayment++;
            }
            totalProfit = (paymentCredit / volumeIssue);

            date = new Date(calendarTime);
            listReport.add(new Report(date, volumeIssue, percent, totalProfit));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            calendarTime = calendar.getTimeInMillis();
        }
        return listReport;
    }
}
