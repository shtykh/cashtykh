# cashtykh

One can not simply load tweets via twitter API.
To do that you should create a json-file like this:

{
  "сonsumerKey": "blablabla0",
  "сonsumerSecret": "blablabla1",
  "accessToken": "blablabla2-blablabla3",
  "accessSecret": "blablabla4"
}

then name it "auth" and put it to project folder.
Otherwise you will be asked to peek the file or (if your file is not good enough) simply won't be allowed to work with twiter from the app.

To get credentials you have to create your twitter app here (https://apps.twitter.com/)