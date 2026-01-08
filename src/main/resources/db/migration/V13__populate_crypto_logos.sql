UPDATE companies
SET logo = CASE symbol
    WHEN 'BINANCE:BTCUSDT' THEN 'https://cryptologos.cc/logos/bitcoin-btc-logo.png?v=040'
    WHEN 'BINANCE:ETHUSDT' THEN 'https://cryptologos.cc/logos/ethereum-eth-logo.png?v=040'
    WHEN 'BINANCE:XRPUSDT' THEN 'https://cryptologos.cc/logos/xrp-xrp-logo.png?v=040'
END
WHERE symbol IN (
    'BINANCE:BTCUSDT',
    'BINANCE:ETHUSDT',
    'BINANCE:XRPUSDT'
);