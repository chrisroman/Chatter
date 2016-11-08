# Chatter

Chatter incorporates many of Firebase's packages to quickly and securely authorize and authenticate users. The intent of this project
is to showcase many features of Firebase in practical and common instances.

## User Authentication and Authorization
Google and Twitter are allowed as third party organizations to authenticate users. In addition, a separate Email/Password authentication
method has been implemented. To make the implementation easier, Chatter uses the FirebaseUI package. This removes much of the boilerplate
code when dealing with each third party organization's different sign in flow. 

Once the user has been authenticated, they are authorized to write to the chat page. This page allows users to post anonymously (while
still needing to be signed in) and shows the timestamp of their message. All users are allowed to navigate to the chat page without
signing in, but will only have read priviledges to the chat log.


## Implementation of Real-time Database
Using Firebase's realtime database, we can write to and load the chat page in real time. Each message entry is a lightweight structure
with a unique id. If a user tries to write to the chat page but has no Internet connection, his/her message will be queued in memory, waiting to be written to the database once they regain connection. This requires no additional logic in the activities because the
Firebase packages handles it for us.


## User Experience/Design
Much of the user interface provided was created by taking default themes from FirebaseUI and customizing them however was desired. Many of the resources (such as layouts, colors, images, etc.) were taken directly from the FirebaseUI package. In no way do I claim credit
or ownership over these files.

## References
<href>https://github.com/firebase/FirebaseUI-Android</href>

<href>https://firebase.google.com/docs/</href>

<href>https://developer.android.com/index.html</href>
