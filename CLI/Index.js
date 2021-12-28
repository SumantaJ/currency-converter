const https = require('http');
const fs = require('fs');
const arguments = require('minimist')(process.argv.slice(2));
const jsonFile = require(arguments["json"]);
const currency = arguments['currency'];
JsonObject = JSON.stringify(jsonFile);
var options = {
    'method': 'POST',
    'hostname': 'localhost',
    'path': `/currencyconverter/convert?currency=${currency}`,
    'port': '8090',
    'headers': {
        'Content-Type' : 'application/json',
        'Content-Length' : Buffer.byteLength(JsonObject, 'utf8'),
    }
};
console.log(options);
var req = https.request(options, function (res) {
    var chunks = [];

    res.on("data", function (chunk) {
        chunks.push(chunk);
    });

    res.on("end", function (chunk) {
        var body = Buffer.concat(chunks);
        body = JSON.stringify(JSON.parse(body.toString()));
        fs.writeFileSync('output.json', body);
    });

    res.on("error", function (error) {
        console.error(error);
    });
});

req.write(JsonObject);
req.end();
