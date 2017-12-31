package org.knowm.xchange.binance.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.binance.BinanceAdapters;
import org.knowm.xchange.binance.dto.marketdata.BinanceAggTrades;
import org.knowm.xchange.binance.dto.marketdata.BinanceKline;
import org.knowm.xchange.binance.dto.marketdata.BinanceOrderbook;
import org.knowm.xchange.binance.dto.marketdata.BinancePriceQuantity;
import org.knowm.xchange.binance.dto.marketdata.BinanceSymbolPrice;
import org.knowm.xchange.binance.dto.marketdata.BinanceTicker24h;
import org.knowm.xchange.binance.dto.marketdata.KlineInterval;
import org.knowm.xchange.binance.dto.meta.BinanceTime;
import org.knowm.xchange.currency.CurrencyPair;

public class BinanceMarketDataServiceRaw extends BinanceBaseService {

  protected BinanceMarketDataServiceRaw(Exchange exchange) {
    super(exchange);
  }

  public void ping() throws IOException {
    binance.ping();
  }

  public Date time() throws IOException {
    BinanceTime time = binance.time();
    return time.getServerTime();
  }

  public BinanceOrderbook getBinanceOrderbook(CurrencyPair pair, Integer limit) throws IOException {
    return binance.depth(BinanceAdapters.toSymbol(pair), limit);
  }

  public List<BinanceAggTrades> aggTrades(CurrencyPair pair, Long fromId, Long startTime, Long endTime, Integer limit) throws IOException {
    return binance.aggTrades(BinanceAdapters.toSymbol(pair), fromId, startTime, endTime, limit);
  }

  public List<BinanceKline> klines(CurrencyPair pair, KlineInterval interval, Integer limit, Long startTime, Long endTime) throws IOException {
    List<Object[]> raw = binance.klines(BinanceAdapters.toSymbol(pair), interval, limit, startTime, endTime);
    return raw.stream().map(obj -> new BinanceKline(obj)).collect(Collectors.toList());
  }

  public BinanceTicker24h ticker24h(CurrencyPair pair) throws IOException {
    BinanceTicker24h ticker24h = binance.ticker24h(BinanceAdapters.toSymbol(pair));
    ticker24h.setCurrencyPair(pair);
    return ticker24h;
  }

  public List<BinanceSymbolPrice> tickerAllPrices() throws IOException {
    return binance.tickerAllPrices();
  }

  public List<BinancePriceQuantity> tickerAllBookTickers() throws IOException {
    return binance.tickerAllBookTickers();
  }
}
