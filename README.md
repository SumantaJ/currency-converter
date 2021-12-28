### Task:

- Server
 - Build a currency conversion API which can convert a value between EUR, USD, and JPY in any direction. Please use exchange rates from a publicly available service and expose your service as an HTTP API.
- CLI
 - Build a CLI that can take a file with one JSON per line, and a target currency as input and outputs a JSON-per-line to STDOUT with all monetary values converted into the given target currency using the currency conversion API created in task 1.

### Solution

The backend application is developed using Spring boot. The CLI script is developed using node js. Please follow the below mentioned steps to run the application and CLI script :

  - First build and run the application
  - Once the application is up then run the CLI script to test the REST application.

Inside CLI folder I have example payload, named as **payload.json** also a malformed payload named **malformed_payload.json**. I have taken reference from problem statement payload to create own payload, if you want to try with a different payload please put the json array inside **payload.json**. The output will be saved as **output.json** inside the CLI folder itself.

I have used "https://github.com/posadskiy/currency-converter" library to get currency conversion rate. <br /> **Note:** Another most important reason to choose this library over other is, this library supports upto three currency conversion api at a time, So that, if one is down it can get the value from other. This will minimises the chances of unavailability in case the external api is down.


I have used the following malformed json payload :

[{ "item": "B", "price": 9.95, "currency": "USD" },
{ "item": "A", "price": 99.95, "currency": "JPY" },
{ "item": "C", "price": 45.76, "currency": "INVALID" },{ "item": null, "price": 4.76, "currency": "JPY" }]

in the output.json file the output was :

[{"item":"B","price":8.44,"currency":"EUR"},{"item":"A","price":0.77,"currency":"EUR"}]

And on the JVM console it printed :

Invalid Item: [item= C, price= 45.76, currency= INVALID]
Invalid Item: [item= null, price= 4.76, currency= JPY]

## Requirements
* Java 8
* Maven
* Node JS (For the CLI)

## How to build the application
Run the following command from the root folder :
```
    mvn clean install
```

## How to run the application
Run the spring boot application :
```
    mvn spring-boot:run
```
The application runs on port 8090.


## How to use the CLI script
Run the following commands from the "CLI" folder :
```
    npm init
```
Press enter for every option in the prompt. I have kept everything default.

Then run :
```
    npm install minimist
```
Finally to hit the REST endpoint run ('--json' argument takes the payload.json path and for '--currency' argument pass the target currency) :
```
    node index.js --json ./payload.json --currency EUR
```
## Swagger documentation:

- Swagger documentation of api is available at http://localhost:8090/swagger-ui.html#/

## Postman request and response

## Request
```
POST: http://localhost:8090/currencyconverter/convert?currency=EUR  (EUR is being passed as a request parameter)

[{ "item": "B", "price": 9.95, "currency": "USD" },
{ "item": "A", "price": 99.95, "currency": "JPY" },
{ "item": "C", "price": 10.99, "currency": "EUR" }]
```
## Response

```
[
    {
        "item": "B",
        "price": 8.44,
        "currency": "EUR"
    },
    {
        "item": "A",
        "price": 0.77,
        "currency": "EUR"
    },
    {
        "item": "C",
        "price": 10.99,
        "currency": "EUR"
    }
]
```
## Design Considerations:

**Will it work with a 2 TB input file as well?**

I expect for a file of this much size, It may create some issue as for normal process JVM assignees a thread per request which could be exhausted soon and some request will wait and eventually get timeout. According to me, optimal solution for this would be to process this in batches.

**What would happen if the input file has one malformed JSON line towards the end of the file?**

I have tried to handle those scenarios, in this scenarios it will print the invalid items in JVM console and will give output for all other valid input in output.json file.

**Assume your API should degrade gracefully / still be available in case the upstream exchange rate service is down. How would you handle this?**

I have chosen https://github.com/posadskiy/currency-converter over other because, it has a unique feature of supporting upto three currency conversion api at a time. So that, if one is down it can connect and get latest rate from other.This would minimise the downtime possibility. However if still this occurs then, We can have some kind of caching mechanism which would show the last conversion rate with time when it fetched. I have tried to implement this using simple google guava cache which has a live period of 60 min. We could use any other caches as well example redis, hazelcast.
