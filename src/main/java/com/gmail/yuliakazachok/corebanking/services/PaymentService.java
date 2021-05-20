package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.entities.states.ClientStates;
import com.gmail.yuliakazachok.corebanking.entities.states.CreditStates;
import com.gmail.yuliakazachok.corebanking.entities.states.PaymentScheduleStates;
import com.gmail.yuliakazachok.corebanking.entities.*;
import com.gmail.yuliakazachok.corebanking.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final СommissionRepository commissionRepository;

    public List<Payment> getPaymentsByIdCredit(Integer idCredit) {
        return paymentRepository.findAllByIdCreditOrderByDate(idCredit);
    }

    @Transactional
    public void savePayment(Payment payment) {
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
                            // если досрочный платеж, считаем их количество
                            ++countEarlyPayments;
                        }
                    } else {
                        // если недостаточно средств для платежа, закидываем на баланс кредита
                        addRestSumToBalance(credit, sumReceived);
                        break;
                    }
                }
                // если оплатили досрочно, то пересчитываем остаток долга
                if (countEarlyPayments != 0) {
                    recalculatePaymentSchedule(credit, listNeedPayments, countMadePayments, countEarlyPayments);
                }
                // если выплатили все платежи, закрываем кредит
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

    private void recalculatePaymentSchedule(Credit credit, List<PaymentSchedule> paymentScheduleList, Integer countMadePayments, Integer countEarlyPayments) {
        Optional<AvailableTariff> availableTariff = availableTariffRepository.findById(credit.getIdAvailTariff());
        if (availableTariff.isPresent()) {
            Optional<Tariff> tariff = tariffRepository.findById(availableTariff.get().getIdTariff());
            if (tariff.isPresent()) {
                float sum = 0.0f;
                for (int i = countMadePayments; i < paymentScheduleList.size(); i++) {
                    sum += paymentScheduleList.get(i).getSum();
                }
                float r = ((float) tariff.get().getRate() / 100 / 12);
                int term = paymentScheduleList.size() - (countMadePayments - countEarlyPayments);
                float sumMonth = (float) (sum * ((r * Math.pow(1 + r, term)) / (Math.pow(1 + r, term) - 1)));
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
}
