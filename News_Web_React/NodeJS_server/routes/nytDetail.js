var express = require('express');
var router = express.Router();
const request = require('request');

function clear(before) {
    const section = before.news_desk;
    const title = before.headline.main;

    let image = "https://upload.wikimedia.org/wikipedia/commons/0/0e/Nytimes_hq.jpg";
    if(before.multimedia == null || before.multimedia.length < 1 || before.multimedia === ""){
        image = "https://upload.wikimedia.org/wikipedia/commons/0/0e/Nytimes_hq.jpg";
    }
    else {
        for (let i = 0; i < before.multimedia.length; i++) {
            if (before.multimedia[i].url != null && before.multimedia[i].url !== ""
                && before.multimedia[i].width != null && before.multimedia[i].width >= 2000) {
                image =  "https://www.nytimes.com/" + before.multimedia[i].url;
                break;
            }
        }
    }

    const date = before.pub_date.substring(0, 10);

    const description = before.abstract;
    const shareUrl = before.web_url;


    return {
        id:shareUrl,
        section: section,
        title: title,
        image: image,
        date: date,
        description: description,
        shareUrl: shareUrl
    };
}

/* GET home page. */
router.get('/', function (req, res, next) {

    request('https://api.nytimes.com/svc/search/v2/articlesearch.json?fq=web_url:("'+ req.query.id +
        '")&api-key=nC9sL1Yv9PvqsOjxYIxaLaKKfbnoU3jc',
        function (error, response, body) {

            if (!error && response.statusCode === 200) {

                let result = JSON.parse(body);
                const content = result.response.docs[0];
                let arr = clear(content);

                res.send(arr);
            }
        })

});


module.exports = router;
