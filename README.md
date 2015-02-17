# cashtykh

Project consists of configurable two-level cache and small application to test its functionality.
The application allows user to add/edit/remove/load_from_twitter stories and to discover new stories by hashtags that may be found in the existing ones. Story is a text and its title which is unique within the cache.

<img src="https://github.com/shtykh/cashtykh/blob/master/Screen%20Shot%202015-02-17%20at%2012.59.53.png?raw=true">

# ( add | edit | remove ) story & edit cache capacity
These actions are available via UI (edit story or its title as text in text editors, change cache capacity via spinners) without authorization in twitter. Figure above shows the interface to edit the story.

# load story from twitter | discover stories using existing hashtags
You may write a search query to find some tweets (number of tweets to find might be set via slider next to the load button) and save them as a story. Or you may launch automatic search that will use all hashtags that could be found in stories stored in the cache.

#note:
One can not simply load tweets via twitter API.
To do that you should create a json file like this:

{<br/>
  "сonsumerKey": "blablabla0", <br/>
  "сonsumerSecret": "blablabla1", <br/>
  "accessToken": "blablabla2-blablabla3", <br/>
  "accessSecret": "blablabla4" <br/>
}

Then name it "auth" and put it to project folder, otherwise you will be asked to peek the file or (if your file is not good enough) you won't be allowed to work with twiter through the app.

To get credentials you have to create your twitter app here (https://apps.twitter.com/)
