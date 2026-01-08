# zerosum

zerosum (temp name?) is a platform to invest with the goal of losing all of your starting funds.

it contains a fixed collection of stocks, that we choose, and you have to invest your
fake money into those. the stocks are updated live, and you can buy/sell them
from each stock's page. 

when you buy, it will be added to your portfolio.

on each stock page, there is a graph of the live stock price. we only store very
recent events, because we intentially designed this app for high-frequency
trading. cool slider bar courtesy of Thomas + Shaki

to make it even more fun, there's a leaderboard page, so you can see how good
you are at losing money :)

in case you've had too much fun, you can always have a look at the news page.
have a look at interest rate cuts or whatever financial people read normally.

## tech stack

this project uses java, mavern, springbot, and postgresql to run. additionally, you need an auth0 application setup. it also loads htmx and lightweight charts automatically, when needed.

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

then install mavern and postgresql (make sure it's running). then, create a new table called zerosum using this command:

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

there's many components, but it boils down to these main ones:

 - finnhub websocket controller to get live stock info from finnhub, and put it
   into our structures
 - controllers for the web interface
 - a REST api (PriceController) to send live updates from the server to any connected client's chart
 - openai (idk much about this one)
 - PriceData which handles storing all the stocks, as well as caching them to
 disk
 - NewsService which handles caching and parsing news data from finnhub
