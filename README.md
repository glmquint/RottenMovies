# Rotten Movies

_Rotten Movies_ is a web service where users can keep track of the general liking for movies,
they can also share their thoughts with the world after signing up. In addition users can follow
renowned top critic to be constantly updated with their latest reviews.

Guest users, as well as all others, can browse or search movies based on lters like: title, year
of release and personnel who worked in it. They can also view a Hall of Fame for the most
positive review genres, production houses and years in which the best movies were released.

For this project, the application has been developed in Java with the Spring framework and
Thymeleaf as templating engine to implement a web GUI. The application uses a document
database to store the main information about movies, users, reviews and personnel; a graph
database is instead used to keep track of the relationship between normal users and top critics
in terms of who follows who and between the movies and the users who reviewed it.
