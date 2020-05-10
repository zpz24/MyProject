var express = require('express');
var router = express.Router();
const request = require('request');

function clear(before){
  let res = [];
  let len = before.length;
  let index = 0;
  for (let i = 0; i < len; i++) {
    if (before[i].blocks == null || before[i].blocks === "" || before[i].blocks.body == null
       || before[i].blocks.body[0] == null || before[i].id == null || before[i].id === "") {
      continue;
    } else{
      const id = before[i].id;
      let title = before[i].webTitle;
      let image;
      if(before[i].blocks.main == null || before[i].blocks.main.elements == null
          || before[i].blocks.main.elements[0] == null || before[i].blocks.main.elements[0].assets == null
          || before[i].blocks.main.elements[0].assets.length < 1){
        image = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
      } else {
        let temp = before[i].blocks.main.elements[0].assets;
        if (temp[temp.length - 1] == null || temp[temp.length - 1].file == null || temp[temp.length - 1].file === "") {
          image = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
        } else {
          image = temp[temp.length - 1].file;
        }
      }

      let section = before[i].sectionId;
      let date = before[i].webPublicationDate;
      if (date != null && date.length >= 10) {
        date = date.substring(0, 10);
      }
      let description = before[i].blocks.body[0].bodyTextSummary;
      const shareUrl = before[i].webUrl;
      if (title == null || title === "" || section == null || section === "" ||
          date == null || date === "" || date.length < 10 || description == null || description === ""
          || before[i].webUrl == null || before[i].webUrl === "") {
        continue;
      }
      else {
        res[index] = {
          key: index,
          id: id,
          title: title,
          image: image,
          section: section,
          date: date,
          description: description,
          shareUrl: shareUrl

        };
        index++;
        if(index >= 10){
          break;
        }
      }
    }
  }
  return res;
}
/* GET home page. */
router.get('/:orderId', function(req, res, next) {

  if(req.params.orderId === "home") {
    request('https://content.guardianapis.com/search?api-key=bd82d723-4548-4e34-a94c-5dfa7108f0de' +
        '&section=(sport|business|technology|politics)&show-blocks=all',
        function (error, response, body) {

          if (!error && response.statusCode === 200) {

            let result = JSON.parse(body);
            const results = result.response.results;
            let arr = clear(results);

            res.send(arr);
          }
        })
  }

  else{
    request('https://content.guardianapis.com/' + req.params.orderId + '?api-key=bd82d723-4548-4e34-a94c-5dfa7108f0de' +
                '&show-blocks=all',
        function (error, response, body) {

          if (!error && response.statusCode === 200) {

            let result = JSON.parse(body);
            const results = result.response.results;
            let arr = clear(results);

            res.send(arr);
          }
        })
  }

});


module.exports = router;
