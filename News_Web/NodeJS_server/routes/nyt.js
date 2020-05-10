var express = require('express');
var router = express.Router();
const request = require('request');

function clear(before){
  let res = [];
  let len = before.length;
  let index = 0;
  for(let i = 0; i < len; i++){

    const title = before[i].title;
    const section = before[i].section;
    let date = before[i].published_date;
    if(date != null && date.length >= 10){
      date = date.substring(0, 10);
    }
    else{
      continue;
    }
    const des = before[i].abstract;
    const shareUrl = before[i].url;
    let image = "https://upload.wikimedia.org/wikipedia/commons/0/0e/Nytimes_hq.jpg";
    if(before[i].multimedia == null || before[i].multimedia === "" || before[i].multimedia.length < 1){
      image = "https://upload.wikimedia.org/wikipedia/commons/0/0e/Nytimes_hq.jpg";
    }
    else{
      for(let j = 0; j < before[i].multimedia.length; j++){
        if(before[i].multimedia[j].url != null && before[i].multimedia[j].url !== ""
            && before[i].multimedia[j].width != null && before[i].multimedia[j].width >= 2000){
          image = before[i].multimedia[j].url;
          break;
        }
      }

    }
    if(title != null && title !== "" && section != null && section !== "" && date != null
        && date !== "" && des != null && des !== "" && shareUrl != null && shareUrl !== ""){
      res[index] = {
        key: index,
        id: shareUrl,
        title: title,
        section: section,
        image: image,
        date: date,
        description: des,
        shareUrl: shareUrl
      };
      index++;
      if(index >= 10){
        break;
      }
    }
  }
  return res;
}
/* GET users listing. */
router.get('/:orderId', function(req, res, next) {
  if(req.params.orderId === "home"){
    request("https://api.nytimes.com/svc/topstories/v2/home.json?api-key=nC9sL1Yv9PvqsOjxYIxaLaKKfbnoU3jc",
        function (error, response, body) {
      if(!error && response.statusCode === 200){
        let result = JSON.parse(body);
        const results = result.results;
        let arr = clear(results);
        res.send(arr);
      }

        })
  }
  else{
    request("https://api.nytimes.com/svc/topstories/v2/"+ req.params.orderId
            +".json?api-key=nC9sL1Yv9PvqsOjxYIxaLaKKfbnoU3jc",
        function (error, response, body) {
          if(!error && response.statusCode === 200){
            let result = JSON.parse(body);
            const results = result.results;
            let arr = clear(results);
            res.send(arr);
          }

        })
  }

});

module.exports = router;
