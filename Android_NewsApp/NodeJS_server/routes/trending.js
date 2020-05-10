var express = require('express');
var router = express.Router();
const request = require('request');


function clear(results) {
    let res = [];
    const data = results.default.timelineData;
    let len = data.length;
    for (let i = 0; i < len; i++) {
        res[i] = data[i].value[0];
    }
    return res;
}

/* GET home page. */
router.get('/', function (req, res, next) {

    const id = "" + req.query.id;
    // request('',
    //     function (error, response, body) {
    //
    //         if (!error && response.statusCode === 200) {
    //
    //             let result = JSON.parse(body);
    //             const results = result.response.results;
    //             let arr = clear(results);
    //             result["savedNews"] = arr;
    //             res.send(result);
    //         }
    //     })

    const googleTrends = require('google-trends-api');
    googleTrends.interestOverTime({keyword: id, startTime: new Date("2019-06-01")}, function(err, results){
        if(err){
            res.send("error")
        }
        else {
            results = JSON.parse(results);
            results["data"] = clear(results);
            res.send(results);
        }
    })


});


module.exports = router;
