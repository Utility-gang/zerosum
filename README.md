# ZeroSum

ZeroSum is a trading platform with the goal of losing all of your starting funds.

It contains a fixed collection of stocks, and you have to invest your
fake money into those. The stocks are updated live, and you can buy/sell them
from each stock's page. Your available cash will be updated accordingly.

When you buy stock units, they will be added to your portfolio.

On each stock page, there is a graph of the live stock price. We only store very
recent events, because we intentionally designed this app for high-frequency
trading. You can choose how much to buy/sell with a cool slider bar (courtesy of Thomas + Shaki).

To make it even more fun, there's a leaderboard page, so you can see how good
you are at losing money :).

In case you've had too much fun, you can always have a look at the news page.
Have a look at interest rate cuts or whatever financial people read normally.

## tech stack

This project uses Java, Maven, Springboot, and PostgreSQL to run. additionally, you need an Auth0 application setup. it also loads HTMX and Lightweight Charts automatically, when needed.

## running

 - first of all, create an auth0 account, and create a new application. 
 - register with finnhub to get a finnhub api key 
 - register with openai to get an openai api key
 - then you should put your api keys in a new file called .env, like so:

```
OKTA_ISSUER=
OKTA_CLIENT_ID=
OKTA_CLIENT_SECRET=
FINNHUB_API_KEY=
OPENAI_API_KEY=
```

then install Maven and PostgreSQL (make sure it's running). Then, create a new table called zerosum using this command:

```
createdb zerosum
```

...then then you can run this command to start the server!:

```
mvn spring-boot:run
```

and finally, to run the tests (must be run after the server is already running):

```
mvn test
```

## what does our server actually do?

There's many components, but it boils down to these main ones:

 - finnhub websocket controller to get live stock info from finnhub, and put it
   into our structures
 - controllers for the web interface
 - a REST api (PriceController) to send live updates from the server to any connected client's chart
 - OpenAI structured outputs for funny summaries of the different companies
 - PriceData which handles storing all the stocks, as well as caching them to
   disk
 - NewsService which handles caching and parsing news data from finnhub
