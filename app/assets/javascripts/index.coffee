$ ->
  ws = new WebSocket $("body").data("ws-url")
  ws.onmessage = (event) ->
    message = JSON.parse event.data
    updates = message.updates

    i = parseInt(0)
    $('#tweets').html("")
    for update in updates
      appendUpdate update, i++

    # tweetWords?index=0
    # userProfile?userId=
    # searchHashTags?tag=

# Method to append SearchResult
appendUpdate = (update, index)  ->
  sentiment = "Neutral "
  sentiment_emoji = "<i class=\"fas fa-meh\" style=\"font-size: 30px; color: orange;\"></i>"

  if(update.overall_sentiment == ":-)")
    sentiment = "Happy "
    sentiment_emoji  = "<i class=\"fas fa-smile-beam\" style=\"font-size: 30px; color: green;\"></i>"
  else if(update.overall_sentiment == ":-(")
    sentiment = "Sad "
    sentiment_emoji = " <i class=\"fas fa-frown\" style=\"font-size: 30px; color: red;\"></i>"

  $('#tweets').append "<h3>Search Keyword: "  + "<a href='tweetWords?index=" + index + "'>"  + update.keyword + "</a>" +
  ", Sentiment : " + sentiment + sentiment_emoji + "</h3>" +"<hr/>"

  appendTweets update.tweets

  # Method to append individual tweets.
appendTweets = (tweets) ->


  sentiment_emoji = "<i class=\"fas fa-meh\" style=\"font-size: 30px; color: orange;\"></i>"

  for tweet in tweets

    hashtag = ""

    if(tweet.hashtag?)
      hashtag = "<a href='searchHashTags?tag=%23" + tweet.hashtag + "'>"  + "#"+ tweet.hashtag + "</a></p>"

    if(tweet.sentiment == ":-)")
      sentiment_emoji  = "<i class=\"fas fa-smile-beam\" style=\"font-size: 30px; color: green;\"></i>"
    else if(tweet.sentiment == ":-(")
      sentiment_emoji = " <i class=\"fas fa-frown\" style=\"font-size: 30px; color: red;\"></i>"

    $('#tweets').append "<div class=\"w3-card-4 w3-center w3-round-large\" style=\"width:50%;  margin: 10px; display:inline-block; background: white;\">" +
    "<div class=\"w3-row\">" +
    "<div class=\"w3-center w3-col\" style=\"width: 20%\">" +
    "<p class=\"w3-text-gray\">User</p>" +
    "<img src='" + tweet.user_image + "'" + "alt=\"alt\" class=\"w3-circle\">" + "<br/>" +
    "<p> <a href='userProfile?userId=" + tweet.user_link + "'>" + tweet.user_name + "</a></p>" +
    "</div>" +
    "<div class=\"w3-center w3-col\" style=\"width: 60%\">" +
    "<p class=\"w3-text-gray\">Tweet</p>" +
    "<p class=\"w3-left\">" + tweet.text + "</p>" +
    "<p class=\"w3-left\">Hashtags: " +
     hashtag +
    "</div>" +
    "<div class=\"w3-center  w3-col\" style=\"width: 20%\">" +
    "<p class=\"w3-text-gray\">Sentiment</p>" +
    sentiment_emoji +
    "</div>" +
    "</div>" +
    "</div>"

