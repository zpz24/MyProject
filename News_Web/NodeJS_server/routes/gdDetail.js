var express = require('express');
var router = express.Router();
const request = require('request');
const url = require("url");

function clear(before) {
    let id = before.id;
    let section = before.sectionId;
    let title = before.webTitle;
    let image;
    if (before.blocks == null || before.blocks.main == null || before.blocks.main.elements == null
        || before.blocks.main.elements[0] == null || before.blocks.main.elements[0].assets == null
        || before.blocks.main.elements[0].assets.length < 1) {
        image = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
    } else {
        let temp = before.blocks.main.elements[0].assets;
        if (temp[temp.length - 1] == null || temp[temp.length - 1].file == null || temp[temp.length - 1].file === "") {
            image = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
        } else {
            image = temp[temp.length - 1].file;
        }
    }

    let date = before.webPublicationDate.substring(0, 10);
    let description = before.blocks.body[0].bodyTextSummary;
    const shareUrl = before.webUrl;

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
                let json = clear(content);

                res.send(json);
            }
        })

});


module.exports = router;
