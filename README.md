# zerosum

zerosum (temp name?) is a platform to invest with the goal of losing all of your starting funds.

## tech stack

this project uses java, mavern, springboot, htmx, and postgresql to run.

## running

first of all, install mavern and postgresql (make sure it's running). then, create a new table called zero sum using this command:

```
createdb zerosum
```

then you can run this command to start the server:

```
mvn spring-boot:run
```

and to run the tests (must be run after the server is already running):

```
mvn test
```
