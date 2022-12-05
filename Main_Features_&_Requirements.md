# Fresh Potatoes${^{tm}}$

## Main feature:

Fresh Potatoes is a service to read and write _reviews for movies_. _Top critics reviews_ are highlighted and _users_ can follows these top critics to keep track with their liking. 

Basic users can write reviews for movies too, but the total score is differentiated between critics and non critics votes. 

Movie pages also contain details and basic information about the film like:
- plot summary
- date of distribution
- cast and crew
  - with relative roles
- votes
  - of the critics
  - of the public
- critics consensus

> (possible extra func: users receive suggestions for new movies based on their previous reviews history)


### Functional requirements

Guest (Unregistered) users can

- login/register into the service
- search movies by search bar
- view movies, their details and relative reviews

Registered user can

- logout from the service
- search movies by search bar and other filters
- view movies, their details and relative reviews
- write a review for a selected film
- follow a _top critics_ user
- view the feed of latest reviews from the followed _top critics_
- view the history of its own reviews
- modify its own reviews
- change its account information

Top Critics user can

- logout from the service
- search movies by search bar and other filters
- view movies, their details and relative reviews
- write a top critic review for a selected film
- view the history of its own top critics reviews
- modify its own top critics reviews
- change its account information

> (possible extra function: add a landing home screen page for top critic users)

Admin user

- can act as a non-admin registered user
- can browse users
- can grant/remove "top critics" status
- can add/remove films
- can add/remove/modify modify films details
- can remove reviews
- can ban users
- admin must be able to see most active users in terms of review

General

- Reviews by top critics are the firsts to be shown for each film

### Non functional requirements

- the system must encrypt users password

- the service must be built with OOP language

- user must have 16 or more years

- (Da confermare: service must be implemented through a responsive website)

  