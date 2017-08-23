package org.knowm.xchange.service;

import java.math.BigDecimal;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.meta.CurrencyPairMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;

/**
 * Top of the hierarchy abstract class for an "exchange service"
 */
public abstract class BaseExchangeService {

  /**
   * The base Exchange. Every service has access to the containing exchange class, which hold meta data and the exchange specification
   */
  protected final Exchange exchange;

  /**
   * Constructor
   */
  protected BaseExchangeService(Exchange exchange) {

    this.exchange = exchange;
  }

  public void verifyOrder(LimitOrder limitOrder) {

    ExchangeMetaData exchangeMetaData = exchange.getExchangeMetaData();
    verifyOrder(limitOrder, exchangeMetaData);
    BigDecimal price = limitOrder.getLimitPrice().stripTrailingZeros();

    if (price.scale() > exchangeMetaData.getCurrencyPairs().get(limitOrder.getCurrencyPair()).getPriceScale()) {
      throw new IllegalArgumentException("Unsupported price scale " + price.scale());
    }
  }

  public void verifyOrder(MarketOrder marketOrder) {

    verifyOrder(marketOrder, exchange.getExchangeMetaData());
  }

  final protected void verifyOrder(Order order, ExchangeMetaData exchangeMetaData) {

    CurrencyPairMetaData metaData = exchangeMetaData.getCurrencyPairs().get(order.getCurrencyPair());
    if (metaData == null) {
      throw new IllegalArgumentException("Invalid CurrencyPair");
    }

    BigDecimal tradableAmount = order.getTradableAmount();
    if (tradableAmount == null) {
      throw new IllegalArgumentException("Missing tradableAmount");
    }

    BigDecimal amount = tradableAmount.stripTrailingZeros();
    BigDecimal minimumAmount = metaData.getMinimumAmount();
    if (minimumAmount != null) {
        if (amount.scale() > minimumAmount.scale()) {
          throw new IllegalArgumentException("Unsupported amount scale " + amount.scale());
        } else if (amount.compareTo(minimumAmount) < 0) {
          throw new IllegalArgumentException("Order amount less than minimum");
        }
    }
  }
}
