var express = require('express');
var router = express.Router();
const request = require('request');
const url = require("url");

function clear(result) {
    const id = result.id;
    const section = result.sectionName;
    const title = result.webTitle;
    const date = result.webPublicationDate;
    const html = result.blocks.body;
    let description = "";
    if(html != null && html.length > 0){
        for(let i = 0; i < html.length; i ++){
            if(html[i] != null && html[i].bodyHtml != null){
                description = description + html[i].bodyHtml;
            }

        }
    }

    const shareUrl = result.webUrl;

    let image = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";

    if (result.blocks.main != null && result.blocks.main.elements !== null
        && result.blocks.main.elements[0].assets != null && result.blocks.main.elements[0].assets.length > 0) {
        const temp = result.blocks.main.elements[0].assets;
        if(temp[temp.length - 1].file != null && temp[temp.length - 1].file !== ""){
            image = temp[temp.length - 1].file;
        }

    }



    return {
        id: id,
        section: section,
        title: title,
        image: image,
        date: date,
        description: description,
        shareUrl: shareUrl

    };
}

router.get('/', function (req, res, next) {

    const id = req.query.id;
    request( "https://content.guardianapis.com/" + id + "?api-key=bd82d723-4548-4e34-a94c-5dfa7108f0de" +
        "&show-blocks=all",
        function (error, response, body) {

            if (!error && response.statusCode === 200) {

                let result = JSON.parse(body);
                const content = result.response.content;
                result["my_article"] = clear(content);

                res.send(result);
            }
        })

});


module.exports = router;
