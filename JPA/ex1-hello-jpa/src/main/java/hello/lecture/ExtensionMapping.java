package hello.lecture;

import hello.entity.Movie;
import javax.persistence.EntityManager;

public class ExtensionMapping {

  private void extension(EntityManager em) {
    Movie movie = new Movie();
    movie.setDirector("Perter Jackson");
    movie.setActor("Mortensen");
    movie.setName("Lord of the rings");
    movie.setPrice(15000);

    em.persist(movie);
    em.flush();
    em.clear();

    Movie findMovie = em.find(Movie.class, movie.getId());
    System.out.println("findMove = " + findMovie);
  }
}
