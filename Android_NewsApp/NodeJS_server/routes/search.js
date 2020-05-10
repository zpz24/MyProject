var express = require('express');
var router = express.Router();
const request = require('request');

function clear(results){
    let res = [];
    let len = results.length;
    let index = 0;
    for (let i = 0; i < len; i++) {
        if ( results[i].webTitle == null || results[i].webTitle === "" || results[i].id == null || results[i].id === ""
            || results[i].sectionId == null || results[i].sectionId === "" || results[i].webPublicationDate == null
            || results[i].webPublicationDate === "" || results[i].webUrl == null || results[i].webUrl === "") {
            continue;
        }
        else{
            const id = results[i].id;
            const title = results[i].webTitle;
            const section = results[i].sectionId;
            const shareUrl = results[i].webUrl;
            const date = results[i].webPublicationDate;

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
router.get('/', function(req, res, next) {

    const id = req.query.id;
    request( "https://content.guardianapis.com/search?q=" + id + "&api-key=bd82d723-4548-4e34-a94c-5dfa7108f0de"
        + "&show-blocks=all",
        function (error, response, body) {

            if (!error && response.statusCode === 200) {

                let result = JSON.parse(body);
                const results = result.response.results;
                let json = clear(results);
                result["search_result"] = json;

                res.send(result);
            }
            else{
                res.send(JSON.parse(body));
            }
        })

});


module.exports = router;
