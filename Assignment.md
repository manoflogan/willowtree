__Backend Test Project: The Name Game__

Leading scientists have proven, via science, that learning your coworker’s names while starting a new job is useful. Your test project is to make it happen! The data source is located at https://www.willowtreeapps.com/api/v1.0/profiles.

# Your Mission

Create an API that consumes the profile data from the URL above and makes it possible to implement a full-featured game on top of it. The client should be very simple, and all of the logic for the game (for example, is this guess right?) should be implemented in the server.  Providing a client implementation is optional.  

The game will present the client/user with six faces and ask them to identify the listed name. It's up to you how to design the API, its endpoints, and its responses. If there’s time, on top of that build in some other features:


## Statistics:

* How many correct/incorrect attempts has the user made? How long does it take on average for a person to identify the subject?

* Leaderboard: Show identifiers and scores for the top 10 scoring sessions.

* Mat(t) mode: Roughly 90% of our co-workers are named Mat(t), so add a challenge mode where the server only presents the clients with Mat(t)s.

* Reverse mode: Show one face and six names. Ask the client to identify the correct name.

* Implement authentication/authorization, but feel free to accept users without passwords or use an authentication-as-a-service provider.
