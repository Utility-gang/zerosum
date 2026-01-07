INSERT INTO companies (symbol, logo) VALUES
                                         -- A. Active / Recent Poor Performers
                                         ('OTLK', 'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/OTLK.png'),
                                         ('KZIA', 'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/KZIA.png'),
                                         ('PTIX', 'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/PTIX.png'),
                                         ('OST',  'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/OST.png'),
                                         ('TRSG', 'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/TRSG.png'),
                                         ('MGLD', 'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/MGLD.png'),
                                         ('BCAB', 'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/BCAB.png'),
                                         ('VSA',  'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/VSA.png'),
                                         ('LFS',  'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/LFS.png'),

                                         -- B. Well-Known Stocks with Persistent Weak Performance
                                         ('RIVN', 'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/RIVN.png'),
                                         ('PARA', 'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/PARA.png'),
                                         ('VZ',   'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/VZ.png'),
                                         ('T',    'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/T.png'),
                                         ('WBD',  'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/WBD.png'),
                                         ('BABA', 'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/BABA.png'),

                                         -- C. Meme / Retail-Fueled Collapses
                                         ('AMC',  'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/AMC.png'),
                                         ('GME',  'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/GME.png'),
                                         ('BB',   'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/BB.png'),
                                         ('NOK',  'https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/NOK.png')
ON CONFLICT (symbol) DO NOTHING;