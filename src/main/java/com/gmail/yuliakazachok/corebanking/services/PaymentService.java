package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.dto.ClientStates;
import com.gmail.yuliakazachok.corebanking.dto.CreditStates;
import com.gmail.yuliakazachok.corebanking.dto.PaymentScheduleStates;
import com.gmail.yuliakazachok.corebanking.entities.*;
import com.gmail.yuliakazachok.corebanking.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        int countMadePayments = 0;
        List<PaymentSchedule> listNeedPayments = getRestPayments(idCredit);

        Optional<Credit> optionalCredit = creditRepository.findById(idCredit);
        if (optionalCredit.isPresent()) {
            Credit credit = optionalCredit.get();
            Float sumReceived = getSumWithoutCommission(payment.getSum(), payment.getIdCommission());
            if (listNeedPayments.isEmpty()) {
                addRestSumToBalance(credit, sumReceived);
            } else {
                float restNotPaidSum = 0.0f;
                boolean isFirstPaymentOnTime = true;
                for (PaymentSchedule paymentSchedule : listNeedPayments) {
                    if (paymentSchedule.getSum() >= sumReceived) {
                        if (paymentSchedule.getState() == PaymentScheduleStates.STATE_MISSED) {
                            // платим просрочку
                            sumReceived = markPayment(paymentSchedule, sumReceived);
                            ++countMadePayments;
                        } else if (paymentSchedule.getState() == PaymentScheduleStates.STATE_NOT_PAID && isFirstPaymentOnTime) {
                            // платим за текущий месяц
                            isFirstPaymentOnTime = false;
                            sumReceived = markPayment(paymentSchedule, sumReceived);
                            ++countMadePayments;
                        } else {
                            // платим досрочно
                            restNotPaidSum += paymentSchedule.getSum(); // TODO: считается не весь остаток!
                        }
                    } else {
                        if (restNotPaidSum == 0.0f) {
                            // если не достаточно средств для платежа по графику, закидываем на баланс кредита
                            addRestSumToBalance(credit, sumReceived);
                        } else {
                            // если есть средства на досрочный платеж, то пересчитываем остаток долга
                            restNotPaidSum += paymentSchedule.getSum();
                            recalculatePaymentSchedule(credit, restNotPaidSum - sumReceived, listNeedPayments, countMadePayments);
                        }
                        // если выплатили все платежи, закрываем кредит
                        if (countMadePayments == listNeedPayments.size()) {
                            closeCredit(credit);
                        }
                        break;
                    }
                }
            }
        }
    }

    private float getSumWithoutCommission(float sumWithCommission, int idCommission) {
        int interest = commissionRepository.findById(idCommission).get().getInterest();
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

    private void recalculatePaymentSchedule(Credit credit, Float sum, List<PaymentSchedule> paymentScheduleList, Integer countMadePayments) {
        AvailableTariff availableTariff = availableTariffRepository.findById(credit.getIdAvailTariff()).get();
        Tariff tariff = tariffRepository.findById(availableTariff.getIdTariff()).get();

        float r = ((float) tariff.getRate() / 100 / 12);
        int term = paymentScheduleList.size() - countMadePayments;
        float sumMonth = (float) (sum * ((r * Math.pow(1 + r, term)) / (Math.pow(1 + r, term) - 1)));
        sumMonth = (float) Math.round(sumMonth * 100f) / 100f;

        for (int i = countMadePayments; i < paymentScheduleList.size(); i++) {
            paymentScheduleList.get(i).setSum(sumMonth);
        }
    }

    private void closeCredit(Credit credit) {
        credit.setState(CreditStates.STATE_CLOSE);
        availableTariffRepository.findById(credit.getIdAvailTariff()).ifPresent(availableTariff -> {
            clientRepository.findById(availableTariff.getNumberPassport()).ifPresent(client -> {
                client.setState(ClientStates.STATE_NOT_CREDIT);
                client.setCountBlockDays(0);
            });
        });
    }
}
