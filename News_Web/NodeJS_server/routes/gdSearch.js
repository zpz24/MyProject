var express = require('express');
var router = express.Router();
const request = require('request');

function clear(before){
    let res = [];
    let len = before.length;
    let index = 0;
    for (let i = 0; i < len; i++) {
        if ( before[i].webTitle == null || before[i].webTitle === "" || before[i].id == null || before[i].id === ""
            || before[i].sectionId == null || before[i].sectionId === "" || before[i].webPublicationDate == null
            || before[i].webPublicationDate === "" || before[i].webPublicationDate.length < 10
            || before[i].webUrl == null || before[i].webUrl === "") {
            continue;
        }
        else{
            const id = before[i].id;
            let title = before[i].webTitle;
            let image;
            if(before[i].blocks == null || before[i].blocks === {} || before[i].blocks.main == null
                || before[i].blocks.main.elements == null || before[i].blocks.main.elements[0] == null
                || before[i].blocks.main.elements[0].assets == null || before[i].blocks.main.elements[0].assets.length < 1){
                image = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
            }
            else {
                let temp = before[i].blocks.main.elements[0].assets;
                if (temp[temp.length - 1] == null || temp[temp.length - 1].file == null || temp[temp.length - 1].file === "") {
                    image = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                } else {
                    image = temp[temp.length - 1].file;
                }
            }

            let section = before[i].sectionId;
            let date = before[i].webPublicationDate.substring(0, 10);

            const shareUrl = before[i].webUrl;
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
router.get('/', function(req, res, next) {

    const id = req.query.q;
    request( "https://content.guardianapis.com/search?q=" + id + "&api-key=bd82d723-4548-4e34-a94c-5dfa7108f0de"
        + "&show-blocks=all",
        function (error, response, body) {

            if (!error && response.statusCode === 200) {

                let result = JSON.parse(body);
                const results = result.response.results;
                let json = clear(results);

                res.send(json);
            }
        })

});


module.exports = router;
