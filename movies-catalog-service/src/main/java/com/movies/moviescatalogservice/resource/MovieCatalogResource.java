package com.movies.moviescatalogservice.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.movies.moviescatalogservice.model.CatalogItems;
import com.movies.moviescatalogservice.model.Movie;
import com.movies.moviescatalogservice.model.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	private RestTemplate restTemplate;

	List<CatalogItems> catalogList;

	@RequestMapping("/{userId}")
	public List<CatalogItems> getCatalog(@PathVariable("userId") String userId) {

//		List<Rating> ratings = Arrays.asList(new Rating("10", 4), new Rating("11", 4), new Rating("12", 3));
		UserRating ratings = restTemplate.getForObject("http://localhost:8992/ratingdata/user/" + userId,
				UserRating.class);

		catalogList = new ArrayList<CatalogItems>();
		ratings.getUserRating().stream().forEach(rating -> {
			Movie movie = restTemplate.getForObject("http://localhost:8991/movies/" + rating.getMovieId(), Movie.class);
			catalogList.add(new CatalogItems(movie.getMovieId(), "Description", rating.getRating()));
		});

//		return Collections.singletonList(new CatalogItems("Home", "Story of Oliver Twist", 4));
		return catalogList;
	}
}
