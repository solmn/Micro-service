package io.solmn.moviecatalogservice.resources;

import io.solmn.moviecatalogservice.models.CatalogItem;
import io.solmn.moviecatalogservice.models.Movie;
import io.solmn.moviecatalogservice.models.Rating;
import io.solmn.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog/")
public class MovieCatalogResource {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        List<Rating> ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId, UserRating.class).getUserRating();

        return ratings.stream().map(rating -> {
            Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
//          Movie movie = webClientBuilder.build()
//                   .get()
//                   .uri("http://localhost:8082/movies/" + rating.getMovieId())
//                   .retrieve().bodyToMono(Movie.class)
//                   .block();

            return new CatalogItem(movie.getName(), "Desc", rating.getRating());
        }).collect(Collectors.toList());

    }
}
