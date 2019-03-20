package com.bjss.assignment.service.impl;

import com.bjss.assignment.PriceBasketException;
import com.bjss.assignment.data.CartTotals;
import com.bjss.assignment.data.Item;
import com.bjss.assignment.service.BasketPrinterService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

@Service
public class BasketPrinterServiceImpl implements BasketPrinterService {

    private static final String NO_OFFERS_AVAILABLE = "no.offers.available";

    @Resource(name = "messageSource")
    private MessageSource messageSource;

    @Override
    public void write(PrintStream out, CartTotals totals) throws PriceBasketException {

        if (out == null) {
            return;
        }
        try (
                OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8")) {
            String subTotalString = getSubTotalMsg(totals.getSubTotal());
            String offersString = getOffersMsg(totals.getOfferTotals());
            String totalString = getTotalMsg(totals.getTotal());
            writer.write(subTotalString + System.lineSeparator());
            writer.write(offersString + System.lineSeparator());
            writer.write(totalString + System.lineSeparator());

        } catch (IOException ex) {
            throw new PriceBasketException(
                    "BasketPrinter encountered an error occurred printing to the specified print stream",
                    ex);
        }
    }

    public String getSubTotalMsg(BigDecimal subTotal) {
        NumberFormat numberFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String formatedValue = numberFormatter.format(subTotal);
        return messageSource.getMessage("subtotal", new Object[]{formatedValue}, Locale.getDefault());
    }

    public String getOffersMsg(Map<String, Item> offerTotals) {
        StringBuilder sb = new StringBuilder();
        if (offerTotals == null || offerTotals.isEmpty()) {
            sb.append(messageSource.getMessage(NO_OFFERS_AVAILABLE, new Object[]{}, Locale.getDefault()));
            sb.append(System.lineSeparator());
        } else {
            for (Map.Entry<String, Item> itemEntry : offerTotals.entrySet()) {
                Item item = itemEntry.getValue();
                BigDecimal discountInPence = (item.getDiscountPrice().multiply(new BigDecimal(100)))
                        .setScale(0, RoundingMode.HALF_UP);
                BigDecimal discountPercentage = item.getDiscount().multiply(new BigDecimal(100));

                sb.append(item.getId()).append(" ").append(discountPercentage).append("% off: -")
                        .append(discountInPence).append("p").append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }

    public String getTotalMsg(BigDecimal total) {
        NumberFormat numberFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String formatedValue = numberFormatter.format(total);
        return messageSource.getMessage("total", new Object[]{formatedValue}, Locale.getDefault());
    }
}
