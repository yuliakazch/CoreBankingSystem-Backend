package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.dto.PaymentInfo;
import com.gmail.yuliakazachok.corebanking.entities.states.ClientStates;
import com.gmail.yuliakazachok.corebanking.entities.states.CreditStates;
import com.gmail.yuliakazachok.corebanking.entities.states.PaymentScheduleStates;
import com.gmail.yuliakazachok.corebanking.entities.*;
import com.gmail.yuliakazachok.corebanking.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentScheduleRepository paymentScheduleRepository;
    private final CreditRepository creditRepository;
    private final AvailableTariffRepository availableTariffRepository;
    private final ClientRepository clientRepository;
    private final TariffRepository tariffRepository;
    private final –°ommissionRepository commissionRepository;

    public List<PaymentInfo> getPaymentsByIdCredit(Integer idCredit) {
        List<PaymentInfo> listPaymentInfo = new ArrayList<>();
        List<Payment> listPayments = paymentRepository.findAllByIdCreditOrderByDate(idCredit);
        listPayments.forEach(payment -> {
            int interest = commissionRepository.findCommissionById(payment.getIdCommission()).getInterest();
            listPaymentInfo.add(new PaymentInfo(payment.getDate(), payment.getSum(), interest));
        });
        return listPaymentInfo;
    }

    @Transactional
    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
        Integer idCredit = payment.getIdCredit();
        List<PaymentSchedule> listNeedPayments = getRestPayments(idCredit);
        Optional<Credit> optionalCredit = creditRepository.findById(idCredit);
        if (optionalCredit.isPresent()) {
            Credit credit = optionalCredit.get();
            Float sumReceived = getSumWithoutCommission(payment.getSum(), payment.getIdCommission());
            if (listNeedPayments.isEmpty()) {
                addRestSumToBalance(credit, sumReceived);
            } else {
                int countMadePayments = 0;
                int countEarlyPayments = 0;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(payment.getDate());
                calendar.add(Calendar.MONTH, 1);
                long timePaymentAddMonth = calendar.getTime().getTime();
                for (PaymentSchedule paymentSchedule : listNeedPayments) {
                    if (paymentSchedule.getSum() <= sumReceived) {
                        long timeSchedule = paymentSchedule.getDate().getTime();
                        sumReceived = markPayment(paymentSchedule, sumReceived);
                        ++countMadePayments;
                        if (timeSchedule > timePaymentAddMonth) {
                            // –Ķ—Ā–Ľ–ł –ī–ĺ—Ā—Ä–ĺ—á–Ĺ—č–Ļ –Ņ–Ľ–į—ā–Ķ–∂, —Ā—á–ł—ā–į–Ķ–ľ –ł—Ö –ļ–ĺ–Ľ–ł—á–Ķ—Ā—ā–≤–ĺ
                            ++countEarlyPayments;
                        }
                    } else {
                        // –Ķ—Ā–Ľ–ł –Ĺ–Ķ–ī–ĺ—Ā—ā–į—ā–ĺ—á–Ĺ–ĺ —Ā—Ä–Ķ–ī—Ā—ā–≤ –ī–Ľ—Ź –Ņ–Ľ–į—ā–Ķ–∂–į, –∑–į–ļ–ł–ī—č–≤–į–Ķ–ľ –Ĺ–į –Ī–į–Ľ–į–Ĺ—Ā –ļ—Ä–Ķ–ī–ł—ā–į
                        addRestSumToBalance(credit, sumReceived);
                        break;
                    }
                }
                // –Ķ—Ā–Ľ–ł –ĺ–Ņ–Ľ–į—ā–ł–Ľ–ł –ī–ĺ—Ā—Ä–ĺ—á–Ĺ–ĺ, —ā–ĺ –Ņ–Ķ—Ä–Ķ—Ā—á–ł—ā—č–≤–į–Ķ–ľ –ĺ—Ā—ā–į—ā–ĺ–ļ –ī–ĺ–Ľ–≥–į
                if (countEarlyPayments != 0) {
                    recalculatePaymentSchedule(credit, listNeedPayments, countMadePayments, getTermRest(credit, payment));
                }
                // –Ķ—Ā–Ľ–ł –≤—č–Ņ–Ľ–į—ā–ł–Ľ–ł –≤—Ā–Ķ –Ņ–Ľ–į—ā–Ķ–∂–ł, –∑–į–ļ—Ä—č–≤–į–Ķ–ľ –ļ—Ä–Ķ–ī–ł—ā
                if (countMadePayments == listNeedPayments.size()) {
                    closeCredit(credit);
                }
            }
        }
    }

    private float getSumWithoutCommission(float sumWithCommission, int idCommission) {
        int interest = commissionRepository.findCommissionById(idCommission).getInterest();
        return sumWithCommission * (100 - interest) * 0.01f;
    }

    private List<PaymentSchedule> getRestPayments(int idCredit) {
        return paymentScheduleRepository.findAllByIdCreditAndStateInOrderByDate(
                idCredit,
                List.of(PaymentScheduleStates.STATE_MISSED, PaymentScheduleStates.STATE_NOT_PAID)
        );
    }

    private void addRestSumToBalance(Credit credit, float sum) {
        float balanceBefore = credit.getBalance();
        credit.setBalance(balanceBefore + sum);
    }

    private float markPayment(PaymentSchedule paymentSchedule, float sumReceived) {
        paymentSchedule.setState(PaymentScheduleStates.STATE_PAID);
        return sumReceived - paymentSchedule.getSum();
    }

    private void recalculatePaymentSchedule(Credit credit, List<PaymentSchedule> paymentScheduleList, Integer countMadePayments, Integer termRest) {
        Optional<AvailableTariff> availableTariff = availableTariffRepository.findById(credit.getIdAvailTariff());
        if (availableTariff.isPresent()) {
            Optional<Tariff> tariff = tariffRepository.findById(availableTariff.get().getIdTariff());
            if (tariff.isPresent()) {
                float sum = 0.0f;
                for (int i = countMadePayments; i < paymentScheduleList.size(); i++) {
                    sum += paymentScheduleList.get(i).getSum();
                }
                float r = ((float) tariff.get().getRate() / 100 / 12);
                float sumMonth = (float) (sum * ((r * Math.pow(1 + r, termRest)) / (Math.pow(1 + r, termRest) - 1)));
                sumMonth = (float) Math.round(sumMonth * 100f) / 100f;

                for (int i = countMadePayments; i < paymentScheduleList.size(); i++) {
                    paymentScheduleList.get(i).setSum(sumMonth);
                }
            }
        }
    }

    private void closeCredit(Credit credit) {
        credit.setState(CreditStates.STATE_CLOSE);
        availableTariffRepository.findById(credit.getIdAvailTariff())
                .flatMap(availableTariff -> clientRepository.findById(availableTariff.getNumberPassport()))
                .ifPresent(client -> {
                    client.setState(ClientStates.STATE_NOT_CREDIT);
                    client.setCountBlockDays(0);
                });
    }

    private int getTermRest(Credit credit, Payment payment) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(payment.getDate());
        LocalDate datePayment = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        calendar.setTime(credit.getDateOpen());
        LocalDate dateOpen = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        int monthsGone = Period.between(dateOpen, datePayment).getMonths();
        return credit.getTerm() - monthsGone;
    }
}
