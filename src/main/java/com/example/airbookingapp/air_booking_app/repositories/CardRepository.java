package com.example.airbookingapp.air_booking_app.repositories;

import com.example.airbookingapp.air_booking_app.jooq.Tables;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Card;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardRepository {
    private final DSLContext dsl;

    public Card findCardByDetails(Integer userId, String cardNumber, String cardholderName, String cvvCode) {
        return dsl.selectFrom(Tables.CARD)
                .where(Tables.CARD.USER_ID.eq(userId))
                .and(Tables.CARD.CARD_NUMBER.eq(cardNumber))
                .and(Tables.CARD.CARDHOLDER_NAME.eq(cardholderName))
                .and(Tables.CARD.CVV_CODE.eq(cvvCode))
                .fetchOneInto(Card.class);
    }

    public void deductBalance(String cardNumber, Long amount) {
        dsl.update(Tables.CARD)
                .set(Tables.CARD.BALANCE_AMOUNT, Tables.CARD.BALANCE_AMOUNT.minus(amount))
                .where(Tables.CARD.CARD_NUMBER.eq(cardNumber))
                .execute();
    }
}
