var express = require('express');
var router = express.Router();
const request = require('request');

function clear(before) {
    let res = [];
    let len = before.length;
    let index = 0;
    for (let i = 0; i < len; i++) {
        if(before[i].headline != null && before[i].headline.main != null && before[i].headline.main !== ""
            && before[i].news_desk != null && before[i].news_desk !== "" && before[i].pub_date != null
            && before[i].pub_date !== "" && before[i].pub_date.length >= 10 && before[i].web_url != null
            && before[i].web_url !== ""){
            const title = before[i].headline.main;
            const section = before[i].news_desk;
            let date = before[i].pub_date.substring(0, 10);
            const shareUrl = before[i].web_url;
            let image = "https://upload.wikimedia.org/wikipedia/commons/0/0e/Nytimes_hq.jpg";
            if (before[i].multimedia == null || before[i].multimedia.length < 1) {
                image = "https://upload.wikimedia.org/wikipedia/commons/0/0e/Nytimes_hq.jpg";
            } else {
                for (let j = 0; j < before[i].multimedia.length; j++) {
                    if (before[i].multimedia[j].url != null && before[i].multimedia[j].url !== ""
                        && before[i].multimedia[j].width != null && before[i].multimedia[j].width >= 2000) {
                        image = "https://www.nytimes.com/" + before[i].multimedia[j].url;
                        break;
                    }
                }

            }

            res[index] = {
                key: index,
                id: shareUrl,
                title: title,
                section: section,
                image: image,
                date: date,
                shareUrl: shareUrl
            };
            index++;
        }


    }
    return res;
}

/* GET users listing. */
router.get('/', function (req, res, next) {
    const id = req.query.q;
    request("https://api.nytimes.com/svc/search/v2/articlesearch.json?q=" + id + "&api-key=nC9sL1Yv9PvqsOjxYIxaLaKKfbnoU3jc",
        function (error, response, body) {

            if (!error && response.statusCode === 200) {

                let result = JSON.parse(body);
                const content = result.response.docs;
                let arr = clear(content);

                res.send(arr);
            }
        })

});

module.exports = router;
