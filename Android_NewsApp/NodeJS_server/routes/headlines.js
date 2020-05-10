var express = require('express');
var router = express.Router();
const request = require('request');

function clear(results) {
    let res = [];
    let len = results.length;
    let index = 0;
    for (let i = 0; i < len; i++) {
        const title = results[i].webTitle;
        let date = results[i].webPublicationDate;
        const section = results[i].sectionName;
        const id = results[i].id;
        const shareUrl = results[i].webUrl;
        if (title == null || title === "" || date == null || date === "" || section == null || section === ""
            || id == null || id === "" || shareUrl == null || shareUrl === "") {
            continue;
        } else {

            let image = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";

            if (results[i].blocks != null && results[i].blocks.main != null && results[i].blocks.main.elements !== null
                && results[i].blocks.main.elements[0].assets != null && results[i].blocks.main.elements[0].assets.length > 0) {
                const temp = results[i].blocks.main.elements[0].assets;
                if(temp[temp.length - 1].file != null && temp[temp.length - 1].file !== ""){
                    image = temp[temp.length - 1].file;
                }

            }

            res[index] = {
                key: index,
                id: id,
                title: title,
                image: image,
                section: section,
                date: date,
                shareUrl: shareUrl

            };
            index++;


        }
    }
    return res;
}

/* GET home page. */
router.get('/', function (req, res, next) {

    const id = req.query.id;
    request('http://content.guardianapis.com/' + id + '?api-key=bd82d723-4548-4e34-a94c-5dfa7108f0de' +
        '&show-blocks=all',
        function (error, response, body) {

            if (!error && response.statusCode === 200) {

                let result = JSON.parse(body);
                const results = result.response.results;
                let arr = clear(results);
                result["savedNews"] = arr;
                res.send(result);
            }
        })

});


module.exports = router;
