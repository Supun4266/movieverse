package com.movieverse.backend.movie;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Fills the database with starter movies the first time the app runs.
 */
@Component
public class MovieDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MovieDataSeeder.class);

    private final MovieRepository movieRepository;

    public MovieDataSeeder(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void run(String... args) {
        if (movieRepository.count() > 0) {
            return;
        }

        List<Movie> movies = List.of(
            movie("Inception", "A thief who steals secrets from people's dreams is given one last job: plant an idea instead of stealing one.", "Sci-Fi", 2010, 8.8, 148),
            movie("Interstellar", "With Earth running out of food, a team of astronauts travels through a wormhole to find humanity a new home.", "Sci-Fi", 2014, 8.7, 169),
            movie("The Matrix", "A computer hacker learns that the world he lives in is a simulation and joins the fight to free humanity.", "Sci-Fi", 1999, 8.7, 136),
            movie("Arrival", "A language expert is asked to communicate with mysterious visitors whose arrival changes how she sees time.", "Sci-Fi", 2016, 7.9, 116),
            movie("Blade Runner 2049", "A young officer uncovers a long-buried secret that could throw what is left of society into chaos.", "Sci-Fi", 2017, 8.0, 164),

            movie("The Dark Knight", "Batman faces the Joker, a criminal mastermind who wants to plunge Gotham City into anarchy.", "Action", 2008, 9.0, 152),
            movie("Mad Max: Fury Road", "In a desert wasteland, a drifter and a rebel warrior race across the sand to escape a tyrant.", "Action", 2015, 8.1, 120),
            movie("Gladiator", "A betrayed Roman general is forced into slavery and fights his way back as a gladiator.", "Action", 2000, 8.5, 155),
            movie("John Wick", "A retired hitman comes back to the criminal underworld to settle a personal score.", "Action", 2014, 7.4, 101),
            movie("Top Gun: Maverick", "A veteran navy pilot returns to train a group of young graduates for a dangerous mission.", "Action", 2022, 8.2, 130),

            movie("Coco", "A boy who dreams of becoming a musician travels to the Land of the Dead to uncover his family's history.", "Animation", 2017, 8.4, 105),
            movie("The Lion King", "A young lion prince runs away after his father's death and must return to claim his place as king.", "Animation", 1994, 8.5, 88),
            movie("Spirited Away", "A girl wanders into a world of spirits and must work in a bathhouse to save her parents.", "Animation", 2001, 8.6, 125),
            movie("Toy Story", "A cowboy doll feels threatened when a flashy new space-ranger toy becomes his owner's favourite.", "Animation", 1995, 8.3, 81),
            movie("Spider-Man: Into the Spider-Verse", "Teenager Miles Morales becomes Spider-Man and meets other spider-heroes from parallel worlds.", "Animation", 2018, 8.4, 117),

            movie("The Shawshank Redemption", "Two imprisoned men form a deep friendship over many years and find hope in small acts of kindness.", "Drama", 1994, 9.3, 142),
            movie("Forrest Gump", "A kind-hearted man with a simple view of life finds himself part of many historic moments.", "Drama", 1994, 8.8, 142),
            movie("The Pursuit of Happyness", "A struggling salesman and his young son face homelessness while he chases a better career.", "Drama", 2006, 8.0, 117),
            movie("Whiplash", "A young drummer at a top music school is pushed to his limits by a demanding teacher.", "Drama", 2014, 8.5, 106),

            movie("The Grand Budapest Hotel", "A legendary hotel concierge and his loyal lobby boy get caught up in the theft of a priceless painting.", "Comedy", 2014, 8.1, 99),
            movie("Paddington 2", "A polite bear is wrongly sent to prison and his adoptive family work to clear his name.", "Comedy", 2017, 7.8, 103),
            movie("The Intouchables", "A wealthy man who cannot move and his carefree new carer form an unlikely friendship.", "Comedy", 2011, 8.5, 112),

            movie("Parasite", "A poor family slowly works their way into the lives of a wealthy household, with unexpected results.", "Thriller", 2019, 8.5, 132),
            movie("Gone Girl", "When a woman goes missing on her anniversary, suspicion falls on her husband.", "Thriller", 2014, 8.1, 149),
            movie("Knives Out", "A detective investigates the death of a wealthy crime novelist and suspects everyone in the family.", "Thriller", 2019, 7.9, 130)
        );

        movieRepository.saveAll(movies);
        log.info("Seeded {} movies into the database", movies.size());
    }

    private Movie movie(String title, String description, String genre, int year, double rating, int minutes) {
        String trailer = "https://www.youtube.com/results?search_query="
                + URLEncoder.encode(title + " " + year + " official trailer", StandardCharsets.UTF_8);
        return new Movie(title, description, genre, year, rating, minutes, trailer);
    }
}
