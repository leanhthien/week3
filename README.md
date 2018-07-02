# Project 3 - *Twitter Client*

**Twitter Client** is an android app that allows a user to view his Twitter timeline and post a new tweet. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: **35** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x]	User can **sign in to Twitter** using OAuth login
* [x]	User can **view tweets from their home timeline**
  * [x] User is displayed the username, name, and body for each tweet
  * [x] User is displayed the [relative timestamp](https://gist.github.com/nesquena/f786232f5ef72f6e10a7) for each tweet "8m", "7h"
  * [x] User can view more tweets as they scroll with [infinite pagination](http://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews-and-RecyclerView). Number of tweets is unlimited.
    However there are [Twitter Api Rate Limits](https://dev.twitter.com/rest/public/rate-limiting) in place.
* [x] User can **compose and post a new tweet**
  * [x] User can click a “Compose” icon in the Action Bar on the top right
  * [x] User can then enter a new tweet and post this to twitter
  * [x] User is taken back to home timeline with **new tweet visible** in timeline

The following **optional** features are implemented:

* [x] While composing a tweet, user can see a character counter with characters remaining for tweet out of 140.
* [ ] User can **click a link within a tweet body** on tweet details view. The click will launch the web browser with relevant page opened.
* [x] User can **pull down to refresh tweets timeline**
* [ ] User can **open the twitter app offline and see last loaded tweets**. Persisted in SQLite tweets are refreshed on every application launch. While "live data" is displayed when app can get it from Twitter API, it is also saved for use in offline mode.
* [ ] User can tap a tweet to **open a detailed tweet view**
* [ ] User can **select "reply" from detail view to respond to a tweet**

The following **bonus** features are implemented:

* [x] User can see embedded image media within the tweet.
* [ ] User can watch embedded video within the tweet.
* [x] Compose tweet functionality is build using modal overlay.
* [ ] Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.com/android/Using-Parceler).
* [x] [Leverage RecyclerView](http://guides.codepath.com/android/Using-the-RecyclerView) as a replacement for the ListView and ArrayAdapter for all lists of tweets.
* [x] Move the "Compose" action to a [FloatingActionButton](https://github.com/codepath/android_guides/wiki/Floating-Action-Buttons) instead of on the AppBar.
* [x] Replace all icon drawables and other static image assets with [vector drawables](http://guides.codepath.com/android/Drawables#vector-drawables) where appropriate.
* [ ] Leverages the [data binding support module](http://guides.codepath.com/android/Applying-Data-Binding-for-Views) to bind data into layout templates.
* [x] Replace Picasso with [Glide](http://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en) for more efficient image rendering.
* [ ] 

The following **additional** features are implemented:

* [x] Add some animation to Objects, Recycler view and transition to Activities.
* [x] Hight light all Hashtags and UserMentions.
*

## Video Walkthrough

Here's a walkthrough of implemented user stories:
<img src="https://i.imgur.com/xXW2QgB.gif"  title='Video Walkthrough' width='' alt='Video Walkthrough' />
<img src="https://i.imgur.com/BU8Qb18.gif"  title='Video Walkthrough' width='' alt='Video Walkthrough' />
<img src="https://i.imgur.com/eKciq7R.gif"  title='Video Walkthrough' width='' alt='Video Walkthrough' />
<img src="https://i.imgur.com/2NirPuy.gif"  title='Video Walkthrough' width='' alt='Video Walkthrough' />



GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

 - Can not create and handle customize DialogFragment.
 - Can not add more features follow the optional.

## Open-source libraries used

- [Retrofit](http://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java
- [Glide](https://bumptech.github.io/glide/) - A fast and efficient image loading library for Android 
- [Material Dialog](https://github.com/afollestad/material-dialogs) - A beautiful, fluid, and customizable dialogs API. 
- [Spark Button]() - An animation alike Twitter like button
- [Wave View]() - An animation alike wave
- [Recyclerview Animators]() - An library for animation RecyclerView
- [Arc Animator]() - An library for arc animator

## License

    Copyright 2018 Le Anh Thien

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
