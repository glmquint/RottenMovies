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
- view the personal page of the author of a selected _top critic_ review

Normal user can

- logout from the service
- search movies by search bar and other filters
- view movies, their details and relative reviews
- write a review for a selected film
- view the personal page of the author of a selected _top critic_ review
- follow a _top critics_ user
- view the feed of latest reviews from the followed _top critics_
- view the history of its own reviews
- modify its own reviews
- delete its own reviews
- change its account information

Top Critics user can

- logout from the service
- search movies by search bar and other filters
- view movies, their details and relative reviews
- write a top critic review for a selected film
- view the history of its own top critics reviews
- modify its own top critics reviews
- delete its own top critics reviews
- change its account information
- see the number of followers


Admin user can 
- logout
- browse _users- & _top critics_
- add/remove films
- modify films details
- remove reviews
- ban _users_ & _top critics_
- register _top critics_ user
- admin must be able to see most active users in terms of review

General

- Reviews by _top critics_ are the firsts to be shown for each film

### Non functional requirements

- the system must encrypt users password

- the service must be built with OOP language

- user must have 16 or more years to register into the service

> (possible extra function:service must be implemented through a responsive website)

  