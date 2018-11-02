# Chatty Chat

#### A multi-threaded chat application, implemented in Java. Completed in my Advanced Programming class, as an introduction to socket and thread programming. 

## Command Line Usage:
* the server must be started before the client(s)

_$ java ChattyChatChatServer [insert port number here]_

_$ java ChattyChatChatClient [insert hostname] [insert port number]_

If you are running this locally, enter 'localhost' for the hostname.

## Commands

##### /nick [enter nickname without spaces here]

can set your nickname, which will allow other users to DM (direct message) you.

##### /dm [user's nickname] rest of message . . . 

can direct message user with specified nickname

##### /quit

Quits the client and removes from list of clients stored on the server.

#### if no commands are specified, the typed text will be sent to all clients
