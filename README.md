# zerosum

zerosum (temp name?) is a platform to invest with the goal of losing all of your starting funds.

## tech stack

this project uses java, mavern, springboot, htmx, and postgresql to run.
additionally, you need an auth0 application setup.

## running

first of all, create an auth0 account, and create a new application. then you should put your api keys in a new file called .env, like so:

```
OKTA_ISSUER=
OKTA_CLIENT_ID=
OKTA_CLIENT_SECRET=
FINNHUB_API_KEY=
```

then install mavern and postgresql (make sure it's running). then, create a new table called zero sum using this command:

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
